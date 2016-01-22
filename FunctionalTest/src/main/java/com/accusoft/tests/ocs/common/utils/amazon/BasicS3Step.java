package com.accusoft.tests.ocs.common.utils.amazon;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;

public class BasicS3Step {

	/** The Constant ASCII_SPACE. */
	private final static String ASCII_SPACE = "%20";

	private final static String ARG_AWS_ACCESS_KEY_ID = "awsAccessKeyId";
	private final static String ARG_AWS_SECRET_ACCESS_KEY = "awsSecretAccessKey";
	private final static String ARG_AWS_S3_BUCKET_NAME = "awsS3BucketName";

	public static Options cliOptions = new Options();

	public static String accessKeyId = null;
	public static String secretAccessKey = null;
	public static String bucketName = null;

	private static AmazonS3 s3Client = null;
	
	public static boolean debugEnabled = false;

	public static void initBasicArguments() {
		Option option = null;

		option = new Option(ARG_AWS_ACCESS_KEY_ID, true, "AWS Access key ID");
		option.setRequired(true);
		cliOptions.addOption(option);

		option = new Option(ARG_AWS_SECRET_ACCESS_KEY, true,
				"AWS Secret Access key");
		option.setRequired(true);
		cliOptions.addOption(option);

		option = new Option(ARG_AWS_S3_BUCKET_NAME, true, "AWS S3 Bucket name");
		option.setRequired(true);
		cliOptions.addOption(option);
	}

	public static void parseBasicArguments(CommandLine commandLine) {

		accessKeyId = commandLine.getOptionValue(ARG_AWS_ACCESS_KEY_ID);
		secretAccessKey = commandLine.getOptionValue(ARG_AWS_SECRET_ACCESS_KEY);
		bucketName = commandLine.getOptionValue(ARG_AWS_S3_BUCKET_NAME);
	}

	public static CommandLine parseCommandLine(String[] args) {

		CommandLine commandLine = null;

		CommandLineParser parser = new GnuParser();

		try {
			commandLine = parser.parse(cliOptions, args, true);
		} catch (ParseException e) {
			System.out
					.println("The problem occurred while parsing command line: "
							+ e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("DownloadDirFromS3", cliOptions);
		}

		return commandLine;
	}

	public static String getAccessKeyID() {
		return accessKeyId;
	}

	public static String getSecretAccessKey() {
		return secretAccessKey;
	}

	public static String getBucketName() {
		return bucketName;
	}

	public static AWSCredentials getAwsCredentionals() {
		return Manager.createAwsCredentionals(accessKeyId, secretAccessKey);
	}

	public static AmazonS3 getAwsClient() {

		if (s3Client == null) {
			s3Client = Manager.createAwsClient(getAwsCredentionals());
		}
		return s3Client;
	}

	public static void releaseBasicResources() {
		s3Client = null;
	}

	public static String getRelativeFilePath(String rootFolder,
			String absFilePath, boolean removeRootFolderName) {

		String a = prettifyFilePath(absFilePath);
		String r = prettifyFilePath(rootFolder);
		if (r.endsWith(File.separator) || r.endsWith("/")) {
			r = r.substring(0, r.length() - 1);
		}

		if (removeRootFolderName) {
			return a.substring(r.length());
		} else {
			return a.substring(r.lastIndexOf("/"));
		}

		// r = prettifyFilePath((removeRootFolderName) ?
		// r:r.substring(r.lastIndexOf("/")));

		// return a.substring(0, a.indexOf(r));
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
		prettifiedPath = prettifiedPath.replace(" ", ASCII_SPACE);
		prettifiedPath = prettifiedPath.replaceAll("//", "/");
		prettifiedPath = prettifiedPath.replaceAll("//", "/");
		return prettifiedPath;
	}


	public static void initAwsCredentionals(String accessKeyId_,
			String secretAccessKey_, String bucketName_) {

		accessKeyId = accessKeyId_;
		secretAccessKey = secretAccessKey_;
		bucketName = bucketName_;
	}
	
	public static class LOGGER {

		public static void info(final String message) {
			System.out.println("[INFO] " + message);
		}

		public static void warn(final String message) {
			System.out.println("[WARNING] " + message);
		}

		public static void error(final String message) {
			System.out.println("[ERROR] " + message);
		}

		public static void debug(final String message) {
			if (debugEnabled) {
				System.out.println("[DEBUG] " + message);
			}
		}

	}
}
