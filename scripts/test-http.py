#!/usr/bin/env python3
"""Run the packaged application and functional suite against disposable PostgreSQL."""
import json
import os
from pathlib import Path
import shutil
import socket
import subprocess
import tempfile
import time
import urllib.error
import urllib.request
import uuid
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]


def run(*args, env=None):
    subprocess.run(args, cwd=ROOT, env=env, check=True)


def request(base, method, path, body=None, expected=200):
    data = None if body is None else json.dumps(body).encode()
    req = urllib.request.Request(base + path, data=data, method=method,
                                 headers={"Content-Type": "application/json"})
    try:
        response = urllib.request.urlopen(req, timeout=10)
    except urllib.error.HTTPError as error:
        response = error
    with response:
        payload = json.load(response)
        assert response.status == expected, (method, path, response.status, payload)
        return payload


def main():
    name = "accounts-http-" + uuid.uuid4().hex[:12]
    app = None
    started = False
    env = os.environ.copy()
    env["GRADLE_USER_HOME"] = env.get("GRADLE_USER_HOME", str(Path.home() / ".gradle"))
    java = str(Path(env["JAVA_HOME"]) / "bin/java") if env.get("JAVA_HOME") else shutil.which("java")
    with tempfile.TemporaryDirectory(prefix="accounts-http-") as scratch:
        log = Path(scratch) / "app.log"
        try:
            started = True
            run("docker", "run", "--detach", "--name", name,
                "-e", "POSTGRES_PASSWORD=local-test-only", "-e", "POSTGRES_DB=accounts_http",
                "-p", "127.0.0.1::5432", "postgres:16-alpine")
            for _ in range(60):
                ready = subprocess.run(["docker", "exec", name, "psql", "-U", "postgres",
                                        "-d", "accounts_http", "-Atc", "SELECT 1"],
                                       stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
                if ready.returncode == 0:
                    break
                time.sleep(1)
            else:
                raise RuntimeError("PostgreSQL readiness timed out")
            port = subprocess.check_output(["docker", "port", name, "5432/tcp"], text=True).strip().rsplit(":", 1)[1]
            url = "jdbc:postgresql://127.0.0.1:" + port + "/accounts_http"
            env.update(FLYWAY_URL=url, FLYWAY_USER="postgres", FLYWAY_PASSWORD="local-test-only",
                       JDBC_DATABASE_URL=url, JDBC_DATABASE_USERNAME="postgres", JDBC_DATABASE_PASSWORD="local-test-only",
                       ACCOUNTS_TEST_DB_URL=url, ACCOUNTS_TEST_DB_USER="postgres", ACCOUNTS_TEST_DB_PASSWORD="local-test-only")
            run("bash", "gradlew", "release", "-Pskip-functional-tests", "--no-daemon", "--max-workers=1", env=env)
            sources = ROOT / "accounts-service/src/test/java"
            for source in sources.rglob("*DatabaseTests.java"):
                class_name = ".".join(source.relative_to(sources).with_suffix("").parts)
                result = ET.parse(ROOT / "accounts-service/build/test-results/test" / ("TEST-" + class_name + ".xml")).getroot()
                assert int(result.get("tests", "0")) > 0
                assert all(int(result.get(key, "0")) == 0 for key in ("failures", "errors", "skipped")), class_name
            run("bash", "gradlew", ":accounts-service:bootJar", ":accounts-service:flywayMigrate",
                "--no-daemon", "--max-workers=1", env=env)
            with socket.socket() as sock:
                sock.bind(("127.0.0.1", 0))
                http_port = sock.getsockname()[1]
            base = "http://127.0.0.1:" + str(http_port) + "/api/"
            with log.open("w") as output:
                app = subprocess.Popen([java, "-jar", str(ROOT / "accounts-service/build/libs/accounts-service-1.0.0.jar"),
                                        "--server.address=127.0.0.1", "--server.port=" + str(http_port)],
                                       cwd=ROOT, env=env, stdout=output, stderr=subprocess.STDOUT)
                for _ in range(90):
                    if app.poll() is not None:
                        raise RuntimeError("Application exited before readiness")
                    try:
                        assert request(base, "GET", "deep_ping")["healthy"]
                        break
                    except (OSError, ValueError, AssertionError):
                        time.sleep(1)
                else:
                    raise RuntimeError("Application readiness timed out")
                env["ACCOUNTS_SERVICE_BASE_URL"] = base
                run("bash", "gradlew", ":functional-tests:test", ":functional-tests:checkstyleTest",
                    "--no-daemon", "--max-workers=1", env=env)
                account = {"title": "HTTP regression", "accountType": "Capital",
                           "subAccounts": [{"description": "original"}]}
                created = request(base, "POST", "accounts", {"requestingUser": "http-test", "account": account}, expected=201)
                assert created["success"]
                account_id = created["accountId"]
                found = request(base, "GET", "accounts/" + account_id)["account"]
                creation = found["subAccounts"][0]["createdAt"]
                found["title"] = "edited"
                found["subAccounts"][0]["description"] = "edited child"
                edited = request(base, "PUT", "accounts", {"updatingUser": "http-test", "updatedAccount": found})
                assert edited["success"]
                child = edited["updatedAccount"]["subAccounts"][0]
                assert child["createdAt"] == creation and child["description"] == "edited child"
                listed = request(base, "GET", "accounts")["accounts"]
                assert any(row["id"] == account_id for row in listed)
                assert request(base, "DELETE", "subAccounts/" + child["id"])["success"]
                assert request(base, "GET", "accounts/" + account_id)["account"]["subAccounts"] == []
                assert request(base, "DELETE", "accounts/" + account_id)["success"]
                assert request(base, "GET", "accounts/" + account_id, expected=404)["code"] == "NOT_FOUND"
                assert request(base, "DELETE", "accounts/" + account_id, expected=404)["code"] == "NOT_FOUND"
                assert request(base, "DELETE", "subAccounts/" + child["id"], expected=404)["code"] == "NOT_FOUND"
                assert request(base, "POST", "accounts", {}, expected=400)["code"] == "INVALID_REQUEST"
                missing = {"id": "missing", "title": "missing", "accountType": "Capital", "subAccounts": []}
                assert request(base, "PUT", "accounts", {"updatingUser": "http-test", "updatedAccount": missing}, expected=404)["code"] == "NOT_FOUND"
                run("docker", "exec", name, "psql", "-U", "postgres", "-d", "accounts_http", "-c",
                    "ALTER TABLE accountsdb.accounts RENAME TO accounts_failure_probe")
                try:
                    failure = request(base, "GET", "accounts", expected=500)
                    assert failure == {"success": False, "code": "DATABASE_ERROR", "message": "Storage operation failed"}
                finally:
                    run("docker", "exec", name, "psql", "-U", "postgres", "-d", "accounts_http", "-c",
                        "ALTER TABLE accountsdb.accounts_failure_probe RENAME TO accounts")
                print("HTTP CRUD, validation, 404 and real storage-failure contracts passed", flush=True)
        except BaseException:
            if log.exists():
                print(log.read_text()[-16000:], flush=True)
            raise
        finally:
            if app is not None:
                if app.poll() is None:
                    app.terminate()
                    try:
                        app.wait(timeout=15)
                    except subprocess.TimeoutExpired:
                        app.kill()
                        app.wait(timeout=10)
            if started:
                remaining = subprocess.check_output(["docker", "ps", "-aq", "--filter", "name=^/" + name + "$"], text=True).strip()
                if remaining:
                    run("docker", "stop", name)
                    run("docker", "rm", name)
                remaining = subprocess.check_output(["docker", "ps", "-aq", "--filter", "name=^/" + name + "$"], text=True).strip()
                assert not remaining, "Owned test container still exists"


if __name__ == "__main__":
    main()
