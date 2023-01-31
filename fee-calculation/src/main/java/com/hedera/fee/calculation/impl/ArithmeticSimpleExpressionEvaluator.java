package com.hedera.fee.calculation.impl;

import java.util.Objects;

class ArithmeticSimpleExpressionEvaluator {

  private static final String BAD_CHARACTER_MESSAGE = "Unexpected character %c in expression at position %d.";
  private static final String MISSING_END_PAREN = "Missing ending ')' at position %d; found %c instead.";
  private int currentPosition = -1;
  private int currentCharacter;
  private final String expressionToEvaluate;

  public ArithmeticSimpleExpressionEvaluator(final String expressionToEvaluate) {
    this.expressionToEvaluate = Objects.requireNonNull(expressionToEvaluate).trim();
    if (this.expressionToEvaluate.isBlank()) {
      throw new IllegalArgumentException("No blank expression allowed!");
    }
    // @todo examine if there is performance benefit to a pattern match for disallowed characters here.
  }

  private void advanceCharacter() {
    currentCharacter =
        (++currentPosition < expressionToEvaluate.length()) ? expressionToEvaluate.charAt(
            currentPosition) : -1;
  }

  private boolean skipPast(final int charToSkipPast) {
    while (Character.isWhitespace(currentCharacter)) {
      advanceCharacter();
    }
    if (currentCharacter == charToSkipPast) {
      advanceCharacter();
      return true;
    }
    return false;
  }

  public long evaluate() {
    try {
      advanceCharacter();
      final long currentValue = parseExpression();
      if (currentPosition < expressionToEvaluate.length()) {
        throw new IllegalStateException(String.format(BAD_CHARACTER_MESSAGE,
            (char) currentCharacter, currentPosition));
      }
      return currentValue;
    } catch (final Exception e) {
      throw new EvaluationException(
          "Error in evaluation of expression '" + expressionToEvaluate + "'", e);
    }
  }

  private long parseExpression() {
    long x = parseTerm();
    for (; ; ) {
      if (skipPast('+')) {
        x += parseTerm(); // addition
      } else if (skipPast('-')) {
        x -= parseTerm(); // subtraction
      } else {
        return x;
      }
    }
  }

  private long parseTerm() {
    long x = parseFactor();
    for (; ; ) {
      if (skipPast('*')) {
        x *= parseFactor(); // multiplication
      } else if (skipPast('/')) {
        x /= parseFactor(); // division
      } else {
        return x;
      }
    }
  }

  private long parseFactor() {
    if (skipPast('+')) {
      return +parseFactor(); // unary plus
    }
    if (skipPast('-')) {
      return -parseFactor(); // unary minus
    }

    final long currentValue;
    final int startPosition = this.currentPosition;
    if (skipPast('(')) { // parentheses
      currentValue = parseExpression();
      if (!skipPast(')')) {
        throw new IllegalArgumentException(String.format(MISSING_END_PAREN,
            currentPosition, (char) currentCharacter));
      }
    } else if (Character.isDigit(currentCharacter)) { // numbers
      while (Character.isDigit(currentCharacter)) {
        advanceCharacter();
      }
      currentValue = Long.parseLong(expressionToEvaluate.substring(startPosition, currentPosition));
    } else {
      throw new IllegalStateException(String.format(BAD_CHARACTER_MESSAGE,
          (char) currentCharacter, currentPosition));
    }
    return currentValue;
  }
}
