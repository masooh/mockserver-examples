package com.github.masooh.mockserver;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class MockServerHttpClient {
    private MockServerHttpClient() { }

    public static void createExpectationFromJson(int serverPort, File requestLogDir) throws IOException {
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut("http://localhost:" + serverPort + "/expectation");

            // set all files in log as expectation
            for (File file : requestLogDir.listFiles()) {
                if (file.getName().endsWith(".json") && file.length() > 0) {
                    httpPut.setEntity(new FileEntity(
                            file,
                            ContentType.APPLICATION_JSON
                    ));
                    HttpResponse response = client.execute(httpPut);

                    if (response.getStatusLine().getStatusCode() != 201) {
                        throw new IllegalStateException("MockServer must respond with status 201, but was : "
                                + response.getStatusLine().getStatusCode());
                    }
                }
            }
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

}
