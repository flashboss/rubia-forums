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
import static org.testng.AssertJUnit.assertTrue;
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
public class ForumTest extends AbstractTestCase {

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
			+ " \"First Test Forum\" "
			+ getBundle("ResourceJSF").getString("Forum_updated_1");
	public final static String UPDATED_FORUM_1_MESSAGE = getBundle(
			"ResourceJSF").getString("Forum_updated_0")
			+ " \"Third Test Forum\" "
			+ getBundle("ResourceJSF").getString("Forum_updated_1");
	public final static String SELECT_FORUM_TYPE = getBundle("ResourceJSF")
			.getString("Delete_all_topics_posts");

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
		message = createForum(selenium, "First Test Forum",
				"First Test Description", "First Test Category");
		assertTrue(message.equals(CREATED_FORUM_0_MESSAGE));
		message = createForum(selenium, "Second Test Forum",
				"Second Test Description", "First Test Category");
		assertTrue(message.equals(CREATED_FORUM_1_MESSAGE));
	}

	@AfterMethod
	public void stop() {
		String message = removeForum(selenium, "First Test Forum",
				"Second Test Forum");
		assertTrue(message.equals(REMOVED_FORUM_0_MESSAGE));
		message = removeForum(selenium, "Second Test Forum", SELECT_FORUM_TYPE);
		assertTrue(message.equals(REMOVED_FORUM_1_MESSAGE));
		message = removeCategory(selenium, "First Test Category",
				CategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_0_MESSAGE));
		message = removeCategory(selenium, "Second Test Category",
				CategoryTest.SELECT_CATEGORY_TYPE);
		assertTrue(message.equals(REMOVED_CATEGORY_1_MESSAGE));
	}

	@Test
	public void testMoveForum() {
		Map<String, Integer> positions = moveForum(selenium,
				"First Test Forum", UP);
		assertTrue(positions.get("newPosition") < positions
				.get("firstPosition"));
		positions = moveForum(selenium, "First Test Forum", DOWN);
		assertTrue(positions.get("newPosition") > positions
				.get("firstPosition"));
	}

	@Test
	public void testLockForum() {
		String message = lockForum(selenium, "First Test Forum");
		assertTrue(message.equals(LOCKED_FORUM_MESSAGE));
		message = lockForum(selenium, "First Test Forum");
		assertTrue(message.equals(UNLOCKED_FORUM_MESSAGE));
	}

	@Test
	public void testUpdateForum() {
		String message = updateForum(selenium, "First Test Forum",
				new String[] { "Third Test Forum", "Third Test Description",
						"Second Test Category" });
		assertTrue(message.equals(UPDATED_FORUM_1_MESSAGE));
		message = updateForum(selenium, "Third Test Forum", new String[] {
				"First Test Forum", "First Test Description",
				"First Test Category" });
		assertTrue(message.equals(UPDATED_FORUM_0_MESSAGE));
	}
}
