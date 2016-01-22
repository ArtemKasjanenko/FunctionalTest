package com.accusoft.tests.ocs.common.utils.pdfUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility class about formatting output to string buffer
 *
 */
class AppendableOutputStream extends OutputStream implements Appendable
{
	Appendable buffer;

	public AppendableOutputStream(Appendable out)
	{
		buffer = out;
	}

	public AppendableOutputStream()
	{
		buffer = new StringBuilder();
	}
	
	@Override
	public void write(int arg) throws IOException
	{
		buffer.append((char) arg);
	}

	public String toString()
	{
		return buffer.toString();
	}

	@Override
	public Appendable append(CharSequence csq) throws IOException
	{
		buffer.append(csq);
		return this;
	}

	@Override
	public Appendable append(char c) throws IOException
	{
		buffer.append(c);
		return this;
	}

	@Override
	public Appendable append(CharSequence csq, int start, int end) throws IOException
	{
		buffer.append(csq, start, end);
		return this;
	}
	
}