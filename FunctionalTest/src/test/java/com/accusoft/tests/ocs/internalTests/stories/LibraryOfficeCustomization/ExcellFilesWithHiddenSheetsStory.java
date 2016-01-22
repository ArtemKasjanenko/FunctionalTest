package com.accusoft.tests.ocs.internalTests.stories.LibraryOfficeCustomization;

import com.accusoft.tests.ocs.internalTests.stories.AbstractStory;

public class ExcellFilesWithHiddenSheetsStory extends AbstractStory {

	public ExcellFilesWithHiddenSheetsStory() {

	}

	public static void scenarioCheckPageCount() {
		System.out.println("\nScenario checkPageCount is started");
		isd.getServiceParameters();
		dsd.getAttributes("LibraryOfficeCustomizations/mysteryExcel.xlsx");
		isd.statusShouldBe(200);
		dsd.checkCountOfPagesInJson("1");
		System.out.println("\nScenario checkPageCount is finished");
	}

	public static void scenarioAllPagesConvertation() {
		System.out.println("\nScenario allPagesConvertation is started");
		isd.getServiceParameters();
		csd.requestConvertAllPages("LibraryOfficeCustomizations/mysteryExcel.xlsx");
		isd.statusShouldBe(200);
		dsd.checkCountOfPagesInConvertedFile(1);
		System.out.println("\nScenario allPagesConvertation is finished");
	}

	public static void scenarioSelectedPageConversion() throws Exception {
		System.out.println("\nScenario selectedPageConvertation is started");
		isd.getServiceParameters();
		csd.requestConvertWithCustomPageNumber(
				"LibraryOfficeCustomizations/mysteryExcel.xlsx", 0);
		isd.statusShouldBe(200);
		csd.compareFiles("LibraryOfficeCustomizations/mysteryExcel.xlsx.page.0.pdf.png");
		System.out.println("\nScenario selectedPageConvertation is finished");
	}

	@Override
	public void runScenarios() {
		System.out
				.println("\nStory excellFilesWithHiddenSheets.story is started");
		try {
			// scenarioCheckPageCount();
			// scenarioAllPagesConvertation();
			scenarioSelectedPageConversion();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out
					.println("Story excellFilesWithHiddenSheets.story is finished");
		}
	}

}
