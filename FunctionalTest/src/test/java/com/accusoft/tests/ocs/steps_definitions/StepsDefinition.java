package com.accusoft.tests.ocs.steps_definitions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDictionary;
import org.jbehave.core.annotations.*;
import org.json.JSONObject;

import com.accusoft.tests.ocs.common.Constants;
import com.accusoft.tests.ocs.common.utils.JsonUtils;
import com.accusoft.tests.ocs.common.utils.OsUtilities;
import com.accusoft.tests.ocs.common.utils.PdfUtils;
import com.accusoft.tests.ocs.common.utils.TestDefinitionUtils;
import com.accusoft.tests.ocs.steps.Steps;

public class StepsDefinition {

	public static final Logger LOGGER = Logger.getLogger(StepsDefinition.class);

	@SuppressWarnings("rawtypes")
	public static Map serviceResponse;
	public static int returnedCode;
	public static String serviceMessage;
	public static String serviceStatus;

	public static String sourceOfficeFilePath;
	public static String convertedPDFFilePath;

	public static String platformName;
	public static String sourceFolderPath;
	public static String convertedFolderPath;
	public static String referenceFolderPath;
	public static int servicePort;
	public static int pdfcsServicePort;

	// page parameters variables
	public static float pageSizeHorizontal;
	public static float pageSizeVertical;
	public static float pageMarginLeft;
	public static float pageMarginTop;
	public static float pageMarginRight;
	public static float pageMarginBottom;
	public static int pageCount;

	// hyperlinks sets
	public static Map<String, Map<Integer, List<COSDictionary>>> hyperlinksSets = new HashMap<String, Map<Integer, List<COSDictionary>>>();

	// converted pdfs sets
	public static List<String> pdfSet = new ArrayList<String>();

	public static String encoding;

	public static Date dateBeforeConversion = null;
	public static Date dateAfterConversion = null;

	@net.thucydides.core.annotations.Steps
	public Steps stepExecutor;

	@Given("Office conversion service is up and running")
	@Then("Office conversion service is up and running")
	public void getServiceParameters() {

		// Reading service parameters
		sourceFolderPath = OsUtilities.prettifyFilePath(TestDefinitionUtils
				.getOriginalFilePath());
		convertedFolderPath = OsUtilities.prettifyFilePath(TestDefinitionUtils
				.getConvertedFilePath());
		referenceFolderPath = OsUtilities.prettifyFilePath(TestDefinitionUtils
				.getReferenceFolderPath());
		servicePort = TestDefinitionUtils.getServicePort();
		pdfcsServicePort = TestDefinitionUtils.getPDFCSPort();
		platformName = TestDefinitionUtils.getPlatformName();

		LOGGER.info("SourceFolderPath: " + sourceFolderPath);
		LOGGER.info("ConvertedFolderPath: " + convertedFolderPath);
		LOGGER.info("ReferenceFolderPath: " + referenceFolderPath);
		LOGGER.info("ServicePort: " + servicePort);
		LOGGER.info("PdfcsServicePort: " + pdfcsServicePort);
		LOGGER.info("PlatformName: " + platformName);

		// Make sure that PCC is alive
		/*
		 * LOGGER.info("Make sure that PCC is alive"); serviceResponse =
		 * stepExecutor.sendingInfoRequestToPrizmService(); //
		 * 
		 * returnedCode = (Integer) serviceResponse.get("ResponseCode");
		 * serviceMessage = (String) serviceResponse.get("ResponseBody");
		 * 
		 * // set timeout to wait until services is up after restarting Long
		 * timeout = 30l; // timeout in sec
		 * 
		 * if (returnedCode != 200) { try {
		 * LOGGER.info("PCC is not up yet. Wait for " + timeout + " sec");
		 * Thread.sleep(timeout * 1000); // Another attempt
		 * LOGGER.info("Make another attempt to verify PCC status");
		 * serviceResponse = stepExecutor .sendingInfoRequestToPrizmService();
		 * returnedCode = (Integer) serviceResponse.get("ResponseCode");
		 * serviceMessage = (String) serviceResponse.get("ResponseBody"); }
		 * catch (InterruptedException e) { e.printStackTrace(); } }
		 * 
		 * stepExecutor.verifyingServiceResponseCode(returnedCode, 200,
		 * "PCC status verification"); //
		 * stepExecutor.serviceStatusVerify(getPrizmStatusFromResponse
		 * (serviceMessage), // "running", serviceMessage);
		 */

		// Make sure that service is alive
		LOGGER.info("Make sure that service is alive");
		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put("pccisVersion", "10.1.1027.3626");
		serviceResponse = stepExecutor
				.sendingInfoRequestToOfficeConversionService(requestBodyJson
						.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");

		stepExecutor.verifyingServiceResponseCode(returnedCode, 200,
				"OCS state verification");

	}

	@Given("that default config file is used")
	public void setServiceDefaultConfigEncoding() {

		// expected to be empty
	}

	@Pending
	@Given("that pageWidth config parameter equals <pageWidth>\n"
			+ " and pageHeight config parameter equals <pageHeight>\n"
			+ " and pageMarginLeft config parameter equals <pageMarginLeft>\n"
			+ " and pageMarginTop config parameter equals <pageMarginTop>\n"
			+ " and pageMarginRight config parameter equals <pageMarginRight>\n"
			+ " and pageMarginBottom config parameter equals <pageMarginBottom>\n"
			+ " and pageUnits config parameter equals <pageUnits>")
	public void setPageSettings(@Named("pageWidth") float pageWidth,
			@Named("pageHeight") float pageHeight,
			@Named("pageMarginLeft") float pageMarginLeft,
			@Named("pageMarginTop") float pageMarginTop,
			@Named("pageMarginRight") float pageMarginRight,
			@Named("pageMarginBottom") float pageMarginBottom) {

	}

	@Then("server must respond with status $code")
	@Alias("server must respond with status: <code>")
	public void statusShouldBe(@Named("code") int code) {

		String message = "Response from service: " + serviceMessage;
		if (serviceMessage != null) {
			stepExecutor.verifyingServiceResponseCode(returnedCode, code,
					message);
		} else {
			stepExecutor.responseCodeVerify(returnedCode, code);
		}

	}

	@Then("error code must be <errorCode>")
	public void serviceErrorCodeShouldBe(@Named("errorCode") int errorCode) {
		String compileReg = "\\d+";
		int returnedServiceCode = Integer.valueOf(OsUtilities
				.searchRegularExpressions(compileReg, serviceMessage));
		String message = "Response from service: " + serviceMessage;
		if (serviceMessage != null)
			stepExecutor.verifyingServiceResponseErrorCode(returnedServiceCode,
					errorCode, message);
	}

	@Then("error message must be <errorMessage>")
	public void serviceErrorMessageShouldBe(
			@Named("errorMessage") String errorMessage) {
		String compileReg = "(\"message\":).+(\\\")";
		String returnedServiceMessage = OsUtilities.searchRegularExpressions(
				compileReg, serviceMessage);
		String message = "Response from service: " + serviceMessage;

		if (serviceMessage != null)
			stepExecutor.verifyingServiceResponseErrorMessage(
					returnedServiceMessage, errorMessage, message);
	}

	 @Then("file should not be created")
	 public void checkThatConvertidFileDoesNotExist(@Named("file") String fileName,
	 @Named("pageNumber") int pageNumber,
	 @Named("destination") String destination) {
	
	 String rootDir = OsUtilities.prettifyFilePath(convertedFolderPath + "/"
	 + destination + "/");
	
	 int responseAmounts = OsUtilities.getNumberOfConvertedFiles(rootDir);
	
	 stepExecutor.compareNotCreateFile(responseAmounts);
	 }

	@Then("number of converted PDF files for current office document is equal to <amount>")
	public void checkAmountOfConvertedFiles(@Named("file") String fileName,
			@Named("pageNumber") int pageNumber, @Named("amount") int amount,
			@Named("destination") String destination,
			@Named("timeOut") int timeOut) throws InterruptedException {

		String rootDir = OsUtilities.prettifyFilePath(convertedFolderPath + "/"
				+ destination + "/");

		Thread.sleep(timeOut);

		int responseAmounts = OsUtilities.getNumberOfConvertedFiles(rootDir);

		stepExecutor.compareCreateFile(responseAmounts, amount);
	}

	@Then("page count equal to specified in <fileAttributesJson>")
	public void pageCountShouldBe(
			@Named("fileAttributesJson") String fileAttributesJsonName) {

		String pathPrefix = referenceFolderPath;
		String referenceJsonPath = "";

		referenceJsonPath = OsUtilities.getSpecificPlatformPath(platformName,
				fileAttributesJsonName, pathPrefix, referenceJsonPath);

		int pageCountGiven = JsonUtils
				.getPageCountFromFileAttributes(referenceJsonPath);
		String fileAttributesJsonContent = "Attribute file content: "
				+ JsonUtils.getfileAttributesJsonContent(referenceJsonPath)
				+ ", Used attribute file path: " + referenceJsonPath;

		stepExecutor.comparingActualAndExpectedPageCountInSelectedFile(
				pageCount, pageCountGiven, fileAttributesJsonContent);
	}

	@Then("conversion result matches the <referenceImage>")
	public void compareFiles(@Named("referenceImage") String referenceImage)
			throws Exception {

		String precise = "0.2";
		compareFilesWithSpecificPrecise(referenceImage, precise);
	}

	@Then("page in pdf format to be converted to png with a size less than the maximum size <maxsize>")
	public void compareFilesWithMaxSize(@Named("maxsize") float maxSize)
			throws Exception {

		if (returnedCode == 200) {
			// Convert PDF to PNG before compare
			String destFormat = "png";
			String outputFilePath = convertedPDFFilePath + "." + destFormat;

			JSONObject requestBodyJson = new JSONObject();
			JSONObject request = new JSONObject();

			requestBodyJson.put("data", request);
			request.put(Constants.SRC,
					OsUtilities.prettifyFilePath(convertedPDFFilePath));
			request.put(Constants.DEST_FORMAT, destFormat);
			request.put(Constants.DEST,
					OsUtilities.prettifyFilePath(outputFilePath));
			request.put("password", "");
			request.put("pageNumber", 0);

			serviceResponse = stepExecutor.convertToPng(requestBodyJson
					.toString());

			returnedCode = (Integer) serviceResponse.get("ResponseCode");
			serviceMessage = (String) serviceResponse.get("ResponseBody");

			if (returnedCode == 200) {
				File f = new File(outputFilePath);
				float pngSize = (float) (f.length());
				stepExecutor
						.comparingSizePageToPngWithMaxSize(pngSize, maxSize);

			} else {
				LOGGER.error(serviceMessage);
				throw new Exception("Unable to convert pdf to png");
			}
		}
	}

	@Then("conversion result matches the image reference <referenceImage> (difference less $precise percent)")
	@Alias("conversion result matches with template <referenceImage> (difference less $precise percent)")
	public void compareFilesWithSpecificPrecise(
			@Named("referenceImage") String referenceImage,
			@Named("precise") String precise) throws Exception {

		if (returnedCode == 200) {

			// Convert PDF to PNG before compare
			String destFormat = "png";
			float prec = Float.parseFloat(precise);

			String outputFilePath = convertedPDFFilePath + "." + destFormat;

			JSONObject requestBodyJson = new JSONObject();
			JSONObject request = new JSONObject();

			requestBodyJson.put("data", request);
			request.put(Constants.SRC,
					OsUtilities.prettifyFilePath(convertedPDFFilePath));
			request.put(Constants.DEST_FORMAT, destFormat);
			request.put(Constants.DEST,
					OsUtilities.prettifyFilePath(outputFilePath));
			request.put("password", "");
			request.put("pageNumber", 0);

			serviceResponse = stepExecutor.convertToPng(requestBodyJson
					.toString());

			returnedCode = (Integer) serviceResponse.get("ResponseCode");
			serviceMessage = (String) serviceResponse.get("ResponseBody");

			if (returnedCode == 200) {

				String pathPrefix = referenceFolderPath;
				String referenceImagePath = "";

				referenceImagePath = OsUtilities.getSpecificPlatformPath(
						platformName, referenceImage, pathPrefix,
						referenceImagePath);

				stepExecutor.compareImages(referenceImagePath, outputFilePath,
						prec);
			} else {
				LOGGER.error(serviceMessage);
				throw new Exception("Unable to convert pdf to png");
			}
		}
	}

	@Then("conversion result page count equal to specified in <fileAttributesJson>")
	public void checkFileAttrsMatch(
			@Named("fileAttributesJson") String fileAttributesJson) {
		String pathPrefix = referenceFolderPath;
		String referenceJsonPath = "";
		pageCount = 0;

		referenceJsonPath = OsUtilities.getSpecificPlatformPath(platformName,
				fileAttributesJson, pathPrefix, referenceJsonPath);

		int pageCountGiven = JsonUtils
				.getPageCountFromFileAttributes(referenceJsonPath);
		String fileAttributesJsonContent = "Attribute file content: "
				+ JsonUtils.getfileAttributesJsonContent(referenceJsonPath)
				+ ", Used attribute file path: " + referenceJsonPath;

		stepExecutor.comparingActualAndExpectedPageCountInSelectedFile(
				pageCount, pageCountGiven, fileAttributesJsonContent);
	}

	@Then("total number of converted pages must be equal to <expectedCountOfPages>")
	public void checkCountOfConvertedPages(
			@Named("expectedCountOfPages") int expectedCountOfPages) {
		stepExecutor.checkCountOfConvertedPagesFromSelectedFile(
				expectedCountOfPages, sourceOfficeFilePath);
	}

	@Then("total number of pages in converted file must be equal to <expectedCountOfPages>")
	public void checkCountOfPagesInConvertedFile(
			@Named("expectedCountOfPages") int expectedCountOfPages) {

		String filePath = convertedPDFFilePath;

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC, filePath);
		request.put("password", "");

		serviceResponse = stepExecutor
				.sendingGetDocumentAttributesRequest(requestBodyJson.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");

		if (returnedCode == 200) {
			pageCount = JsonUtils.getPageCountFromResponse(serviceMessage);
		}

		stepExecutor.compareActualAndExpectedCountOfPagesInConvertedFile(
				pageCount, expectedCountOfPages, sourceOfficeFilePath);
	}

	@Then("converted PDF file has <expectedCountOfGoToLinks> \"GoTo\" link(s) with page destination value <expectedPageDestinationValue>")
	public void checkAllGoToHyperlinksInPDF(
			@Named("expectedCountOfGoToLinks") int expectedCountOfGoToLinks,
			@Named("expectedPageDestinationValue") int expectedPageDestinationValue) {
		checkGoToHyperlinksInPDF(expectedCountOfGoToLinks, -1,
				expectedPageDestinationValue);
	}

	@Then("converted PDF file has <expectedTotalCountOfGoToLinks> \"GoTo\" link(s)")
	public void calculateHyperlinksInPDF(
			@Named("expectedTotalCountOfGoToLinks") int expectedTotalCountOfGoToLinks) {
		checkGoToHyperlinksInPDF(expectedTotalCountOfGoToLinks, -1, -1);
	}

	@When("all GoTo hyperlinks are extracted from all pages of generated single PDF file and saved to $setName")
	public void extractHyperlinksFromSinglePDF(String setName) {

		hyperlinksSets = new HashMap<String, Map<Integer, List<COSDictionary>>>();
		pdfSet = new ArrayList<String>();

		String filePath = convertedPDFFilePath;

		Map<Integer, List<COSDictionary>> links = stepExecutor
				.extractGoToLinksFromPdfDocument(filePath);

		hyperlinksSets.put(setName, links);

	}

	@When("all GoTo hyperlinks are extracted from all generated PDF files and saved to $setName")
	public void extractHyperlinksFromSetOfPDFFiles(String setName) {

		Map<Integer, List<COSDictionary>> allLinks = new HashMap<Integer, List<COSDictionary>>();

		LOGGER.info("Extracting links from pdfSet with size " + pdfSet.size());

		for (int pageCnt = 0; pageCnt < pdfSet.size(); pageCnt++) {
			Map<Integer, List<COSDictionary>> pageLinks = stepExecutor
					.extractGoToLinksFromPdfDocument(pdfSet.get(pageCnt));

			LOGGER.info("Extracting links from page " + pdfSet.get(pageCnt));
			allLinks.put(pageCnt, pageLinks.get(new Integer(0)));
		}

		hyperlinksSets.put(setName, allLinks);

	}

	@Then("count, location and pageDestination values af hyperlinks between $setName1 and $setName1 must be the same")
	public void compareSetOfHyperlinks(String setName1, String setName2) {
		stepExecutor.compareSetsOfHyperlinks(hyperlinksSets.get(setName1),
				hyperlinksSets.get(setName2));
	}

	@Then("converted PDF file has \"GoTo\" hyperlink with number <linkNumber> and value <expectedPageDestinationValue>")
	public void checkSelectedGoToHyperlinkInPDFOnSelectedPage(
			@Named("linkNumber") int linkNumber,
			@Named("expectedPageDestinationValue") int expectedPageDestinationValue) {

		// NOTE: linkNumber is zero based

		String filePath = convertedPDFFilePath;

		Map<Integer, List<COSDictionary>> links = stepExecutor
				.extractGoToLinksFromPdfDocument(filePath);

		Integer[] actualDestinations = JsonUtils
				.getLinksDestinations(links, -1);

		stepExecutor.checkThatNumberOfLinkIsNotMoreThanCountOfLinkInDocument(
				linkNumber, actualDestinations.length);

		Integer actualPageDestinationValue = actualDestinations[linkNumber];

		stepExecutor.compareActualEndExpectedPageDestinationOfSelectedLink(
				actualPageDestinationValue, expectedPageDestinationValue);

	}

	@Then("converted PDF file has <expectedCountOfGoToLinksOnSelectedPage> \"GoTo\" link(s) on page <selectedPageNumber> with page destination value <expectedPageDestinationValue>")
	public void checkGoToHyperlinksInPDFOnSelectedPage(
			@Named("expectedCountOfGoToLinksOnSelectedPage") int expectedCountOfGoToLinks,
			@Named("selectedPageNumber") int selectedPageNumber,
			@Named("expectedPageDestinationValue") int expectedPageDestinationValue) {
		checkGoToHyperlinksInPDF(expectedCountOfGoToLinks, selectedPageNumber,
				expectedPageDestinationValue);
	}

	public void checkGoToHyperlinksInPDF(int expectedCountOfGoToLinks,
			int selectedPageNumber, int expectedPageDestinationValue) {

		// NOTE: selectedPageNumber is ZERO based

		String filePath = convertedPDFFilePath;

		Map<Integer, List<COSDictionary>> links = null;
		int actualNumberOfGoToLinks = 0;
		if (selectedPageNumber >= 0) {
			links = stepExecutor.extractGoToLinksFromSelectedPageOfPdfDocument(
					filePath, selectedPageNumber);
			actualNumberOfGoToLinks = JsonUtils.getNumberOfLinks(links
					.get(selectedPageNumber));

			stepExecutor
					.compareActualAndExpectedNumberOfGoToLinksOnSelectedPageInsidePdf(
							selectedPageNumber, actualNumberOfGoToLinks,
							expectedCountOfGoToLinks);

			if (expectedPageDestinationValue >= 0) {

				Integer[] actualTestinations = JsonUtils.getLinksDestinations(
						links, selectedPageNumber);
				stepExecutor
						.compareActualAndExpectedValueOfAllGoToLinksOnSelectedPageInsidePdf(
								selectedPageNumber, actualTestinations,
								expectedPageDestinationValue);
			}

		} else {
			links = stepExecutor.extractGoToLinksFromPdfDocument(filePath);
			actualNumberOfGoToLinks = JsonUtils.getNumberOfLinks(links);
			stepExecutor.compareActualAndExpectedNumberOfGoToLinksInPdf(
					actualNumberOfGoToLinks, expectedCountOfGoToLinks);

			if (expectedPageDestinationValue >= 0) {

				Integer[] actualTestinations = JsonUtils.getLinksDestinations(
						links, -1);
				stepExecutor
						.compareActualAndExpectedValueOfAllGoToLinksOnSelectedPageInsidePdf(
								selectedPageNumber, actualTestinations,
								expectedPageDestinationValue);
			}
		}

	}

	@Then("date/time inside converted PDF file has correct value and matches with format <expectedWindowsFormat> on Windows or <expectedRHELFormat> on RHEL or <expectedUbuntuFormat> on Ubuntu or <expectedCentosFormat> on Centos platforms")
	public void checkIfDateTimeIsInAllowedRangeAndMatchesExpectedFormat(
			@Named("expectedWindowsFormat") String dateTimeFormatWindows,
			@Named("expectedRHELFormat") String dateTimeFormatRHEL,
			@Named("expectedUbuntuFormat") String dateTimeFormatUbuntu,
			@Named("expectedCentosFormat") String dateTimeFormatCentos)
			throws Exception {

		String dateTimeFormat = null;

		if (OsUtilities.isUbuntu()) {
			dateTimeFormat = dateTimeFormatUbuntu;
		} else if (OsUtilities.isCentos()) {
			dateTimeFormat = dateTimeFormatWindows;
		} else if (OsUtilities.isRHEL()) {
			dateTimeFormat = dateTimeFormatRHEL;
		} else if (OsUtilities.isWindows()) {
			dateTimeFormat = dateTimeFormatWindows;
		}

		String contentOfPDFFile = PdfUtils.getTextFromPDF(convertedPDFFilePath,
				1, 1);

		String dateTimeFromPDF = stepExecutor
				.extractDateTimeFromPDFContent(contentOfPDFFile);

		LOGGER.info("Content of PDF file [" + contentOfPDFFile
				+ "] contains dateTime string [" + dateTimeFromPDF + "]");

		Date dateFromPDF = stepExecutor.extractDateFromString(dateTimeFromPDF,
				dateTimeFormat);

		LOGGER.info("Parsed date in universal format is "
				+ (new SimpleDateFormat("dd.MM.yyyy HH:mm"))
						.format(dateFromPDF));

		stepExecutor.checkIfDateTimeIsInAllowedRange(dateFromPDF,
				dateBeforeConversion, dateAfterConversion);

	}

	@BeforeStory
	public void beforeStory() {
	}

	@BeforeStories
	public void beforeStories() {
		// Refresh content folder with files for testing
		String pathToFilesForTestingPath = OsUtilities
				.prettifyFilePath(TestDefinitionUtils.getOriginalFilePath());

		// File pathToFilesForTestingDir = new File(pathToFilesForTestingPath);
		// try {
		// FileUtils.cleanDirectory(pathToFilesForTestingDir);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// String zipFilePath = StepsDefinition.class.getClassLoader()
		// .getResource(".").getPath()
		// + "/../../filesForTesting.zip";
		//
		// try {
		// FSUtils.UnzipUtility.unzip(new File(zipFilePath), new File(
		// pathToFilesForTestingPath));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

}
