/******************************************************************************
 * Vige, Home of Professional Open Source Copyright 2010, Vige, and           *
 * individual contributors by the @authors tag. See the copyright.txt in the  *
 * distribution for a full listing of individual contributors.                *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may    *
 * not use this file except in compliance with the License. You may obtain    *
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0        *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/
package org.vige.rubia.ui;

import static java.lang.String.valueOf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * Created on May 11, 2006
 *
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
public abstract class PageNavigator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5846942580424927599L;

	public static final int PAGINATION_SIZE = 8;

	/**
     * 
     */
	private int totalPages;
	private int pageSize;
	private int currentPage;
	private int numberOfEntries;
	private Collection<Integer> page;
	private SortedSet<Integer> currentPaginationLeft;
	private SortedSet<Integer> currentPaginationRight;

	/**
     * 
     *
     */
	public PageNavigator(int numberOfEntries, int pageSize, int currentPage) {
		if (numberOfEntries < 0) {
			throw new IllegalStateException("PageNavigator cannot be initialized for negative number of entries");
		}

		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.numberOfEntries = numberOfEntries;

		// calculate the totalNumberofPages that will be made
		double pageSizeDbl = pageSize;
		double pageCountDbl = numberOfEntries / pageSizeDbl;
		totalPages = (int) Math.ceil(pageCountDbl);

		currentPaginationLeft = new TreeSet<Integer>();
		currentPaginationRight = new TreeSet<Integer>();

		// Initializing pagination so that selected page will be in the middle.
		if (currentPage >= totalPages) {
			for (int i = 2; i <= PAGINATION_SIZE && i <= totalPages; i++) {
				currentPaginationRight.add(i);
			}
		} else {
			int counter = 1;
			for (int j = 1; j < totalPages && counter < PAGINATION_SIZE; j++) {
				if ((currentPage + 1) - j > 0) {
					currentPaginationLeft.add((currentPage + 1) - j);
					counter++;
					if (counter == PAGINATION_SIZE) {
						break;
					}
				}
				if ((currentPage + 1) + j <= totalPages) {
					counter++;
					currentPaginationRight.add((currentPage + 1) + j);
				}
			}
		}

		page = initializePage();
	}

	/**
	 * Every non-abstract class extending this class will have to implement this
	 * method and initialize page with objects.
	 */
	protected abstract Collection<Integer> initializePage();

	/**
     *
     * 
     */
	public int getNumberOfEntries() {
		return numberOfEntries;
	}

	/**
     * 
     *
     */
	public int getTotalPages() {
		return totalPages;
	}

	/**
     * 
     *
     */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
     * 
     *
     */
	public int getPageSize() {
		return pageSize;
	}

	/**
     * 
     *
     */
	public int getBeginIndex() {
		int beginIndex = 0;

		beginIndex = currentPage * pageSize;

		return beginIndex;
	}

	/**
     * 
     *
     */
	public int getEndIndex() {
		int endIndex = 0;

		endIndex = getBeginIndex() + pageSize;
		if (endIndex >= numberOfEntries) {
			endIndex = numberOfEntries;
		}

		return endIndex;
	}

	/**
     * 
     *
     */
	public Collection<Integer> getPage() {
		if (page == null) {
			return new ArrayList<Integer>();
		}
		return page;
	}

	/**
     * 
     *
     */
	public String getPageNumber() {
		return valueOf(getCurrentPage() + 1);
	}

	/**
     * 
     *
     */
	public int[] getPages() {
		int[] pages = new int[getTotalPages()];
		for (int i = 0; i < pages.length; i++) {
			pages[i] = i + 1;
		}
		return pages;
	}

	/**
     * 
     *
     */
	public int getPageAfterAdd() {
		int pageAfterAdd = 0;

		double totalDbl = numberOfEntries + 1;
		double pageSizeDbl = pageSize;
		double pageCountDbl = totalDbl / pageSizeDbl;
		pageAfterAdd = ((int) Math.ceil(pageCountDbl)) - 1;

		return pageAfterAdd;
	}

	/**
     * 
     *
     */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public Object[] getCurrentPaginationLeft() {
		return currentPaginationLeft.toArray();
	}

	public Object[] getCurrentPaginationRight() {
		return currentPaginationRight.toArray();
	}

}
