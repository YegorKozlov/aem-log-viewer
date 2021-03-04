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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Component(service = Servlet.class, immediate = true, property = {
        ServletResolverConstants.SLING_SERVLET_PATHS + "=" + "/bin/file-list",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET
})
public class FileListServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws  IOException {
        String path = request.getParameter("path");
        if (path == null) {
            throw new IllegalArgumentException("path is required, e.g.");
        }
        Resource resource = request.getResourceResolver().getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("resource not found: " + path);
        }
        File dir = resource.adaptTo(File.class);
        if (dir == null) {
            throw new IllegalArgumentException("cannot adapt resource to a File: " + resource.getPath());
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("not a directory: " + dir.getPath());
        }

        PrintWriter out = response.getWriter();
        File[] files = dir.listFiles();
        for(File f : files){
            out.print(f.getName() + "\t" + f.length() + "\t" + f.lastModified() + "\t" + (f.isDirectory() ? "Directory" : "File")+"\n");
        }

    }

}
