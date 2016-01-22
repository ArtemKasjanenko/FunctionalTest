package com.accusoft.tests.ocs.steps_definitions.internal;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.accusoft.tests.ocs.common.Constants.Headers;
import com.accusoft.tests.ocs.common.utils.TestDefinitionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class Request {

    public static final Logger LOGGER = Logger.getLogger(Request.class);

    private String path;
    private String requestBody;
    private String serviceIpAddress;
    private int servicePort;
    private String responseBody;
    private int responseCode;
    private URI uri;

    public Request (String path){
        this.path = path;
        this.serviceIpAddress = TestDefinitionUtils.getServiceIpAddress();
        this.servicePort = TestDefinitionUtils.getPrizmPort();
    }

    public Request (String path, String requestBody) {
        this.path = path;
        this.requestBody = requestBody;
        this.serviceIpAddress = TestDefinitionUtils.getServiceIpAddress();
        this.servicePort = TestDefinitionUtils.getServicePort();
    }

    public Request (String path, String requestBody, int servicePort){
        this(path, requestBody);
        this.servicePort = servicePort;
    }

    public void sendPost(){
        try {

            //set timeouts
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(5000)
                    .build();

            HttpClients.custom().setDefaultRequestConfig(config);

            HttpClient httpClient = HttpClientBuilder.create().build();

            // sendPost request
            HttpResponse response =
                    httpClient.execute(buildPost(serviceIpAddress, servicePort, path, requestBody));

            // get response
            responseCode = response.getStatusLine().getStatusCode();

            LOGGER.info("\nSending 'POST' request to URL : " + uri);
            LOGGER.info("Sending request body json : " + requestBody);
            LOGGER.info("Response Code : " + responseCode);

            // read response content
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String outcome;
            while ((outcome = br.readLine()) != null) {
                LOGGER.info("Output from Server .... " + outcome);
                responseBody=outcome;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendGet(){
        try {

            //set timeouts
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(5000)
                    .build();

            HttpClients.custom().setDefaultRequestConfig(config);

            HttpClient httpClient = HttpClientBuilder.create().build();

            // sendPost request
            HttpResponse response =
                    httpClient.execute(buildGet(serviceIpAddress, servicePort, path));

            // get response
            responseCode = response.getStatusLine().getStatusCode();

            LOGGER.info("\nSending 'POST' request to URL : " + uri);
            LOGGER.info("Response Code : " + responseCode);

            // read response content
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String outcome;
            while ((outcome = br.readLine()) != null) {
                LOGGER.info("Output from Server .... " + outcome);
                responseBody=outcome;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private HttpPost buildPost(String serviceIpAddress, int servicePort, String path, String requestBodyJson) {

        HttpPost post = null;

        try {

            // build the connection url
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(serviceIpAddress)
                    .setPath(path)
                    .setPort(servicePort)
                    .build();
            post = new HttpPost(uri);

            // add headers
            post.setHeader(Headers.GID.getHeaderName(), Headers.GID.getHeaderValue());
            post.setHeader(Headers.PARENT_NAME.getHeaderName(), Headers.PARENT_NAME.getHeaderValue());
            post.setHeader(Headers.PARENT_PID.getHeaderName(), Headers.PARENT_PID.getHeaderValue());
            post.setHeader(Headers.PARENT_TASKID.getHeaderName(), Headers.PARENT_TASKID.getHeaderValue());

            // add body
            StringEntity requestBody = new StringEntity(requestBodyJson, "UTF-8");
            requestBody.setContentType("application/json");
            post.setEntity(requestBody);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return post;
    }

    private HttpGet buildGet(String serviceIpAddress, int servicePort, String path) {

        HttpGet get = null;

        try {

            // build the connection url
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(serviceIpAddress)
                    .setPath(path)
                    .setPort(servicePort)
                    .build();
            get = new HttpGet(uri);

            // add headers
            get.setHeader(Headers.GID.getHeaderName(), Headers.GID.getHeaderValue());
            get.setHeader(Headers.PARENT_NAME.getHeaderName(), Headers.PARENT_NAME.getHeaderValue());
            get.setHeader(Headers.PARENT_PID.getHeaderName(), Headers.PARENT_PID.getHeaderValue());
            get.setHeader(Headers.PARENT_TASKID.getHeaderName(), Headers.PARENT_TASKID.getHeaderValue());

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return get;
    }

    public String getResponseBody(){
        return responseBody;
    }

    public int getResponseCode(){
        return responseCode;
    }
        
}
