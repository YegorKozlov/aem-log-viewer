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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class LogList {
    private static final Logger logger = LoggerFactory.getLogger(LogList.class);

    @Self
    private SlingHttpServletRequest request;

    private List<LogEntry> logs;

    @PostConstruct
    protected void init() {
        logs = new ArrayList<>();

        String configPath = request.getRequestPathInfo().getSuffix();
        if (configPath == null) {
            logger.error("suffix is required, e.g.");
            return;
        }
        Resource resource = request.getResourceResolver().getResource(configPath);
        if (resource == null) {
            logger.error("not found: " + configPath);
            return;
        }

        String host = resource.getValueMap().get("host", String.class);
        String remotePath = request.getParameter("dir");
        if(remotePath == null) remotePath = resource.getValueMap().get("dir", "/logs");

        try(CloseableHttpClient hc = HttpClients.createDefault()){
            HttpGet get = new HttpGet(host + "/bin/file-list?path=" + remotePath);
            get.setHeader(new BasicHeader("Cookie", request.getHeader("Cookie")));

            HttpResponse rsp = hc.execute(get);
            String txt = EntityUtils.toString(rsp.getEntity());
            for(String ln : txt.split("\n")){
                String[] p = ln.split("\t");
                String path = p[0];
                long size= Long.parseLong(p[1]);
                long modified= Long.parseLong(p[2]);
                String type = p[3];
                String url = host + remotePath + "/" + URLEncoder.encode(path, "utf-8");
                LogEntry entry = new LogEntry(url, path, size, modified, type);
                logs.add(entry);
            }
         } catch (Exception e){
            logger.error("failed to list remote logs", e);
        }

    }


    public List<LogEntry> getLogs(){
        return logs;
    }
}