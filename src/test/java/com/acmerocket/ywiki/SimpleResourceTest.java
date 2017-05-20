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

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.acmerocket.ywiki.model.Pet;

/**
 * @author philion
 *
 */
public class SimpleResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(WikiResource.class);
	}

	@Test
	public void test() {
		final Pet pet = target("wiki/1234").request().get(Pet.class);
		assertNotNull(pet.getId());
	}
}
