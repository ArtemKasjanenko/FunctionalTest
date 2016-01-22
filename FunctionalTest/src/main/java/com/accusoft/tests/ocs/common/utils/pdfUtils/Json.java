package com.accusoft.tests.ocs.common.utils.pdfUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map.Entry;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSBoolean;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSString;

/**
 * JSON helper
 *
 */
public class Json
{

	/**
	 * Quote string for JSON format (Moved from TextExtraction class)
	 * 
	 * @param string
	 *            string to quote
	 * @return quoted string
	 */
	public static String quote(String string)
	{
		if (string == null || string.length() == 0)
		{
			return "\"\"";
		}

		char c = 0;
		int i;
		int len = string.length();
		StringBuilder sb = new StringBuilder(len + 4);
		String t;

		sb.append('"');
		for (i = 0; i < len; i += 1)
		{
			c = string.charAt(i);
			switch (c)
			{
			case '\\':
			case '"':
				sb.append('\\');
				sb.append(c);
				break;
			case '/':
				// if (b == '<') {
				sb.append('\\');
				// }
				sb.append(c);
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\r':
				sb.append("\\r");
				break;
			default:
				if (c < ' ')
				{
					t = "000" + Integer.toHexString(c);
					sb.append("\\u" + t.substring(t.length() - 4));
				} else
				{
					sb.append(c);
				}
			}
		}
		sb.append('"');
		return sb.toString();
	}

	/**
	 * Print out COS object to output stream without intermediate objects
	 * @param obj object to print
	 * @param out stream to print to
	 * @throws IOException 
	 */
	public static void printCOS(COSBase obj, PrintStream out) throws IOException
	{
		Class<? extends COSBase> objClass = obj.getClass();

		if (objClass == COSInteger.class)
		{
			out.print(((COSInteger) obj).intValue());
		} 
		else if (objClass == COSFloat.class)
		{
			out.print(((COSFloat) obj).floatValue());
		} 
		else if (objClass == COSBoolean.class)
		{
			out.print(((COSBoolean) obj).getValue() ? "true" : "false");
		} 
		else if (objClass == COSName.class)
		{
			out.print(quote(((COSName) obj).getName()));
		}
		else if (objClass == COSNull.class)
		{
			out.print("null");
		} 
		else if (objClass == COSString.class)
		{
			out.print(quote(((COSString) obj).getString()));
		} 
		else if (objClass == COSArray.class)
		{
			ListDecorator list = new ListDecorator(out, "[", "]");
			for (COSBase element : (COSArray) obj)
			{
				list.next();
				printCOS(element, out);
			}
			list.close();
		} 
		else if (objClass == COSDictionary.class)
		{
			ListDecorator list = new ListDecorator(out, "{", "}");
			for (Entry<COSName, COSBase> element : ((COSDictionary) obj).entrySet())
			{
				list.next();
				
				out.print(quote(element.getKey().getName()));
				out.print(":");
				// Print dictionary item with reference resolving
				printCOS(((COSDictionary)obj).getDictionaryObject(element.getKey()), out);
			}
			list.close();
		} 
		else if (objClass == COSObject.class)
		{
			out.println(quote(((COSObject)obj).toString()));
		}
		else
		{
			out.print(quote(obj.toString()));
		}

	}
	
}
