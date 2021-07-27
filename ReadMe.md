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