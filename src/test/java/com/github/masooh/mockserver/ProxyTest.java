package com.github.masooh.mockserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.verify.VerificationTimes.exactly;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.proxy.ProxyClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.ProxyRule;
import org.mockserver.model.HttpResponse;

public class ProxyTest {

    /**
     * calls ClientAndProxy#startClientAndProxy for every test with free port and sets result to field with type ProxyClient
     */
    @Rule
    public ProxyRule mockServerRule = new ProxyRule(this);

    /**
     * instantiated by ProxyRule
     */
    private ProxyClient proxyClient;

    private int proxyPort;

    private CloseableHttpClient client;
    private ClientAndServer mockServer;

    @Before
    public void configureClient() throws Exception {
        proxyPort = mockServerRule.getHttpPort();

        HttpHost proxyHost = new HttpHost("localhost", proxyPort);
        client = HttpClients.custom().setRoutePlanner(new DefaultProxyRoutePlanner(proxyHost)).build();
    }

    @Before
    public void startMockServer() throws Exception {
        mockServer = startClientAndServer(1080);
    }

    @After
    public void tearDown() throws Exception {
        mockServer.stop();
    }

    @Test
    public void testDumpToLogJson() throws Exception {
        final int times = 2;
        for (int i = 1; i <= times; i++) {
            HttpGet httpGet = new HttpGet("http://jsonplaceholder.typicode.com/posts/" + i);
            org.apache.http.HttpResponse response = client.execute(httpGet);

            assertEquals(200, response.getStatusLine().getStatusCode());
            assertTrue(response.getEntity().getContentLength() > 0);
            Thread.sleep(1000);
        }

        proxyClient.verify(
                request()
                        .withPath("/posts/.*"),
                exactly(times)
        );

        // logger is named REQUEST -> see logback-test.xml -> mockserver_request.log
        proxyClient.dumpToLogAsJSON();
    }
}
