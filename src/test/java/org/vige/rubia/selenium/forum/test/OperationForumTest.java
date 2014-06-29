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
package org.vige.rubia.selenium.forum.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.vige.rubia.selenium.adminpanel.action.CreateCategory.createCategory;
import static org.vige.rubia.selenium.adminpanel.action.CreateForum.createForum;
import static org.vige.rubia.selenium.adminpanel.action.RemoveCategory.removeCategory;
import static org.vige.rubia.selenium.adminpanel.action.RemoveForum.removeForum;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.CREATED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.CREATED_CATEGORY_2_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.REMOVED_CATEGORY_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.CategoryTest.REMOVED_CATEGORY_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.CREATED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.CREATED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.REMOVED_FORUM_0_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.REMOVED_FORUM_1_MESSAGE;
import static org.vige.rubia.selenium.adminpanel.test.ForumTest.SELECT_FORUM_TYPE;
import static org.vige.rubia.selenium.forum.action.VerifyForum.getForumsOfCategories;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.vige.rubia.model.Forum;
import org.vige.rubia.selenium.adminpanel.test.CategoryTest;

@RunWith(Arquillian.class)
public class OperationForumTest {

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

	@Test
	public void verifyCreatedForums() {
		List<Forum> forums = getForumsOfCategories(driver,
				"First Test Category", "Second Test Category");
		assertEquals(forums.size(), 2);
		assertEquals(forums.get(0).getName(), "First Test Forum");
		assertEquals(forums.get(0).getDescription(), "First Test Description");
		assertEquals(forums.get(0).getCategory().getTitle(), "First Test Category");
		assertEquals(forums.get(1).getName(), "Second Test Forum");
		assertEquals(forums.get(1).getDescription(), "Second Test Description");
		assertEquals(forums.get(1).getCategory().getTitle(), "First Test Category");
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
}
