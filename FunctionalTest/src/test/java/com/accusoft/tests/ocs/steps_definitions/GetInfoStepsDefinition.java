package com.accusoft.tests.ocs.steps_definitions;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.xerces.impl.xpath.regex.Match;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.json.JSONObject;

import com.accusoft.tests.ocs.common.Constants;
import com.accusoft.tests.ocs.common.build.SetNumberOfOCSInstances;
import com.accusoft.tests.ocs.common.utils.JsonUtils;
import com.accusoft.tests.ocs.steps.Steps;


public class GetInfoStepsDefinition extends StepsDefinition {

	public static final Logger LOGGER = Logger.getLogger(GetInfoStepsDefinition.class);
	public static ArrayList timeResponce = new ArrayList();

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
				requestBodyJson.toString(), ofParentName, ofParentPid,
				ofParentTaskid);

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");
	}

	@When("user sends info request with missing Parent-Name")
	public void infoRequestWithMissingParentName() {
		getServiceInfoMissingHeader(false, true, true);
	}

	@When("user sends info request with missing Parent-Pid")
	public void infoRequestWithMissingParentPid() {
		getServiceInfoMissingHeader(true, false, true);
	}

	@When("user sends info request with missing Parent-Taskid")
	public void infoRequestWithMissingParentTaskid() {
		getServiceInfoMissingHeader(true, true, false);
	}

	@When("user sends info request with correct header")
	public void infoRequestWithCorrectHeader() {
		getServiceInfoMissingHeader(true, true, true);
	}

	@When("number of instances is set to $instancesForRound")
	public void numberOfInstances(
			@Named("instancesForRound") int instancesForRound) throws Exception {

		SetNumberOfOCSInstances.setNumberInstances(instancesForRound);
		LOGGER.info("number of instances is set to: " + instancesForRound);
	}

	@When("response for get info request is arrived")
	public void timeResponceInfoRequest() throws InterruptedException {

		while (true) {
			getServiceInfo();
			if (returnedCode == 200) {
				break;
			}
		}

		long currentTimeMsEnd = (new Date()).getTime();
		timeResponce.add(currentTimeMsEnd - beforePCCStartTime);
		returnedCode = 0;
	}

	@When("service is started compleatelly")
	public void waitUntilServiceIsStarted() {
		while (true) {
			if (isServiceStarted) {
				LOGGER.info("Prizm service is started compleatelly");
				return;
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Then("max getInfo response time must be not more the then <difference> percents from first get response time")
	public void comparisonTimeBetweenRestartAndFirstResponceGetInfo(
			@Named("difference") float difference) {

		float differenceResponce;
		float firstTime = Float.valueOf((Long) timeResponce.get(0));
		float maxTime = Float.valueOf((Long) timeResponce.get(1));
		int i = 2;

		while (i < timeResponce.size()) {

			if (Float.valueOf((Long) timeResponce.get(i)) > maxTime) {
				maxTime = Float.valueOf((Long) timeResponce.get(i));
			}
			i++;
		}

		if (firstTime >= maxTime) {
			differenceResponce = maxTime / firstTime;
		} else {
			differenceResponce = firstTime / maxTime;
		}

		differenceResponce = (1 - differenceResponce);
		LOGGER.info("max getInfo response time =" + maxTime
				+ " ms, first get response time = " + firstTime
				+ " ms, the percents difference = " + differenceResponce + " %");
		stepExecutor.checkDifferencePercents(differenceResponce, difference);
	}

}
