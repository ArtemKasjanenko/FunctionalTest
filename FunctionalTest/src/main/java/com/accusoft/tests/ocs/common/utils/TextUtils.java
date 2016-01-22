package com.accusoft.tests.ocs.common.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.accusoft.tests.ocs.common.parsers.AbstractStreamParser;

public class TextUtils {

	private static String DEFAULT_FORMAT_PATTERN = "MM/dd/yyyy hh:mm:ss";

	public static Date getDateFromString(String dateTimeString,
			String formatPattern) throws Exception {

		if (dateTimeString == null) {
			return null;
		}

		if (formatPattern == null || formatPattern.trim().isEmpty()) {
			formatPattern = DEFAULT_FORMAT_PATTERN;
		}

		SimpleDateFormat format = new SimpleDateFormat(formatPattern);

		return format.parse(dateTimeString);

	}

	public static String getDateTimeFromText(String text) {
		ByteArrayInputStream is = new ByteArrayInputStream(text.getBytes());

		DateTimeParser parser = new DateTimeParser(is);

		return parser.getDateTime();
	}

	private static class DateTimeParser extends AbstractStreamParser {

		/** The date time grp index. */

		private static int DATE_TIME_GRP_INDX = 1;

		/** The date time pattern. */

		public static String DATE_TIME_PATTERN = "([0-9]+.{1}[0-9]+.{1}[0-9]+\\s+[0-9]+.{1}[0-9]+.{1}[0-9]+(\\s+[PpMmAa]{2})*)";

		public DateTimeParser(InputStream is) {

			super(is);

		}

		@Override
		protected String getPattern() {

			return DATE_TIME_PATTERN;

		}

		public String getDateTime() {

			if (getMatcher() == null) {

				parse();

			}

			return getMatcher().group(DATE_TIME_GRP_INDX);

		}

	}
}
