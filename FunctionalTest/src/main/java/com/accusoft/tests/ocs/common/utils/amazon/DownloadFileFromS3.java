package com.accusoft.tests.ocs.common.utils.amazon;

import java.io.File;
import java.util.List;

import com.accusoft.tests.ocs.common.utils.FSUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class DownloadFileFromS3 extends BasicS3Step {

	private final static int NUMBER_OF_S3_REQUEST_RETRIES = 10;

	private final static String ARG_S3_FILE_NAME = "awsS3FileName";
	private final static String ARG_LOCAL_FILE_NAME = "localFileName";

	private final static String ARG_OVERWRITE_EXISTING_FILE = "overwriteExistingFile";

	private final static String ARG_AWS_ACCESS_KEY_ID = "awsAccessKeyId";
	private final static String ARG_AWS_SECRET_ACCESS_KEY = "awsSecretAccessKey";
	private final static String ARG_AWS_S3_BUCKET_NAME = "awsS3BucketName";

	private static String s3FileName = null;
	private static String localFileName = null;
	private static boolean overwriteExistingFile = false;
	
	private static boolean checkExistanceOfFileInS3 = false;

	public static void main(String[] args) {
		initTestProperties();
		run();
		System.exit(0);
	}

	public static void initTestProperties() {

		s3FileName = FSUtils.prettifyFilePath(System
				.getProperty(ARG_S3_FILE_NAME));

		localFileName = FSUtils.prettifyFilePath(System
				.getProperty(ARG_LOCAL_FILE_NAME));

		overwriteExistingFile = Boolean.parseBoolean(FSUtils
				.prettifyFilePath(System.getProperty(
						ARG_OVERWRITE_EXISTING_FILE, "false")));

		accessKeyId = FSUtils.prettifyFilePath(System
				.getProperty(ARG_AWS_ACCESS_KEY_ID));
		secretAccessKey = FSUtils.prettifyFilePath(System
				.getProperty(ARG_AWS_SECRET_ACCESS_KEY));
		bucketName = FSUtils.prettifyFilePath(System
				.getProperty(ARG_AWS_S3_BUCKET_NAME));

		LOGGER.info("\nDOWNLOAD FILE FROM S3 STEP PROPERTIES:");

		LOGGER.info("S3 FILE NAME: " + s3FileName);

		LOGGER.info("LOCAL FILE NAME: " + localFileName);

		LOGGER.info("OVERWRITE EXISTING FILE: " + overwriteExistingFile);

		LOGGER.info("ACCESS KEY ID: " + accessKeyId);

		LOGGER.info("SECRET ACCESS KEY: " + secretAccessKey);

		LOGGER.info("BUCKET NAME: " + bucketName);

	}

	public static void init(String accessKeyId_, String secretAccessKey_,
			String bucketName_, String s3FileName_, String localFileName_,
			boolean overwriteExistingFile_) {

		accessKeyId = accessKeyId_;
		secretAccessKey = secretAccessKey_;
		bucketName = bucketName_;
		s3FileName = s3FileName_;
		localFileName = localFileName_;
		overwriteExistingFile = overwriteExistingFile_;

	}



	public static void run(String accessKeyId_, String secretAccessKey_,
			String bucketName_, String s3FileName_, String localFileName_,
			boolean overwriteExistingFile_, boolean checkExistanceOfFileInS3_) {
		accessKeyId = accessKeyId_;
		secretAccessKey = secretAccessKey_;
		bucketName = bucketName_;
		s3FileName = s3FileName_;
		localFileName = localFileName_;
		overwriteExistingFile = overwriteExistingFile_;
		checkExistanceOfFileInS3 = checkExistanceOfFileInS3_;

		run();
	}

	public static void run() {

		//
		if (s3FileName == null) {
			throw new RuntimeException("S3 File Name can not be null or empty");
		}

		if (s3FileName.isEmpty()) {
			throw new RuntimeException("S3 File Name can not be null or empty");
		}

		// Check if local file exists
		File localFile = new File(localFileName);
		boolean localFileExists = localFile.exists();

		if (localFileExists) {
			LOGGER.info("Local file " + localFileName + " exists");
		}

		AmazonS3 s3Client = getAwsClient();

		// Check if file exists in S3

		if(checkExistanceOfFileInS3){
			String s3DirName = FSUtils.getParentFolderPath(s3FileName);

			List<S3ObjectSummary> fileList = Manager.getFolderContentList(
					getBucketName(), s3DirName, s3Client);

			boolean fileExistsInS3 = false;
			for (S3ObjectSummary file : fileList) {
				if (file.getKey().equalsIgnoreCase(s3FileName)) {
					LOGGER.info("File [" + s3FileName
							+ "]  exists inside S3 Bucket folder");
					fileExistsInS3 = true;
					break;
				}

			}

			if (!fileExistsInS3) {
				throw new RuntimeException("Requested file does not exist in S3 "
						+ s3FileName);
			}
		}

		if (overwriteExistingFile || !localFileExists) {

			// Creation of local dir
			String localDirPath = FSUtils
					.getParentFolderPath(localFileName);
			File localDir = new File(localDirPath);
			if (!localDir.exists()) {
				localDir.mkdirs();
			}

			LOGGER.debug("Downloading S3 file "
					+ FSUtils.prettifyFilePath(getBucketName()
							+ File.pathSeparator + s3FileName)
					+ "] to local file system  [" + localFileName + "]");

			boolean error = false;

			for (int retries = 1; retries <= NUMBER_OF_S3_REQUEST_RETRIES; retries++) {
				try {
					s3Client.getObject(new GetObjectRequest(getBucketName(),
							s3FileName), new File(localFileName));
					error = false;
					break;
				} catch (Exception ex) {
					LOGGER.error("Some error during file transmission from AWS S3 bucket "
							+ ex.getMessage());
					LOGGER.error("Resending download request [" + (retries + 1)
							+ "] from [" + NUMBER_OF_S3_REQUEST_RETRIES
							+ "] in 1 second");
					error = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			if (error) {
				throw new RuntimeException("Can not download file from S3 ["
						+ s3FileName + "] to local location [" + localFileName
						+ "]");
			}

			LOGGER.debug("AWS S3 File " + s3FileName + " is downloaded to "
					+ localFileName);

		}

	}
}
