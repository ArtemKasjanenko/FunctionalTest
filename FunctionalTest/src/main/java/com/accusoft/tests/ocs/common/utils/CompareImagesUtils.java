package com.accusoft.tests.ocs.common.utils;

import java.io.BufferedReader;

/**
 * The Class Utils.
 */
public class CompareImagesUtils {

	/** The Constant ASCII_SPACE. */
	private final static String ASCII_SPACE = "%20";

	/**
	 * Gets the resource path.
	 * 
	 * @param resourceName
	 *            the resource name
	 * @return the resource path
	 */
	public static String getResourcePath(String resourceName) {
		URL url = CompareImagesUtils.class.getClassLoader().getResource(resourceName);
		if (url == null) {
			return null;
		}
		String path = new File(url.getPath()).getAbsolutePath();
		return path.replace(ASCII_SPACE, " ");
	}

	/**
	 * Gets the os name.
	 *
	 * @return the os name
	 */
	public static String getOsName() {
		return System.getProperty("os.name");
	}

	/**
	 * Checks if is windows.
	 *
	 * @return true, if is windows
	 */
	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	/**
	 * Prepare command string.
	 *
	 * @param nativeUtilPath
	 *            the native util path
	 * @param args
	 *            the args
	 * @return the string
	 */
	public static String prepareCommandString(String nativeUtilPath,
			String[] args) {

		String arguments = join(args, " ");

		return prepareCommandString(nativeUtilPath, arguments);
	}

	/**
	 * Prepare command string.
	 *
	 * @param nativeUtilPath
	 *            the native util path
	 * @param arguments
	 *            the arguments
	 * @return the string
	 */
	public static String prepareCommandString(String nativeUtilPath,
			String arguments) {

		if (isWindows()) {
			return "cmd /C " + nativeUtilPath + " " + arguments;
		} else {
			return nativeUtilPath + " " + arguments;
		}
	}

	/**
	 * Join.
	 *
	 * @param str the str
	 * @param separator the separator
	 * @return the string
	 */
	public static String join(String[] str, String separator) {

		if (str == null || str.length == 0) {
			return "";
		}

		StringBuffer b = new StringBuffer();
		for (int i = 0; i < str.length; i++) {

			b.append(str[i]);

			if (i != (str.length - 1)) {
				b.append(separator);
			}

		}

		return b.toString();

	}
}