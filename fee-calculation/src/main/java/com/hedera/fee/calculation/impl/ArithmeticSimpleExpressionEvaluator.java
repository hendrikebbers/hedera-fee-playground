package com.hedera.fee.calculation.impl;

class ArithmeticSimpleExpressionEvaluator {
    private static final String BAD_CHARACTER_MESSAGE = "Unexpected character %c in expression at position %d.";
    private static final String  MISSING_END_PAREN = "Missing ending ')' at position %d; found %c instead.";
    private int currentPosition = -1;
    private int currentCharacter;
    private final String expressionToEvaluate;

    public ArithmeticSimpleExpressionEvaluator(final String expressionToEvaluate) {
        this.expressionToEvaluate = expressionToEvaluate;
        // @todo examine if there is performance benefit to a pattern match for disallowed characters here.
    }

    private void advanceCharacter() {
        currentCharacter =
            (++currentPosition < expressionToEvaluate.length()) ? expressionToEvaluate.charAt(currentPosition) : -1;
    }

    private boolean skipPast(int charToSkipPast) {
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
        advanceCharacter();
        long currentValue = parseExpression();
        if (currentPosition < expressionToEvaluate.length()) {
            throw new IllegalArgumentException(String.format(BAD_CHARACTER_MESSAGE,
                (char) currentCharacter, currentPosition));
        }
        return currentValue;
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

        long currentValue;
        int startPosition = this.currentPosition;
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
            throw new IllegalArgumentException(String.format(BAD_CHARACTER_MESSAGE,
                (char) currentCharacter, currentPosition));
        }
        return currentValue;
    }
}
