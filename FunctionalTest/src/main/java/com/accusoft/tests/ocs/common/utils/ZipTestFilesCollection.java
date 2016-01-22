package com.accusoft.tests.ocs.common.utils;

import java.io.File;

public class ZipTestFilesCollection {

	public static void main(String[] args) {

		// ZIP content of filesForTesting folder

		String pathToFilesForTesting = ZipTestFilesCollection.class.getClassLoader()
				.getResource(".").getPath()
				+ "/../../filesForTesting/";

		String zipFile = pathToFilesForTesting + "/../" + "filesForTesting.zip";

		// Remove previously created ZIP file 
		
		File curZipFile = new File(zipFile);
		if (curZipFile.exists()) {
			curZipFile.delete();
		}

		System.out.println("Moving file from " + pathToFilesForTesting
				+ " folder to " + zipFile + " archive");

		try {
			FSUtils.ZipUtility.zip(pathToFilesForTesting, zipFile);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			System.out.println("Done");
		}
	}

}
