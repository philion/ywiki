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

/**
 * @author philion
 *
 */
public class RandomTinker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// create random array
		final int size = (int) (Math.random() * 100);
		final int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = i;
		}
		
		long currtime = 0;
		long random = 0;
		long start = 0;
	
		for (int i = 0; i < 10000; i++) {
			// loop for rand
			start = System.currentTimeMillis();
			for (int j = 0; j < 5000; j++) {
				int index = (int) (Math.random() * size);
				int value = array[index];
				assert value == index;
			}
			long end = System.currentTimeMillis();
			random += end - start;
			start = end;
			
			// for time
			for (int j = 0; j < 5000; j++) {
				int index = (int) (System.currentTimeMillis() % (long)size);
				int value = array[index];
				assert value == index;
			}
		}
		
		System.out.println("Random took " + random + "ms");
		System.out.println("mod took " + currtime + "ms");
	}
}
