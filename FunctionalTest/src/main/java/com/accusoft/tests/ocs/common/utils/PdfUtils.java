package com.accusoft.tests.ocs.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfUtils {



    public static String getStringWithLinkFromPdf (String filePath, String uri){

        StringBuilder sb = new StringBuilder();
        String keyword = "Link";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();

            while (line != null) {
                if (line.contains(keyword)&&line.contains(uri)){
                    sb.append(System.lineSeparator());
                    sb.append(line);
                }
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
	public static String getTextFromPDF(String absPathToPDF, int startPage,
			int endPage) {
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File(absPathToPDF);

		String parsedText = null;
		PDFParser parser = null;
		try {
			parser = new PDFParser(new FileInputStream(file));
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(startPage);
			pdfStripper.setEndPage(endPage);
			parsedText = pdfStripper.getText(pdDoc);
		} catch (Exception ex) {
			System.out
					.println("Something is wrong " + ex.getLocalizedMessage());
		} finally {
			if (parser != null) {
				try {
					pdDoc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (parsedText != null) {
			parsedText = parsedText.trim();
		}

		return parsedText;
	}
}