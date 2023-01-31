package com.hedera.fee.calculation;

import com.hedera.fee.calculation.impl.FeeCalculatorImpl;
import com.hedera.fee.calculation.model.OperationMetadata;
import com.hedera.fee.calculation.model.ServiceMetadata;
import java.util.Map;
import java.util.Set;

/**
 * Public API for fee calculation
 */
public sealed interface FeeCalculator permits FeeCalculatorImpl {

  /**
   * Returns a set of all services that are available for fee calculation.
   * @return
   */
  Set<ServiceMetadata> getAllServices();

  /**
   * Does a concrete fee calculation for the given operation by using the given input as values for
   * all variables of the calculation formula.
   * @param operation the operation
   * @param input the input values
   * @return the result of the calculation
   */
  long calculate(OperationMetadata operation, final Map<String, Long> input);
}
