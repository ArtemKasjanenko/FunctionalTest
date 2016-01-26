package com.accusoft.tests.ocs.common.build;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class SetNumberOfOCSInstances {

	private static String ARG_OCS_CONFIG_LOCATION_NAME = "config_location";
	private static String ARG_NUMBER_OF_OFICE_INSTANCES_NAME = "number_of_instances";

	private static String CONFIG_OFFICE_INSTANCE_COUNT_REG_EXP = "[^\\n.]*resourceUsage.ocs.numInstances:[^\\n.]*";

	private static String configLocation = null;

	private static int numberOFInstances = -1;

	public static void main(String[] args) throws Exception {

		init();

		showInputParameters();

		validateInputParameters();

		showContentOfConfigurationFile();

		updateContentOfOCSConfigurationFile();

		showContentOfConfigurationFile(); // show content of configuration file
											// after modification

	}

	private static void showInputParameters() {

		showHeader("Input Parameters:");

		System.out.println("Location of OCS configuation file ["
				+ configLocation + "]");

		System.out.println("Number of office instances  [" + numberOFInstances
				+ "]");
	}

	private static void showContentOfConfigurationFile() throws Exception {
		showHeader("Content of configuration file:");

		System.out.println(readFile(configLocation));
	}

	private static void updateContentOfOCSConfigurationFile() {

		showHeader("Updating content of OCS configuration file");

		String numberOfOfficeInstances = null;
		try {
			numberOfOfficeInstances = getCurrentNumberOfOfficeInstances();
		} catch (Exception ex) {
			// ignore
		}

		if (numberOfOfficeInstances == null) {
			throw new RuntimeException(
					"Can not find number of office instances inside configuation file ["
							+ configLocation + "]");
		}

		System.out.println("Fount current number of office instances ["
				+ numberOfOfficeInstances + "]");

		String replaceString = "resourceUsage.ocs.numInstances: " + numberOFInstances;

		System.out.println("Replacing [" + numberOfOfficeInstances + "] by ["
				+ replaceString + "] unside [" + configLocation + "]");

		updateFileContentByRegexp(configLocation, numberOfOfficeInstances,
				replaceString);
	}

	private static void init() {

		configLocation = System.getProperty(ARG_OCS_CONFIG_LOCATION_NAME);
		numberOFInstances = Integer.parseInt(System
				.getProperty(ARG_NUMBER_OF_OFICE_INSTANCES_NAME));

	}

	private static void validateInputParameters()
			throws IllegalArgumentException {

		if (configLocation == null) {
			throw new IllegalArgumentException(
					"Location of OCS configuration file can not be empty");
		}

		if (numberOFInstances <= 0) {
			throw new IllegalArgumentException(
					"Number of office instances can not be negative value");
		}

		File configFile = new File(configLocation);
		if (!configFile.exists()) {
			throw new IllegalArgumentException(
					"Provided location of OCS configuration [" + configLocation
							+ "] does not exist");
		}

	}

	private static String getCurrentNumberOfOfficeInstances()
			throws IOException {

		return findInFile(configLocation, CONFIG_OFFICE_INSTANCE_COUNT_REG_EXP,
				0);

	}

	private static String findInFile(String pathToFile, String regexp,
			int groupNumber) throws IOException {

		CharSequence fileContent = readFile(pathToFile);

		System.out.println("Applying regexp  [" + regexp + "] to file ["
				+ pathToFile + "]");

		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(fileContent);

		// Find all matches
		while (matcher.find()) {
			// Get the matching string
			return matcher.group(groupNumber);
		}

		return null;
	}

	private static String readFile(String filename) throws IOException {
		return FileUtils.readFileToString(new File(filename), "UTF-8");
	}

	private static void updateFileContentByRegexp(String pathToFile,
			String search, String replace) {
		try {

			String content = readFile(pathToFile);
			content = content.replaceAll(search, replace);
			FileUtils.writeStringToFile(new File(pathToFile), content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException("Writting to file [" + pathToFile
					+ "] failed", e);
		}
	}

	private static void showHeader(String headerName) {
		System.out
				.println("******************************************************");

		System.out.println(headerName.toUpperCase());
		System.out
				.println("******************************************************");

	}

}
