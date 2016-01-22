package com.accusoft.tests.ocs.internalTests.stories;

import com.accusoft.tests.ocs.common.utils.TestDefinitionUtils;
import com.accusoft.tests.ocs.steps.Steps;
import com.accusoft.tests.ocs.steps_definitions.ConvertStepsDefinition;
import com.accusoft.tests.ocs.steps_definitions.DocumentAttributesStepsDefinition;
import com.accusoft.tests.ocs.steps_definitions.GetInfoStepsDefinition;

public abstract class AbstractStory implements Story{
	
    public static String platformName;
    public static String sourceFolderPath;
    public static String convertedFolderPath;
    public static String referenceFolderPath;
    public static int servicePort;
    public static int pdfcsServicePort;
    //public static int  prizmPort;
    public static String serviceIpAddress;
    
    protected static GetInfoStepsDefinition isd = new GetInfoStepsDefinition();
    protected static ConvertStepsDefinition csd = new ConvertStepsDefinition();
    protected static DocumentAttributesStepsDefinition dsd = new DocumentAttributesStepsDefinition();
    protected static Steps steps =  new Steps();
    
    static {
    	isd.stepExecutor = steps;
    	csd.stepExecutor = steps;
    	dsd.stepExecutor = steps;
    }
    
	public static void init(){
        sourceFolderPath = TestDefinitionUtils.getOriginalFilePath();
        convertedFolderPath = TestDefinitionUtils.getConvertedFilePath();
        referenceFolderPath = TestDefinitionUtils.getReferenceFolderPath();
        servicePort = TestDefinitionUtils.getServicePort();
        pdfcsServicePort = TestDefinitionUtils.getPDFCSPort();
        platformName = TestDefinitionUtils.getPlatformName();
        serviceIpAddress = TestDefinitionUtils.getServiceIpAddress();
        //prizmPort = TestDefinitionUtils.getPrizmPort();
	}

	public AbstractStory() {
	}
}
