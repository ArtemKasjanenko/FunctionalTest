package com.accusoft.tests.ocs.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.CharEncoding;
import org.apache.log4j.Logger;

import com.accusoft.tests.ocs.common.utils.CompareImagesUtils.CmdOutput;
import com.accusoft.tests.ocs.common.utils.CompareImagesUtils.Delegator;
import com.sun.jna.Library;
import com.sun.jna.Native;

public final class OsUtilities {

	public static final Logger LOGGER = Logger.getLogger(OsUtilities.class);

	private final static String ASCII_SPACE = "%20";

	static interface Kernel32 extends Library {

		public static Kernel32 INSTANCE = (Kernel32) Native.loadLibrary(
				"kernel32", Kernel32.class);

		public int GetProcessId(Long hProcess);
	}

	private static String OS = null;

	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	public static boolean isCentos() {
		String platformName = System.getProperty("platform");
		if (platformName != null) {
			return platformName.toLowerCase().contains("centos");
		}
		return false;
	}

	public static boolean isLinux() {
		return getOsName().startsWith("Linux");
	}

	public static boolean isUbuntu() {
		String platformName = System.getProperty("platform");
		if (platformName != null) {
			return platformName.toLowerCase().contains("ubuntu");
		}
		return false;

	}

	public static boolean isRHEL() {
		String platformName = System.getProperty("platform");
		if (platformName != null) {
			return platformName.toLowerCase().contains("rhel");
		}
		return false;

	}

	/**
	 * Operating system name
	 * 
	 * @return
	 */
	public static String getOsName() {
		if (OS == null) {
			OS = System.getProperty("os.name");
		}
		return OS;
	}

	/**
	 * Operating system version
	 * 
	 * @return
	 */
	public static String getOsVersion() {
		return System.getProperty("os.version");
	}

	/**
	 * Operating system architecture
	 * 
	 * @return
	 */
	public static String getOsArch() {
		return System.getProperty("os.arch");
	}

	public static String getSpecificPlatformPath(String platformName,
			String referenceFileName, String pathPrefix,
			String referenceFilePath) {

		// First try the platform-specific path
		if (!platformName.equals("")) {

			referenceFilePath = pathPrefix + File.separator + platformName
					+ File.separator + referenceFileName;
		}

		if (!referenceFilePath.equals("")) {
			LOGGER.info("Validating Platform specific path: "
					+ referenceFilePath);
			File f = new File(referenceFilePath);
			if (!f.exists()) {
				// No platform-specific file
				LOGGER.warn("No file under location: " + referenceFilePath);
				referenceFilePath = "";
			} else {
				LOGGER.info("Validation of Platform specific path is passed for "
						+ referenceFilePath);
			}
		}

		// If custom file was not found, try the regular path
		if (referenceFilePath.equals(""))
			referenceFilePath = pathPrefix + File.separator + referenceFileName;

		LOGGER.info("Platform specific path is " + referenceFilePath);

		return prettifyFilePath(referenceFilePath);
	}

	public static class ShellExecutor {

		public static final Logger LOGGER = Logger
				.getLogger(ShellExecutor.class);

		public static String runCommand(String commandName) {
			return runCommand(commandName, "");
		}

		public static String runCommand(String commandName, String arguments) {

			String stdout, errout;

			String cmd = CompareImagesUtils.prepareCommandString(commandName,
					arguments);

			try {

				LOGGER.info("Executing command: " + cmd);

				CmdOutput out;
				try {
					out = Delegator.delegate(cmd);
				} catch (Exception ex) {
					throw new RuntimeException("Can not execute command "
							+ commandName, ex);
				}

				stdout = out.readStdInput();
				errout = out.readErrInput();

				LOGGER.info("\nSTD Output: {" + stdout + "}");
				LOGGER.info("\nERR Output: {" + errout + "}");

			} catch (IOException ex) {
				throw new RuntimeException("Unexpected error:", ex);
			}

			return stdout;
		}
	}

	/**
	 * Gets the resource path.
	 * 
	 * @param resourceName
	 *            the resource name
	 * @return the resource path
	 */
	public static String getResourcePath(String resourceName) {
		URL url = OsUtilities.class.getClassLoader().getResource(resourceName);
		if (url == null) {
			return null;
		}
		String path = new File(url.getPath()).getAbsolutePath();
		return path.replace(ASCII_SPACE, " ");
	}

	/**
	 * Prettify file path.
	 *
	 * @param filePath
	 *            the file path
	 * @return the string
	 */
	public static String prettifyFilePath(String filePath) {

		String prettifiedPath = filePath;
		prettifiedPath = prettifiedPath.replace('\\', '/');
		prettifiedPath = prettifiedPath.replaceAll("//", "/");
		prettifiedPath = prettifiedPath.replace(" ", ASCII_SPACE);
		return prettifiedPath;
	}

	public static String searchRegularExpressions(String compile, String message) {
		String searchMessage = null;
		Pattern pat = Pattern.compile(compile);
		Matcher m = pat.matcher(message);
		if (m.find()) {
			searchMessage = m.group();

		}
		return searchMessage;
	}

	public static int getNumberOfConvertedFiles(String rootDir) {

		File f = new File(rootDir);
		
		if(f.isDirectory()){
			return f.list().length;
		}
		
		return 0;
		
	
	}
}
