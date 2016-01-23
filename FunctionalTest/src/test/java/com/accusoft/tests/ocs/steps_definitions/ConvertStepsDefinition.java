package com.accusoft.tests.ocs.steps_definitions;

import java.util.Date;

import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.json.JSONObject;

import com.accusoft.tests.ocs.common.Constants;
import com.accusoft.tests.ocs.common.utils.JsonUtils;
import com.accusoft.tests.ocs.common.utils.OsUtilities;
import com.accusoft.tests.ocs.steps.Steps;
import com.accusoft.tests.ocs.steps_definitions.StepsDefinition;
import com.gargoylesoftware.htmlunit.WebConsole.Logger;

public class ConvertStepsDefinition extends StepsDefinition {

	@When("user sends request to convert page 0 of office file <file> to $format format")
	@Aliases(values = {
			"user sends request to convert page 0 of office file <file> with different content to $format format",
			"user sends request to convert page 0 of office file <file> with local references to $format format",
			"user sends request to convert page 0 of office file <file> with unsupported content to $format format",
			"user sends request to convert page 0 of unsupported file <file> to $format format",
			"user sends request to convert single page Excel document <file> with one internal HyperLinks to $format format",
			"user sends request to convert single page LOCalc document <file> with one internal HyperLinks to $format format",
			"user sends request to convert single page Word document <file> with one internal HyperLinks to $format format",
			"user sends request to convert single page LOWritter document <file> with one internal HyperLinks to $format format",
			"user sends request to convert single page PowerPoint document <file> with one internal HyperLinks to $format format",
			"user sends request to convert single page LOImpress document <file> with one internal HyperLinks to $format format",
			"user sends request to convert page 0 of office file <file> with not supported $format format" })
	public void requestConvertWithSomeParameters(
			@Named("file") String fileName, @Named("format") String destFormat) {

		generalConversionRequest(fileName, destFormat, 0, null);
	}

	@When("user sends request to convert page 0 from password protected Word file <file> to PDF format with correct password <password>")
	@Aliases(values = {
			"user sends request to convert page 0 from password protected Excel file <file> to PDF format with correct password <password>",
			"user sends request to convert page 0 from password protected PowerPoint file <file> to PDF format with correct password <password>",
			"user sends request to convert page 0 from password protected LOCalc file <file> to PDF format with correct password <password>",
			"user sends request to convert page 0 from password protected LOImpress file <file> to PDF format with correct password <password>",
			"user sends request to convert page 0 from password protected LOWritter file <file> to PDF format with correct password <password>" })
	public void requestConvertOfPasswordProtectedDocumentToPdfFormat(
			@Named("file") String fileName, @Named("password") String password) {
		generalConversionRequest(fileName, "pdf", 0, password);
	}

	@When("user sends request to convert page 0 from password protected Word file <file> to PDF format with wrong password <password>")
	@Aliases(values = {
			"user sends request to convert page 0 from password protected Excel file <file> to PDF format with wrong password <password>",
			"user sends request to convert page 0 from password protected PowerPoint file <file> to PDF format with wrong password <password>",
			"user sends request to convert page 0 from password protected LOCalc file <file> to PDF format with wrong password <password>",
			"user sends request to convert page 0 from password protected LOImpress file <file> to PDF format with wrong password <password>",
			"user sends request to convert page 0 from password protected LOWritter file <file> to PDF format with wrong password <password>" })
	public void requestConvertOfPasswordProtectedDocumentToPdfFormatWithIncorrectPassword(
			@Named("file") String fileName, @Named("password") String password) {
		generalConversionRequest(fileName, "pdf", 0, password);
	}

	@Then("converted file must be not created")
	public void checkThatConvertedFileDoesNotExist() {
		return;
	}

	public void generalConversionRequest(String fileName, String destFormat,
			int pageNumber, String password) {

		sourceOfficeFilePath = OsUtilities.prettifyFilePath(sourceFolderPath
				+ fileName);
		String outputFile = fileName + ".page." + pageNumber + "." + destFormat;
		convertedPDFFilePath = OsUtilities.prettifyFilePath(convertedFolderPath
				+ outputFile);

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC, sourceOfficeFilePath);
		request.put(Constants.DEST_FORMAT, destFormat);
		request.put(Constants.OUTPUT_TEMPLATE, convertedPDFFilePath);

		request.put("password", (password == null) ? "" : password);
		request.put("pageNumber", pageNumber);
		// request.put("ignorePageNumber", true);

		serviceResponse = stepExecutor.sendingConvertRequest(requestBodyJson
				.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");

		LOGGER.info("Service response code: " + returnedCode);
		LOGGER.info("Service response message: " + serviceMessage);

	}

	@SuppressWarnings("static-access")
	@When("user sends request to convert single page Office document <file> with one dateTime field to PDF format")
	public void convertOfficeDocumentWithDateTimeToPDFFormat(
			@Named("file") String fileName) throws Exception {

		dateBeforeConversion = new Date();

		Thread.currentThread().sleep(1 * 1000);

		// perform conversion to PDF
		requestConvertWithSomeParameters(fileName, "pdf");

		Thread.currentThread().sleep(1 * 1000);

		dateAfterConversion = new Date();
	}

	@When("user sends request to convert page <pageNumber> of text document <file> with charset <charset> and content language <language> to pdf format")
	public void requestConvertWithSomeParameters(
			@Named("file") String fileName, @Named("charset") String charset,
			@Named("language") String language,
			@Named("pageNumber") int pageNumber) {
		requestConvertWithSomeConversionParameters(fileName, pageNumber,
				charset, language, null, null);
	}

	@When("user sends request to convert page <pageNumber> of text document <file> with charset <charset> and content language <language> to pdf format with additional request parameter $additionalAttributeName with value $additionalAttributeValue")
	public void requestConvertWithSomeConversionParameters(
			@Named("file") String fileName,
			@Named("pageNumber") int pageNumber,
			@Named("charset") String charset,
			@Named("language") String language,
			@Named("additionalAttributeName") String additionalAttributeName,
			@Named("additionalAttributeValue") String additionalAttributeValue) {

		String destFormat = "pdf";
		sourceOfficeFilePath = sourceFolderPath + fileName;
		String outputFile = fileName + ".page." + pageNumber + "." + destFormat;
		convertedPDFFilePath = convertedFolderPath + outputFile;

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC,
				OsUtilities.prettifyFilePath(sourceOfficeFilePath));
		request.put(Constants.DEST_FORMAT, destFormat);
		request.put(Constants.OUTPUT_TEMPLATE,
				OsUtilities.prettifyFilePath(convertedPDFFilePath));
		request.put("password", "");
		request.put("pageNumber", pageNumber);
		if (additionalAttributeName != null) {
			request.put(additionalAttributeName, additionalAttributeValue);
		}
		serviceResponse = stepExecutor.sendingConvertRequest(requestBodyJson
				.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	@When("user sends request to convert page <pageNumber> of office document <file> to pdf format")
	@Aliases(values = {
			"user sends request to convert page <pageNumber> of text document <file> to pdf format",
			"user sends request to convert page <pageNumber> of visio document <file> to pdf format",
			"user sends request to convert page <pageNumber> from Excel document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert page <pageNumber> from LOCalc document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert page <pageNumber> from Word document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert page <pageNumber> from LOWritter document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert page <pageNumber> from PowerPoint document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert page <pageNumber> from LOImpress document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert page <pageNumber> of missing documents <file> to pdf format" })
	public void requestConvertWithCustomPageNumber(
			@Named("file") String fileName, @Named("pageNumber") int pageNumber) {
		requestConvertWithSomeConversionParameters(fileName, pageNumber, null,
				null, null, null);
	}

	@When("user sends request to convert all pages from office document <file> to pdf format")
	@Aliases(values = {
			"user sends request to convert all pages from text document <file> to pdf format",
			"user sends request to convert all pages from Excel document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert all pages from Excel document <file> with multiple internal HyperLinks to one PDF file",
			"user sends request to convert all pages from LOCalc document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert all pages from LOCalc document <file> with multiple internal HyperLinks to one PDF file",
			"user sends request to convert all pages from Word document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert all pages from Word document <file> with multiple internal HyperLinks to one PDF file",
			"user sends request to convert all pages from LOWritter document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert all pages from LOWritter document <file> with multiple internal HyperLinks to one PDF file",
			"user sends request to convert all pages from PowerPoint document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert all pages from PowerPoint document <file> with multiple internal HyperLinks to one PDF file",
			"user sends request to convert all pages from LOImpress document <file> with multiple internal HyperLinks to PDF format",
			"user sends request to convert all pages from LOImpress document <file> with multiple internal HyperLinks to one PDF file"

	})
	public void requestConvertAllPages(@Named("file") String fileName) {

		String destFormat = "pdf";
		sourceOfficeFilePath = OsUtilities.prettifyFilePath(sourceFolderPath
				+ fileName);

		String outputFile = fileName + ".all_pages." + destFormat;
		convertedPDFFilePath = OsUtilities.prettifyFilePath(convertedFolderPath
				+ outputFile);

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC, sourceOfficeFilePath);
		request.put(Constants.DEST_FORMAT, destFormat);
		request.put(Constants.OUTPUT_TEMPLATE, convertedPDFFilePath);
		request.put("password", "");
		request.put("pageNumber", 0);
		request.put("ignorePageNumber", true);

		serviceResponse = stepExecutor.sendingConvertRequest(requestBodyJson
				.toString());

		LOGGER.debug("SERVICE RESPONSE BEFORE..." + serviceMessage);

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");

		LOGGER.debug("SERVICE RESPONSE AFTER..." + serviceMessage);
	}

	@When("user sends convert request which contains unexpected JSON data <postData>")
	@Aliases(values = {
			"user sends convert request with missing mandatory parameter in JSON data <postData>",
			"user sends convert request which contains parameter with unexpected type <postData>",
			"user sends convert request which contains unexpected non-null parameter value <postData>" })
	public void sendInvalidConvertRequest(@Named("postData") String postData) {

		serviceResponse = stepExecutor.sendingConvertRequest(postData);

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	@When("user sends set of requests to convert all pages from Excel document <file> with multiple internal HyperLinks set of PDF files")
	@Aliases(values = {
			"user sends set of requests to convert all pages from LOCalc document <file> with multiple internal HyperLinks set of PDF files",
			"user sends set of requests to convert all pages from Word document <file> with multiple internal HyperLinks set of PDF files",
			"user sends set of requests to convert all pages from LOWritter document <file> with multiple internal HyperLinks set of PDF files",
			"user sends set of requests to convert all pages from PowerPoint document <file> with multiple internal HyperLinks set of PDF files",
			"user sends set of requests to convert all pages from LOImpress document <file> with multiple internal HyperLinks set of PDF files" })
	public void convertOfficeFileToSetOfPdfPages(@Named("file") String file) {
		// Get number of pags in office file
		DocumentAttributesStepsDefinition dasd = new DocumentAttributesStepsDefinition();
		dasd.stepExecutor = new Steps();
		dasd.getServiceParameters();
		dasd.calculatePageCount(file);
		int numberOfPages = DocumentAttributesStepsDefinition.pageCount;

		LOGGER.info("Number of pages in office document is " + numberOfPages);

		for (int pageCnt = 0; pageCnt < numberOfPages; pageCnt++) {
			requestConvertWithCustomPageNumber(file, pageCnt);

			LOGGER.info("Page " + convertedPDFFilePath
					+ " is added to PDF pages set");

			pdfSet.add(pageCnt, new String(convertedPDFFilePath));

		}
	}

	@When("user sends convert request which contains src parameter which refers to non-existent source file <file> to $format format")
	public void requestConvert(@Named("file") String fileName,
			@Named("format") String destFormat) {

		sourceOfficeFilePath = OsUtilities.prettifyFilePath(sourceFolderPath
				+ fileName);
		String outputFile = fileName + "." + destFormat;
		convertedPDFFilePath = OsUtilities.prettifyFilePath(convertedFolderPath
				+ outputFile);

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC, sourceOfficeFilePath);
		request.put(Constants.OUTPUT_TEMPLATE, convertedPDFFilePath);
		request.put(Constants.DEST_FORMAT, destFormat);
		request.put("password", "");
		request.put("pageNumber", 0);

		serviceResponse = stepExecutor.sendingConvertRequest(requestBodyJson
				.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	public void convertMissingParameters(@Named("file") String fileName,
			@Named("pageNumber") int pageNumber,
			@Named("pageOf") boolean pageOf, @Named("srcOf") boolean srcOf,
			@Named("templateOf") boolean templateOf,
			@Named("format") boolean format) {

		String destFormat = "pdf";
		sourceOfficeFilePath = OsUtilities.prettifyFilePath(sourceFolderPath
				+ fileName);
		String outputFile = fileName + "." + destFormat;
		convertedPDFFilePath = OsUtilities.prettifyFilePath(convertedFolderPath
				+ outputFile);

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		if (srcOf == true)
			request.put(Constants.SRC, sourceOfficeFilePath);
		if (templateOf == true)
			request.put(Constants.OUTPUT_TEMPLATE, convertedPDFFilePath);
		if (format == true)
			request.put(Constants.DEST_FORMAT, destFormat);
		request.put("password", "");
		if (pageOf == true)
			request.put("pageNumber", 0);

		serviceResponse = stepExecutor.sendingConvertRequest(requestBodyJson
				.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");

	}

	@When("user sends a request with missing src parametr on conversion of single page <pageNumber> of the office document <file> in PDF format")
	public void requestConvertWithMissingSrc(@Named("file") String fileName,
			@Named("pageNumber") int pageNumber) {
		convertMissingParameters(fileName, pageNumber, true, false, true, true);
	}

	@When("user sends a request with missing pageNumber parametr on conversion of single page <pageNumber> of the office document <file> in PDF format")
	public void requestConvertWithMissingPageNumber(
			@Named("file") String fileName, @Named("pageNumber") int pageNumber) {
		convertMissingParameters(fileName, pageNumber, false, true, true, true);
	}

	@When("user sends a request with missing outputTemplate parametr on conversion of single page <pageNumber> of the office document <file> in PDF format")
	public void requestConvertWithMissingOutputTemplate(
			@Named("file") String fileName, @Named("pageNumber") int pageNumber) {
		convertMissingParameters(fileName, pageNumber, true, true, false, true);
	}

	@When("user sends a request with missing destFormat parametr on conversion of single page <pageNumber> of the office document <file>")
	public void requestConvertWithMissingDestFormat(
			@Named("file") String fileName, @Named("pageNumber") int pageNumber) {
		convertMissingParameters(fileName, pageNumber, true, true, true, false);
	}

	@When("user sends request to convert on conversion of single page <pageNumber> of the office document <file> with invalid HTTP GET method")
	public void requestConvertHttpMetodInvalid(@Named("file") String fileName,
			@Named("pageNumber") int pageNumber) {

		String destFormat = "pdf";
		sourceOfficeFilePath = sourceFolderPath + fileName;
		String outputFile = fileName + ".page." + pageNumber + "." + destFormat;
		convertedPDFFilePath = convertedFolderPath + outputFile;

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC,
				OsUtilities.prettifyFilePath(sourceOfficeFilePath));
		request.put(Constants.DEST_FORMAT, destFormat);
		request.put(Constants.OUTPUT_TEMPLATE,
				OsUtilities.prettifyFilePath(convertedPDFFilePath));
		request.put("password", "");
		request.put("pageNumber", pageNumber);
		serviceResponse = stepExecutor.sendingConvertRequestGet(requestBodyJson
				.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	@When("user sends convert request with unexpected JSON data <postData>")
	public void getServiceConvertIncorrectJson(
			@Named("postData") String postData) {

		serviceResponse = stepExecutor.sendingConvertRequest(postData);

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");

		if (returnedCode == 200) {
			serviceStatus = JsonUtils
					.getServiceStatusFromResponse(serviceMessage);
		}
	}

	public void getServiceConvertMissingHeader(String fileName,
			boolean ofParentName, boolean ofParentPid, boolean ofParentTaskid) {

		String destFormat = "pdf";
		sourceOfficeFilePath = sourceFolderPath + fileName;
		String outputFile = fileName + ".page." + 0 + "." + destFormat;
		convertedPDFFilePath = convertedFolderPath + outputFile;

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC,
				OsUtilities.prettifyFilePath(sourceOfficeFilePath));
		request.put(Constants.DEST_FORMAT, destFormat);
		request.put(Constants.OUTPUT_TEMPLATE,
				OsUtilities.prettifyFilePath(convertedPDFFilePath));
		request.put("password", "");
		request.put("pageNumber", 0);
		serviceResponse = stepExecutor.sendingConvertRequestMissingHeader(
				requestBodyJson.toString(), ofParentName, ofParentPid,
				ofParentTaskid);

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	@When("user sends a request with missing Parent-Name on conversion of page 0 of the office document <file>")
	public void convertRequestWithMissingParentName(
			@Named("file") String fileName) {
		getServiceConvertMissingHeader(fileName, false, true, true);
	}

	@When("user sends a request with missing Parent-Pid on conversion of page 0 of the office document <file>")
	public void convertRequestWithMissingParentPid(
			@Named("file") String fileName) {
		getServiceConvertMissingHeader(fileName, true, false, true);
	}

	@When("user sends a request with missing Parent-Taskid on conversion of page 0 of the office document <file>")
	public void convertRequestWithMissingParentTaskid(
			@Named("file") String fileName) {
		getServiceConvertMissingHeader(fileName, true, true, false);
	}

	@When("user sends convert request with correct header for given file <file>")
	public void convertRequestWithCorrectHaeder(@Named("file") String fileName) {
		getServiceConvertMissingHeader(fileName, true, true, true);
	}

	@When("user sends a request with hint parameter first <first> and last <last> to convert pages <pageNumber> office document <file>")
	public void requestConvertWithHintParameters(
			@Named("file") String fileName,
			@Named("pageNumber") int pageNumber, @Named("first") String first,
			@Named("last") String last) {

		String destFormat = "pdf";
		sourceOfficeFilePath = sourceFolderPath + fileName;

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();
		JSONObject hint = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC,
				OsUtilities.prettifyFilePath(sourceOfficeFilePath));
		request.put(Constants.DEST_FORMAT, destFormat);
		request.put(
				Constants.OUTPUT_TEMPLATE,
				OsUtilities.prettifyFilePath(convertedFolderPath + fileName
						+ ".page." + "{pageNumber}" + "." + destFormat));
		request.put("password", "");
		request.put("pageNumber", pageNumber);

		if (!first.equalsIgnoreCase("false")) {
			hint.put("first", Integer.valueOf(first));
		}

		if (!last.equalsIgnoreCase("false")) {
			hint.put("last", Integer.valueOf(last));
		}

		request.put("hint", hint);

		System.out.println(requestBodyJson.toString());
		serviceResponse = stepExecutor.sendingConvertRequest(requestBodyJson
				.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}
}