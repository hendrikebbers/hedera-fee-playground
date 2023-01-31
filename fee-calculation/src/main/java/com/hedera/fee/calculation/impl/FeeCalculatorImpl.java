package com.hedera.fee.calculation.impl;

import com.hedera.fee.calculation.FeeCalculator;
import com.hedera.fee.calculation.model.OperationMetadata;
import com.hedera.fee.calculation.model.ParameterMetadata;
import com.hedera.fee.calculation.model.ServiceMetadata;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class FeeCalculatorImpl implements FeeCalculator {
    private static final String MISSING_NOT_OPTIONAL =
        "The operation '%s' requires the non-optional parameter '%s' that is not part of the given input parameters.";
    private static final String JSON_FILE = "fee-model.json";

    private final Set<ServiceMetadata> services;

    public FeeCalculatorImpl() {
        final URL jsonFileUrl = FeeCalculator.class.getResource(JSON_FILE);
        services = FeeModelJsonParser.parseJsonModel(jsonFileUrl);
    }

    @Override
    public Set<ServiceMetadata> getAllServices() {
        return services;
    }

    @Override
    public long calculate(final OperationMetadata operation, final Map<String, Long> input) {
        Objects.requireNonNull(operation);
        Objects.requireNonNull(input);
        final Set<String> requiredParameterNames = operation.parameters().stream()
            .filter(parameter -> !parameter.optional())
            .map(ParameterMetadata::name)
            .collect(Collectors.toSet());
        final Set<String> inputParamNames = input.keySet();
        requiredParameterNames.stream()
            .filter(name -> !inputParamNames.contains(name))
            .findAny()
            .ifPresent(name -> {
                throw new IllegalArgumentException(String.format(MISSING_NOT_OPTIONAL, operation.name(), name));
            });
        final String formula = operation.formulaExpression();
        return processFormula(formula, input);
    }

    private static long processFormula(final String formulaToCalculate, final Map<String, Long> parameterValues) {
        final String stringToEvaluate = replaceValues(formulaToCalculate, parameterValues);
        return new ArithmeticSimpleExpressionEvaluator(stringToEvaluate).evaluate();
    }

    private static String replaceValues(final String formulaToCalculate, final Map<String, Long> parameterValues) {
        String replacementExpression = formulaToCalculate;
        for(String placeholder : parameterValues.keySet()) {
            replacementExpression = replacementExpression.replaceAll(placeholder, parameterValues.get(placeholder).toString());
        }
        return replacementExpression;
    }
}
