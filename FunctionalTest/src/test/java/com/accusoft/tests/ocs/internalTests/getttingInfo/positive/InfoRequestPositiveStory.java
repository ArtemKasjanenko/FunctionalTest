package com.accusoft.tests.ocs.internalTests.getttingInfo.positive;

import com.accusoft.tests.ocs.internalTests.stories.AbstractStory;

public class InfoRequestPositiveStory extends AbstractStory{

	public InfoRequestPositiveStory() {
		
	}

	public static void scenarioInfoRequestPositive(){
		System.out.println("\nStory InfoRequest.positive.story is started");
		isd.getServiceParameters();
		isd.getServiceInfo();
		isd.statusShouldBe(200);

		System.out.println("Story InfoRequest.positive.story is finished");
	}
	
	public static void InfoRequestPositive(){

	}

	@Override
	public void runScenarios() {
		scenarioInfoRequestPositive();
	}
	
	
	

}
