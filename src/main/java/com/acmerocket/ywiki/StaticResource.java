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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

@Path("/static")
@Singleton
public class StaticResource {
    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(StaticResource.class);
    
    private static final String ROOT = "/static/"; //automate, from path?

    @GET 
    @Path("{path:(.+)?}") // use a regex to capture full path with '/'. https://docs.oracle.com/javaee/7/api/javax/ws/rs/Path.html
    public Response get(@PathParam("path") String path) throws IOException {        
        return Response.ok().entity(this.readAll(ROOT + path)).build();
    }

	/**
	 * @param path
	 * @return
	 */
	private byte[] readAll(String path) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(path);
		if (in != null) {
			return IOUtils.toByteArray(in);
		}
		else {
			throw new FileNotFoundException(path);
		}
    }
}