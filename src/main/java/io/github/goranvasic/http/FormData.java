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

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public final class FormData {

    private final List<BasicNameValuePair> data;

    public FormData() {
        this.data = new ArrayList<>();
    }

    public FormData(String name, String value) {
        this.data = new ArrayList<>();
        add(name, value);
    }

    public void add(String name, String value) {
        data.add(new BasicNameValuePair(name, value));
    }

    protected List<BasicNameValuePair> get() {
        List<BasicNameValuePair> dataClone = new ArrayList<>();
        if (!data.isEmpty()) {
            for (BasicNameValuePair nameValuePair : data) {
                dataClone.add(new BasicNameValuePair(nameValuePair.getName(), nameValuePair.getValue()));
            }
        }
        return dataClone;
    }
}
