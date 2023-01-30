package com.hedera.fee.calculation;

import com.hedera.fee.calculation.impl.FeeCalculatorImpl;
import com.hedera.fee.calculation.model.OperationMetadata;
import com.hedera.fee.calculation.model.ServiceMetadata;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public sealed interface FeeCalculator permits FeeCalculatorImpl {

  Set<ServiceMetadata> getAllServices();

  BigDecimal calculate(OperationMetadata operation, final Map<String, BigDecimal> input);
}
