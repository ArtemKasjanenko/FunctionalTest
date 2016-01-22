package com.accusoft.tests.ocs.common.utils.pdfUtils;

import java.io.IOException;

/**
 * Helper for list output
 * 
 * Usage example:
 * 
 * {@code
 *     // Print SQL conditions 
 *     ListDecorator list(out, "(", " AND ", ")");
 *     for(String condition : conditions)
 *     {
 *       list.next();
 *       out.print(i);
 *     }
 *     list.close();
 * }
 *
 */
public class ListDecorator
{
	private Appendable out;
	private String divider, closer;
	
	private enum Status {
	    First, Next, Closed 
	}
	private Status status = Status.First;
	
	/**
	 * Constructor of list decorator. Opener will be printed at this point.
	 * @param out stream to print to
	 * @param opener opening bracket of list
	 * @param divider item divider of list
	 * @param closer closing bracket of list
	 * @throws IOException 
	 */
	public ListDecorator(Appendable out, String opener, String divider, String closer) throws IOException
	{
		this.divider = divider;
		this.init(out, opener, closer);
	}

	/**
	 * Constructor of list decorator with comma divider. Opener will be printed at this point.
	 * @param out stream to print to
	 * @param opener opening bracket of list
	 * @param closer closing bracket of list
	 * @throws IOException 
	 */
	public ListDecorator(Appendable out, String opener, String closer) throws IOException
	{
		this.divider = ",";
		this.init(out, opener, closer);
	}
	
	private void init(Appendable out, String opener, String closer) throws IOException
	{
		this.out = out;
		this.closer = closer;
		out.append(opener);
	}
	
	/**
	 * Call this method before each next print of list item
	 * @throws IOException 
	 */
	public void next() throws IOException
	{
		switch(status)
		{
		case First:
			status = Status.Next;
			break;
		case Next:
			out.append(divider);
			break;
		default:
			throw new IllegalStateException("List already closed");
		}
	}

	/**
	 * Call this method after list printing to close it
	 * @throws IOException 
	 */
	public void close() throws IOException
	{
		if(status == Status.Closed)
			throw new IllegalStateException("List already closed");

		out.append(closer);
		status = Status.Closed;
	}

}
