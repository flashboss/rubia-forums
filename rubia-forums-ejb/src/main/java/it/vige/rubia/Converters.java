package it.vige.rubia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import it.vige.rubia.dto.AttachmentBean;
import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.dto.ForumWatchBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PollOptionBean;
import it.vige.rubia.dto.PollVotedBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.dto.TopicWatchBean;
import it.vige.rubia.dto.WatchBean;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.ForumInstance;
import it.vige.rubia.model.ForumWatch;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.PollVoted;
import it.vige.rubia.model.PollVotedPK;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.model.TopicWatch;
import it.vige.rubia.model.Watch;

public interface Converters {

	Function<Attachment, AttachmentBean> AttachmentToAttachmentBean = new Function<Attachment, AttachmentBean>() {

		public AttachmentBean apply(Attachment t) {
			PostBean post = new PostBean();
			post.setId(t.getPost().getId());
			AttachmentBean attachment = new AttachmentBean(t.getName(), t.getComment(), t.getContent(), post,
					t.getContentType(), t.getSize());
			attachment.setId(t.getId());

			return attachment;
		}
	};

	Function<AttachmentBean, Attachment> AttachmentBeanToAttachment = new Function<AttachmentBean, Attachment>() {

		public Attachment apply(AttachmentBean t) {
			Post post = new Post();
			post.setId(t.getPost().getId());
			Attachment attachment = new Attachment(t.getName(), t.getComment(), t.getContent(), post,
					t.getContentType(), t.getSize());
			attachment.setId(t.getId());

			return attachment;
		}
	};

	Function<Category, CategoryBean> CategoryToCategoryBean = new Function<Category, CategoryBean>() {

		public CategoryBean apply(Category t) {
			ForumInstanceBean forumInstanceBean = new ForumInstanceBean();
			forumInstanceBean.setId(t.getForumInstance().getId());
			CategoryBean category = new CategoryBean(t.getTitle());
			category.setForumInstance(forumInstanceBean);
			List<Forum> forums = t.getForums();
			if (forums != null) {
				List<ForumBean> forumBeans = new ArrayList<ForumBean>();
				for (Forum forum : forums) {
					ForumBean forumBean = new ForumBean();
					forumBean.setId(forum.getId());
					forumBeans.add(forumBean);
				}
				category.setForums(forumBeans);
			}
			category.setOrder(t.getOrder());

			return category;
		}
	};

	Function<CategoryBean, Category> CategoryBeanToCategory = new Function<CategoryBean, Category>() {

		public Category apply(CategoryBean t) {
			ForumInstance forumInstance = new ForumInstance();
			forumInstance.setId(t.getForumInstance().getId());
			Category category = new Category(t.getTitle());
			category.setForumInstance(forumInstance);
			List<ForumBean> forumBeans = t.getForums();
			if (forumBeans != null) {
				List<Forum> forums = new ArrayList<Forum>();
				for (ForumBean forumBean : forumBeans) {
					Forum forum = new Forum();
					forum.setId(forumBean.getId());
					forums.add(forum);
				}
				category.setForums(forums);
			}
			category.setOrder(t.getOrder());

			return category;
		}
	};

	Function<Forum, ForumBean> ForumToForumBean = new Function<Forum, ForumBean>() {

		public ForumBean apply(Forum t) {
			CategoryBean categoryBean = new CategoryBean();
			categoryBean.setId(t.getCategory().getId());
			ForumBean forum = new ForumBean(t.getName(), t.getDescription(), categoryBean);
			Collection<Watch> forumWatches = t.getForumWatch();
			if (forumWatches != null) {
				List<WatchBean> watchBeans = new ArrayList<WatchBean>();
				for (Watch watch : forumWatches) {
					WatchBean watchBean = new WatchBean();
					watchBean.setId(watch.getId());
					watchBeans.add(watchBean);
				}
				forum.setForumWatch(watchBeans);
			}
			forum.setOrder(t.getOrder());
			forum.setPostCount(t.getPostCount());
			forum.setPruneEnable(t.getPruneEnable());
			forum.setPruneNext(t.getPruneNext());
			forum.setStatus(t.getStatus());
			forum.setTopicCount(t.getTopicCount());
			Collection<Topic> topics = t.getTopics();
			if (topics != null) {
				List<TopicBean> topicBeans = new ArrayList<TopicBean>();
				for (Topic topic : topics) {
					TopicBean topicBean = new TopicBean();
					topicBean.setId(topic.getId());
					topicBeans.add(topicBean);
				}
				forum.setTopics(topicBeans);
			}
			Collection<Watch> watches = t.getWatches();
			if (watches != null) {
				List<WatchBean> watchBeans = new ArrayList<WatchBean>();
				for (Watch watch : watches) {
					WatchBean watchBean = new WatchBean();
					watchBean.setId(watch.getId());
					watchBeans.add(watchBean);
				}
				forum.setWatches(watchBeans);
			}

			return forum;
		}
	};

	Function<ForumBean, Forum> ForumBeanToForum = new Function<ForumBean, Forum>() {

		public Forum apply(ForumBean t) {
			Category category = new Category();
			category.setId(t.getCategory().getId());
			Forum forum = new Forum(t.getName(), t.getDescription(), category);
			Collection<WatchBean> forumWatchBeans = t.getForumWatch();
			if (forumWatchBeans != null) {
				List<Watch> watches = new ArrayList<Watch>();
				for (WatchBean watchBean : forumWatchBeans) {
					Watch watch = new Watch();
					watch.setId(watchBean.getId());
					watches.add(watch);
				}
				forum.setForumWatch(watches);
			}
			forum.setOrder(t.getOrder());
			forum.setPostCount(t.getPostCount());
			forum.setPruneEnable(t.getPruneEnable());
			forum.setPruneNext(t.getPruneNext());
			forum.setStatus(t.getStatus());
			forum.setTopicCount(t.getTopicCount());
			Collection<TopicBean> topicBeans = t.getTopics();
			if (topicBeans != null) {
				List<Topic> topics = new ArrayList<Topic>();
				for (TopicBean topicBean : topicBeans) {
					Topic topic = new Topic();
					topic.setId(topicBean.getId());
					topics.add(topic);
				}
				forum.setTopics(topics);
			}
			Collection<WatchBean> watchBeans = t.getWatches();
			if (watchBeans != null) {
				List<Watch> watches = new ArrayList<Watch>();
				for (WatchBean watchBean : watchBeans) {
					Watch watch = new Watch();
					watch.setId(watchBean.getId());
					watches.add(watch);
				}
				forum.setWatches(watches);
			}

			return forum;
		}
	};

	Function<ForumInstance, ForumInstanceBean> ForumInstanceToForumInstanceBean = new Function<ForumInstance, ForumInstanceBean>() {

		public ForumInstanceBean apply(ForumInstance t) {
			ForumInstanceBean forumInstance = new ForumInstanceBean();
			Collection<Category> categories = t.getCategories();
			if (categories != null) {
				List<CategoryBean> categoryBeans = new ArrayList<CategoryBean>();
				for (Category category : categories) {
					CategoryBean categoryBean = new CategoryBean();
					categoryBean.setId(category.getId());
					categoryBeans.add(categoryBean);
				}
				forumInstance.setCategories(categoryBeans);
			}
			forumInstance.setId(t.getId());
			forumInstance.setName(t.getName());

			return forumInstance;
		}
	};

	Function<ForumInstanceBean, ForumInstance> ForumInstanceBeanToForumInstance = new Function<ForumInstanceBean, ForumInstance>() {

		public ForumInstance apply(ForumInstanceBean t) {
			ForumInstance forumInstance = new ForumInstance();
			Collection<CategoryBean> categoryBeans = t.getCategories();
			if (categoryBeans != null) {
				List<Category> categories = new ArrayList<Category>();
				for (CategoryBean categoryBean : categoryBeans) {
					Category category = new Category();
					category.setId(categoryBean.getId());
					categories.add(category);
				}
				forumInstance.setCategories(categories);
			}
			forumInstance.setId(t.getId());
			forumInstance.setName(t.getName());

			return forumInstance;
		}
	};

	Function<ForumWatch, ForumWatchBean> ForumWatchToForumWatchBean = new Function<ForumWatch, ForumWatchBean>() {

		public ForumWatchBean apply(ForumWatch t) {
			ForumWatchBean forumWatch = new ForumWatchBean();
			ForumBean forumBean = new ForumBean();
			forumBean.setId(t.getForum().getId());
			forumWatch.setForum(forumBean);
			forumWatch.setMode(t.getMode());
			PosterBean posterBean = new PosterBean();
			posterBean.setId(t.getPoster().getId());
			forumWatch.setPoster(posterBean);

			return forumWatch;
		}
	};

	Function<ForumWatchBean, ForumWatch> ForumWatchBeanToForumWatch = new Function<ForumWatchBean, ForumWatch>() {

		public ForumWatch apply(ForumWatchBean t) {
			ForumWatch forumWatch = new ForumWatch();
			Forum forum = new Forum();
			forum.setId(t.getForum().getId());
			forumWatch.setForum(forum);
			forumWatch.setMode(t.getMode());
			Poster poster = new Poster();
			poster.setId(t.getPoster().getId());
			forumWatch.setPoster(poster);

			return forumWatch;
		}
	};

	Function<Message, MessageBean> MessageToMessageBean = new Function<Message, MessageBean>() {

		public MessageBean apply(Message t) {
			MessageBean message = new MessageBean(t.getText());
			message.setBBCodeEnabled(t.getBBCodeEnabled());
			message.setHTMLEnabled(t.getHTMLEnabled());
			message.setSignatureEnabled(t.getSignatureEnabled());
			message.setSmiliesEnabled(t.getSmiliesEnabled());
			message.setSubject(t.getSubject());

			return message;
		}
	};

	Function<MessageBean, Message> MessageBeanToMessage = new Function<MessageBean, Message>() {

		public Message apply(MessageBean t) {
			Message message = new Message(t.getText());
			message.setBBCodeEnabled(t.getBBCodeEnabled());
			message.setHTMLEnabled(t.getHTMLEnabled());
			message.setSignatureEnabled(t.getSignatureEnabled());
			message.setSmiliesEnabled(t.getSmiliesEnabled());
			message.setSubject(t.getSubject());

			return message;
		}
	};

	Function<Poll, PollBean> PollToPollBean = new Function<Poll, PollBean>() {

		public PollBean apply(Poll t) {
			Collection<PollOption> pollOptions = t.getOptions();
			List<PollOptionBean> pollOptionBeans = null;
			if (pollOptions != null) {
				pollOptionBeans = new ArrayList<PollOptionBean>();
				for (PollOption pollOption : pollOptions) {
					PollOptionBean pollOptionBean = new PollOptionBean();
					pollOptionBean.setPollOptionPosition(pollOption.getPollOptionPosition());
					pollOptionBeans.add(pollOptionBean);
				}
			}
			PollBean poll = new PollBean(t.getTitle(), pollOptionBeans, t.getLength());
			poll.setCreationDate(t.getCreationDate());
			poll.setTitle(t.getTitle());

			return poll;
		}
	};

	Function<PollBean, Poll> PollBeanToPoll = new Function<PollBean, Poll>() {

		public Poll apply(PollBean t) {
			Collection<PollOptionBean> pollOptionBeans = t.getOptions();
			List<PollOption> pollOptions = null;
			if (pollOptionBeans != null) {
				pollOptions = new ArrayList<PollOption>();
				for (PollOptionBean pollOptionBean : pollOptionBeans) {
					PollOption pollOption = new PollOption();
					pollOption.setPollOptionPosition(pollOptionBean.getPollOptionPosition());
					pollOptions.add(pollOption);
				}
			}
			Poll poll = new Poll(t.getTitle(), pollOptions, t.getLength());
			poll.setCreationDate(t.getCreationDate());
			poll.setTitle(t.getTitle());

			return poll;
		}
	};

	Function<PollOption, PollOptionBean> PollOptionToPollOptionBean = new Function<PollOption, PollOptionBean>() {

		public PollOptionBean apply(PollOption t) {
			PollBean pollBean = new PollBean();
			pollBean.setId(t.getPoll().getId());
			PollOptionBean pollOption = new PollOptionBean(t.getQuestion());
			pollOption.setPoll(pollBean);
			pollOption.setPollOptionPosition(t.getPollOptionPosition());
			pollOption.setVotes(t.getVotes());

			return pollOption;
		}
	};

	Function<PollOptionBean, PollOption> PollOptionBeanToPollOption = new Function<PollOptionBean, PollOption>() {

		public PollOption apply(PollOptionBean t) {
			Poll poll = new Poll();
			poll.setId(t.getPoll().getId());
			PollOption pollOption = new PollOption(t.getQuestion());
			pollOption.setPoll(poll);
			pollOption.setPollOptionPosition(t.getPollOptionPosition());
			pollOption.setVotes(t.getVotes());

			return pollOption;
		}
	};

	Function<PollVoted, PollVotedBean> PollVotedToPollVotedBean = new Function<PollVoted, PollVotedBean>() {

		public PollVotedBean apply(PollVoted t) {
			PollBean pollBean = new PollBean();
			pollBean.setId(t.getPoll().getId());
			PollVotedBean pollVoted = new PollVotedBean();
			pollVoted.setPoll(pollBean);
			pollVoted.setPoll2(t.getPk().getPoll2());
			pollVoted.setPollVoted(t.getPk().getPollVoted());

			return pollVoted;
		}
	};

	Function<PollVotedBean, PollVoted> PollVotedBeanToPollVoted = new Function<PollVotedBean, PollVoted>() {

		public PollVoted apply(PollVotedBean t) {
			Poll poll = new Poll();
			poll.setId(t.getPoll().getId());
			PollVoted pollVoted = new PollVoted();
			pollVoted.setPoll(poll);
			PollVotedPK pollVotedPK = new PollVotedPK();
			pollVotedPK.setPoll2(t.getPoll2());
			pollVotedPK.setPollVoted(t.getPollVoted());
			pollVoted.setPk(pollVotedPK);

			return pollVoted;
		}
	};

	Function<Post, PostBean> PostToPostBean = new Function<Post, PostBean>() {

		public PostBean apply(Post t) {
			Collection<Attachment> attachments = t.getAttachments();
			List<AttachmentBean> attachmentBeans = null;
			if (attachments != null) {
				attachmentBeans = new ArrayList<AttachmentBean>();
				for (Attachment attachment : attachments) {
					AttachmentBean attachmentBean = new AttachmentBean();
					attachmentBean.setId(attachment.getId());
					attachmentBeans.add(attachmentBean);
				}
			}
			TopicBean topicBean = new TopicBean();
			topicBean.setId(t.getTopic().getId());
			PostBean post = new PostBean(topicBean, t.getMessage().getText(), attachmentBeans);
			post.setCreateDate(t.getCreateDate());
			post.setEditCount(t.getEditCount());
			post.setEditDate(t.getEditDate());
			post.setId(t.getId());
			MessageBean messageBean = new MessageBean();
			messageBean.setText(t.getMessage().getText());
			post.setMessage(messageBean);
			PosterBean posterBean = new PosterBean();
			posterBean.setId(t.getPoster().getId());
			post.setPoster(posterBean);
			post.setUser(t.getUser());

			return post;
		}
	};

	Function<PostBean, Post> PostBeanToPost = new Function<PostBean, Post>() {

		public Post apply(PostBean t) {
			Collection<AttachmentBean> attachmentBeans = t.getAttachments();
			List<Attachment> attachments = null;
			if (attachmentBeans != null) {
				attachments = new ArrayList<Attachment>();
				for (AttachmentBean attachmentBean : attachmentBeans) {
					Attachment attachment = new Attachment();
					attachment.setId(attachmentBean.getId());
					attachments.add(attachment);
				}
			}
			Topic topic = new Topic();
			topic.setId(t.getTopic().getId());
			Post post = new Post(topic, t.getMessage().getText(), attachments);
			post.setCreateDate(t.getCreateDate());
			post.setEditCount(t.getEditCount());
			post.setEditDate(t.getEditDate());
			post.setId(t.getId());
			Message message = new Message();
			message.setText(t.getMessage().getText());
			post.setMessage(message);
			Poster poster = new Poster();
			poster.setId(t.getPoster().getId());
			post.setPoster(poster);
			post.setUser(t.getUser());

			return post;
		}
	};

	Function<Poster, PosterBean> PosterToPosterBean = new Function<Poster, PosterBean>() {

		public PosterBean apply(Poster t) {
			PosterBean poster = new PosterBean(t.getUserId());
			poster.setId(t.getId());

			return poster;
		}
	};

	Function<PosterBean, Poster> PosterBeanToPoster = new Function<PosterBean, Poster>() {

		public Poster apply(PosterBean t) {
			Poster poster = new Poster(t.getUserId());
			poster.setId(t.getId());

			return poster;
		}
	};

	Function<Topic, TopicBean> TopicToTopicBean = new Function<Topic, TopicBean>() {

		public TopicBean apply(Topic t) {
			ForumBean forumBean = new ForumBean();
			forumBean.setId(t.getForum().getId());
			PollBean pollBean = new PollBean();
			pollBean.setId(t.getPoll().getId());
			Collection<Post> posts = t.getPosts();
			List<PostBean> postBeans = null;
			if (posts != null) {
				postBeans = new ArrayList<PostBean>();
				for (Post post : posts) {
					PostBean postBean = new PostBean();
					postBean.setId(post.getId());
					postBeans.add(postBean);
				}
			}
			TopicBean topic = new TopicBean(forumBean, t.getSubject(), postBeans, t.getType(), pollBean);
			topic.setId(t.getId());
			topic.setLastPostDate(t.getLastPostDate());
			PosterBean posterBean = new PosterBean();
			posterBean.setId(t.getPoster().getId());
			topic.setPoster(posterBean);
			topic.setReplies(t.getReplies());
			topic.setStatus(t.getStatus());
			topic.setViewCount(t.getViewCount());
			Collection<Watch> watches = t.getWatches();
			List<WatchBean> watchBeans = null;
			if (watches != null) {
				watchBeans = new ArrayList<WatchBean>();
				for (Watch watch : watches) {
					WatchBean watchBean = new WatchBean();
					watchBean.setId(watch.getId());
					watchBeans.add(watchBean);
				}
			}
			topic.setWatches(watchBeans);

			return topic;
		}
	};

	Function<TopicBean, Topic> TopicBeanToTopic = new Function<TopicBean, Topic>() {

		public Topic apply(TopicBean t) {
			Forum forum = new Forum();
			forum.setId(t.getForum().getId());
			Poll poll = new Poll();
			poll.setId(t.getPoll().getId());
			Collection<PostBean> postBeans = t.getPosts();
			List<Post> posts = null;
			if (postBeans != null) {
				posts = new ArrayList<Post>();
				for (PostBean postBean : postBeans) {
					Post post = new Post();
					post.setId(postBean.getId());
					posts.add(post);
				}
			}
			Topic topic = new Topic(forum, t.getSubject(), posts, t.getType(), poll);
			topic.setId(t.getId());
			topic.setLastPostDate(t.getLastPostDate());
			Poster poster = new Poster();
			poster.setId(t.getPoster().getId());
			topic.setPoster(poster);
			topic.setReplies(t.getReplies());
			topic.setStatus(t.getStatus());
			topic.setViewCount(t.getViewCount());
			Collection<WatchBean> watchBeans = t.getWatches();
			List<Watch> watches = null;
			if (watchBeans != null) {
				watches = new ArrayList<Watch>();
				for (WatchBean watchBean : watchBeans) {
					Watch watch = new Watch();
					watch.setId(watchBean.getId());
					watches.add(watch);
				}
			}
			topic.setWatches(watches);

			return topic;
		}
	};

	Function<TopicWatch, TopicWatchBean> TopicWatchToTopicWatchBean = new Function<TopicWatch, TopicWatchBean>() {

		public TopicWatchBean apply(TopicWatch t) {
			TopicWatchBean topicWatch = new TopicWatchBean();
			TopicBean topicBean = new TopicBean();
			topicBean.setId(t.getTopic().getId());
			topicWatch.setTopic(topicBean);
			topicWatch.setMode(t.getMode());
			PosterBean posterBean = new PosterBean();
			posterBean.setId(t.getPoster().getId());
			topicWatch.setPoster(posterBean);

			return topicWatch;
		}
	};

	Function<TopicWatchBean, TopicWatch> TopicWatchBeanToTopicWatch = new Function<TopicWatchBean, TopicWatch>() {

		public TopicWatch apply(TopicWatchBean t) {
			TopicWatch topicWatch = new TopicWatch();
			Topic topic = new Topic();
			topic.setId(t.getTopic().getId());
			topicWatch.setTopic(topic);
			topicWatch.setMode(t.getMode());
			Poster poster = new Poster();
			poster.setId(t.getPoster().getId());
			topicWatch.setPoster(poster);

			return topicWatch;
		}
	};

	Function<Watch, WatchBean> WatchToWatchBean = new Function<Watch, WatchBean>() {

		public WatchBean apply(Watch t) {
			WatchBean watch = new WatchBean();
			watch.setMode(t.getMode());
			PosterBean posterBean = new PosterBean();
			posterBean.setId(t.getPoster().getId());
			watch.setPoster(posterBean);

			return watch;
		}
	};

	Function<WatchBean, Watch> WatchBeanToWatch = new Function<WatchBean, Watch>() {

		public Watch apply(WatchBean t) {
			Watch watch = new Watch();
			watch.setMode(t.getMode());
			Poster poster = new Poster();
			poster.setId(t.getPoster().getId());
			watch.setPoster(poster);

			return watch;
		}
	};
}