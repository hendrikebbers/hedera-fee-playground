package com.hedera.fee.calculation.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.hedera.fee.calculation.FeeCalculator;
import com.hedera.fee.calculation.FeeCalculatorFactory;
import com.hedera.fee.calculation.model.ServiceMetadata;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FeeCalculatorImplTest {

  @Test
  void testAllServicesAvailable() {
    //given
    final FeeCalculator calculator = FeeCalculatorFactory.createFeeCalculator();

    //when
    final Set<ServiceMetadata> allServices = calculator.getAllServices();

    //then
    assertNotNull(allServices);
    assertEquals(1, allServices.size());
  }

}