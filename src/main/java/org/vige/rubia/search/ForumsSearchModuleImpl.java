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
package org.vige.rubia.search;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static org.apache.lucene.document.DateTools.dateToString;
import static org.apache.lucene.document.DateTools.Resolution.MINUTE;
import static org.apache.lucene.search.BooleanClause.Occur.MUST;
import static org.apache.lucene.util.Version.LUCENE_36;
import static org.hibernate.search.Search.getFullTextSession;
import static org.vige.rubia.search.SortBy.POST_TIME;
import static org.vige.rubia.search.SortBy.valueOf;
import static org.vige.rubia.search.SortOrder.DESC;
import static org.vige.rubia.search.TimePeriod.ALL;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.vige.rubia.ModuleException;
import org.vige.rubia.model.Post;
import org.vige.rubia.model.Topic;

@Stateless
public class ForumsSearchModuleImpl implements ForumsSearchModule {

	@PersistenceContext(unitName = "forums")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public ResultPage<Post> findPosts(SearchCriteria criteria)
			throws ModuleException {
		if (criteria != null) {
			try {
				EntityManager session = getSession();
				FullTextSession fullTextSession = getFullTextSession((Session) session
						.getDelegate());

				BooleanQuery query = new BooleanQuery();

				String keywords = criteria.getKeywords();
				if (keywords != null && keywords.length() != 0) {
					String[] fields = null;

					Searching searching = Searching.valueOf(criteria
							.getSearching());
					switch (searching) {
					case TITLE_MSG:
						fields = new String[] { "message.text", "topic.subject" };

						break;
					case MSG:
						fields = new String[] { "message.text" };

						break;
					}

					MultiFieldQueryParser parser = new MultiFieldQueryParser(
							LUCENE_36, fields, new StandardAnalyzer(LUCENE_36));
					query.add(parser.parse(keywords), MUST);
				}

				String forumId = criteria.getForum();
				if (forumId != null && forumId.length() != 0) {
					query.add(
							new TermQuery(new Term("topic.forum.id", forumId)),
							MUST);
				}

				String categoryId = criteria.getCategory();
				if (categoryId != null && categoryId.length() != 0) {
					query.add(new TermQuery(new Term("topic.forum.category.id",
							categoryId)), MUST);
				}

				String userName = criteria.getAuthor();
				if (userName != null && userName.length() != 0) {
					query.add(new WildcardQuery(new Term("poster.userId",
							userName)), MUST);
				}

				String timePeriod = criteria.getTimePeriod();
				if (timePeriod != null && timePeriod.length() != 0) {
					addPostTimeQuery(query, TimePeriod.valueOf(timePeriod));
				}

				FullTextQuery fullTextQuery = fullTextSession
						.createFullTextQuery(query, Post.class);

				SortOrder sortOrder = SortOrder
						.valueOf(criteria.getSortOrder());
				String sortByStr = criteria.getSortBy();
				SortBy sortBy = null;
				if (sortByStr != null)
					sortBy = valueOf(sortByStr);
				fullTextQuery.setSort(getSort(sortBy, sortOrder));

				fullTextQuery.setFirstResult(criteria.getPageSize()
						* criteria.getPageNumber());
				fullTextQuery.setMaxResults(criteria.getPageSize());

				ResultPage<Post> resultPage = new ResultPage<Post>();
				resultPage.setPage(fullTextQuery.list());
				resultPage.setResultSize(fullTextQuery.getResultSize());

				return resultPage;
			} catch (ParseException e) {

				return null;
			} catch (Exception e) {
				throw new ModuleException(e.getMessage(), e);
			}
		} else {
			throw new IllegalArgumentException("criteria cannot be null");
		}
	}

	@SuppressWarnings("unchecked")
	public ResultPage<Topic> findTopics(SearchCriteria criteria)
			throws ModuleException {
		if (criteria != null) {
			try {
				EntityManager session = getSession();
				FullTextSession fullTextSession = getFullTextSession((Session) session
						.getDelegate());

				BooleanQuery query = new BooleanQuery();

				String keywords = criteria.getKeywords();
				if (keywords != null && keywords.length() != 0) {
					String[] fields = null;

					Searching searching = Searching.valueOf(criteria
							.getSearching());
					switch (searching) {
					case TITLE_MSG:
						fields = new String[] { "message.text", "topic.subject" };

						break;
					case MSG:
						fields = new String[] { "message.text" };

						break;
					}

					MultiFieldQueryParser parser = new MultiFieldQueryParser(
							LUCENE_36, fields, new StandardAnalyzer(LUCENE_36));
					query.add(parser.parse(keywords), MUST);
				}

				String forumId = criteria.getForum();
				if (forumId != null && forumId.length() != 0) {
					query.add(
							new TermQuery(new Term("topic.forum.id", forumId)),
							MUST);
				}

				String categoryId = criteria.getCategory();
				if (categoryId != null && categoryId.length() != 0) {
					query.add(new TermQuery(new Term("topic.forum.category.id",
							categoryId)), MUST);
				}

				String userName = criteria.getAuthor();
				if (userName != null && userName.length() != 0) {
					query.add(
							new WildcardQuery(new Term("poster.userId", userName)),
							MUST);
				}

				String timePeriod = criteria.getTimePeriod();
				if (timePeriod != null && timePeriod.length() != 0) {
					addPostTimeQuery(query, TimePeriod.valueOf(timePeriod));
				}

				FullTextQuery fullTextQuery = fullTextSession
						.createFullTextQuery(query, Post.class);

				SortOrder sortOrder = SortOrder
						.valueOf(criteria.getSortOrder());
				SortBy sortBy = valueOf(criteria.getSortBy());
				fullTextQuery.setSort(getSort(sortBy, sortOrder));

				fullTextQuery.setProjection("topic.id");

				LinkedHashSet<Integer> topicIds = new LinkedHashSet<Integer>();
				LinkedHashSet<Integer> topicToDispIds = new LinkedHashSet<Integer>();

				int start = criteria.getPageSize() * criteria.getPageNumber();
				int end = start + criteria.getPageSize();
				int index = 0;
				for (Object o : fullTextQuery.list()) {
					Integer id = (Integer) ((Object[]) o)[0];

					if (topicIds.add(id)) {

						if (index >= start && index < end) {
							topicToDispIds.add(id);
						}

						index++;
					}
				}

				List<Topic> topics = null;
				if (topicToDispIds.size() > 0) {
					Query q = session
							.createQuery("from Topic as t join fetch t.poster where t.id IN ( :topicIds )");
					q.setParameter("topicIds", topicToDispIds);

					List<Topic> results = q.getResultList();

					topics = new LinkedList<Topic>();
					for (Integer id : topicToDispIds) {
						for (Topic topic : results) {
							if (id.equals(topic.getId())) {
								topics.add(topic);
								break;
							}
						}
					}
				}

				ResultPage<Topic> resultPage = new ResultPage<Topic>();
				resultPage.setPage(topics);
				resultPage.setResultSize(topicIds.size());

				return resultPage;
			} catch (ParseException e) {

				return null;
			} catch (Exception e) {
				throw new ModuleException(e.getMessage(), e);
			}
		} else {
			throw new IllegalArgumentException("criteria cannot be null");
		}
	}

	protected Sort getSort(SortBy sortBy, SortOrder sortOrder) {

		String fieldName = null;

		if (sortBy != null) {
			fieldName = sortBy.getFieldName();
		}

		if (fieldName == null) {
			fieldName = POST_TIME.getFieldName();
		}

		int reverse = 1;

		if (sortOrder == DESC) {
			reverse = 0;
		}

		Sort sort = new Sort(new SortField(fieldName, reverse));

		return sort;
	}

	protected void addPostTimeQuery(BooleanQuery query, TimePeriod period) {

		if (period != ALL) {
			Calendar calendar = getInstance();

			Date startDate = null;
			Date endDate = calendar.getTime();

			switch (period) {
			case DAY:
				calendar.add(DATE, -1);
				startDate = calendar.getTime();

				break;
			case SEVEN_DAYS:
				calendar.add(DATE, -7);
				startDate = calendar.getTime();

				break;
			case TWO_WEEKS:
				calendar.add(DATE, -14);
				startDate = calendar.getTime();

				break;
			case MONTH:
				calendar.add(MONTH, -1);
				startDate = calendar.getTime();

				break;
			case THREE_MONTHS:
				calendar.add(MONTH, -3);
				startDate = calendar.getTime();

				break;
			case SIX_MONTHS:
				calendar.add(MONTH, -6);
				startDate = calendar.getTime();

				break;
			case YEAR:
				calendar.add(YEAR, -1);
				startDate = calendar.getTime();

				break;
			case ALL:
				calendar.add(DATE, -1);
				startDate = calendar.getTime();

				break;
			}

			if (startDate != null) {
				query.add(
						new TermRangeQuery("createDate", dateToString(
								startDate, MINUTE), dateToString(endDate,
								MINUTE), true, true), MUST);
			}
		}
	}

	protected EntityManager getSession() {

		return em;
	}

}
