package com.accusoft.tests.ocs.steps_definitions;

import java.io.File;

import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.json.JSONObject;

import com.accusoft.tests.ocs.common.Constants;
import com.accusoft.tests.ocs.common.exceptions.IncorrectTestDefinitionException;
import com.accusoft.tests.ocs.common.utils.JsonUtils;

public class DocumentAttributesStepsDefinition extends StepsDefinition {

    @When("user sends documentAttributes request which contains unexpected JSON data <postData>")
    @Aliases(values = {"user sends documentAttributes request with missing mandatory parameter in JSON data <postData>",
            "user sends documentAttributes request which contains unexpected parameter type in JSON data <postData>",
            "user sends documentAttributes request which contains unexpected non-null parameter value <postData>"})
    public void sendInvalidGetAttributeRequest(@Named("postData") String postData){

        serviceResponse = stepExecutor.sendingGetDocumentAttributesRequest(postData);

        returnedCode = (Integer) serviceResponse.get("ResponseCode");
        serviceMessage = (String) serviceResponse.get("ResponseBody");
    }

    @When("user sends documentAttributes request for given file <file>")
    @Aliases(values = {"user sends documentAttributes request which contains src parameter which refers to non-existent source file <file>",
            "user sends documentAttributes request for unsupported file <file>"})
    public void getAttributes(@Named("file")String fileName){
    	
        sourceOfficeFilePath = sourceFolderPath + fileName;
        
        //File f = new File(sourceOfficeFilePath);
        
        //if(!f.exists()){
        	//throw new IncorrectTestDefinitionException("Input source does not exist: " + sourceOfficeFilePath);
        //}

        JSONObject requestBodyJson = new JSONObject();
        JSONObject request = new JSONObject();

        requestBodyJson.put("data", request);
        request.put(Constants.SRC, sourceOfficeFilePath);
        request.put("password", "");

        serviceResponse = stepExecutor.sendingGetDocumentAttributesRequest(requestBodyJson.toString());

        returnedCode = (Integer) serviceResponse.get("ResponseCode");
        serviceMessage = (String) serviceResponse.get("ResponseBody");

        if (returnedCode == 200) {
            pageCount = JsonUtils.getPageCountFromResponse(serviceMessage);
        }
    }

    @When("user sends request to get page count for given file <file>")
    public void calculatePageCount(@Named("file") String fileName){

    	sourceOfficeFilePath = sourceFolderPath + fileName;

        JSONObject requestBodyJson = new JSONObject();
        JSONObject request = new JSONObject();

        requestBodyJson.put("data", request);
        request.put(Constants.SRC, sourceOfficeFilePath);
        request.put("password", "");

        serviceResponse = stepExecutor.sendingGetDocumentAttributesRequest(requestBodyJson.toString());

        returnedCode = (Integer) serviceResponse.get("ResponseCode");
        serviceMessage = (String) serviceResponse.get("ResponseBody");

        if (returnedCode == 200) {
            pageCount = JsonUtils.getPageCountFromResponse(serviceMessage);
        }
    }
    
    @Then("total number of pages in JSON response must be equal to <expectedCountOfPages>")
    public void checkCountOfPagesInJson(@Named("expectedCountOfPages") String expectedCountOfPages){
    	stepExecutor.comparingActualAndExpectedPageCountInSelectedFile(pageCount, Integer.parseInt(expectedCountOfPages), sourceOfficeFilePath);	
    }
    
	public void getAttributesMissingHeader(String fileName,
			boolean ofParentName, boolean ofParentPid, boolean ofParentTaskid) {

		sourceOfficeFilePath = sourceFolderPath + fileName;

		JSONObject requestBodyJson = new JSONObject();
		JSONObject request = new JSONObject();

		requestBodyJson.put("data", request);
		request.put(Constants.SRC, sourceOfficeFilePath);
		request.put("password", "");

		serviceResponse = stepExecutor
				.sendingDocumentAttributesRequestMissingHeader(requestBodyJson
						.toString(), ofParentName, ofParentPid, ofParentTaskid);

		returnedCode = (Integer) serviceResponse.get("ResponseCode");
		serviceMessage = (String) serviceResponse.get("ResponseBody");

		if (returnedCode == 200) {
			pageCount = JsonUtils.getPageCountFromResponse(serviceMessage);
		}
	}

	@When ("user sends documentAttributes request with missing Parent-Name for given file <file>")
	public void attributesRequestWithMissingParentName(@Named("file") String fileName){
		getAttributesMissingHeader(fileName, false, true, true);
	}
	
	@When ("user sends documentAttributes request with missing Parent-Pid for given file <file>")
	public void attributesRequestWithMissingParentPid(@Named("file") String fileName){
		getAttributesMissingHeader(fileName, true, false, true);
	}
	
	@When ("user sends documentAttributes request with missing Parent-Taskid for given file <file>")
	public void attributesRequestWithMissingParentTaskid(@Named("file") String fileName){
		getAttributesMissingHeader(fileName, true, true, false);
	}
	
	@When ("user sends documentAttributes request with correct header for given file <file>")
	public void attributesRequestWithCorrectHeader(@Named("file") String fileName){
		getAttributesMissingHeader(fileName, true, true, true);
	}
}