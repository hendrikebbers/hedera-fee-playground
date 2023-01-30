package com.hedera.fee.calculation.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record ServiceMetadata(String name, String description, Set<OperationMetadata> operations) {

  public ServiceMetadata(final String name, final String description,
      final Set<OperationMetadata> operations) {
    this.name = Objects.requireNonNull(name);
    this.description = Objects.requireNonNull(description);
    Objects.requireNonNull(operations);
    this.operations = Collections.unmodifiableSet(operations);
  }
}
