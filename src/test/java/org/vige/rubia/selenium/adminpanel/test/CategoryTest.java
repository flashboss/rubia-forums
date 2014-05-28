/*
 * Vige, Home of Professional Open Source
 * Copyright 2010, Vige, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vige.rubia.selenium.adminpanel.test;

import static java.util.ResourceBundle.getBundle;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.Move.DOWN;
import static org.vige.rubia.selenium.adminpanel.action.Move.UP;
import static org.vige.rubia.selenium.adminpanel.action.MoveCategory.moveCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.UpdateCategory.updateCategory;
import static java.lang.Thread.sleep;

import java.util.Map;

import org.jboss.test.selenium.AbstractTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests receipts functionality of the example.
 * 
 * @author <a href="http://www.vige.it">Luca Stancapiano</a>
 */
public class CategoryTest extends AbstractTestCase {

	public final static String CREATED_CATEGORY_1_MESSAGE = getBundle(
			"ResourceJSF").getString("Category_created_0")
			+ " \"First Test Category\" "
			+ getBundle("ResourceJSF").getString("Category_created_1");
	public final static String CREATED_CATEGORY_2_MESSAGE = getBundle(
			"ResourceJSF").getString("Category_created_0")
			+ " \"Second Test Category\" "
			+ getBundle("ResourceJSF").getString("Category_created_1");
	public final static String REMOVED_CATEGORY_0_MESSAGE = getBundle(
			"ResourceJSF").getString("Category_deleted_0")
			+ " \"First Test Category\" "
			+ getBundle("ResourceJSF").getString("Category_deleted_1");
	public final static String REMOVED_CATEGORY_1_MESSAGE = getBundle(
			"ResourceJSF").getString("Category_deleted_0")
			+ " \"Second Test Category\" "
			+ getBundle("ResourceJSF").getString("Category_deleted_1");
	public final static String UPDATED_CATEGORY_MESSAGE = getBundle(
			"ResourceJSF").getString("Category_updated_0")
			+ " \"Third Test Category\" "
			+ getBundle("ResourceJSF").getString("Category_updated_1");
	public final static String SELECT_CATEGORY_TYPE = getBundle("ResourceJSF")
			.getString("Delete_all_forums_topics_posts");

	@BeforeMethod
	public void setUp() {
		selenium.open(contextPath);
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String message = createCategory(selenium, "First Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(selenium, "Second Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
	}

	@AfterMethod
	public void stop() {
		String message = removeCategory(selenium, "First Test Category",
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(selenium, "Second Test Category",
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}

	@Test
	public void testMoveCategory() {
		Map<String, Integer> positions = moveCategory(selenium,
				"First Test Category", UP);
		assertTrue(positions.get("newPosition") < positions
				.get("firstPosition"));
		positions = moveCategory(selenium, "First Test Category", DOWN);
		assertTrue(positions.get("newPosition") > positions
				.get("firstPosition"));
	}

	@Test
	public void testUpdateCategory() {
		String message = updateCategory(selenium, "First Test Category",
				"Third Test Category");
		assertTrue(message.equals(UPDATED_CATEGORY_MESSAGE));
		message = updateCategory(selenium, "Third Test Category",
				"First Test Category");
		assertFalse(message.equals(UPDATED_CATEGORY_MESSAGE));
	}
}
