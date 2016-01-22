package com.accusoft.tests.ocs.internalTests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.accusoft.tests.ocs.common.utils.pdfUtils.AnnotationExtractor;
import com.accusoft.tests.ocs.internalTests.stories.HyperLinks.HyperLinksInExcellStory;

public class HypelinksScaner {

	private String PATH_TO_PDF = "";
	private String PAGE_NUMBER = "";

	public static void main(String[] args) {

	}

	private Map<Integer, List<COSDictionary>> findHyperlinks(
			String pdfFilePath, Integer page) {

		Map<Integer, List<COSDictionary>> links = new HashMap<Integer, List<COSDictionary>>();

		try {

			PDDocument document = PDDocument.load(pdfFilePath);

			if (page != null) {
				links.put(page,
						AnnotationExtractor.extract(document, page + 1, true));
			} else {
				for (int pageCnt = 0; pageCnt < document.getNumberOfPages(); pageCnt++) {
					links.put(pageCnt, AnnotationExtractor.extract(document,
							pageCnt + 1, true));
				}
			}
			document.close();
		} catch (Exception ex) {
			throw new RuntimeException("Can not load PDF file " + pdfFilePath
					+ ": " + ex.getMessage(), ex);
		}

		return links;

	}

	private void showHyperlinks(Map<Integer, List<COSDictionary>> links) {

	}

}
