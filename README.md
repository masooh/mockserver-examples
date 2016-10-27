# Mockserver and Wiremock Samples
Latest Travis-Build [![Build Status](https://travis-ci.org/masooh/mockserver.svg?branch=master)](https://travis-ci.org/masooh/mockserver)

Comparison of [MockServer](http://mock-server.com/) and [WireMock](http://wiremock.org/).

## Mockserver

```bash
# status -> only returning port in my examples
http PUT :7080/status
# create expectation
http PUT :7080/expectation < log/simple.json
# call mockserver to receive mocked response from above
http :7080/posts/1
# request log of mock server
http PUT :7080/retrieve
# expectations of mock server
http PUT ":7080/retrieve?type=expectation"
# clear request log and expectations based on request body which acts as matcher
http PUT :7080/clear < requestmatcher.json
# clear all
http PUT :7080/reset
# stop server 
http PUT :7080/stop
# for all see org.mockserver.mockserver.MockServerHandler
```

```java
public class Expectation extends ObjectWithJsonToString {

    private final HttpRequest httpRequest;
    private final Times times;
    private final HttpRequestMatcher httpRequestMatcher;
    private HttpResponse httpResponse;
```