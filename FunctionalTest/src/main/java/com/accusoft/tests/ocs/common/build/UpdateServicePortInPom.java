package com.accusoft.tests.ocs.common.build;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class UpdateServicePortInPom {

	private static String ARG_CONFIG_LOCATION_NAME = "config_location";
	private static String ARG_POM_LOCATION_NAME = "pom_location";
	private static String ARG_POM_NODE_NAME = "pom_node_name";

	private static String CONFIG_PORT_REG_EXP = "\"port\":(\\d+)";

	private static String pomLocation = null;

	private static String configLocation = null;

	private static String pomNodeName = null;

	public static void main(String[] args) throws Exception {

		init();

		showInputParameters();

		validateInputParameters();

		showContentOfConfigurationFile();

		showContentOfPom(); // show content of pom.xml before modification

		updateServicePortInsidePom();

		showContentOfPom(); // show content of pom.xml after modification

	}

	private static void showInputParameters() {

		showHeader("Input Parameters:");

		System.out.println("Location of configuation file [" + configLocation
				+ "]");

		System.out.println("Location of pom.xml [" + pomLocation + "]");

		System.out
				.println("Name of node inside pom.xml  [" + pomNodeName + "]");
	}

	private static void showContentOfConfigurationFile() throws Exception {
		showHeader("Content of configuration file:");

		System.out.println(readFile(configLocation));
	}

	private static void showContentOfPom() throws Exception {
		showHeader("Content of pom.xml file before modification:");

		System.out.println(readFile(pomLocation));
	}

	private static void updateServicePortInsidePom() {

		showHeader("Updating content of pom.xml");

		// get service port from configuration

		String configPort = null;
		try {
			configPort = getServicePortFromConfig();
		} catch (Exception ex) {
			// ignore
		}

		if (configPort == null) {
			throw new RuntimeException(
					"Can not find port inside configuation file ["
							+ configLocation + "]");
		}

		System.out.println("Found Service Port inside configuration ["
				+ configPort + "]");

		// get service port from pom.xml

		String pomPort = null;
		try {
			pomPort = getServicePortFromPom();
		} catch (Exception ex) {
			// ignore
		}

		if (pomPort == null) {
			throw new RuntimeException(
					"Can not find port inside configuation file ["
							+ configLocation + "]");
		}

		System.out.println("Found Service Port definition inside pom.xml ["
				+ pomPort + "]");

		String replaceString = "<" + pomNodeName + ">" + configPort + "</"
				+ pomNodeName + ">";

		System.out.println("Replacing [" + pomPort + "] by [" + replaceString
				+ "] unside [" + pomLocation + "]");

		updateFileContentByRegexp(pomLocation, pomPort, replaceString);
	}

	private static void init() {

		pomLocation = System.getProperty(ARG_POM_LOCATION_NAME);
		configLocation = System.getProperty(ARG_CONFIG_LOCATION_NAME);
		pomNodeName = System.getProperty(ARG_POM_NODE_NAME);

	}

	private static void validateInputParameters()
			throws IllegalArgumentException {
		if (pomLocation == null) {
			throw new IllegalArgumentException(
					"Location of pom.xml can not be empty");
		}

		if (configLocation == null) {
			throw new IllegalArgumentException(
					"Location of configuration file can not be empty");
		}

		if (pomNodeName == null && pomNodeName.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Name of node inside pom.xml can not be empty");
		}

		File pomFile = new File(pomLocation);
		if (!pomFile.exists()) {
			throw new IllegalArgumentException("Provided location of pom.xml ["
					+ pomLocation + "] does not exist");
		}

		File configFile = new File(configLocation);
		if (!configFile.exists()) {
			throw new IllegalArgumentException(
					"Provided location of configuration [" + configLocation
							+ "] does not exist");
		}

	}

	private static String getServicePortFromConfig() throws IOException {

		return findInFile(configLocation, CONFIG_PORT_REG_EXP, 1);

	}

	private static String getServicePortFromPom() throws IOException {

		String searchPattern = "<" + pomNodeName + ">.*</" + pomNodeName + ">";

		return findInFile(pomLocation, searchPattern, 0);

	}

	private static String findInFile(String pathToFile, String regexp,
			int groupNumber) throws IOException {

		CharSequence fileContent = readFile(pathToFile);

		// System.out.println("Applying regexp  [" + regexp + "] to file ["
		// + pathToFile + "]");

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
