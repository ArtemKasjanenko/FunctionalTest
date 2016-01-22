package com.accusoft.tests.ocs.common.utils;

import java.io.BufferedReader;import java.io.ByteArrayInputStream;import java.io.ByteArrayOutputStream;import java.io.File;import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.net.URL;import org.apache.commons.io.IOUtils;

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

	}			/**	 * The Class CmdOutput.	 */	public static class CmdOutput {		/** The process. */		Process process;		/** The std str. */		ByteArrayOutputStream stdStr = null;				/** The err str. */		ByteArrayOutputStream errStr = null;		/**		 * Instantiates a new cmd output.		 *		 * @param process the process		 * @throws IOException Signals that an I/O exception has occurred.		 */		public CmdOutput(Process process) throws IOException{			this.process = process;			readStdInputStream();			readErrInputStream();		}		/**		 * Read std input stream.		 *		 * @throws IOException Signals that an I/O exception has occurred.		 */		private void readStdInputStream() throws IOException {			BufferedReader reader = new BufferedReader(					new InputStreamReader(process.getInputStream()));			this.stdStr = new ByteArrayOutputStream();			IOUtils.copy(reader, stdStr);		}		/**		 * Read err input stream.		 *		 * @throws IOException Signals that an I/O exception has occurred.		 */		private void readErrInputStream() throws IOException {			BufferedReader reader = new BufferedReader(new InputStreamReader(					process.getErrorStream()));			this.errStr = new ByteArrayOutputStream();			IOUtils.copy(reader, errStr);		}		/**		 * Gets the std input stream.		 *		 * @return the std input stream		 * @throws IOException Signals that an I/O exception has occurred.		 */		public InputStream getStdInputStream() throws IOException {			return new ByteArrayInputStream(stdStr.toByteArray());		}		/**		 * Gets the err input stream.		 *		 * @return the err input stream		 * @throws IOException Signals that an I/O exception has occurred.		 */		public InputStream getErrInputStream() throws IOException {			return new ByteArrayInputStream(errStr.toByteArray());		}//		/**//		 * Write std input.//		 *//		 * @throws IOException Signals that an I/O exception has occurred.//		 *///		public void writeStdInput() throws IOException {//			writeStream(stdStr);//		}	////		/**//		 * Write err input.//		 *//		 * @throws IOException Signals that an I/O exception has occurred.//		 *///		public void writeErrInput() throws IOException {//			writeStream(errStr);//		}//		/**//		 * Write stream.//		 *//		 * @param bos the bos//		 *///		protected void writeStream(ByteArrayOutputStream bos){//			if(bos.size()>0){//				System.out.println(new String(bos.toByteArray()));//			}//		}				public String readStdInput() throws IOException {			return readStream(stdStr);		}		public String readErrInput() throws IOException {			return readStream(errStr);		}			/**		 * Checks if is err input empty.		 *		 * @return true, if is err input empty		 * @throws IOException Signals that an I/O exception has occurred.		 */		public boolean isErrInputEmpty() throws IOException {			return errStr.size() == 0;		}		/**		 * Checks if is std input empty.		 *		 * @return true, if is std input empty		 * @throws IOException Signals that an I/O exception has occurred.		 */		public boolean isStdInputEmpty() throws IOException {			return stdStr.size() == 0;		}						/**		 * Read stream.		 *		 * @param bos the bos		 * @return the string		 */		protected String readStream(ByteArrayOutputStream bos){			return new String(bos.toByteArray());		}	}		/**	 * The Class Delegator.	 */	public static class Delegator {		/**		 * Delegate.		 *		 * @param cmd the cmd		 * @return the cmd output		 * @throws IOException Signals that an I/O exception has occurred.		 */		public static CmdOutput delegate(String cmd)				throws  IOException {			Process	p = Runtime.getRuntime().exec(cmd);			if (p != null) {				return new CmdOutput(p);			}			return null;		}	}	
}
