package com.hedera.fee.calculation.model;

import java.math.BigDecimal;
import java.util.Objects;

public record ParameterMetadata(String name, String description, boolean optional,
                                BigDecimal defaultValue) {

  public ParameterMetadata(
      final String name, final String description, final boolean optional,
      final BigDecimal defaultValue) {
    this.name = Objects.requireNonNull(name);
    this.description = Objects.requireNonNull(description);
    this.optional = optional;
    if (!this.optional) {
      Objects.requireNonNull(defaultValue);
    }
    this.defaultValue = defaultValue;
  }

  public ParameterMetadata(final String name, final String description) {
    this(name, description, true, null);
  }
}
