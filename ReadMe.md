# Overview

Welcome to the `AccountsService` Repository.
This Repository holds several different code packages:

### accounts-service

This is the main Service which will hold all the API Definitions and how they will
connect to the external world.

Internal Models should be defined within this code package.

Logic classes should be defined as well in here.

### functional-tests

A Code package that holds a reference to the APIClient
vended by the `accounts-shared-library`. This package
will only make functional-tests to the `accounts-service` and is not meant to be
distributed as a sole code package.

### accounts-shared-library

All shared models, clients, definitions should live in here.

# Building

In order to build the project just run: `./gradlew release` at the root of the repository.
Each code-package has their own `release` task definition.

### Database Setup

In order to create a devo database follow the steps below:

* `docker run --name custom-db -p 5432:5432 -e POSTGRES_PASSWORD=docker -d postgres:12`: Which will run an empty Database using PostgreSLQ, and will expose the port 5432.
* Log into your favorite SQL Workbench using postgres and docker as username and passwords respectively.
Run the commands located under `src/main/resources/db/schema/initial-setup.sql`
* Finally run: `.gradlew flywayMigrate` so your database is up-to-date.