package com.github.masooh.mockserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;

/**
 * Standalone server has to be started before:
 * <code>java -jar mockserver-netty-3.10.4-jar-with-dependencies.jar -serverPort 7080</code>
 */
@Ignore
public class StandaloneTest {

    private CloseableHttpClient client;

    @Before
    public void setUp() throws Exception {
        client = HttpClients.createDefault();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void createExpectation() throws Exception {
        new MockServerClient("localhost", 7080)
            .when(
                request()
            ).respond(
                response()
                    .withBody("Standalone answering")
            );
    }

    @Test
    public void createExpectationFromJson() throws Exception {
        final HttpPut httpPut = new HttpPut("http://localhost:7080/expectation");

        // set all files in log as expectation
        final File logDir = new File("log");
        for (File file : logDir.listFiles()) {
            if (file.getName().endsWith(".json")) {
                httpPut.setEntity(new FileEntity(
                        file,
                        ContentType.APPLICATION_JSON
                ));
                client.execute(httpPut);
            }
        }
    }

    @Test
    public void callMock() throws Exception {
        HttpGet httpGet = new HttpGet("http://localhost:7080/posts/2");
        org.apache.http.HttpResponse response = client.execute(httpGet);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(response.getEntity().getContentLength() > 0);
        System.out.println(response.getEntity().getContent().toString());
    }
}
