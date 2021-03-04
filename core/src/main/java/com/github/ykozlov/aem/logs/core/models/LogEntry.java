/*
 * #%L
 * AEM Log Viewer
 * %%
 * Copyright (C) 2021 Yegor Kozlov
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.github.ykozlov.aem.logs.core.models;

import com.day.cq.searchpromote.xml.result.Zone;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    String url;
    String path;
    String type;
    long size;
    long lastModified;

    LogEntry(String url, String path, long size, long lastModified, String type) {
        this.url = url;
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getSize() {
        long kb = size / 1024;
        return new DecimalFormat("####,###").format(kb) + " " + "KB";
    }

    public String getLastModified() {
        Instant i = Instant.ofEpochMilli(lastModified);
        LocalDateTime l = LocalDateTime.ofInstant(i, ZoneId.systemDefault());
        return DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a").format(l);
    }
}
