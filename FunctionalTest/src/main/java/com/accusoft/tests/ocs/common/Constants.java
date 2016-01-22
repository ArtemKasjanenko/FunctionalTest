package com.accusoft.tests.ocs.common;


public final class Constants {

	//
	public static final String PCCIS_VERSION= "10.1.1000.0";
	
    // Request body constants
    public static final String SRC = "src";                          // input image source local path or URL
    public static final String OUTPUT_TEMPLATE = "outputTemplate";              // converted file path, name and optional page pattern
    public static final String DEST = "dest";              // converted file path, name and optional page pattern
    public static final String DEST_FORMAT = "destFormat";           // output file extension "pdf"
    
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
}

