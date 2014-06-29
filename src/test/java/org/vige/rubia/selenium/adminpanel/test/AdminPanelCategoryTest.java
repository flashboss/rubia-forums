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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.Move.DOWN;
import static org.vige.rubia.selenium.adminpanel.action.Move.UP;
import static org.vige.rubia.selenium.adminpanel.action.MoveCategory.moveCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.UpdateCategory.updateCategory;

import java.util.Map;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * This class tests receipts functionality of the example.
 * 
 * @author <a href="http://www.vige.it">Luca Stancapiano</a>
 */
@RunWith(Arquillian.class)
public class AdminPanelCategoryTest {

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

	@Drone
	private FirefoxDriver driver;

	@Before
	public void setUp() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = createCategory(driver, "First Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, "Second Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
	}

	@After
	public void stop() {
		String message = removeCategory(driver, "First Test Category",
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, "Second Test Category",
				SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}

	@Test
	public void testMoveCategory() {
		Map<String, Integer> positions = moveCategory(driver,
				"First Test Category", UP);
		assertTrue(positions.get("newPosition") < positions
				.get("firstPosition"));
		positions = moveCategory(driver, "First Test Category", DOWN);
		assertTrue(positions.get("newPosition") > positions
				.get("firstPosition"));
	}

	@Test
	public void testUpdateCategory() {
		String message = updateCategory(driver, "First Test Category",
				"Third Test Category");
		assertTrue(message.equals(UPDATED_CATEGORY_MESSAGE));
		message = updateCategory(driver, "Third Test Category",
				"First Test Category");
		assertFalse(message.equals(UPDATED_CATEGORY_MESSAGE));
	}
}
