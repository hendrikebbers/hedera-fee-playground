package com.hedera.fee.calculation.impl;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ArithmeticSimpleExpressionEvaluatorTest {

  @ParameterizedTest
  @MethodSource("evaluationTestCases")
  void testEvaluateExpressions(final String expressionToEvaluate,
      final Map<String, Long> parameterValues,
      final Long expectedResult) {
    final String replacedExpression = replaceValues(expressionToEvaluate, parameterValues);
    final ArithmeticSimpleExpressionEvaluator testEvaluator = new ArithmeticSimpleExpressionEvaluator(
        replacedExpression);
    final long actualResult = testEvaluator.evaluate();
    Assertions.assertEquals(expectedResult, actualResult);
  }

  @Test
  void testNullInput() {
    //given
    final String expressionToEvaluate = null;

    //then
    Assertions.assertThrows(NullPointerException.class,
        () -> new ArithmeticSimpleExpressionEvaluator(expressionToEvaluate));
  }

  @Test
  void testEmptyInput() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new ArithmeticSimpleExpressionEvaluator(""));
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new ArithmeticSimpleExpressionEvaluator("     "));
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new ArithmeticSimpleExpressionEvaluator("\t"));
  }


  @ParameterizedTest
  @ValueSource(strings = {"null", "e", "hello", "a+b", "1,4", "9223372036854775808", "1/0", "1+",
      "1-", "1*", "1/", "-", "+", "/", "*"})
  void testBadInput(final String expressionToEvaluate) {
    //given
    final ArithmeticSimpleExpressionEvaluator testEvaluator = new ArithmeticSimpleExpressionEvaluator(
        expressionToEvaluate);

    //then
    Assertions.assertThrows(EvaluationException.class, () -> testEvaluator.evaluate());
  }

  @ParameterizedTest
  @MethodSource("evaluationEdgeCases")
  void testEdgeCases(final String expressionToEvaluate, final Long expectedResult) {
    //given
    final ArithmeticSimpleExpressionEvaluator testEvaluator = new ArithmeticSimpleExpressionEvaluator(
        expressionToEvaluate);

    //when
    final long result = testEvaluator.evaluate();

    //then
    Assertions.assertEquals(expectedResult, result);
  }

  @ParameterizedTest
  @MethodSource("evaluationSimpleMathTestCases")
  void testSimpleExpressions(final String expressionToEvaluate, final Long expectedResult) {
    //given
    final ArithmeticSimpleExpressionEvaluator testEvaluator = new ArithmeticSimpleExpressionEvaluator(
        expressionToEvaluate);

    //when
    final long result = testEvaluator.evaluate();

    //then
    Assertions.assertEquals(expectedResult, result);
  }

  public static Stream<Arguments> evaluationEdgeCases() {
    return Stream.of(Arguments.of("9223372036854775807", Long.MAX_VALUE),
        Arguments.of("-9223372036854775808", Long.MIN_VALUE),
        Arguments.of("    1                 +                            1    ", 2L),
        Arguments.of("\t1\t+\t1\t", 2L),
        Arguments.of("1+++++++1", 2L), // I ask myself if we should support such expression
        Arguments.of("1---1", 0L), // I ask myself if we should support such expression
        Arguments.of("---1", -1L), // I ask myself if we should support such expression
        Arguments.of("-+1", -1L), // I ask myself if we should support such expression
        Arguments.of("++--+1", 1L), // I ask myself if we should support such expression
        Arguments.of("+-+++1", -1L) // I ask myself if we should support such expression
    );
  }

  public static Stream<Arguments> evaluationSimpleMathTestCases() {
    return Stream.of(Arguments.of("1", 1L),
        Arguments.of("-1", -1L),
        Arguments.of("0", 0L),
        Arguments.of("1+1", 2L),
        Arguments.of("1-1", 0L),
        Arguments.of("1+2", 3L),
        Arguments.of("1-2", -1L),
        Arguments.of("2*2", 4L),
        Arguments.of("4/2", 2L),
        Arguments.of("3 + 2 * 2", 7L),
        Arguments.of("2 * 2 + 3", 7L),
        Arguments.of("(2 * 2) + 3", 7L),
        Arguments.of("3 + 4 / 2", 5L),
        Arguments.of("4 / 2 + 3", 5L),
        Arguments.of("(4 / 2) + 3", 5L),
        Arguments.of("3 - 2 * 2", -1L),
        Arguments.of("2 * 2 - 3", 1L),
        Arguments.of("(2 * 2) - 3", 1L),
        Arguments.of("3 - 4 / 2", 1L),
        Arguments.of("4 / 2 - 3", -1L),
        Arguments.of("(4 / 2) - 3", -1L)
    );
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

  private static String replaceValues(final String formulaToCalculate,
      final Map<String, Long> parameterValues) {
    String replacementExpression = formulaToCalculate;
    for (final String placeholder : parameterValues.keySet()) {
      replacementExpression = replacementExpression.replaceAll(placeholder,
          parameterValues.get(placeholder).toString());
    }
    return replacementExpression;
  }
}
