/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.acmerocket.ywiki;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

@Path("/")
@Singleton
public class RootResource {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(RootResource.class);
    
    // TODO: Make configurable
    private static final String ROOT = "/static/"; //automate, from path?
    private static final String DEFAULT_PAGE = "index.html";

    @GET 
    @Path("{path:(.+)?}") // use a regex to capture full path with '/'. https://docs.oracle.com/javaee/7/api/javax/ws/rs/Path.html
    public Response get(@PathParam("path") String path) throws IOException {
        String fullPath = ROOT + path;
        
        LOG.trace("got request: '{}' => {}", path, fullPath);
        
        byte[] entity = this.readAll(fullPath);
        if (entity != null) {
            return Response.ok().entity(entity).build();
        } 
        else {
            throw new FileNotFoundException(path);
        }        
    }

	/**
	 * @param path
	 * @return
	 */
	private byte[] readAll(final String path) throws IOException {	    
	    String fullPath = path;	    
	    if (this.isDirectory(path)) {
	        fullPath = path + "/" + DEFAULT_PAGE;
	    }
	            
		InputStream in = this.getClass().getResourceAsStream(fullPath);
		
		if (in != null) {
			return IOUtils.toByteArray(in);
		}		
		else {
		    return null;
		}
    }
	
	private boolean isDirectory(String path) {
	    URL url = this.getClass().getResource(path);
	    File file = FileUtils.toFile(url);
	    return file != null && file.isDirectory();
	}
}