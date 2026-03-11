package com.reciproci.loyalty.burnrule.exception;

public class BurnRuleAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BurnRuleAlreadyExistsException(String message) {
		super(message);
	}
}
