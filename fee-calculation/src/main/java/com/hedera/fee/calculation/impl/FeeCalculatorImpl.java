package com.hedera.fee.calculation.impl;

import com.hedera.fee.calculation.FeeCalculator;
import com.hedera.fee.calculation.model.OperationMetadata;
import com.hedera.fee.calculation.model.ServiceMetadata;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class FeeCalculatorImpl implements FeeCalculator {

  public static final String JSON_FILE = "fee-model.json";

  private final Set<ServiceMetadata> services;

  public FeeCalculatorImpl() {
    final URL jsonFileUrl = FeeCalculator.class.getResource(JSON_FILE);
    services = FeeModelJsonParser.parseJsonModel(jsonFileUrl);
  }

  @Override
  public Set<ServiceMetadata> getAllServices() {
    return services;
  }

  @Override
  public BigDecimal calculate(final OperationMetadata operation,
      final Map<String, BigDecimal> input) {
    Objects.requireNonNull(operation);
    Objects.requireNonNull(input);

    final Set<String> requiredParameterNames = operation.parameters().stream()
        .filter(parameter -> !parameter.optional())
        .map(parameter -> parameter.name())
        .collect(Collectors.toSet());

    final Set<String> inputParamNames = input.keySet();

    requiredParameterNames.stream()
        .filter(name -> !inputParamNames.contains(name))
        .findAny()
        .ifPresent(name -> {
          throw new IllegalArgumentException(
              "The operation '" + operation.name() + "' requires the non-optional parameter '"
                  + name
                  + "' that is not part of the given input parameters");
        });

    final String formula = operation.formulaExpression();

    return calculate(formula, input);
  }

  private static BigDecimal calculate(final String formula, final Map<String, BigDecimal> input) {
    throw new IllegalStateException("Not yet implemented");
  }

}
