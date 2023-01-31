package com.hedera.app.service.token;

import com.hedera.fee.calculation.FeeCalculator;
import com.hedera.fee.calculation.model.OperationMetadata;
import java.util.Map;
import java.util.Objects;

/**
 * A sample handler of the token service that shows how the fee caclulation might look like.
 */
public class CreateTokenHandler {

  private final FeeCalculator feeCalculator;

  /**
   * The feeCalculator should be provided by the app-spi
   * @param feeCalculator
   */
  public CreateTokenHandler(final FeeCalculator feeCalculator) {
    this.feeCalculator = Objects.requireNonNull(feeCalculator);
  }


  /**
   * The handle method of the service
   */
  public static void handle() {
    throw new IllegalStateException("Not yet implemented");
  }

  /**
   * This method does the fee calculation. Here we can use the concreate variables of the calculation as input params.
   * @param tokenSize a concrete variable for the calculation (just a sample)
   * @return the fee
   */
  public long calculateFee(final Long tokenSize) {
    final Map<String, Long> input = Map.of("tokenSize", tokenSize);
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
