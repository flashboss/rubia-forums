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
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.LockForum.lockForum;
import static org.vige.rubia.selenium.adminpanel.action.Move.DOWN;
import static org.vige.rubia.selenium.adminpanel.action.Move.UP;
import static org.vige.rubia.selenium.adminpanel.action.MoveForum.moveForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.adminpanel.action.UpdateForum.updateForum;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.REMOVED_CATEGORY_1_MESSAGE;

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
public class ForumTest {

	public final static String CREATED_FORUM_0_MESSAGE = getBundle(
			"ResourceJSF").getString("Forum_created_0")
			+ " \"First Test Forum\" "
			+ getBundle("ResourceJSF").getString("Forum_created_1");
	public final static String CREATED_FORUM_1_MESSAGE = getBundle(
			"ResourceJSF").getString("Forum_created_0")
			+ " \"Second Test Forum\" "
			+ getBundle("ResourceJSF").getString("Forum_created_1");
	public final static String REMOVED_FORUM_0_MESSAGE = getBundle(
			"ResourceJSF").getString("Forum_deleted_0")
			+ " \"First Test Forum\" "
			+ getBundle("ResourceJSF").getString("Forum_deleted_1");
	public final static String REMOVED_FORUM_1_MESSAGE = getBundle(
			"ResourceJSF").getString("Forum_deleted_0")
			+ " \"Second Test Forum\" "
			+ getBundle("ResourceJSF").getString("Forum_deleted_1");
	public final static String LOCKED_FORUM_MESSAGE = getBundle("ResourceJSF")
			.getString("Forum_locked");
	public final static String UNLOCKED_FORUM_MESSAGE = getBundle("ResourceJSF")
			.getString("Forum_unlocked");
	public final static String UPDATED_FORUM_0_MESSAGE = getBundle(
			"ResourceJSF").getString("Forum_updated_0")
			+ " \"Second Test Forum\" "
			+ getBundle("ResourceJSF").getString("Forum_updated_1");
	public final static String UPDATED_FORUM_1_MESSAGE = getBundle(
			"ResourceJSF").getString("Forum_updated_0")
			+ " \"Third Test Forum\" "
			+ getBundle("ResourceJSF").getString("Forum_updated_1");
	public final static String SELECT_FORUM_TYPE = getBundle("ResourceJSF")
			.getString("Delete_all_topics_posts");

    @Drone
    private FirefoxDriver driver;
    
	@Before
	public void setUp() {
		driver.get("http://root:gtn@localhost:8080/rubia-forums/");
		String message = createCategory(driver, "First Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_1_MESSAGE));
		message = createCategory(driver, "Second Test Category");
		assertTrue(message.equals(CREATED_CATEGORY_2_MESSAGE));
		message = createForum(driver, "First Test Forum",
				"First Test Description", "First Test Category");
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createForum(driver, "Second Test Forum",
				"Second Test Description", "First Test Category");
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
	}

	@After
	public void stop() {
		String message = removeForum(driver, "First Test Forum",
				"Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(driver, "Second Test Forum", SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(driver, "First Test Category",
				CategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(driver, "Second Test Category",
				CategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	} 

	@Test
	public void testMoveForum() {
		Map<String, Integer> positions = moveForum(driver, "First Test Forum", DOWN);
		assertTrue(positions.get("newPosition") > positions
				.get("firstPosition"));
		positions = moveForum(driver,
				"First Test Forum", UP);
		assertTrue(positions.get("newPosition") < positions
				.get("firstPosition"));
	}

	@Test
	public void testLockForum() {
		String message = lockForum(driver, "First Test Forum");
		assertTrue(message.equals(LOCKED_FORUM_MESSAGE));
		message = lockForum(driver, "First Test Forum");
		assertTrue(message.equals(UNLOCKED_FORUM_MESSAGE));
	}

	@Test
	public void testUpdateForum() {
		String message = updateForum(driver, "Second Test Forum",
				new String[] { "Third Test Forum", "Third Test Description",
						"Second Test Category" });
		assertTrue(message.equals(UPDATED_FORUM_1_MESSAGE));
		message = updateForum(driver, "Third Test Forum", new String[] {
				"Second Test Forum", "First Test Description",
				"First Test Category" });
		assertTrue(message.equals(UPDATED_FORUM_0_MESSAGE));
	}
}
