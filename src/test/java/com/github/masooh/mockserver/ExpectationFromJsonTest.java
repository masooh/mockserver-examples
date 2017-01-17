package com.github.masooh.mockserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;

/**
 * required log files can be created with {@link ProxyTest}
 */
public class ExpectationFromJsonTest {

    private CloseableHttpClient client;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);

    private MockServerClient mockServerClient;

    private Integer serverPort;

    @Before
    public void setUp() throws Exception {
        client = HttpClients.createDefault();
        serverPort = mockServerRule.getPort();
        MockServerHttpClient.createExpectationFromJson(serverPort, new File("src/test/resources/request-logs"));
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void callPreparedMockPost1() throws Exception {
        HttpGet httpGet = new HttpGet("http://localhost:" + serverPort + "/posts/1");
        org.apache.http.HttpResponse response = client.execute(httpGet);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(response.getEntity().getContentLength() > 0);
    }

    @Test
    public void callPreparedMockPost2() throws Exception {
        HttpGet httpGet = new HttpGet("http://localhost:" + serverPort + "/posts/2");
        org.apache.http.HttpResponse response = client.execute(httpGet);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(response.getEntity().getContentLength() > 0);
    }

}
