package com.accusoft.tests.ocs.common.utils.amazon;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.accusoft.tests.ocs.common.utils.amazon.BasicS3Step.LOGGER;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class Manager {

	private static String SUFFIX = "/";

	private final static int NUMBER_OF_S3_REQUEST_RETRIES = 10;

	private static String AWS_ACCESS_KEY_ID = "YourAccessKeyID";
	private static String AWS_SECRET_ACCESS_KEY = "YourSecretAccessKey";

	public static void main(String[] args) {

		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for
		// this example to work
		AWSCredentials credentionals = createAwsCredentionals(
				AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);

		// create a client connection based on credentials
		AmazonS3 s3client = createAwsClient(credentionals);

		// create bucket - name must be unique for all S3 users
		String bucketName = "pcc-test-resources";
		// createBucket(s3client, bucketName); // have no permissions to create
		// bucket

		// list buckets
		listBuckets(s3client);

		// create folder into bucket
		String folderName = "testFolder";
		createFolderInBucket(bucketName, folderName, s3client);

		// upload file to folder and set it to public
		File file = getTestFile();
		uploadFileInBucket(bucketName, folderName, file, s3client);

		// delete file
		deleteFile(bucketName, folderName + SUFFIX + file.getName(), s3client);

		// delete folder
		deleteFolder(bucketName, folderName, s3client);

		// deletes bucket
		// deleteBucket(bucketName, s3client); // have no permissions to create
		// bucket
	}

	public static AWSCredentials createAwsCredentionals(
			final String accessKeyId, final String secretAccessKey) {

		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,
				secretAccessKey);

		return credentials;
	}

	public static AmazonS3 createAwsClient(final AWSCredentials credentials) {

		AmazonS3 s3client = new AmazonS3Client(credentials);

		return s3client;
	}

	public static void createBucket(final AmazonS3 s3client,
			final String bucketName) {

		if (s3client != null) {
			s3client.createBucket(bucketName);
		} else {
			throw new RuntimeException("S3 client can not be null");
		}
	}

	public static void listBuckets(final AmazonS3 s3client) {

		if (s3client != null) {
			for (Bucket bucket : s3client.listBuckets()) {
				System.out.println(" - " + bucket.getName());
			}
		} else {
			throw new RuntimeException("S3 client can not be null");
		}

	}

	public static void createFolderInBucket(final String bucketName,
			final String folderName, final AmazonS3 s3client) {

		if (s3client != null) {
			// create meta-data for your folder and set content-length to 0
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);
			// create empty content
			InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
			// create a PutObjectRequest passing the folder name suffixed by /
			PutObjectRequest putObjectRequest = new PutObjectRequest(
					bucketName, folderName + SUFFIX, emptyContent, metadata);
			// send request to S3 to create folder
			s3client.putObject(putObjectRequest);
		} else {
			throw new RuntimeException("S3 client can not be null");
		}

	}

	public static void uploadFileInBucket(final String bucketName,
			final String folderName, final File file, final AmazonS3 s3client) {

		if (s3client != null) {

			if (folderName != null) {
				createFolderInBucket(bucketName, folderName, s3client);
			}

			String fileName = folderName + SUFFIX + file.getName();

			boolean error = false;

			for (int retries = 1; retries <= NUMBER_OF_S3_REQUEST_RETRIES; retries++) {
				try {
					s3client.putObject(new PutObjectRequest(bucketName,
							fileName, file));
					// .withCannedAcl(CannedAccessControlList.PublicRead));
					error = false;
					break;
				} catch (Exception ex) {
					LOGGER.error("Some error during file transmission to AWS S3 bucket "
							+ ex.getMessage());
					LOGGER.error("Resending upload request [" + (retries + 1)
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
				throw new RuntimeException("Can not upload file to S3 ["
						+ fileName + "] from local location ["
						+ file.getAbsolutePath() + "]");
			}

		} else {
			throw new RuntimeException("S3 client can not be null");
		}

	}

	public static void deleteBucket(final String bucketName,
			final AmazonS3 s3client) {

		if (s3client != null) {
			s3client.deleteBucket(bucketName);
		} else {
			throw new RuntimeException("S3 client can not be null");
		}

	}

	public static void deleteFile(final String bucketName,
			final String fileName, final AmazonS3 s3client) {

		if (s3client != null) {
			s3client.deleteObject(bucketName, fileName);
		} else {
			throw new RuntimeException("S3 client can not be null");
		}

	}

	public static void deleteFolder(final String bucketName,
			final String folderName, final AmazonS3 s3client) {

		if (s3client != null) {
			List<S3ObjectSummary> fileList = s3client.listObjects(bucketName,
					folderName).getObjectSummaries();
			for (S3ObjectSummary file : fileList) {
				s3client.deleteObject(bucketName, file.getKey());
			}
			s3client.deleteObject(bucketName, folderName);
		} else {
			throw new RuntimeException("S3 client can not be null");
		}

	}

	public static List<S3ObjectSummary> getFolderContentList(
			final String bucketName, final String folderName,
			final AmazonS3 s3client) {

		List<S3ObjectSummary> fileList = new ArrayList<S3ObjectSummary>();

		if (s3client != null) {

			ObjectListing objects = s3client
					.listObjects(bucketName, folderName);
			fileList.addAll(objects.getObjectSummaries());

			while (objects.isTruncated()) {
				objects = s3client.listNextBatchOfObjects(objects);
				fileList.addAll(objects.getObjectSummaries());
			}

		} else {
			throw new RuntimeException("S3 client can not be null");
		}

		return fileList;
	}

	public void CopyObjectSingleOperation(AmazonS3 s3client, String bucketName,
			String sourceKey, String destinationKey) {

		try {
			// Copying object
			CopyObjectRequest copyObjRequest = new CopyObjectRequest(
					bucketName, sourceKey, bucketName, destinationKey);
			System.out.println("Copying object.");
			s3client.copyObject(copyObjRequest);

		} catch (AmazonServiceException ase) {
			LOGGER.error("requestn to Amazon S3 was rejected with an error "
					+ ase.getErrorMessage());
			LOGGER.info("Error Message:    " + ase.getMessage());
			LOGGER.info("HTTP Status Code: " + ase.getStatusCode());
			LOGGER.info("AWS Error Code:   " + ase.getErrorCode());
			LOGGER.info("Error Type:       " + ase.getErrorType());
			LOGGER.info("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			LOGGER.error("Client encountered "
					+ "an internal error while trying to "
					+ " communicate with S3, "
					+ "such as not being able to access the network: "
					+ ace.getLocalizedMessage());
		}
	}

	private static File getTestFile() {
		URL url = Manager.class.getClassLoader().getResource("TestFile.txt");
		return new File(url.getPath());
	}
}
