package com.hedera.fee.calculation;

import com.hedera.fee.calculation.impl.FeeCalculatorImpl;

public class FeeCalculatorFactory {

  private FeeCalculatorFactory() {
  }

  public static FeeCalculator createFeeCalculator() {
    return new FeeCalculatorImpl();
  }
}
