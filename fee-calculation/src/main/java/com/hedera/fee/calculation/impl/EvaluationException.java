package com.hedera.fee.calculation.impl;

public class EvaluationException extends IllegalStateException {

  public EvaluationException(final String s) {
    super(s);
  }

  public EvaluationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
