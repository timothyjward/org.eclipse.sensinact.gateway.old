/*
 * Copyright (c) 2017 CEA.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    CEA - initial API and implementation
 */
package org.eclipse.sensinact.web.swagger;

import java.io.IOException;
import java.net.URL;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.sensinact.gateway.util.IOUtils;

@WebServlet(asyncSupported = true)
public final class ResourceServlet extends HttpServlet 
{
    /**
     * @param path the alias of the servlet
     */
    ResourceServlet() 
    {}

    /**
     * @inheritDoc
     *
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {    	 
    	if(response.isCommitted())
        {
        	return;
        } 
        final AsyncContext asyncContext;  
        if(request.isAsyncStarted())
        {
        	asyncContext = request.getAsyncContext();
        	
        } else
        {
        	asyncContext = request.startAsync(request,response);
        }        

        ((HttpServletResponse)asyncContext.getResponse()).getOutputStream().setWriteListener(
        	new WriteListener()
        	{
                private String resName;
                private URL url;
                private HttpServletRequest request;
                private HttpServletResponse response;

                @Override
                public void onWritePossible() throws IOException {
                    this.request = (HttpServletRequest) asyncContext.getRequest();
                    this.response = (HttpServletResponse) asyncContext.getResponse();

                    String target = request.getPathInfo();

                    if (target == null)	{
                        target = "";
                    }

                    if (!target.startsWith("/")) {
                        target += "/" + target;
                    }

                    this.resName = target;

                    try  {
                        this.url = ResourceServlet.this.getServletContext().getResource(resName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (url == null) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }

                    try {
                        String contentType = ResourceServlet.this.getServletContext().getMimeType(resName);

                        if (contentType != null) {
                            response.setContentType(contentType);
                        }

                        ServletOutputStream output = response.getOutputStream();
                        if(output.isReady()) {
                            byte[] resourceBytes = IOUtils.read(url.openStream(), true);
                            int length;

                            if((length=resourceBytes==null?0:resourceBytes.length) > 0) {
                                response.setContentLength(resourceBytes.length);
                                response.setBufferSize(resourceBytes.length);
                                output.write(resourceBytes, 0, length);
                            }
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    } finally 
                    {
    					if(request.isAsyncStarted())
    					{
    						asyncContext.complete();
    					}
                    }
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                }
            }
        );
    }
}