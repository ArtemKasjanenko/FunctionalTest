package com.accusoft.tests.ocs.common.utils;

import org.apache.log4j.Logger;

public class TestDefinitionUtils {

    public static final Logger LOGGER = Logger.getLogger(TestDefinitionUtils.class);

    public static String getPlatformName() {
        String platformName = System.getProperty("platform");
        
        
        if(platformName==null || platformName.trim().isEmpty()){
        	platformName = OsUtilities.isWindows()?"Windows":"Linux";
        }else{
//        	if(!(platformName.equalsIgnoreCase("windows") || platformName.equalsIgnoreCase("linux"))){
//        		platformName = OsUtilities.isWindows()?"Windows":"Linux";	
//        	}
        	platformName = platformName.trim().toUpperCase();
        }
        LOGGER.info("Reading config file server parameters: PLATFORM=" + platformName);
        return platformName;
    }

    public static String getServiceIpAddress() {
        String serviceIpAddress = System.getProperty("service.ip");
        LOGGER.info("Reading config file server parameters: IP=" + serviceIpAddress);
        return serviceIpAddress;
    }

    public static int getServicePort() {
        int servicePort = Integer.parseInt(System.getProperty("service.port"));
        LOGGER.info("Reading config file server parameters: SERVICE_PORT=" + servicePort);
        return servicePort;
    }
    
    public static int getPDFCSPort() {
        int servicePort = Integer.parseInt(System.getProperty("pdfcs.service.port"));
        LOGGER.info("Reading config file server parameters: PDFCS_SERVICE_PORT=" + servicePort);
        return servicePort;
    }    

    public static int getPrizmPort() {
        int prizmPort = Integer.parseInt(System.getProperty("prizm.port"));
        LOGGER.info("Reading config file server parameters: PRIZM_PORT=" + prizmPort);
        return prizmPort;
    }

    public static String getServiceName() {
        String serviceName = System.getProperty("service.name");
        LOGGER.info("Reading config file server parameters: NAME=" + serviceName);
        return serviceName;
    }

    public static String getOriginalFilePath() {
        String originalFilePath = System.getProperty("input.folder");
        LOGGER.info("Reading config file server parameters: INPUT=" + originalFilePath);
        return originalFilePath;
    }

    public static String getConvertedFilePath() {
        String convertedFilePath = System.getProperty("output.folder");
        LOGGER.info("Reading config file server parameters: OUT=" + convertedFilePath);
        return convertedFilePath;
    }

    public static String getReferenceFolderPath() {
        String referenceFolderPath = System.getProperty("reference.folder");
        LOGGER.info("Reading config file server parameters: REFERENCE=" + referenceFolderPath);
        return referenceFolderPath;
    }
}