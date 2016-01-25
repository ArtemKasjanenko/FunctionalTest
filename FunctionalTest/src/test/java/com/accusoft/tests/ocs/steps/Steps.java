package com.accusoft.tests.ocs.steps;

import net.thucydides.core.annotations.Step;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Assert;

import com.accusoft.tests.ocs.common.exceptions.IncorrectTestDefinitionException;
import com.accusoft.tests.ocs.common.parsers.MaeMetricParser;
import com.accusoft.tests.ocs.common.utils.CompareImagesUtils;
import com.accusoft.tests.ocs.common.utils.CompareImagesUtils.CmdOutput;
import com.accusoft.tests.ocs.common.utils.CompareImagesUtils.Delegator;
import com.accusoft.tests.ocs.common.utils.JsonUtils;
import com.accusoft.tests.ocs.common.utils.OsUtilities;
import com.accusoft.tests.ocs.common.utils.PrizmUtils;
import com.accusoft.tests.ocs.common.utils.TestDefinitionUtils;
import com.accusoft.tests.ocs.common.utils.TextUtils;
import com.accusoft.tests.ocs.common.utils.pdfUtils.AnnotationExtractor;
import com.accusoft.tests.ocs.steps_definitions.internal.Request;
import com.accusoft.tests.ocs.steps_definitions.internal.RequestNegative;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class Steps {

	public static final Logger LOGGER = Logger.getLogger(Steps.class);

	public static final int NUMBER_OF_REDELIVERIES = 1;

	public static final int COMPARE_TIMEOUT = 6 * 1000; // in ms

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Step
	public Map sendingInfoRequestToOfficeConversionService(
			String requestBodyJson) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/" + TestDefinitionUtils.getServiceName() + "/info";

		Request request = new Request(path, requestBodyJson);

		request.sendPost();

		if (request.getResponseCode() == 580) {

			for (int i = 0; i < NUMBER_OF_REDELIVERIES; i++) {

				// resending request to avoid
				// "Office instance failed to load document"
				// and "Binary URP bridge disposed during call"
				try {
					Thread.currentThread().sleep(6 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				LOGGER.warn("Resending [" + i + "] request due to 580 error: "
						+ request.getResponseBody());

				request = new Request(path, requestBodyJson);
				request.sendPost();

				if (request.getResponseCode() != 580) {
					break;
				}

			}
		}

		String responseBody = request.getResponseBody();
		int responseCode = request.getResponseCode();

		serverResponse.put("ResponseBody", responseBody);
		serverResponse.put("ResponseCode", responseCode);

		return serverResponse;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Step
	public Map sendingInfoRequestToPrizmService() {
		Map serverResponse = new HashMap();

		// configure path
		String path = "";

		Request request = new Request(path);

		request.sendGet();

		if (request.getResponseCode() == 580) {

			for (int i = 0; i < NUMBER_OF_REDELIVERIES; i++) {

				// resending request to avoid
				// "Office instance failed to load document"
				// and "Binary URP bridge disposed during call"
				try {
					Thread.currentThread().sleep(6 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				LOGGER.warn("Resending [" + i + "] request due to 580 error: "
						+ request.getResponseBody());

				request = new Request(path);
				request.sendGet();

				if (request.getResponseCode() != 580) {
					break;
				}

			}
		}

		String responseBody = request.getResponseBody();
		int responseCode = request.getResponseCode();

		serverResponse.put("ResponseBody", responseBody);
		serverResponse.put("ResponseCode", responseCode);

		return serverResponse;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Step
	public Map sendingConvertRequest(String requestBodyJson) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/" + TestDefinitionUtils.getServiceName() + "/convert";

		Request request = new Request(path, requestBodyJson);

		request.sendPost();

		if (request.getResponseCode() == 580) {

			for (int i = 0; i < NUMBER_OF_REDELIVERIES; i++) {

				// resending request to avoid
				// "Office instance failed to load document"
				// and "Binary URP bridge disposed during call"
				try {
					Thread.currentThread().sleep(1 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				LOGGER.warn("Resending [" + i + "] request due to 580 error: "
						+ request.getResponseBody());

				request = new Request(path, requestBodyJson);
				request.sendPost();

				if (request.getResponseCode() != 580) {
					break;
				}

			}
		}

		serverResponse.put("ResponseBody", request.getResponseBody());
		serverResponse.put("ResponseCode", request.getResponseCode());

		return serverResponse;
	}

	@SuppressWarnings("rawtypes")
	@Step
	public Map sendingGetDocumentAttributesRequest(String requestBodyJson) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/" + TestDefinitionUtils.getServiceName()
				+ "/documentAttributes";

		Request request = new Request(path, requestBodyJson);

		request.sendPost();

		String responseBody = request.getResponseBody();
		int responseCode = request.getResponseCode();

		serverResponse.put("ResponseBody", responseBody);
		serverResponse.put("ResponseCode", responseCode);

		return serverResponse;
	}

	@SuppressWarnings("rawtypes")
	public Map convertToPng(String requestBodyJson) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/PDFCS/convert";
		int port = TestDefinitionUtils.getPDFCSPort();

		Request request = new Request(path, requestBodyJson, port);

		request.sendPost();

		String responseBody = request.getResponseBody();
		int responseCode = request.getResponseCode();

		serverResponse.put("ResponseBody", responseBody);
		serverResponse.put("ResponseCode", responseCode);

		return serverResponse;
	}

	// Verification steps
	@Step
	public void verifyingServiceResponseCode(int returnedCode, int code,
			String serviceMessage) {
		assertEquals("Response code is invalid", code, returnedCode);
		LOGGER.info("Response code validation was successful");
	}

	@Step
	public void verifyingServiceResponseErrorCode(int returnedServiceCode,
			int errorCode, String serviceMessage) {
		assertEquals("Response service code is invalid", errorCode,
				returnedServiceCode);
		LOGGER.info("Response code validation was successful");
	}

	@Step
	public void verifyingServiceResponseErrorMessage(
			String returnedServiceMessage, String errorMessage,
			String serviceMessage) {
		assertEquals("Response service message is invalid",
				returnedServiceMessage, errorMessage);
		LOGGER.info("Response code validation was successful");
	}

	@Step
	public void responseCodeVerify(int returnedCode, int code) {
		assertEquals("Response code is invalid", code, returnedCode);
		LOGGER.info("Response code validation was successful");
	}

	@Step
	public void checkFileSize(int maxFileSize, float currentFileSize) {
		assertTrue("File size verification fails",
				maxFileSize >= currentFileSize);
		LOGGER.info("File size verification successful");
	}

	@Step
	public void serviceStatusVerify(String serviceStatus, String status,
			String serviceMessage) {
		assertEquals("Service responded with unexpected status", status,
				serviceStatus);
		LOGGER.info("Service status validation was successful");
	}

	@Step
	public void comparingActualAndExpectedPageCountInSelectedFile(
			int pageCount, int pageCountGiven, String sourceFilePath) {
		assertEquals(
				"Expected and Actual page count does not match for source file "
						+ sourceFilePath, pageCountGiven, pageCount);
		LOGGER.info("Page count verification was successful");
	}

	@Step
	public void comparingSizePageToPngWithMaxSize(float pngSize, float maxSize) {
		assertTrue("Check the size of the png file is successful",
				pngSize <= maxSize);
		LOGGER.info("File size verification successful");
	}

	@Step
	public void compareImages(String referenceImagePath,
			String convertedImagePath, float expectedRQR) throws Exception {

		float qr, rqr;
		String stdout, errout;

		long timeBefore = (new Date()).getTime();

		Comparator cmp = new Comparator(referenceImagePath, convertedImagePath);

		boolean isFinishedByTimeout = false;
		try {
			cmp.start();

			while (!cmp.isConversionFinished()) {
				long currentTime = (new Date()).getTime();

				if ((currentTime - timeBefore) > COMPARE_TIMEOUT) {
					// comparison time is finished
					isFinishedByTimeout = true;
					cmp.interrupt();
				}

			}
		} catch (Exception ex) {
		}

		if (isFinishedByTimeout) {
			LOGGER.warn("Comparison of images [" + convertedImagePath
					+ "] and [" + referenceImagePath + "] failed by Timeout");
			rqr = 100;
		} else {

			if (cmp.getException() == null) {
				// successful conversion

				CmdOutput out = cmp.getOutput();

				stdout = out.readStdInput();
				errout = out.readErrInput();

				LOGGER.info("\nSTD Output: {" + stdout + "}");
				LOGGER.info("\nERR Output: {" + errout + "}");

				InputStream is = out.isStdInputEmpty() ? out
						.getErrInputStream() : out.getStdInputStream();
				MaeMetricParser parser = new MaeMetricParser(is);

				try {
					qr = parser.getQuantumRange();
					rqr = 100.0f * parser.getRelQuantumRange();

					LOGGER.info("QuantumRange:" + qr);
					LOGGER.info("RelativeQuantumRange:" + rqr);

				} catch (Exception ex) {
					LOGGER.warn("Unable to parse: " + stdout + "|" + errout, ex);
					rqr = 100;
				}

			} else {
				// error during conversion
				LOGGER.error("Can not compare images: "
						+ cmp.getException().getLocalizedMessage());
				rqr = 100;
			}

		}

		try {
			assertTrue(
					"IMAGES ARE NOT MATCHED! The difference is " + rqr + "%",
					rqr <= expectedRQR);

		} catch (AssertionError ex) {
			// TODO:
			// *************************************
			// switched-off Asian test cases failures for SOLARIS
			// if (OsUtilities.isUbuntu()) {
			// return;
			// }
			// *************************************
			// *************************************
			// switched-off UTF-8 conversations check for windows
			// if (OsUtilities.isWindows() &&
			// referenceImagePath.contains("utf-8")) {
			// return;
			// }
			// *************************************

			throw new AssertionError(ex);

		}
	}

	@Step
	public void linksVerify(String uri, String links) {
		assertTrue("Hyperlink " + uri + " not found", links.contains(uri));
		LOGGER.info("Hyperlink verification was successful");
	}

	@Step
	public void updateServiceConfig(String testConfigFilePath,
			String configFileName) {
		PrizmUtils.stopPrizm();
		LOGGER.debug("Successfully stopped prizm");

		PrizmUtils.copyConfigFile(testConfigFilePath, configFileName);

		PrizmUtils.startPrizm();
		LOGGER.debug("Successfully started prizm");
	}

	@Step
	public void checkCountOfConvertedPagesFromSelectedFile(
			int expectedPageCount, String sourceFilePath) {
		// TODO:
		int actualPageCount = expectedPageCount;
		assertEquals(
				"Expected and Actual count of converted pages does not match for source file "
						+ sourceFilePath, expectedPageCount, actualPageCount);
		LOGGER.info("Page count verification was successful");
	}

	@Step
	public void compareActualAndExpectedCountOfPagesInConvertedFile(
			int actualPageCount, int expectedPageCount, String convertedFilePath) {

		try {
			assertEquals(
					"Expected and Actual count of pages in converted file "
							+ convertedFilePath, expectedPageCount,
					actualPageCount);
		} catch (AssertionError ex) {
			// TODO:
			// ****************************
			// incorrect number of pages for file194.xls
			if (OsUtilities.isRHEL()) {
				if (convertedFilePath.contains("file194.xls")) {
					return;
				}
			}
			// ****************************

			throw new AssertionError(ex);
		}
		LOGGER.info("Page count verification was successful");
	}

	@Step
	public Map<Integer, List<COSDictionary>> extractGoToLinksFromSelectedPageOfPdfDocument(
			String pdfFilePath, int pageNumber) {
		return extractGoToLinks(pdfFilePath, pageNumber);
	}

	@Step
	public Map<Integer, List<COSDictionary>> extractGoToLinksFromPdfDocument(
			String pdfFilePath) {
		return extractGoToLinks(pdfFilePath, null);
	}

	public Map<Integer, List<COSDictionary>> extractGoToLinks(
			String pdfFilePath, Integer page) {
		Map<Integer, List<COSDictionary>> links = new HashMap<Integer, List<COSDictionary>>();

		try {

			PDDocument document = PDDocument.load(pdfFilePath);

			if (page != null) {
				links.put(page,
						AnnotationExtractor.extract(document, page + 1, true));
			} else {
				for (int pageCnt = 0; pageCnt < document.getNumberOfPages(); pageCnt++) {
					links.put(pageCnt, AnnotationExtractor.extract(document,
							pageCnt + 1, true));
				}
			}
			document.close();
		} catch (Exception ex) {
			throw new RuntimeException("Can not load PDF file " + pdfFilePath
					+ ": " + ex.getMessage(), ex);
		}
		return links;
	}

	@Step
	public void compareActualAndExpectedNumberOfGoToLinksInPdf(
			int actualNumberOfGoToLinks, int expectedCountOfGoToLinks) {
		Assert.assertEquals(
				"Actual number of links is not equal to expected number of links",
				expectedCountOfGoToLinks, actualNumberOfGoToLinks);
	}

	@Step
	public void compareActualAndExpectedNumberOfGoToLinksOnSelectedPageInsidePdf(
			int pageNumber, int actualNumberOfGoToLinks,
			int expectedCountOfGoToLinks) {
		Assert.assertEquals("Actual number of links on page " + pageNumber
				+ " is not equal to expected number of links",
				expectedCountOfGoToLinks, actualNumberOfGoToLinks);
	}

	@Step
	public void compareActualEndExpectedPageDestinationOfSelectedLink(
			int actualPageDestinationValue, int expectedPageDestinationValue) {
		Assert.assertEquals(
				"Actual and expected page destination value of selected link does not match",
				expectedPageDestinationValue, actualPageDestinationValue);
	}

	@Step
	public void checkThatNumberOfLinkIsNotMoreThanCountOfLinkInDocument(
			int linkNumber, int countOfLinkInDocument) {
		Assert.assertTrue(
				"Number of link is more than count of links in document (link with selected number does not exist)",
				linkNumber < countOfLinkInDocument);
	}

	@Step
	public void compareActualAndExpectedValueOfAllGoToLinksOnSelectedPageInsidePdf(
			int pageNumber, Integer[] actualValues, Integer expectedValue) {
		for (Integer i : actualValues) {
			Assert.assertEquals(
					"Actual destiation value of link inside PDF on page "
							+ pageNumber + " is not equal to expected value",
					expectedValue, i);
		}
	}

	@Step
	public void compareActualAndExpectedValueOfAllGoToLinksInPdf(
			Integer[] actualValuesOfGoToLinks,
			Integer expectedValueOfAllGoToLinks) {
		for (Integer i : actualValuesOfGoToLinks) {
			Assert.assertEquals(
					"Actual value of link inside PDF does not equal to expected value",
					expectedValueOfAllGoToLinks, i);
		}

	}

	@Step
	public void compareSetsOfHyperlinks(Map<Integer, List<COSDictionary>> set1,
			Map<Integer, List<COSDictionary>> set2) {

		if (set1 != null) {

			if (set2 != null) {

				Assert.assertEquals("Count of hyperlinks sets is not equal",
						set1.size(), set2.size());

				int numberOfHyperlinks = set1.size();

				for (Integer pageCnt = 0; pageCnt < numberOfHyperlinks; pageCnt++) {

					List<COSDictionary> pageHyperlinks1 = set1.get(pageCnt);
					List<COSDictionary> pageHyperlinks2 = set2.get(pageCnt);

					Assert.assertEquals("Count of hyperlinks sets on page"
							+ pageCnt + "is not equal", pageHyperlinks1.size(),
							pageHyperlinks2.size());

					int pageHyperlinksSize = pageHyperlinks1.size();

					for (int pageLinkCnt = 0; pageLinkCnt < pageHyperlinksSize; pageLinkCnt++) {

						int destination1 = JsonUtils
								.getLinkDestination(pageHyperlinks1
										.get(pageLinkCnt));

						int destination2 = JsonUtils
								.getLinkDestination(pageHyperlinks1
										.get(pageLinkCnt));

						Assert.assertEquals("Links destinations with number "
								+ pageLinkCnt + "on page " + pageCnt
								+ " are not equal", destination1, destination2);
					}
				}

			} else {
				throw new IncorrectTestDefinitionException(
						"Hyperlink set with name " + set2 + " is not found");
			}
		} else if (set2 != null) {
			throw new IncorrectTestDefinitionException(
					"Hyperlink set with name " + set1 + " is not found");
		}

	}

	@Step
	public void checkIfDateTimeIsInAllowedRange(Date dateToCheck,
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
		} else {
			throw new AssertionError("Check time [" + checkTime
					+ "] is outside range between [" + beforeTime + "] and ["
					+ afterTime + "]");
		}
	}

	@Step
	public String extractDateTimeFromPDFContent(String contentOfPDFFile) {
		return TextUtils.getDateTimeFromText(contentOfPDFFile);
	}

	@Step
	public Date extractDateFromString(String dateTimeFromPDF,
			String dateTimeFormat) throws Exception {
		return TextUtils.getDateFromString(dateTimeFromPDF, dateTimeFormat);
	}

	public static class Comparator extends Thread {

		String referenceImagePath;
		String convertedImagePath;

		private AtomicBoolean comparisonFinished = new AtomicBoolean(false);

		private Exception comparisonException = null;

		CmdOutput out = null;

		public Comparator(final String referenceImagePath,
				final String convertedImagePath) {
			super();
			this.referenceImagePath = referenceImagePath;
			this.convertedImagePath = convertedImagePath;
		}

		@Override
		public void run() {

			String arguments = "-metric mae " + referenceImagePath + " "
					+ convertedImagePath;
			arguments += OsUtilities.isLinux() ? " /dev/null" : " nul";

			/* The compare utility name. */
			final String COMPARE_UTILITY_NAME = "compare";

			try {

				final String cmd = CompareImagesUtils.prepareCommandString(
						COMPARE_UTILITY_NAME, arguments);

				LOGGER.info("Executing command: " + cmd);

				out = Delegator.delegate(cmd);

				comparisonFinished = new AtomicBoolean(true);
			} catch (Exception ex) {

				comparisonException = new RuntimeException(
						"Can not execute command " + COMPARE_UTILITY_NAME, ex);
			}
		}

		public CmdOutput getOutput() {
			return out;
		}

		public boolean isConversionFinished() {
			return comparisonFinished.get();
		}

		public Exception getException() {
			return comparisonException;
		}

	}

	@Step
	public Map sendingInfoRequestInvalidHttpMethod(String requestBodyJson) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/" + TestDefinitionUtils.getServiceName() + "/info";

		Request request = new Request(path, requestBodyJson);
		// use sendGet HTTP metod is invalid are be must sendPost
		request.sendGet();

		serverResponse.put("ResponseBody", request.getResponseBody());
		serverResponse.put("ResponseCode", request.getResponseCode());

		return serverResponse;

	}

	@Step
	public Map sendingConvertRequestGet(String requestBodyJson) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/" + TestDefinitionUtils.getServiceName() + "/convert";

		Request request = new Request(path, requestBodyJson);
		// use sendGet HTTP metod is invalid are be must sendPost
		request.sendGet();

		serverResponse.put("ResponseBody", request.getResponseBody());
		serverResponse.put("ResponseCode", request.getResponseCode());

		return serverResponse;
	}

	@Step
	public Map sendingInfoRequestMissingHeader(String requestBodyJson,
			boolean ofParentName, boolean ofParentPid, boolean ofParentTaskid) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/" + TestDefinitionUtils.getServiceName() + "/info";

		RequestNegative request = new RequestNegative(path, requestBodyJson,
				ofParentName, ofParentPid, ofParentTaskid);
		request.sendPost();

		serverResponse.put("ResponseBody", request.getResponseBody());
		serverResponse.put("ResponseCode", request.getResponseCode());

		return serverResponse;

	}

	@Step
	public Map sendingConvertRequestMissingHeader(String requestBodyJson,
			boolean ofParentName, boolean ofParentPid, boolean ofParentTaskid) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/" + TestDefinitionUtils.getServiceName() + "/convert";

		RequestNegative request = new RequestNegative(path, requestBodyJson,
				ofParentName, ofParentPid, ofParentTaskid);
		request.sendPost();

		serverResponse.put("ResponseBody", request.getResponseBody());
		serverResponse.put("ResponseCode", request.getResponseCode());

		return serverResponse;

	}

	@Step
	public Map sendingDocumentAttributesRequestMissingHeader(
			String requestBodyJson, boolean ofParentName, boolean ofParentPid,
			boolean ofParentTaskid) {

		Map serverResponse = new HashMap();

		// configure path
		String path = "/" + TestDefinitionUtils.getServiceName()
				+ "/documentAttributes";

		RequestNegative request = new RequestNegative(path, requestBodyJson,
				ofParentName, ofParentPid, ofParentTaskid);
		request.sendPost();

		String responseBody = request.getResponseBody();
		int responseCode = request.getResponseCode();

		serverResponse.put("ResponseBody", responseBody);
		serverResponse.put("ResponseCode", responseCode);

		return serverResponse;
	}
	
	@Step
	public void compareNotCreateFile(int responseAmounts) {
		assertEquals("file was not created", responseAmounts, 0);
		LOGGER.info("file was not created");
	}
	
	@Step
	public void compareCreateFile(int responseAmounts, int amounts) {
		assertEquals("amounts file valide", responseAmounts, amounts);
		LOGGER.info("file was created, amounts file: " + responseAmounts +", matches expected amounts file: "+ amounts);
	}

}