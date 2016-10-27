package com.github.masooh.logback;

import java.io.IOException;

import org.mockserver.client.serialization.ObjectMapperFactory;
import org.mockserver.client.serialization.model.ExpectationDTO;
import org.mockserver.client.serialization.model.HttpRequestDTO;
import org.mockserver.client.serialization.model.HttpResponseDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;

public class FilterExpectationEncoder<E> extends EncoderBase<E> {
    private ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();

    boolean excludeRequestHeaders = true;
    boolean excludeResponseHeaders = false;
    boolean excludeRequestCookies = true;
    boolean excludeResponseCookies = true;

    public void doEncode(E event) throws IOException {
        ILoggingEvent loggingEvent = (ILoggingEvent) event;
        ExpectationDTO expectationDTO = objectMapper.readValue(loggingEvent.getMessage(), ExpectationDTO.class);

        final HttpRequestDTO httpRequest = expectationDTO.getHttpRequest();
        if (excludeRequestHeaders) {
            httpRequest.getHeaders().clear();
        }
        if (excludeRequestCookies) {
            httpRequest.getCookies().clear();
        }

        final HttpResponseDTO httpResponse = expectationDTO.getHttpResponse();
        if (excludeResponseHeaders) {
            httpResponse.getHeaders().clear();
        }
        if (excludeResponseCookies) {
            httpResponse.getCookies().clear();
        }

        final String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectationDTO);
        outputStream.write(json.getBytes());
        outputStream.flush();
    }

    public void close() throws IOException {

    }

    // The following methods are for binding encoder config of logback.xml

    public void addExcludeRequestHeaders(String excludeRequestHeaders) {
        this.excludeRequestHeaders = Boolean.valueOf(excludeRequestHeaders);
    }

    public void addExcludeResponseHeaders(String excludeResponseHeaders) {
        this.excludeResponseHeaders = Boolean.valueOf(excludeResponseHeaders);
    }

    public void addExcludeRequestCookies(String excludeRequestCookies) {
        this.excludeRequestCookies = Boolean.valueOf(excludeRequestCookies);
    }

    public void addExcludeResponseCookies(String excludeResponseCookies) {
        this.excludeResponseCookies = Boolean.valueOf(excludeResponseCookies);
    }
}
