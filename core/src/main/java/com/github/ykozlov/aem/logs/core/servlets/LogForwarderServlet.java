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
package com.github.ykozlov.aem.logs.core.servlets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component(service = Servlet.class, immediate = true, property = {
        ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=" + "e2e-tools/components/tools/log-viewer/download",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET
})
public class LogForwarderServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getParameter("url");
        if (url == null) {
            throw new IllegalArgumentException("url is required, e.g.");
        }
        String filename = new File(url).getName();
        String ext = request.getParameter("ext");

        try(CloseableHttpClient hc = HttpClients.createDefault()){
            HttpGet get = new HttpGet(url);
            get.setHeader(new BasicHeader("Cookie", request.getHeader("Cookie")));

            HttpResponse rsp = hc.execute(get);

            switch (ext) {
                case "zip":
                    response.setContentType("application/zip");
                    response.setHeader("Content-Disposition", "attachment;filename=" + filename+ ".zip");
                    ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
                    try (InputStream is = rsp.getEntity().getContent()) {
                        zos.putNextEntry(new ZipEntry(filename));
                        IOUtils.copy(is, zos);
                        zos.closeEntry();
                    }
                    zos.close();
                    break;
                case "raw":
                    response.setContentType("text/plain");
                    try (InputStream is = rsp.getEntity().getContent()) {
                        IOUtils.copy(is, response.getOutputStream());
                    }
                    break;
            }
       }

    }

}
