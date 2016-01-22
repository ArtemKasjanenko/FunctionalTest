package com.accusoft.tests.ocs.common.utils.pdfUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDestinationNameTreeNode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.action.type.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.type.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;

/**
 * Class to extract from PDF and convert to JSON link annotations
 */

public class AnnotationExtractor {
	
	 public static final Logger LOGGER = Logger.getLogger(AnnotationExtractor.class);

	private static final String COLOR_PARAM = "C";
	private static final String APPEARANCE_NORMAL_STATE = "N";
	private static final String APPEARANCE_STATE = "AS";
	private static final String APPEARANCE_DICTIONARY = "AP";
	public static final String CONTENTS = "Contents";
	public static final String ALTERNATE_DESCRIPTION = "alternateDescription";

	private static final int LOCKED_CONTENTS = 9;

	/**
	 * @param document
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	//public static String extract(PDDocument document, int page)
	public static List<COSDictionary> extract(PDDocument document, int page, boolean extractGoToOnly)
		throws IOException {
		
		List<COSDictionary> links = new ArrayList<COSDictionary>(); 

		AppendableOutputStream buffer = new AppendableOutputStream();

		ListDecorator list = new ListDecorator(buffer, "[", ", ", "]");
		
		for (PDAnnotation annotation : ((PDPage) (document.getDocumentCatalog().
						getAllPages().get(page - 1))).getAnnotations()) {
			try {
				if (annotation.getClass() != PDAnnotationLink.class)
					continue;

				PDAnnotationLink link = (PDAnnotationLink) annotation;
				PDAction action;
				String subtype;

				// test every step or will be exceptions
				boolean isURI = ((action = link.getAction()) != null)
								&& ((subtype = action.getSubType()) != null)
								&& subtype.equals("URI");
				if(isURI) {
					String href = action.getCOSDictionary().getString("URI");
					if(href != null && href.startsWith("file:///")) {
						continue;
					}
				}

				boolean isGoTo = ((action = link.getAction()) != null)
								&& ((subtype = action.getSubType()) != null)
								&& subtype.equals("GoTo")
								&& ((PDActionGoTo) link.getAction()).getDestination() != null;
				isGoTo = isGoTo || link.getDestination() != null;

	            if (!isURI && !isGoTo)
					continue;
				list.next();
				
				float pageHeight = ((PDPage) (document.getDocumentCatalog().getAllPages().get(page- 1))).getMediaBox().getHeight();
				//Json.printCOS(reformattedLink(document, link, pageHeight, isGoTo), new PrintStream(buffer));
				
				if(extractGoToOnly && !isGoTo){
					continue;
				}
				
				
				links.add(reformattedLink(document, link, pageHeight, isGoTo));

			} catch (Exception e) {
				// PCC-22128: handle exception so that we can process next link correctly
				LOGGER.warn("Unexpected error during link extraction", e);
			}
		}
		list.close();

		//String extract = buffer.toString();

		//return (extract != null && !extract.isEmpty() && !extract.equalsIgnoreCase("[]")) ? extract : null;
		
		return links;
	}

	/**
	 * Specific format converting procedure.
	 *
	 * @param link
	 * @return converted link
	 * @throws IOException
	 */
	static COSDictionary reformattedLink(PDDocument doc, PDAnnotationLink link, float pageHeight, boolean isGoTo) throws IOException {

		PDAction action = link.getAction();

		COSDictionary base = new COSDictionary();

		base.setString("changeType", "Add");
		base.setString("markType", "DocumentHyperlink");

		COSDictionary props = new COSDictionary();
		base.setItem("properties", props);

		if (isGoTo) {
			PDDestination dest;
			if (action != null) {
				PDActionGoTo gotoAction = (PDActionGoTo) action;
				dest = gotoAction.getDestination();
			} else {
				dest = link.getDestination();
			}

            props.setString("href", String.valueOf(findPageDestination(dest, doc)));
		} else {
			props.setString("href", action.getCOSDictionary().getString("URI"));
		}

		props.setItem("rectangle", convertRectangle(link, pageHeight));

		COSDictionary linkDict = link.getDictionary();

		if (linkDict.containsKey("F")) {
			props.setItem("pdfFlags", translateFlags(link));
		}

		if (linkDict.containsKey("H")) {
			props.setString("highlightMode", translateHighlightStyle(link.getHighlightMode()));
		}

		if (linkDict.containsKey(CONTENTS)) {
			props.setString(ALTERNATE_DESCRIPTION, linkDict.getNameAsString(CONTENTS));
		}

		if (linkDict.containsKey(APPEARANCE_DICTIONARY)) {
			COSDictionary ap = (COSDictionary) linkDict.getDictionaryObject(APPEARANCE_DICTIONARY);
			ap = (COSDictionary) ap.getDictionaryObject(APPEARANCE_NORMAL_STATE); // we're using NORMAL state
			if (linkDict.containsKey(APPEARANCE_STATE)) {
				String apName = linkDict.getString(APPEARANCE_STATE);
				ap = (COSDictionary) ap.getDictionaryObject(apName);
			}
		}

		if (linkDict.containsKey("Border") || linkDict.containsKey("BS")) {
			if (linkDict.containsKey("BS")) {
				// change it to link.getBorderStyle(); when upgrade to Pdfbox 1.8.9
				PDBorderStyleDictionary style = getLinkBorderStyle(link);
				props.setString("borderStyle", translateBorderStyleName(style.getStyle()));
				props.setFloat("borderThickness", style.getWidth());
				props.setItem("borderPattern", style.getDashStyle().getCOSDashPattern());
				// add more border style attributes processing here
			} else if (linkDict.containsKey("Border")) {
				COSArray linkBorder = (COSArray) linkDict.getDictionaryObject("Border");
				props.setFloat("borderHorizontalRadius", (float) ((COSNumber) linkBorder.get(0)).doubleValue());
				props.setFloat("borderVerticalRadius", (float) ((COSNumber) linkBorder.get(1)).doubleValue());
				props.setFloat("borderThickness", (float) ((COSNumber) linkBorder.get(2)).doubleValue());
				if (linkBorder.size() > 3 && linkBorder.get(3).getClass() == COSArray.class) {
					props.setItem("borderPattern", linkBorder.get(3));
				}
			}
			int borderOpacity = 255;
			if (linkDict.containsKey(COLOR_PARAM)) {
				COSArray color = (COSArray) linkDict.getDictionaryObject(COLOR_PARAM);
				int val = 0;
				if (color.size() > 0) {
					switch (color.size()) {
					case 1:
						val = getColorComponent(color, 0);
						val = (val << 16) + (val << 8) + val;
						break;
					case 3:
						int r = getColorComponent(color, 0);
						int g = getColorComponent(color, 1);
						int b = getColorComponent(color, 2);
						val = (r << 16) + (g << 8) + b;
						break;
					case 4:
						int[] arr = cmykToRgb(
							getColorComponent(color, 0),
							getColorComponent(color, 1),
							getColorComponent(color, 2),
							getColorComponent(color, 3));
						val = (arr[0] << 16) + (arr[1] << 8) + arr[2];
						break;
					default:
						throw new RuntimeException("Malformed PDF, color array of unexpected size: " + color.size());
					}
					props.setString("borderColor", toColorHex(val));
				} else { // otherwise transparent
					borderOpacity = 0;
				}
			}
			props.setInt("borderOpacity", borderOpacity);
		}

		if (linkDict.containsKey("QuadPoints")) {
			props.setItem("quadPoints", translateQuadPoints(link));
		}

		// ... convert more link attributes here

		return base;
	}

	private static String toColorHex(int val) {
		String rt = Integer.toHexString(val);
		while (rt.length() < 6) {
			rt = "0" + rt;
		}
		return "#" + rt;
	}

	private static int[] cmykToRgb(int cyan, int magenta, int yellow, int black)
	{
	    if (black!=255) {
	        int r = ((255-cyan) * (255-black)) / 255;
	        int g = ((255-magenta) * (255-black)) / 255;
	        int b = ((255-yellow) * (255-black)) / 255;
	        return new int[] {r,g,b};
	    } else {
	        int r = 255 - cyan;
	        int g = 255 - magenta;
	        int b = 255 - yellow;
	        return new int[] {r,g,b};
	    }
	}

	private static int getColorComponent(COSArray array, int index) throws IOException {
		COSBase obj = array.getObject(index);
		if (obj instanceof COSInteger) {
			int val = ((COSInteger) obj).intValue();
			if (val != 0 && val != 1) {
				throw new IOException("Error parsing PDF color component, unexpected integer value: " + val);
			}
			return val * 255;
		}
		return (int) (255 * ((COSFloat) obj).floatValue());
	}

	/**
	 * Translate QuadPoint attribute to JSON form
	 *
	 * @param link
	 *            link to get QuadPoint from
	 * @return composed object
	 */
	private static COSArray translateQuadPoints(PDAnnotationLink link) {

		float[] points = link.getQuadPoints();
		COSArray items = new COSArray();

		for (int start = 0; start < points.length; start += 8) {
			COSArray item = new COSArray();

			for (int n = 0; n < 4; ++n)
				item.add(getCosPoint(points[start + n * 2], points[start + n * 2 + 1]));
			items.add(item);
		}
		return items;
	}

	private static COSDictionary convertRectangle(PDAnnotationLink link, float pageHeight) {

		COSDictionary rectangle = new COSDictionary();

		float width = new BigDecimal(link.getRectangle().getWidth()).setScale(2, BigDecimal.ROUND_UP).floatValue();
		float height = new BigDecimal(link.getRectangle().getHeight()).setScale(2, BigDecimal.ROUND_UP).floatValue();
		float x = new BigDecimal(link.getRectangle().getLowerLeftX()).setScale(2, BigDecimal.ROUND_UP).floatValue();
		float y = new BigDecimal(link.getRectangle().getLowerLeftY()).setScale(2, BigDecimal.ROUND_UP).floatValue();

		//Use this coordinate system where y = 0 is at the top of the page and positive y extends down the page, PCC-19988
		float invertY = new BigDecimal((pageHeight - y) - height).setScale(2, BigDecimal.ROUND_UP).floatValue();

		rectangle.setFloat("x", x);
		rectangle.setFloat("y", invertY);
		rectangle.setFloat("width", width);
		rectangle.setFloat("height", height);

		return rectangle;
	}

	/**
	 * Compose {x:.., y:...} object from two floats
	 *
	 * @param x
	 * @param y
	 * @return composed object
	 */
	private static COSDictionary getCosPoint(float x, float y) {

		COSDictionary point = new COSDictionary();

		point.setFloat("x", x);
		point.setFloat("y", y);
		return point;
	}

	/**
	 * Translate PDF border style name to JSON name
	 *
	 * @param pdfName
	 *            PDF name
	 * @return translated name
	 */
	static String translateBorderStyleName(String pdfName) {

		if (pdfName.equals(PDBorderStyleDictionary.STYLE_SOLID))
			return "Solid";
		else if (pdfName.equals(PDBorderStyleDictionary.STYLE_BEVELED))
			return "Beveled";
		else if (pdfName.equals(PDBorderStyleDictionary.STYLE_DASHED))
			return "Dashed";
		else if (pdfName.equals(PDBorderStyleDictionary.STYLE_INSET))
			return "Inset";
		else if (pdfName.equals(PDBorderStyleDictionary.STYLE_UNDERLINE))
			return "Underline";

		// return pdf style name if not recognized as one of standard
		return pdfName;
	}

	/*
	 * 1.7 version static String translateBorderStyleName(String pdfName) {
	 * switch(pdfName) { case PDBorderStyleDictionary.STYLE_SOLID: return
	 * "Solid"; case PDBorderStyleDictionary.STYLE_BEVELED: return "Beveled";
	 * case PDBorderStyleDictionary.STYLE_DASHED: return "Dashed"; case
	 * PDBorderStyleDictionary.STYLE_INSET: return "Inset"; case
	 * PDBorderStyleDictionary.STYLE_UNDERLINE: return "Underline"; } // return
	 * pdf style name if not recognized as one of standard return pdfName; }
	 */

	/**
	 * Translate PDF highlight mode to JSON name
	 *
	 * @param pdfName
	 *            PDF name
	 * @return translated name
	 */
	static String translateHighlightStyle(String pdfName) {

		if (pdfName.equals(PDAnnotationLink.HIGHLIGHT_MODE_NONE))
			return "None";
		else if (pdfName.equals(PDAnnotationLink.HIGHLIGHT_MODE_INVERT))
			return "Invert";
		else if (pdfName.equals(PDAnnotationLink.HIGHLIGHT_MODE_OUTLINE))
			return "Outline";
		else if (pdfName.equals(PDAnnotationLink.HIGHLIGHT_MODE_PUSH))
			return "Push";

		return pdfName;
	}

	/*
	 * 1.7 version static String translateHighlightStyle(String pdfName) {
	 * switch(pdfName) { case PDAnnotationLink.HIGHLIGHT_MODE_NONE: return
	 * "None"; case PDAnnotationLink.HIGHLIGHT_MODE_INVERT: return "Invert";
	 * case PDAnnotationLink.HIGHLIGHT_MODE_OUTLINE: return "Outline"; case
	 * PDAnnotationLink.HIGHLIGHT_MODE_PUSH: return "Push"; default: return
	 * pdfName; } }
	 */

	static COSArray translateFlags(PDAnnotationLink link) {

		COSArray flags = new COSArray();
		if (link.isHidden())
			flags.add(new COSString("Hidden"));
		if (link.isInvisible())
			flags.add(new COSString("Invisible"));
		if (link.isLocked())
			flags.add(new COSString("Locked"));
		if (link.isNoRotate())
			flags.add(new COSString("NoRotate"));
		if (link.isNoView())
			flags.add(new COSString("NoView"));
		if (link.isNoZoom())
			flags.add(new COSString("NoZoom"));
		if (link.isPrinted())
			flags.add(new COSString("Print"));
		if (link.isReadOnly())
			flags.add(new COSString("ReadOnly"));
		if (link.isToggleNoView())
			flags.add(new COSString("ToggleNoView"));
		if ((BigInteger.valueOf(link.getAnnotationFlags()).testBit(LOCKED_CONTENTS)))
			flags.add(new COSString("LockedContents"));

		return flags;
	}

	/**
	 * Get border style for annotation link. Fix for pdfbox-1.8.8 Remove this
	 * function when upgrade to Pdfbox-1.8.9
	 *
	 * @param link
	 * @return border style dictionary
	 */
	static PDBorderStyleDictionary getLinkBorderStyle(PDAnnotationLink link) {

		COSDictionary bs = (COSDictionary) link.getDictionary().getDictionaryObject(COSName.getPDFName("BS"));

		return bs != null ? new PDBorderStyleDictionary(bs) : null;
	}

    /*
     * Find destination page number
     *
     * @param destination - PDDestination.
     * @param document - PDDocument.
     *
     * @return int - destination page number
     */
    private static int findPageDestination(PDDestination destination, PDDocument document) throws IOException {
        int dest = 0; //destination initial value

        if(destination instanceof PDPageDestination) {
            PDPageDestination pageDestination = (PDPageDestination) destination;
            dest = pageDestination.findPageNumber();
        } else if(destination instanceof PDNamedDestination) {
            PDNamedDestination namedDestination = (PDNamedDestination) destination;
            PDDocumentNameDictionary nameDictionary = document.getDocumentCatalog().getNames();
            if(nameDictionary != null) {
                PDDestinationNameTreeNode destsTree = nameDictionary.getDests();
                if(destsTree != null) {
                    Object obj = destsTree.getValue(namedDestination.getNamedDestination());
                    if(obj instanceof PDPageDestination) {
                        PDPageDestination pageDestination = (PDPageDestination) obj;
                        dest = pageDestination.findPageNumber();
                    }
                }
            }
        }

        return dest;
    }
}
