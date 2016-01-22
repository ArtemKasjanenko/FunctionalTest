package com.accusoft.tests.ocs.steps_definitions;

import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.When;
import org.json.JSONObject;

import com.accusoft.tests.ocs.common.Constants;
import com.accusoft.tests.ocs.common.utils.JsonUtils;

public class GetInfoStepsDefinition extends StepsDefinition {

	@When("user requests OfficeConversionService information")
	@Alias("user sends info request")
	public void getServiceInfo() {

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put("pccisVersion", Constants.PCCIS_VERSION);

		serviceResponse = stepExecutor
				.sendingInfoRequestToOfficeConversionService(requestBodyJson
						.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	@When("user sends info request with unexpected JSON data <postData>")
	public void getServiceInfoIncorrectJson(@Named("postData") String postData) {

		serviceResponse = stepExecutor
				.sendingInfoRequestToOfficeConversionService(postData);

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");

		if (returnedCode == 200) {
			serviceStatus = JsonUtils
					.getServiceStatusFromResponse(serviceMessage);
		}
	}

	@When("user sends info request with missing pccisVersion")
	public void getServiceInfoMissingVersion() {

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);

		serviceResponse = stepExecutor
				.sendingInfoRequestToOfficeConversionService(requestBodyJson
						.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	@When("user sends info request with invalid HTTP method")
	public void getServiceInfoInvalidHttpMethod() {

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put("pccisVersion", Constants.PCCIS_VERSION);

		serviceResponse = stepExecutor
				.sendingInfoRequestInvalidHttpMethod(requestBodyJson.toString());

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	public void getServiceInfoMissingHeader(boolean ofParentName,
			boolean ofParentPid, boolean ofParentTaskid) {

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put("pccisVersion", Constants.PCCIS_VERSION);

		serviceResponse = stepExecutor.sendingInfoRequestMissingHeader(
				requestBodyJson.toString(), ofParentName, ofParentPid, ofParentTaskid);

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}
	
	@When ("user sends info request with missing Parent-Name")
	public void infoRequestWithMissingParentName(){
		getServiceInfoMissingHeader(false, true, true);
	}
	
	@When ("user sends info request with missing Parent-Pid")
	public void infoRequestWithMissingParentPid(){
		getServiceInfoMissingHeader(true, false, true);
	}
	
	@When ("user sends info request with missing Parent-Taskid")
	public void infoRequestWithMissingParentTaskid(){
		getServiceInfoMissingHeader(true, true, false);
	}
	
	@When ("user sends info request with correct header")
	public void infoRequestWithCorrectHeader(){
		getServiceInfoMissingHeader(true, true, true);
	}
	
}
