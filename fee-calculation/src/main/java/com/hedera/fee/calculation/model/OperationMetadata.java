package com.hedera.fee.calculation.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record OperationMetadata(String name, String description, Set<ParameterMetadata> parameters,
                                String formulaExpression, BigDecimal minimalValue) {

  public OperationMetadata(final String name, final String description,
      final Set<ParameterMetadata> parameters,
      final String formulaExpression, final BigDecimal minimalValue) {
    this.name = Objects.requireNonNull(name);
    this.description = Objects.requireNonNull(description);
    Objects.requireNonNull(parameters);
    this.parameters = Collections.unmodifiableSet(parameters);
    this.formulaExpression = Objects.requireNonNull(formulaExpression);
    this.minimalValue = Objects.requireNonNull(minimalValue);
  }
}
