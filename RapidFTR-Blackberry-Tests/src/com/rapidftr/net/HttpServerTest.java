package com.rapidftr.net;

import com.rapidftr.datastore.MockStore;
import com.rapidftr.utilities.HttpSettings;
import com.rapidftr.utilities.Settings;
import com.sun.me.web.request.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpServerTest {

    private HttpGateway httpGateway;
    private Response response;
    private HttpSettings httpSettings;

    @Before
    public void setup() {
        Settings settings = new Settings(new MockStore());
        httpSettings = new HttpSettings(settings);
        httpSettings.setHost("http://www.google.com");

        httpGateway = mock(HttpGateway.class);
        response = new Response();
        response.setResponseCode(200);
    }

    @Test
    public void shouldPerformGetRequest() throws IOException {

        Request request = Request.createGetRequest("http://www.google.com/;deviceside=true;ConnectionTimeout=10000", httpGateway, null);
        when(httpGateway.perform(request)).thenReturn(response);

        HttpServer server = new HttpServer(httpSettings, httpGateway);
        response = server.getFromServer("", null, null);

        assertEquals(200, response.getCode());
    }

    @Test
    public void shouldPerformGetRequestAsynchronously() throws Exception {

        TestRequestListener listener = new TestRequestListener();
        Request request = Request.createGetRequest("http://www.google.com/;deviceside=true;ConnectionTimeout=10000",
                httpGateway, listener);
        when(httpGateway.perform(request)).thenReturn(response);

        HttpServer server = new HttpServer(httpSettings, httpGateway);

        server.getFromServer("", null, null, listener, null);

        waitForResponse(1000, listener);

        assertEquals(response, listener.getResponse());
    }

    @Test
    public void shouldPerformPostRequestAsynchronously() throws Exception {
        TestRequestListener listener = new TestRequestListener();
        Part apart = new Part("hello".getBytes(), null);
        
        Part[] parts = new Part[] { apart };

        PostData postData = new PostData(parts, "");
        
        Request request = Request.createPostRequest("http://www.google.com/;deviceside=true;ConnectionTimeout=10000",
                httpGateway, listener, postData);
        
        when(httpGateway.perform(request)).thenReturn(response);

        HttpServer server = new HttpServer(httpSettings, httpGateway);

        server.postToServer("", null, null, listener, postData, null);

        waitForResponse(1000, listener);

        assertEquals(response, listener.getResponse());
    }

    private void waitForResponse(long timeout, TestRequestListener listener) throws InterruptedException {
        long waitedFor = 0;
        long waitFor = 1000;
        if (listener.getResponse() == null && waitedFor < timeout) {
            Thread.sleep(waitFor);
            waitedFor += waitFor;
        }
    }

    private class TestRequestListener implements RequestListener {
        private Response response;

        public void done(Object context, Response result) throws Exception {
            response = result;
        }

        public void readProgress(Object context, int bytes, int total) {
        }

        public void writeProgress(Object context, int bytes, int total) {
        }

        public Response getResponse() {
            return response;
        }
    }
}
