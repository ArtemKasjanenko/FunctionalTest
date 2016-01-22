package com.accusoft.tests.ocs.common.utils.amazon;

import java.io.File;
import java.util.List;

import com.accusoft.tests.ocs.common.utils.FSUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class UploadFileToS3 extends BasicS3Step {

	private final static String ARG_S3_DIR_NAME = "awsS3DirName";
	private final static String ARG_LOCAL_FILE_NAME = "localFileName";

	private final static String ARG_OVERWRITE_EXISTING_FILE = "overwriteExistingFile";

	private final static String ARG_AWS_ACCESS_KEY_ID = "awsAccessKeyId";
	private final static String ARG_AWS_SECRET_ACCESS_KEY = "awsSecretAccessKey";
	private final static String ARG_AWS_S3_BUCKET_NAME = "awsS3BucketName";

	private static String s3DirName = null;
	private static String localFileName = null;
	private static boolean overwriteExistingFile = false;

	public static void main(String[] args) {
		initTestProperties();
		run();
		System.exit(0);
	}

	public static void initTestProperties() {

		s3DirName = FSUtils.prettifyFilePath(System
				.getProperty(ARG_S3_DIR_NAME));

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

		LOGGER.info("\nUPLOAD FILE TO S3 STEP PROPERTIES:");

		LOGGER.info("S3 DIRECTORY NAME: " + s3DirName);

		LOGGER.info("LOCAL FILE NAME: " + localFileName);

		LOGGER.info("OVERWRITE EXISTING FILE: " + overwriteExistingFile);

		LOGGER.info("ACCESS KEY ID: " + accessKeyId);

		LOGGER.info("SECRET ACCESS KEY: " + secretAccessKey);

		LOGGER.info("BUCKET NAME: " + bucketName);

	}

	public static void run(String accessKeyId_, String secretAccessKey_,
			String bucketName_, String s3DirName_, String localFileName_,
			boolean overwriteExistingFile_) {
		accessKeyId = accessKeyId_;
		secretAccessKey = secretAccessKey_;
		bucketName = bucketName_;
		s3DirName = s3DirName_;
		localFileName = localFileName_;
		overwriteExistingFile = overwriteExistingFile_;

		run();
	}

	public static void run() {

		//
		if (s3DirName == null) {
			throw new RuntimeException(
					"S3 Directory Name can not be null or empty");
		}

		if (s3DirName.isEmpty()) {
			throw new RuntimeException(
					"S3 Directory Name can not be null or empty");
		}

		if (s3DirName.endsWith("/")) {
			s3DirName = s3DirName.substring(0, s3DirName.length() - 1);
		}

		// Check if local file exists
		File localFile = new File(localFileName);
		if (!localFile.exists()) {
			throw new RuntimeException("Local file " + localFile
					+ " does not exist");
		}

		AmazonS3 s3Client = getAwsClient();

		// Check if file already exists in S3

		List<S3ObjectSummary> fileList = Manager.getFolderContentList(
				getBucketName(), s3DirName, s3Client);

		boolean fileExists = false;

		if (!overwriteExistingFile) {
			for (S3ObjectSummary file : fileList) {
				if (file.getKey().equalsIgnoreCase(
						s3DirName + File.separator + localFile.getName())) {
					LOGGER.info("File [" + localFileName
							+ "] already exists inside S3 Bucket folder ["
							+ s3DirName + "]");
					fileExists = true;
					break;
				}
			}
		}

		if (overwriteExistingFile || !fileExists) {

			LOGGER.debug("Uploading local file ["
					+ localFile
					+ "] to AWS S3 Folder ["
					+ FSUtils.prettifyFilePath(getBucketName()
							+ File.pathSeparator + s3DirName) + "]");

			Manager.uploadFileInBucket(getBucketName(), s3DirName, localFile,
					s3Client);

			LOGGER.debug("Local file ["
					+ localFile
					+ "] is uploaded to AWS S3 Folder ["
					+ FSUtils.prettifyFilePath(getBucketName()
							+ File.pathSeparator + s3DirName) + "]");
		}

	}

}
