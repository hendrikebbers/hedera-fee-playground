package com.hedera.fee.calculation;

import com.hedera.fee.calculation.impl.FeeCalculatorImpl;

/**
 * Factory to create a {@link FeeCalculator}.
 */
public class FeeCalculatorFactory {

  private FeeCalculatorFactory() {
  }

  /**
   * Creates a new {@link FeeCalculator} instance.
   * @return new {@link FeeCalculator} instance.
   */
  public static FeeCalculator createFeeCalculator() {
    return new FeeCalculatorImpl();
  }
}
