package com.accusoft.tests.ocs.internalTests.stories.HyperLinks;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.accusoft.tests.ocs.common.utils.FSUtils;
import com.accusoft.tests.ocs.common.utils.ZipTestFilesCollection;
import com.accusoft.tests.ocs.internalTests.stories.AbstractStory;

@SuppressWarnings("static-access")
public class HyperLinksInExcellStory extends AbstractStory {

	public HyperLinksInExcellStory() {

	}

	
	public static void scenarioCheckNumberOfLinksInPdf() {
		System.out.println("\nScenario 'check number of links on single page document' started");
    	csd.convertedPDFFilePath="D:/Development/Accusoft/tasks/PCC-25379/report/convertedFiles/Hyperlinks/Word/customer/docx/TestHyperlinks.docx.page.2.pdf ";
    	//csd.convertedPDFFilePath="D:/Development/Accusoft/tasks/PCC-25379/report/convertedFiles/Hyperlinks/Word/P1_OneLinkToP3_P2_NoLinks_P3_NoLinks.doc.all_pages.pdf";
    	
    	int expectedCountOfGoToLinks = 1;
    	int pageNumber = 0;
    	int expectedPageDestinationValue = 3;
    	
    	csd.checkGoToHyperlinksInPDF(expectedCountOfGoToLinks, pageNumber, expectedPageDestinationValue);
		//csd.checkHyperlinksInPDF(2, 1, 0);
		System.out.println("\nScenario 'check number of links on single page document' is finished");
	}

	public static void findHyperLinksInPdfFiles() {
		System.out.println("\nScenario 'scan hyperlinks on set of pdf document' started");
		
		//String roolLocation = "C:/pdf/yahoo_generated";
		//String roolLocation = "c:/412312/results/convertedPDFRegression/doc/";//
		//String roolLocation = "d:/Development/Accusoft/tasks/PCC-25903/Data/Collections/OpenOfficeInternalHyperlinks/ConvertedToPDF/";//
		String roolLocation = "d:/Development/Accusoft/tasks/PCC-25903/Data/Collections/InternalHyperlinks/ConvertedToPDF/";//
		
		
		String type = "pdf";
    	   	
    	 Collection<File> pdfs = FSUtils.getFilesList(roolLocation, type);
    	 
    	 int cnt = 1;
    	 for (File file : pdfs) {
    		 csd.convertedPDFFilePath=file.getPath();
    		 try{
    			 int pageNumber = 1;
    			 int expectedCountOfGotoLinks = 1;
    			 int expectedPageDestinationValue = 0;
    			 
    			 csd.checkGoToHyperlinksInPDF(pageNumber, expectedCountOfGotoLinks, expectedPageDestinationValue);
    		 }catch(java.lang.AssertionError ex){
    			 System.out.println(cnt++ + "---"+ file.getName() + "-----" + ex.getMessage());
    		 }
		}

    	 //csd.checkHyperlinksInPDF(2, 1, 0);
		System.out.println("\nScenario 'scan hyperlinks on set of pdf document' is finished");
	}	
	
	
	public static void unzipTestFiles() {
		System.out.println("\nScenario 'unzip test files' started");
		
		
		String pathToFilesForTesting = ZipTestFilesCollection.class.getClassLoader()
				.getResource(".").getPath()
				+ "/../../filesForTesting/";

		String zipFilePath = pathToFilesForTesting + "/../" + "filesForTesting.zip";

		String destDirectory =  "c:/!testUnzip/tmp/";
		
		try {
			//FSUtils.UnzipUtility.unzip(zipFilePath, destDirectory);
			FSUtils.UnzipUtility.unzip(new File(zipFilePath), new File(destDirectory));
		} catch (IOException e) {
			e.printStackTrace();
		}


		System.out.println("\nScenario 'unzip test files' is finished");
	}	
	
	public static void zipTestFiles() {
		System.out.println("\nScenario 'zip test files' started");
		
		String zipFilePath =  "c:/!testZip/zip.zip";
		String sourceDirectory =  "c:/!testZip/filesForTesting/";
		
		try {
			//FSUtils.UnzipUtility.unzip(zipFilePath, destDirectory);
			FSUtils.ZipUtility.zip(sourceDirectory, zipFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}


		System.out.println("\nScenario 'zip test files' is finished");
	}	
	
	


	@Override
	public void runScenarios() {
		System.out
				.println("\nStory HyperLinksInExcell.java.story is started");
		try {
			findHyperLinksInPdfFiles();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out
					.println("Story HyperLinksInExcell.java.story is finished");
		}
	}
	
	
	

}
