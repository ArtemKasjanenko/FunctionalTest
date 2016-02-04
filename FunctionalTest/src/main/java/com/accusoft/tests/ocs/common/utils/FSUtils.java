package com.accusoft.tests.ocs.common.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import com.accusoft.tests.ocs.common.exceptions.IncorrectTestDefinitionException;

public class FSUtils {

	private final static String ASCII_SPACE = "%20";

	/**
	 * Gets the files list.
	 * 
	 * @param ignoreFileType
	 *            the ignore file type
	 * @return the files list
	 */
	public static Collection<File> getFilesList(String rootLocation, String type) {

		try {

			File rootFolfer = new File(rootLocation);

			if (type != null) {

				String[] extensions = new String[2];
				extensions[0] = type.toLowerCase();
				extensions[1] = type.toUpperCase();

				return org.apache.commons.io.FileUtils.listFiles(rootFolfer,
						extensions, true);
			} else {

				return org.apache.commons.io.FileUtils.listFiles(rootFolfer,
						null, true);
			}

		} catch (Exception e) {
			throw new IncorrectTestDefinitionException(
					"Can not read files from " + rootLocation, e);
		}
	}

	public static class UnzipUtility {
		/**
		 * Size of the buffer to read/write data
		 */
		private static final int BUFFER_SIZE = 4096;

		/**
		 * Extracts a zip file specified by the zipFilePath to a directory
		 * specified by destDirectory (will be created if does not exists)
		 * 
		 * @param zipFilePath
		 * @param destDirectory
		 * @throws IOException
		 */
		static public void unzip(String zipFilePath, String destDirectory)
				throws IOException {
			File destDir = new File(destDirectory);
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(
					zipFilePath));
			ZipEntry entry = zipIn.getNextEntry();
			// iterates over entries in the zip file
			while (entry != null) {
				String filePath = destDirectory + File.separator
						+ entry.getName();
				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					extractFile(zipIn, filePath);
				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
			zipIn.close();
		}

		/**
		 * Extracts a zip entry (file entry)
		 * 
		 * @param zipIn
		 * @param filePath
		 * @throws IOException
		 */
		private static void extractFile(ZipInputStream zipIn, String filePath)
				throws IOException {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(filePath));
			byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
			bos.close();
		}

		public static int unzip(File inputZip, File outputFolder)
				throws IOException {
			int count = 0;
			FileInputStream fis = null;
			ZipArchiveInputStream zis = null;
			FileOutputStream fos = null;
			try {
				byte[] buffer = new byte[8192];
				fis = new FileInputStream(inputZip);
				zis = new ZipArchiveInputStream(fis, "Cp1252", true); // this
																		// supports
																		// non-USACII
																		// names
				ArchiveEntry entry;
				while ((entry = zis.getNextEntry()) != null) {
					File file = new File(outputFolder, entry.getName());
					if (entry.isDirectory()) {
						file.mkdirs();
					} else {
						count++;
						file.getParentFile().mkdirs();
						fos = new FileOutputStream(file);
						int read;
						while ((read = zis.read(buffer, 0, buffer.length)) != -1)
							fos.write(buffer, 0, read);
						fos.close();
						fos = null;
					}
				}
			} finally {
				try {
					zis.close();
				} catch (Exception e) {
				}
				try {
					fis.close();
				} catch (Exception e) {
				}
				try {
					if (fos != null)
						fos.close();
				} catch (Exception e) {
				}
			}
			return count;
		}

	}

	public static class ZipUtility {

		public static void zip(String sourceDir, String outputFile)
				throws IOException, FileNotFoundException {
			ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(
					outputFile));
			compressDirectoryToZipfile(sourceDir, sourceDir, zipFile);
			IOUtils.closeQuietly(zipFile);
		}

		private static void compressDirectoryToZipfile(String rootDir,
				String sourceDir, ZipOutputStream out) throws IOException,
				FileNotFoundException {
			for (File file : new File(sourceDir).listFiles()) {
				if (file.isDirectory()) {
					compressDirectoryToZipfile(rootDir,
							sourceDir + file.getName() + File.separator, out);
				} else {
					ZipEntry entry = new ZipEntry(
							sourceDir.replace(rootDir, "") + file.getName());
					out.putNextEntry(entry);

					FileInputStream in = new FileInputStream(sourceDir
							+ file.getName());
					IOUtils.copy(in, out);
					IOUtils.closeQuietly(in);
				}
			}
		}
	}

	/**
	 * Prettify file path.
	 *
	 * @param filePath
	 *            the file path
	 * @return the string
	 */
	public static String prettifyFilePath(String filePath) {

		if (filePath == null) {
			return null;
		}

		String prettifiedPath = filePath;
		prettifiedPath = prettifiedPath.replace('\\', '/');
		prettifiedPath = prettifiedPath.replace(" ", ASCII_SPACE);
		prettifiedPath = prettifiedPath.replaceAll("//", "/");
		prettifiedPath = prettifiedPath.replaceAll("//", "/");
		return prettifiedPath;
	}

	public static String getParentFolderPath(String pathToFile) {
		if (pathToFile == null) {
			return null;
		}

		String path = prettifyFilePath(pathToFile);

		return path.substring(0, path.lastIndexOf("/"));

	}

	public static int getNumberOfFiles(String rootDir) {

		File f = new File(rootDir);

		if (f.isDirectory()) {
			return f.list().length;
		}

		return 0;

	}

	public static void cleanFolder(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File f = new File(dir, children[i]);
				cleanFolder(f);
			}
			dir.delete();
		} else
			dir.delete();
	}

}
