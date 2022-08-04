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

import io.github.goranvasic.http.HttpClient;
import io.github.goranvasic.http.HttpMethod;
import io.github.goranvasic.http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTests {
    @Test
    @DisplayName("HTTP Client GET Request Example")
    void httpClientGetRequestExample() {
        HttpClient httpClient = new HttpClient();
        HttpResponse httpResponse = httpClient.execute(HttpMethod.GET, "http://example.com/");

        assertEquals(200, httpResponse.getStatus());
        assertEquals("text/html; charset=UTF-8", httpResponse.getHeaders().get("Content-Type"));
        assertTrue(httpResponse.getBody().contains("Example Domain"));
        assertTrue(httpResponse.getResponseTime() < 10000);
    }
}
