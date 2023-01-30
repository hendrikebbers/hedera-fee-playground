# hedera-fee-playground
A repo to create a new definition and API to calculate fees for the usage of the hedera network.

## Overview

The base of the repo is a JSON that contains all data that is needed to do the calculations.
The JSON is located at [fee-calculation/src/main/resources/com/hedera/fee/calculation/fee-model.json](fee-calculation/src/main/resources/com/hedera/fee/calculation/fee-model.json).

Since the JSON will be mutated whenever anything in the calculation is changed A JSON Schema is defined for the file.
The JSON schema can found at [fee-calculation/src/main/resources/com/hedera/fee/calculation/fee-model-schema.json](fee-calculation/src/main/resources/com/hedera/fee/calculation/fee-model-schema.json).
When building the project a unit tests checks if the current model is valid against the schema.

The `fee-calculation` module contains a service that can be used to interact with the model.
The `com.hedera.fee.calculation.FeeCalculator` interface defines the API and an instance can be 
created by using the `com.hedera.fee.calculation.FeeCalculatorFactory`.
The `FeeCalculator` interface defines an API to get access to all metadata that is defined by the JSON.
Next to this it contains a function to start a fee calculation. 
Currently the concrete implementation of the calculation is missing and an exception will be thrown.

The `token-service` module contains a sample handler implementation that shows how a handler might
use the API to provide a concrete fee calculation for the usacase of the handler.

## Build the project

The project is based on [Java 17](https://adoptium.net/de/temurin/releases/?version=17) and 
[Maven](https://maven.apache.org) is used as build tool.
A local maven installation is not needed since the [maven wrapper](https://maven.apache.org/wrapper/)
is part of the project.
To execute a full build just call
```
mvnw verify
```