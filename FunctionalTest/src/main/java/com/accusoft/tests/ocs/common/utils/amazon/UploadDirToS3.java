package com.accusoft.tests.ocs.common.utils.amazon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.codehaus.plexus.util.FileUtils;

import com.accusoft.tests.ocs.common.utils.FSUtils;
import com.amazonaws.services.s3.AmazonS3;

public class UploadDirToS3 extends BasicS3Step {

	private final static String ARG_AWS_S3_DIR_NAME = "awsS3DirName";
	private final static String ARG_LOCAL_DIR_NAME = "localDirName";
	private final static String ARG_COPY_DIR_CONTENT_ONLY = "copyDirContentOnly";

	private final static String COPY_DIR_CONTENT_DEFAULT = "false";

	private static String awsS3DirName = null;
	private static String localDirName = null;

	private static List<String> uploladedFilesList = new ArrayList<String>();

	@SuppressWarnings("unused")
	private static boolean copyDirContentOnly = false;

	public static void main(String[] args) {

		initBasicArguments();
		initStepArguments();

		CommandLine commandLine = parseCommandLine(args);

		if (commandLine != null) {
			parseBasicArguments(commandLine);
			parseStepArguments(commandLine);
		}

		run();

		releaseBasicResources();

		System.exit(0);
	}

	public static void run(String accessKeyId_, String secretAccessKey_,
			String bucketName_, String awsS3DirName_, String localDirName_,
			boolean copyDirContentOnly_) {
		accessKeyId = accessKeyId_;
		secretAccessKey = secretAccessKey_;
		bucketName = bucketName_;
		awsS3DirName = awsS3DirName_;
		localDirName = localDirName_;
		copyDirContentOnly = copyDirContentOnly_;

		run();
	}

	private static void run() {

		AmazonS3 s3Client = getAwsClient();

		List<String> relativeFilesPatches = getListOfFilesInFolder(localDirName);

		for (String relFilePath : relativeFilesPatches) {

			String absLocalFilePath = FSUtils.prettifyFilePath(localDirName
					+ File.separator + relFilePath);
			String relativePath = getRelativeFileFolderPath(localDirName,
					absLocalFilePath);

			String awsFolderPath = FSUtils.prettifyFilePath(awsS3DirName
					+ ((relativePath == null) ? "" : File.separator
							+ relativePath));

			// Check if current file is already uploaded
			if (!uploladedFilesList.contains(absLocalFilePath)) {
				Manager.uploadFileInBucket(getBucketName(), awsFolderPath,
						new File(absLocalFilePath), s3Client);
				LOGGER.debug("Local file '" + absLocalFilePath
						+ "' is uploaded to bucket " + getBucketName()
						+ " to S3 folder '" + awsFolderPath + "'");
				uploladedFilesList.add(absLocalFilePath);
			}
		}

	}

	private static String getRelativeFileFolderPath(String localRootDirName,
			String absLocalFilePath) {
		String prettifiedRootDir = FSUtils.prettifyFilePath(localRootDirName
				+ File.separator);
		String prettifiedRelativeFilePath = FSUtils
				.prettifyFilePath(absLocalFilePath);

		// LOGGER.info("prettifiedRootDir=" + prettifiedRootDir);
		// LOGGER.info("prettifiedRelativeFilePath=" +
		// prettifiedRelativeFilePath);

		String relativeFileFolderPath = null;
		if (prettifiedRootDir.length() <= prettifiedRelativeFilePath
				.lastIndexOf("/")) {
			relativeFileFolderPath = prettifiedRelativeFilePath.substring(
					prettifiedRootDir.length(),
					prettifiedRelativeFilePath.lastIndexOf("/"));
		}

		// LOGGER.info("relativeFileFolderPath=" + relativeFileFolderPath);

		return relativeFileFolderPath;
	}

	public static void initStepArguments() {

		Option option = null;
		option = new Option(ARG_AWS_S3_DIR_NAME, true, "AWS S3 directory name");
		option.setRequired(true);
		cliOptions.addOption(option);

		option = new Option(ARG_LOCAL_DIR_NAME, true,
				"Path to local parent directory");
		option.setRequired(true);
		cliOptions.addOption(option);

		option = new Option(ARG_COPY_DIR_CONTENT_ONLY, true,
				"Copy content of S3 Directory only (without parent folder)");
		option.setRequired(false);
		cliOptions.addOption(option);

	}

	public static void parseStepArguments(CommandLine commandLine) {

		try {
			awsS3DirName = commandLine.getOptionValue(ARG_AWS_S3_DIR_NAME);

			localDirName = commandLine.getOptionValue(ARG_LOCAL_DIR_NAME);

			copyDirContentOnly = Boolean.valueOf(commandLine.getOptionValue(
					ARG_COPY_DIR_CONTENT_ONLY, COPY_DIR_CONTENT_DEFAULT));

		} catch (IllegalArgumentException e) {
			System.out.println("Invalid arguments provided for command line. "
					+ e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("convert", cliOptions);
		} catch (Throwable e) {
			System.out.println("Unexpected error: " + e.toString());
		}

	}

	public static List<String> getListOfFilesInFolder(String dirName) {

		new ArrayList<String>();

		FileSource source = new FileSource(dirName, "not empty", "MIXED", true,
				true);

		return source.getRelativeFilesPath(true);
	}

	/**
	 * The Class FileSource.
	 */
	private static class FileSource {

		/** The empty state. */
		private static String EMPTY_STATE = "empty";

		/** The type tiff. */
		private static String TYPE_TIFF = "tiff";

		/** The type png. */
		private static String TYPE_PNG = "png";

		/** The type pdf. */
		private static String TYPE_PDF = "pdf";

		/** The type img. */
		private static String TYPE_IMG = "img";

		/** The type jpg. */
		private static String TYPE_JPG = "jpg";

		/** The location. */
		private final String location;

		/** The type. */
		private final String type;

		/** The initial state. */
		private final String initialState;

		/** The write able. */
		final boolean writeAble;

		/** The is directory. */
		final boolean isDirectory;

		/**
		 * Instantiates a new file source.
		 *
		 * @param location
		 *            the location
		 * @param initialState
		 *            the initial state
		 * @param type
		 *            the type
		 * @param writeAble
		 *            the write able
		 * @param isDirectory
		 *            the is directory
		 */
		public FileSource(final String location, final String initialState,
				final String type, final boolean writeAble,
				final boolean isDirectory) {
			this.location = location;
			this.initialState = initialState;
			this.type = type;
			this.writeAble = writeAble;
			this.isDirectory = isDirectory;

			if (initialState != null
					&& initialState.equalsIgnoreCase(EMPTY_STATE)) {
				clean();
			}

			checkAvailability();
			// checkType(type);

			if (isDirectory && initialState != null
					&& initialState.equalsIgnoreCase(EMPTY_STATE)) {
				try {
					FileUtils.cleanDirectory(location);
				} catch (IOException e) {
					// ignore
				}
			}

		}

		/**
		 * Gets the location.
		 *
		 * @return the location
		 */
		public String getLocation() {
			return location;
		}

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * Gets the initial state.
		 *
		 * @return the initial state
		 */
		public String getInitialState() {
			return this.initialState;
		}

		/**
		 * Clean.
		 */
		public void clean() {

		}

		/**
		 * Check availability.
		 */
		private void checkAvailability() {

			if (this.location == null) {
				throw new IncorrectTestDefinitionException(
						"Root directory for " + type + " files can not be null");
			}

			File file = new File(this.location);

			if (!file.exists()) {
				throw new IncorrectTestDefinitionException(
						"Specified root directory {" + this.location + "} for "
								+ type + " files does not exist");
			}

			if (!file.canRead()) {
				throw new IncorrectTestDefinitionException(
						"Specified root directory {" + this.location + "} for "
								+ type + " files is not readable");
			}

			if (writeAble) {
				if (!file.canWrite()) {
					throw new IncorrectTestDefinitionException(
							"Specified root directory {" + this.location
									+ "} for " + type
									+ " files is not writeable");
				}
			}

			if (isDirectory && !file.isDirectory()) {
				throw new IncorrectTestDefinitionException("Specified path {"
						+ this.location + "} to " + type
						+ " files is not directory");
			}

			if (!isDirectory && file.isDirectory()) {
				throw new IncorrectTestDefinitionException("Specified path {"
						+ this.location + "} to " + type
						+ " files is not file (is it directory)");
			}

		}

		/**
		 * Check type.
		 *
		 * @param sourceType
		 *            the source type
		 * @throws IncorrectTestDefinitionException
		 *             the incorrect test definition exception
		 */
		public void checkType(String sourceType)
				throws IncorrectTestDefinitionException {
			if (sourceType == null) {
				throw new IncorrectTestDefinitionException(
						"Data Source type can not be null");
			}

			if (sourceType == "") {
				throw new IncorrectTestDefinitionException(
						"Data Source type can not be empty");
			}

			if (sourceType.equalsIgnoreCase(TYPE_TIFF)
					|| sourceType.equalsIgnoreCase(TYPE_PNG)
					|| sourceType.equalsIgnoreCase(TYPE_PDF)
					|| sourceType.equalsIgnoreCase(TYPE_IMG)
					|| sourceType.equalsIgnoreCase(TYPE_JPG)) {

				return;
			}

			throw new IncorrectTestDefinitionException(
					"Unsupported Data Source Type: " + sourceType);

		}

		/**
		 * Gets the files list.
		 *
		 * @param ignoreFileType
		 *            the ignore file type
		 * @return the files list
		 */
		public Collection<File> getFilesList(boolean ignoreFileType) {

			File rootFolfer = new File(this.location);

			if (!isDirectory) {
				List<File> files = new ArrayList<File>();
				files.add(rootFolfer);
				return files;
			}

			String[] extensions = new String[2];
			extensions[0] = type.toLowerCase();
			extensions[1] = type.toUpperCase();

			try {

				if (ignoreFileType) {
					return org.apache.commons.io.FileUtils.listFiles(
							rootFolfer, null, true);
				} else {
					return org.apache.commons.io.FileUtils.listFiles(
							rootFolfer, extensions, true);
				}

			} catch (Exception e) {
				throw new IncorrectTestDefinitionException(
						"Can not read files from " + location, e);
			}
		}

		/**
		 * Gets the relative files path.
		 *
		 * @param ignoreFileType
		 *            the ignore file type
		 * @return the relative files path
		 */
		public List<String> getRelativeFilesPath(boolean ignoreFileType) {

			if (!isDirectory) {
				return null;
			}

			List<String> relativeFilesPath = new ArrayList<String>();

			for (File file : getFilesList(ignoreFileType)) {
				relativeFilesPath.add(getRelativePath(file, new File(
						this.location)));
			}

			return relativeFilesPath;
		}

		/**
		 * Gets the relative path.
		 *
		 * @param file
		 *            the file
		 * @param folder
		 *            the folder
		 * @return the relative path
		 */
		private String getRelativePath(File file, File folder) {
			String filePath = file.getAbsolutePath();
			String folderPath = folder.getAbsolutePath();
			if (filePath.startsWith(folderPath)) {
				return FSUtils.prettifyFilePath(filePath
						.substring(folderPath.length() + 1));
			} else {
				return null;
			}
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof FileSource) {
				FileSource fs = (FileSource) object;
				if (fs.type != null && fs.type.equalsIgnoreCase(this.type)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * The Class IncorrectTestDefinitionException.
	 */
	public static class IncorrectTestDefinitionException extends
			IllegalArgumentException {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new incorrect test definition exception.
		 *
		 * @param msg
		 *            the msg
		 */
		public IncorrectTestDefinitionException(String msg) {
			super(msg);
		}

		/**
		 * Instantiates a new incorrect test definition exception.
		 *
		 * @param msg
		 *            the msg
		 * @param error
		 *            the error
		 */
		public IncorrectTestDefinitionException(String msg, Throwable error) {
			super(msg, error);
		}

	}
}
