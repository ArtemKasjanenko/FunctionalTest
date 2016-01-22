package com.accusoft.tests.ocs.internalTests;

import com.accusoft.tests.ocs.internalTests.stories.HyperLinks.HyperLinksInExcellStory;
import com.accusoft.tests.ocs.internalTests.stories.LibraryOfficeCustomization.ExcellFilesWithHiddenSheetsStory;

public class TestMe {

	public static void main(String[] args) {
		setSystemProperties();
		//(new ExcellFilesWithHiddenSheetsStory()).runScenarios();
		(new HyperLinksInExcellStory()).runScenarios();
	}
	
	private static void setSystemProperties(){

		//Linux
		System.setProperty("input.folder", "/root/filesForTesting-FT/");
		System.setProperty("output.folder", "/root/convertedFiles-FT/");
		System.setProperty("reference.folder", "/root/referencedImages/");
		System.setProperty("prizm.port", "18680");
		System.setProperty("service.port", "38509");
		System.setProperty("pdfcs.service.port", "38505");
		System.setProperty("platform", "Linux");
		System.setProperty("service.ip", "127.0.0.1");
		System.setProperty("service.name", "OCS");
		
		//Windows
//		System.setProperty("input.folder", "");
//		System.setProperty("output.folder", "");
//		System.setProperty("reference.folder", "");
//		System.setProperty("prizm.port", "18680");
//		System.setProperty("service.port", "38509");
//		System.setProperty("pdfcs.service.port", "38505");
//		System.setProperty("platform", "Windows");
//		System.setProperty("service.ip", "127.0.0.1");
//		System.setProperty("service.name", "OCS");
	}
	
	

}
