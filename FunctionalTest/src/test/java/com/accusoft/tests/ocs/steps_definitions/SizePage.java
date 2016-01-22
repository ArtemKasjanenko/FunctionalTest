package com.accusoft.tests.ocs.steps_definitions;

import java.io.File;

import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;

import com.accusoft.tests.ocs.steps.Steps;

public class SizePage {
	@net.thucydides.core.annotations.Steps
	public Steps stepExecutor;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Then("size of the page less than the max fixed-size <maxPageSize>")
	public void checkThePageSizeWithMaxSize (@Named ("maxPageSize") int maxSize, @Named ("file") String pathFile ) {
		float pageSize;
		String path = StepsDefinition.convertedPDFFilePath;
		File f = new File(path);
		pageSize = (float) (f.length());
		stepExecutor.checkFileSize(maxSize, pageSize);
	}
}
