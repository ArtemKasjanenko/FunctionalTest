package com.accusoft.tests.ocs.common.utils;

public class HttpUtils {
	
	
    // Request body constants
    public static final String SRC = "src";                          // input image source local path or URL
    public static final String DEST = "dest";                        // converted file path and name
    public static final String DEST_FORMAT = "destFormat";           // output file extension "png" | "jpeg" | "tiff"
    public static final String PAGE_NUMBER = "pageNumber";
	
	public enum Headers {

	    //@Headers
	    GID("Accusoft-Gid","7WAQZvMTSkuEqHUiW7r9Jg"),
	    PARENT_NAME("Accusoft-Parent-Name","RCS"),
	    PARENT_PID("Accusoft-Parent-Pid","7357"),
	    PARENT_TASKID("Accusoft-Parent-Taskid","132");

	    private final String headerName;
	    private final String headerValue;

	    Headers(String headerName, String headerValue) {
	        this.headerName = headerName;
	        this.headerValue = headerValue;
	    }

	    public String getHeaderName() {
	        return headerName;
	    }
	    public String getHeaderValue() {
	        return headerValue;
	    }
	}
	
	
	public HttpUtils() {
		// TODO Auto-generated constructor stub
	}
	
	

	
}
