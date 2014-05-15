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

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.util.ResourceBundle;

import org.jboss.test.selenium.AbstractTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.vige.rubia.selenium.adminpanel.action.CreateCategory;
import org.vige.rubia.selenium.adminpanel.action.Move;
import org.vige.rubia.selenium.adminpanel.action.MoveCategory;
import org.vige.rubia.selenium.adminpanel.action.RemoveCategory;
import org.vige.rubia.selenium.adminpanel.action.UpdateCategory;

/**
 * This class tests receipts functionality of the example.
 * 
 * @author <a href="http://www.vige.it">Luca Stancapiano</a>
 */
public class CategoryTest extends AbstractTestCase {

	public final static String CREATED_CATEGORY_1_MESSAGE = ResourceBundle
			.getBundle("ResourceJSF").getString("Category_created_0")
			+ "\"First Category\""
			+ ResourceBundle.getBundle("ResourceJSF").getString(
					"Category_created_1");
	public final static String CREATED_CATEGORY_2_MESSAGE = ResourceBundle
			.getBundle("ResourceJSF").getString("Category_created_0")
			+ "\"Second Category\""
			+ ResourceBundle.getBundle("ResourceJSF").getString(
					"Category_created_1");
	public final static String REMOVED_CATEGORY_0_MESSAGE = ResourceBundle
			.getBundle("ResourceJSF").getString("Category_deleted_0")
			+ "\"First Category\""
			+ ResourceBundle.getBundle("ResourceJSF").getString(
					"Category_deleted_1");
	public final static String REMOVED_CATEGORY_1_MESSAGE = ResourceBundle
			.getBundle("ResourceJSF").getString("Category_deleted_0")
			+ "\"Second Category\""
			+ ResourceBundle.getBundle("ResourceJSF").getString(
					"Category_deleted_1");
	public final static String UPDATED_CATEGORY_MESSAGE = ResourceBundle
			.getBundle("ResourceJSF").getString("Category_updated_0")
			+ "\"Third Category\""
			+ ResourceBundle.getBundle("ResourceJSF").getString(
					"Category_updated_1");

	@BeforeMethod
	public void setUp() {
		selenium.open(contextPath);
		selenium.waitForPageToLoad();
	}

	@Test
	public void testCreateCategory() {
		String message = CreateCategory.createCategory(selenium,
				"First Category");
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = CreateCategory.createCategory(selenium, "Second Category");
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
	}

	@Test
	public void testMoveUpCategory() {
		String message = MoveCategory.moveCategory(selenium, "First Category",
				Move.UP);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
	}

	@Test
	public void testMoveDownCategory() {
		String message = MoveCategory.moveCategory(selenium, "First Category",
				Move.DOWN);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
	}

	@Test
	public void testRemoveCategory() {
		String message = RemoveCategory.removeCategory(selenium,
				"First Category");
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = RemoveCategory.removeCategory(selenium, "Second Category");
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}

	@Test
	public void testUpdateCategory() {
		String message = UpdateCategory.updateCategory(selenium,
				"First Category", "Third Category");
		assertTrue(message.equals(UPDATED_CATEGORY_MESSAGE));
		message = UpdateCategory.updateCategory(selenium,
				"Not Existent Category", "Third Category");
		assertFalse(message.equals(UPDATED_CATEGORY_MESSAGE));
	}
}
