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

public enum HttpMethod {
    GET("GET", "The GET method requests a representation of the specified resource. Requests using GET should only retrieve data."),
    HEAD("HEAD", "The HEAD method asks for a response identical to that of a GET request, but without the response body."),
    POST("POST", "The POST method is used to submit an entity to the specified resource, often causing a change in state or side effects on the server."),
    PUT("PUT", "The PUT method replaces all current representations of the target resource with the request payload."),
    DELETE("DELETE", "The DELETE method deletes the specified resource."),
    OPTIONS("OPTIONS", "The OPTIONS method is used to describe the communication options for the target resource."),
    TRACE("TRACE", "The TRACE method performs a message loop-back test along the path to the target resource."),
    PATCH("PATCH", "The PATCH method is used to apply partial modifications to a resource.");

    private final String method;
    private final String description;

    HttpMethod(String method, String description) {
        this.method = method;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return method;
    }
}
