package com.hedera.fee.calculation.impl;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ArithmeticSimpleExpressionEvaluatorTest {

    @ParameterizedTest
    @MethodSource("evaluationTestCases")
    void testEvaluateExpressions(String expressionToEvaluate, Map<String, Long> parameterValues, Long expectedResult) {
        String replacedExpression = replaceValues(expressionToEvaluate, parameterValues);
        ArithmeticSimpleExpressionEvaluator testEvaluator = new ArithmeticSimpleExpressionEvaluator(replacedExpression);
        long actualResult = testEvaluator.evaluate();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> evaluationTestCases() {
        return Stream.of(Arguments.of("-100000000-min + 3000", Map.of("min", Long.MIN_VALUE + 1),
                ((-100000000L - (Long.MIN_VALUE + 1)) + 3000)),
            Arguments.of("tokenCount * 2500 + (keyCount + ownerCount) * 4500 + " +
                    "1791*(\t(( \t(royaltyDurationMonths+contentDurationMonths) )*contentSizeBytes ) )",
                Map.of("tokenCount", 3L, "keyCount", 1L, "ownerCount", 2L, "royaltyDurationMonths", 4L,
                    "contentSizeBytes", 2048L, "contentDurationMonths", 6L),
                (3L * 2500L + (1L + 2L) * 4500L + 1791L * ((4L + 6L) * 2048L))),
            Arguments.of("(tokenCount+keyCount*contentSizeBytes)/ownerCount",
                Map.of("tokenCount", 2L, "keyCount", 1L, "ownerCount", 3L, "royaltyDurationMonths", 4L,
                    "contentSizeBytes", 512L, "contentDurationMonths", 5L), 171L),
            Arguments.of("one+one-zero*(200 - 1 * zero) + 450/one", Map.of("zero", 0L, "one", 1L), 452L)
        );
    }

    private static String replaceValues(final String formulaToCalculate, final Map<String, Long> parameterValues) {
        String replacementExpression = formulaToCalculate;
        for (String placeholder : parameterValues.keySet()) {
            replacementExpression = replacementExpression.replaceAll(placeholder,
                parameterValues.get(placeholder).toString());
        }
        return replacementExpression;
    }
}
