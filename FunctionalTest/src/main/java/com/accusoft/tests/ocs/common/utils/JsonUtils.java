package com.accusoft.tests.ocs.common.utils;

import org.apache.pdfbox.cos.COSDictionary;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {

	public static String getVersionFromResponse(String responseBody) {

		org.json.JSONObject response = new JSONObject(responseBody);

		JSONObject data = (JSONObject) response.get("data");

		return data.get("htmlcsVersion").toString();
	}

	public static String getServiceStatusFromResponse(String responseBody) {

		JSONObject response = new JSONObject(responseBody);

		JSONObject data = (JSONObject) response.get("data");

		return data.get("serviceStatus").toString();
	}

	public static String getPrizmStatusFromResponse(String responseBody) {

		JSONObject response = new JSONObject(responseBody);

		return response.get("serviceStatus").toString();
	}

	public static int getPageCountFromResponse(String responseBody) {

		JSONObject response = new JSONObject(responseBody);

		JSONObject data = (JSONObject) response.get("data");

		return (Integer) data.get("pageCount");
	}

	public static int getPageCountFromFileAttributes(String fileAttributePath) {

		JSONParser parser = new JSONParser();
		int pageCount = 0;

		try {

			org.json.simple.JSONObject fileAttributes = (org.json.simple.JSONObject) parser
					.parse(new FileReader(fileAttributePath));

			pageCount = ((Long) fileAttributes.get("pageCount")).intValue();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return pageCount;
	}

	public static String getfileAttributesJsonContent(String fileAttributePath) {

		JSONParser parser = new JSONParser();
		String fileContent = null;

		try {

			org.json.simple.JSONObject fileAttributes = (org.json.simple.JSONObject) parser
					.parse(new FileReader(fileAttributePath));

			fileContent = fileAttributes.toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return fileContent;
	}

	public static int getNumberOfLinks(Map<Integer, List<COSDictionary>> links) {
		int numberOfLinks = 0;
		for (Map.Entry<Integer, List<COSDictionary>> pageLinks : links
				.entrySet()) {
			numberOfLinks += pageLinks.getValue().size();
		}
		return numberOfLinks;
	}

	public static int getNumberOfLinks(List<COSDictionary> pageLinks) {
		return pageLinks.size();
	}

	public static int getNumberOfLinksFromJsonContent(String json) {
		List<String> links = getAllLinks(json);
		return links.size();
	}

	public static int getNumberOfLinksFromJsonContent(Map<Integer, String> links) {

		int numberOfLinks = 0;
		for (Map.Entry<Integer, String> link : links.entrySet()) {
			List<String> pageLinks = getAllLinks(link.getValue());
			numberOfLinks += pageLinks.size();
		}
		return numberOfLinks;
	}

	public static Integer[] getLinksDestinations(
			Map<Integer, List<COSDictionary>> links, int pageNumber) {

		List<Integer> destinations = new ArrayList<Integer>();

		if (pageNumber >= 0) {
			destinations.addAll(getLinksDestination(links.get(pageNumber)));
		} else {
			for (Map.Entry<Integer, List<COSDictionary>> link : links
					.entrySet()) {
				destinations.addAll(getLinksDestination(link.getValue()));
			}
		}

		Integer[] arr = new Integer[destinations.size()];
		destinations.toArray(arr);

		return arr;

	}

	public static List<Integer> getLinksDestination(
			List<COSDictionary> pageLinks) {

		List<Integer> destinations = new ArrayList<Integer>();

		for (COSDictionary pageLink : pageLinks) {

			destinations.add(getLinkDestination(pageLink));
		}

		return destinations;
	}

	public static Integer getLinkDestination(COSDictionary link) {
		return Integer.parseInt(((COSDictionary) link.getItem("properties"))
				.getString("href"));
	}

	// public static Integer[] getLinksDestinations(Map<Integer, String> links,
	// int pageNumber) {
	//
	// List<Integer> destinations = new ArrayList<Integer>();
	//
	// if (pageNumber >= 0) {
	// destinations.addAll(getLinksDestination(links.get(pageNumber)));
	// } else {
	// for (Map.Entry<Integer, String> link : links.entrySet()) {
	// destinations.addAll(getLinksDestination(link.getValue()));
	// }
	// }
	//
	// Integer[] arr = new Integer[destinations.size()];
	// destinations.toArray(arr);
	//
	// return arr;
	//
	// }

	// public static List<Integer> getLinksDestination(String json) {
	//
	// List<Integer> destinations = new ArrayList<Integer>();
	//
	// JSONParser parser = new JSONParser();
	//
	// try {
	//
	// List<String> links = getAllLinks(json);
	//
	// for (String link : links) {
	// org.json.simple.JSONObject fileAttributes = (org.json.simple.JSONObject)
	// parser
	// .parse(link);
	//
	// destinations.add((Integer) fileAttributes.get("href"));
	// }
	//
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// return destinations;
	// }

	public static List<String> getAllLinks(String json) {

		List<String> links = new ArrayList<String>();

		if (json == null) {
			return links;
		}

		boolean isFirst = true;
		for (String link : json
				.split("\\{\"changeType\":\"Add\",\"markType\":\"")) {
			if (isFirst) {
				isFirst = false;
				continue;
			}

			if (link.trim().endsWith(",")) {
				links.add(link.substring(0, link.lastIndexOf(',')));
			} else {
				links.add(link.trim());
			}

		}

		return links;
	}

}