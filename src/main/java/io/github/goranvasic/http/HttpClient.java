/*******************************************************************************
 * Copyright 2022 Goran Vasic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package io.github.goranvasic.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpClient {

    private final Map<String, String> headers = new LinkedHashMap<>();
    private final int lowerStatusCodeBoundary;
    private final int upperStatusCodeBoundary;
    private final StopWatch stopWatch = new StopWatch();
    private String requestBody = "";
    private UrlEncodedFormEntity formData;
    private final long responseTime = -1;

    public HttpClient() {
        lowerStatusCodeBoundary = 200;
        upperStatusCodeBoundary = 299;
    }

    public HttpClient(final int lowerStatusCodeBoundary, final int upperStatusCodeBoundary) {
        this.lowerStatusCodeBoundary = lowerStatusCodeBoundary;
        this.upperStatusCodeBoundary = upperStatusCodeBoundary;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public void addHeader(Header header) {
        headers.put(header.getName(), header.getValue());
    }

    public HttpResponse execute(HttpMethod method, String url) {
        HttpResponse response;
        stopWatch.reset();
        stopWatch.start();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            response = httpClient.execute(getHttpRequest(method, url), getResponseHandler());
            this.requestBody = "";
            headers.clear();
        } catch (IOException e) {
            throw new HttpClientException("Could not make a successful HTTP request.", e);
        }
        stopWatch.stop();
        response.setResponseTime(stopWatch.getTime());
        return response;
    }

    private HttpRequestBase getHttpRequest(HttpMethod method, String url) {
        HttpRequestBase request;
        try {
            switch (method) {
                case GET:
                    request = new HttpGet(url);
                    break;
                case HEAD:
                    request = new HttpHead(url);
                    break;
                case POST:
                    request = new HttpPost(url);
                    break;
                case PUT:
                    request = new HttpPut(url);
                    break;
                case DELETE:
                    request = new HttpDelete(url);
                    break;
                case OPTIONS:
                    request = new HttpOptions(url);
                    break;
                case TRACE:
                    request = new HttpTrace(url);
                    break;
                case PATCH:
                    request = new HttpPatch(url);
                    break;
                default:
                    throw new HttpClientException();
            }
            if (!headers.isEmpty()) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.setHeader(header.getKey(), header.getValue());
                }
                headers.clear();
            }
            if (!requestBody.isEmpty()) {
                switch (method) {
                    case POST:
                    case PUT:
                    case PATCH:
                        ((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(requestBody));
                        requestBody = "";
                        break;
                    default:
                        requestBody = "";
                }
            }
            if (null != formData) {
                switch (method) {
                    case POST:
                    case PUT:
                    case PATCH:
                        ((HttpEntityEnclosingRequestBase) request).setEntity(formData);
                        formData = null;
                        break;
                    default:
                        formData = null;
                }
            }
            return request;
        } catch (UnsupportedEncodingException e) {
            throw new HttpClientException();
        }
    }

    private ResponseHandler<HttpResponse> getResponseHandler() {

        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= lowerStatusCodeBoundary && status <= upperStatusCodeBoundary) {
                HttpEntity entity = response.getEntity();
                String body = (null != entity) ? EntityUtils.toString(entity) : "";
                headers.clear();
                for (Header header : response.getAllHeaders()) {
                    headers.put(header.getName(), header.getValue());
                }
                return (null != entity) ? new HttpResponse(status, body, headers) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }

    public void addBody(String body) {
        formData = null;
        if (null == body || body.isEmpty()) {
            requestBody = "";
        } else {
            requestBody = body;
        }
    }

    public void addBody(FormData formData) {
        requestBody = "";
        this.formData = new UrlEncodedFormEntity(formData.get(), Consts.UTF_8);
    }

    public void setBasicAuth(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);
        addHeader("Authorization", authHeader);
    }
}
