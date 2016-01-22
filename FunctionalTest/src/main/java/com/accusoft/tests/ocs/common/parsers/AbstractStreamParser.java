package com.accusoft.tests.ocs.common.parsers;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * The Class AbstractStreamParser.
 */
public abstract class AbstractStreamParser {

	/** The matcher. */
	private MatchResult matcher = null;
	
	/** The is. */
	InputStream is;
	
	/**
	 * Instantiates a new abstract stream parser.
	 *
	 * @param is the is
	 */
	public AbstractStreamParser(InputStream is){
		this.is = is;
	}

	/**
	 * Parses the.
	 */
	protected void parse() {

		Scanner scanner = null;
		try {
			scanner = new Scanner(is);
			scanner.findWithinHorizon(getPattern(), 0);
			this.matcher = scanner.match();

		} catch (Exception ex) {
			throw new RuntimeException("Can not parse stream with pattern: "
					+ getPattern(), ex);
		} finally {
			if (scanner != null)
				scanner.close();
		}

	}
	
	/**
	 * Gets the pattern.
	 *
	 * @return the pattern
	 */
	protected abstract String getPattern();
	
	/**
	 * Gets the matcher.
	 *
	 * @return the matcher
	 */
	protected MatchResult getMatcher(){
		return matcher;
	}
}
