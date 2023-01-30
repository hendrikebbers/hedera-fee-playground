package com.hedera.fee.calculation.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hedera.fee.calculation.model.OperationMetadata;
import com.hedera.fee.calculation.model.ParameterMetadata;
import com.hedera.fee.calculation.model.ServiceMetadata;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FeeModelJsonParser {

  private FeeModelJsonParser() {
  }

  static Set<ServiceMetadata> parseJsonModel(final URL jsonFileUrl) {
    try {
      final Set<ServiceMetadata> result = new HashSet<>();
      final String jsonFileContent = Files.readString(Paths.get(jsonFileUrl.toURI()));
      final JsonArray servicesArray = JsonParser.parseString(jsonFileContent).getAsJsonArray();
      servicesArray.forEach(element -> result.add(parseService(element.getAsJsonObject())));
      return result;
    } catch (final Exception e) {
      throw new RuntimeException("Error while parsing json", e);
    }
  }

  private static ServiceMetadata parseService(final JsonObject jsonObject) {
    Objects.requireNonNull(jsonObject);
    final String name = jsonObject.get("name").getAsString();
    final String description = jsonObject.get("description").getAsString();
    final Set<OperationMetadata> operations = jsonObject.getAsJsonArray("operations")
        .asList().stream().map(element -> parseOperation(element.getAsJsonObject()))
        .collect(Collectors.toSet());
    return new ServiceMetadata(name, description, operations);
  }

  private static OperationMetadata parseOperation(final JsonObject jsonObject) {
    Objects.requireNonNull(jsonObject);
    final String name = jsonObject.get("name").getAsString();
    final String description = jsonObject.get("description").getAsString();
    final String formulaExpression = jsonObject.get("formulaExpression").getAsString();
    final BigDecimal minimalValue = new BigDecimal(jsonObject.get("minimalValue").getAsString());
    final Set<ParameterMetadata> parameters = jsonObject.getAsJsonArray("parameters")
        .asList().stream().map(element -> parseParameter(element.getAsJsonObject()))
        .collect(Collectors.toSet());
    return new OperationMetadata(name, description, parameters, formulaExpression, minimalValue);
  }

  private static ParameterMetadata parseParameter(final JsonObject jsonObject) {
    Objects.requireNonNull(jsonObject);
    final String name = jsonObject.get("name").getAsString();
    final String description = jsonObject.get("description").getAsString();
    if (jsonObject.has("optional")) {
      final boolean optional = jsonObject.get("optional").getAsBoolean();
      if (!optional) {
        final BigDecimal defaultValue = new BigDecimal(
            jsonObject.get("defaultValue").getAsString());
        return new ParameterMetadata(name, description, optional, defaultValue);
      }
    }
    return new ParameterMetadata(name, description);
  }
}
