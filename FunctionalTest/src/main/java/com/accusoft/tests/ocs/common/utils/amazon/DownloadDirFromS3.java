package com.accusoft.tests.ocs.common.utils.amazon;

import java.io.File;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;

import com.accusoft.tests.ocs.common.utils.FSUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class DownloadDirFromS3 extends BasicS3Step {

	private final static int NUMBER_OF_S3_REQUEST_RETRIES = 10;

	private final static String ARG_AWS_S3_DIR_NAME = "awsS3DirName";
	private final static String ARG_LOCAL_DESTINATION = "localDirName";
	private final static String ARG_COPY_DIR_CONTENT_ONLY = "copyDirContentOnly";

	private final static String COPY_DIR_CONTENT_DEFAULT = "false";
	
	private final static String ARG_AWS_ACCESS_KEY_ID = "awsAccessKeyId";
	private final static String ARG_AWS_SECRET_ACCESS_KEY = "awsSecretAccessKey";
	private final static String ARG_AWS_S3_BUCKET_NAME = "awsS3BucketName";

	private static String awsS3DirName = null;
	private static String localDestination = null;
	private static boolean copyDirContentOnly = false;
	private static List<String> filesToIgnore = null;

	public static void main(String[] args) {

		initTestProperties();
		run();
		releaseBasicResources();
		System.exit(0);
	}
	
	public static void initTestProperties() {

		awsS3DirName = FSUtils.prettifyFilePath(System
				.getProperty(ARG_AWS_S3_DIR_NAME));

		localDestination = FSUtils.prettifyFilePath(System
				.getProperty(ARG_LOCAL_DESTINATION));

		copyDirContentOnly = Boolean.parseBoolean(FSUtils
				.prettifyFilePath(System.getProperty(
						ARG_COPY_DIR_CONTENT_ONLY, COPY_DIR_CONTENT_DEFAULT)));

		accessKeyId = FSUtils.prettifyFilePath(System
				.getProperty(ARG_AWS_ACCESS_KEY_ID));
		secretAccessKey = FSUtils.prettifyFilePath(System
				.getProperty(ARG_AWS_SECRET_ACCESS_KEY));
		bucketName = FSUtils.prettifyFilePath(System
				.getProperty(ARG_AWS_S3_BUCKET_NAME));

		LOGGER.info("\nDOWNLOAD DIR FROM S3 STEP PROPERTIES:");

		LOGGER.info("S3 DIR NAME: " + awsS3DirName);

		LOGGER.info("LOCAL DIR NAME: " + localDestination);

		LOGGER.info("COPY DIR CONTENT ONLY: " + copyDirContentOnly);

		LOGGER.info("ACCESS KEY ID: " + accessKeyId);

		LOGGER.info("SECRET ACCESS KEY: " + secretAccessKey);

		LOGGER.info("BUCKET NAME: " + bucketName);

	}

	public static void run(String accessKeyId_, String secretAccessKey_,
			String bucketName_, String awsS3DirName_, String localDestination_,
			boolean copyDirContentOnly_, List<String> filesToIgnore_) {
		accessKeyId = accessKeyId_;
		secretAccessKey = secretAccessKey_;
		bucketName = bucketName_;
		awsS3DirName = awsS3DirName_;
		localDestination = localDestination_;
		copyDirContentOnly = copyDirContentOnly_;
		filesToIgnore = filesToIgnore_;

		run();
	}

	private static void run() {

		int progress = 0;
		
		AmazonS3 s3Client = getAwsClient();

		List<S3ObjectSummary> fileList = Manager.getFolderContentList(
				getBucketName(), awsS3DirName, s3Client);

		LOGGER.info("AWS S3 Bucket folder " + awsS3DirName + " has "
				+ fileList.size() + " files/folders");

		int totalNumberOfFiles = fileList.size();
		int numberOfCurrentFile = 1;

		for (S3ObjectSummary file : fileList) {

			if (filesToIgnore != null && filesToIgnore.contains(file.getKey())) {
				LOGGER.debug("SKIPPING downloading of S3 file  ["
						+ file.getKey() + "] according to ignore list");

			} else {
				String localPath = prettifyFilePath(localDestination
						+ File.separator
						+ getRelativeFilePath(awsS3DirName, file.getKey(),
								copyDirContentOnly));

				String remotePath = prettifyFilePath(getBucketName()
						+ File.separator + awsS3DirName + File.separator
						+ file.getKey());

				// Downloading of selected resource to local file system

				File localFile = null;
				boolean error = false;
				if (file.getSize() == 0) {

					// Creating local directory

					localFile = new File(localPath);
					if (!localFile.exists()) {
						localFile.mkdirs();
					}
					LOGGER.debug("Local directory " + localPath + " is created");
				} else {

					// Copy file from AWS S3 Bucket to local file system

					for (int retries = 1; retries <= NUMBER_OF_S3_REQUEST_RETRIES; retries++) {
						try {
							s3Client.getObject(new GetObjectRequest(
									getBucketName(), file.getKey()), new File(
									localPath));
							LOGGER.debug("File " + remotePath + " is copied to "
									+ localPath);
							error = false;
							break;
						} catch (Exception ex) {
							LOGGER.error("Some error during file transmission from AWS S3 bucket "
									+ ex.getMessage());
							LOGGER.error("Resending download request ["
									+ (retries + 1) + "] from ["
									+ NUMBER_OF_S3_REQUEST_RETRIES
									+ "] in 1 second");
							error = true;
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

				}

				if (error) {
					throw new RuntimeException(
							"Can not download file from S3 [" + remotePath
									+ "] to local location [" + localPath + "]");
				}
			}

			int currentProgress = ((numberOfCurrentFile*100)/totalNumberOfFiles);
			
			if(currentProgress != progress){
				progress = currentProgress;
				LOGGER.info("Files copy progress [" + currentProgress + "%]");
			}
			
			
		}
	}

	public static void initStepArguments() {

		Option option = null;
		option = new Option(ARG_AWS_S3_DIR_NAME, true, "AWS S3 directory name");
		option.setRequired(true);
		cliOptions.addOption(option);

		option = new Option(ARG_LOCAL_DESTINATION, true,
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

			localDestination = commandLine
					.getOptionValue(ARG_LOCAL_DESTINATION);

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
}
