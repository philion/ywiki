/**
 * Copyright 2017 Acme Rocket Company [acmerocket.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acmerocket.ywiki;

import java.io.IOException;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

public class WikiResourceTest extends JerseyTest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WikiResourceTest.class);

	@Override
	protected Application configure() {
		return new ResourceConfig().register(WikiBinder.INSTANCE).packages("com.acmerocket.ywiki");
	}

	//@Test
	public void test() throws IOException {
	    //java.util.logging.Logger.getLogger("org.glassfish.jersey").setLevel(Level.FINEST);
	    
	    // create the wikipage
	    //final WikiEntry entry = new WikiEntry();
	    //entry.setPath("index");
	    //entry.setTitle("Test");
	    //entry.setUser("testUser");
	    //entry.setContent("# Test\\n"
	    //        + "*This* is a **test**.");
	    
	    //Response response = target("wiki").request().post(Entity.entity(entry, MediaType.APPLICATION_JSON));
	    //LOG.debug("response: {}", response);
	    //assertEquals(200, response.getStatus());
	    
	    //final WikiEntry check = target("wiki/index").request().get(WikiEntry.class);
	    //LOG.info("response: {}", check);
	    
		//assertEquals(entry.getPath(), check.getPath());
        //assertEquals(entry.getTitle(), check.getTitle());
        //assertEquals(entry.getUser(), check.getUser());
	}
}
