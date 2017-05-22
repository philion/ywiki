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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.acmerocket.ywiki.model.WikiEntry;

@Path("/wiki")    // ywiki???
@Singleton
public class WikiResource {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WikiResource.class);
    
    private final WikiDAO dao;
    
    @Inject
    public WikiResource(final WikiDAO dao) {
        this.dao = dao;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(final WikiEntry entry) {
        LOG.info("POST: {}", entry);
        
        // TODO error checking?
        this.dao.update(entry);
        
        //return Response.status(200).entity(entry).build();
        return Response.status(200).build();
    }

    @GET 
    @Path("{path:(.+)?}")    // use a regex to capture full path with '/'. https://docs.oracle.com/javaee/7/api/javax/ws/rs/Path.html
    @Produces(MediaType.APPLICATION_JSON)
    public WikiEntry get(@PathParam("path") String path) {
                
        WikiEntry entry = this.dao.get(path);
        
        LOG.info("found: {} -> {}", path, entry);
        
        return entry;
    }
}