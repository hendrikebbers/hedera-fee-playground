package com.hedera.app.service.token;

import com.hedera.fee.calculation.FeeCalculator;
import com.hedera.fee.calculation.model.OperationMetadata;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class CreateTokenHandler {

  private final FeeCalculator feeCalculator;

  public CreateTokenHandler(final FeeCalculator feeCalculator) {
    this.feeCalculator = Objects.requireNonNull(feeCalculator);
  }

  public static void handle() {
    throw new IllegalStateException("Not yet implemented");
  }

  public BigDecimal calculateFee(final BigDecimal tokenSize) {
    final Map<String, BigDecimal> input = Map.of("tokenSize", tokenSize);
    final OperationMetadata createTokenOperation = feeCalculator.getAllServices().stream()
        .filter(service -> Objects.equals("TokenService", service.name()))
        .flatMap(service -> service.operations().stream())
        .filter(operation -> Objects.equals("CreateToken", operation.name()))
        .findAny()
        .orElseThrow(
            () -> new IllegalStateException("Metadata for 'CreateToken' operation not found!"));
    return feeCalculator.calculate(createTokenOperation, input);
  }
}
