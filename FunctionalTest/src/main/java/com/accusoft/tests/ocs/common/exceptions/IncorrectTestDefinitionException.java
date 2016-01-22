package com.accusoft.tests.ocs.common.exceptions;

/**
 * The Class IncorrectTestDefinitionException.
 */
public class IncorrectTestDefinitionException extends IllegalArgumentException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new incorrect test definition exception.
	 *
	 * @param msg
	 *            the msg
	 */
	public IncorrectTestDefinitionException(String msg) {
		super(msg);
	}

	/**
	 * Instantiates a new incorrect test definition exception.
	 *
	 * @param msg
	 *            the msg
	 * @param error
	 *            the error
	 */
	public IncorrectTestDefinitionException(String msg, Throwable error) {
		super(msg, error);
	}

}
