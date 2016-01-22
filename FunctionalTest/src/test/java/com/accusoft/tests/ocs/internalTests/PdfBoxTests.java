package com.accusoft.tests.ocs.internalTests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.accusoft.tests.ocs.common.parsers.AbstractStreamParser;
import com.accusoft.tests.ocs.steps.Steps;

public class PdfBoxTests {

	// private static String PDF_DATE_TIME = "C:/!!!/dateTime.xlsx.page.0.pdf";

	private static String DEFAULT_FORMAT_PATTERN = "MM/dd/yyyy hh:mm";

	public static final Logger LOGGER = Logger.getLogger(Steps.class);

	private static Date dateBeforeConversion = null;
	private static Date dateAfterConversion = null;

	private static String pathToConvertedPDF = null;

	private static String formatPattern = null;

	public static void main(String[] args) {

		try {

//			// check XLSX
//			pathToConvertedPDF = "d:/Development/Accusoft/tasks/PCC-UNKNOWN/testFiles/convertedPdfFiles/dateTime.xlsx.page.0.pdf";
//			formatPattern = "MM/dd/yy hh:mm:ss a";
//			doConversionToPDFTest();

//			// check DOCX
//			pathToConvertedPDF = "d:/Development/Accusoft/tasks/PCC-UNKNOWN/testFiles/convertedPdfFiles/dateTime.docx.page.0.pdf";
//			formatPattern = DEFAULT_FORMAT_PATTERN;
//			doConversionToPDFTest();
//
//			// check PPTX
//			pathToConvertedPDF = "d:/Development/Accusoft/tasks/PCC-UNKNOWN/testFiles/convertedPdfFiles/dateTime.pptx.page.0.pdf";
//			formatPattern = "MM/dd/yy hh:mm";
//			doConversionToPDFTest();
			
			// check PPTM
			pathToConvertedPDF = "d:/Development/Accusoft/tasks/PCC-UNKNOWN/testFiles/convertedPdfFiles/PowerPoint/dateTime.pptm.pdf";
			formatPattern = "MM/dd/yy hh:mm:ss a";
			doConversionToPDFTest();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean compareDateTime(String dateTimeString1,
			String dateTimeFormat1, String dateTimeString2,
			String dateTimeFormat2) throws Exception {

		SimpleDateFormat inFormat1 = new SimpleDateFormat(dateTimeFormat1);
		SimpleDateFormat inFormat2 = new SimpleDateFormat(dateTimeFormat2);

		SimpleDateFormat uniFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");

		Date dateTime1 = inFormat1.parse(dateTimeString1);
		Date dateTime2 = inFormat2.parse(dateTimeString2);

		String uniString1 = uniFormat.format(dateTime1);
		String uniString2 = uniFormat.format(dateTime2);

		if (uniString1.compareTo(uniString2) == 0) {
			return true;
		} else {
			System.out.println("DateTime [" + dateTimeString1
					+ "] does not match with dateTime [" + dateTimeString2
					+ "]");
		}

		return false;
	}

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

	public static boolean checkIfDateTimeIsInAllowedRange(Date dateToCheck,
			Date dateBefore, Date dateAfter) throws IllegalArgumentException {

		if (dateToCheck == null) {
			throw new IllegalArgumentException("Date to check can not be null");
		}

		if (dateBefore == null) {
			throw new IllegalArgumentException("Date before can not be null");
		}

		if (dateAfter == null) {
			throw new IllegalArgumentException("Date after can not be null");
		}

		long checkTime = dateToCheck.getTime();
		long beforeTime = dateBefore.getTime();
		long afterTime = dateAfter.getTime();

		if (checkTime >= beforeTime && checkTime <= afterTime) {
			LOGGER.info("Check time [" + checkTime
					+ "] is inside range between [" + beforeTime + "] and ["
					+ afterTime + "]");
			return true;
		}

		LOGGER.info("Check time [" + checkTime + "] is outside range between ["
				+ beforeTime + "] and [" + afterTime + "]");

		return false;

	}

	@When("user sends request to convert single page Office document <file> with one date/Time field to PDF format")
	public static void convertOfficeDocumentWithDateTimeToPDFFormat(
			@Named("file") String fileName) {

		dateBeforeConversion = new Date();

		// perform conversion to PDF
		// requestConvertWithSomeParameters(fileName, "pdf");

		dateAfterConversion = new Date();
	}

	@Then("date/time inside converted PDF file has correct value and matches with format <expectedFormat>")
	public static void checkIfDateTimeIsInAllowedRangeAndMatchesExpectedFormat(
			@Named("expectedFormat") String dateTimeFormat) throws Exception {

		// get path to converted pdf file
		// String pathToConvertedPDF = pathToConvertedPDF;

		String contentOfPDFFile = getTextFromPDF(pathToConvertedPDF, 1, 1);

		String dateTimeFromPDF = getDateTimeFromText(contentOfPDFFile);//"26/8/15 08:04:18 PM"; //getDateTimeFromText(contentOfPDFFile);

		LOGGER.info("Content of PDF file [" + contentOfPDFFile
				+ "] contains dateTime string [" + dateTimeFromPDF + "]");

		Date dateFromPDF = getDateFromString(dateTimeFromPDF, dateTimeFormat);

		LOGGER.info("Parsed date in universal format is "
				+ (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"))
						.format(dateFromPDF));
		
		System.out.println();

		if (checkIfDateTimeIsInAllowedRange(dateFromPDF, dateBeforeConversion,
				dateAfterConversion)) {
		}

	}

	public static String getDateTimeFromText(String text) {
		ByteArrayInputStream is = new ByteArrayInputStream(text.getBytes());

		DateTimeParser parser = new DateTimeParser(is);

		return parser.getDateTime();
		
	}

	public static String getTextFromPDF(String absPathToPDF, int startPage,
			int endPage) {
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File(absPathToPDF);

		String parsedText = null;
		PDFParser parser = null;
		try {
			parser = new PDFParser(new FileInputStream(file));
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(startPage);
			pdfStripper.setEndPage(endPage);
			parsedText = pdfStripper.getText(pdDoc);
		} catch (Exception ex) {
			System.out
					.println("Something is wrong " + ex.getLocalizedMessage());
		} finally {
			if (parser != null) {
				try {
					pdDoc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (parsedText != null) {
			parsedText = parsedText.trim();
		}

		return parsedText;
	}

	public static void doConversionToPDFTest() throws Exception {
		convertOfficeDocumentWithDateTimeToPDFFormat("");
		checkIfDateTimeIsInAllowedRangeAndMatchesExpectedFormat(formatPattern);
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
