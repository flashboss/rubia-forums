package it.vige.rubia;

import static java.util.stream.Collectors.toList;

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
			if (t != null) {
				PostBean post = new PostBean();
				post.setId(t.getPost().getId());
				AttachmentBean attachment = new AttachmentBean(t.getName(), t.getComment(), t.getContent(), post,
						t.getContentType(), t.getSize());
				attachment.setId(t.getId());

				return attachment;
			} else
				return null;
		}
	};

	Function<AttachmentBean, Attachment> AttachmentBeanToAttachment = new Function<AttachmentBean, Attachment>() {

		public Attachment apply(AttachmentBean t) {
			if (t != null) {
				Post post = null;
				if (t.getPost() != null) {
					post = new Post();
					post.setId(t.getPost().getId());
				}
				Attachment attachment = new Attachment(t.getName(), t.getComment(), t.getContent(), post,
						t.getContentType(), t.getSize());
				attachment.setId(t.getId());

				return attachment;
			} else
				return null;
		}
	};

	Function<Category, CategoryBean> CategoryToCategoryBean = new Function<Category, CategoryBean>() {

		public CategoryBean apply(Category t) {
			if (t != null) {
				ForumInstanceBean forumInstanceBean = new ForumInstanceBean();
				forumInstanceBean.setId(t.getForumInstance().getId());
				CategoryBean category = new CategoryBean(t.getTitle());
				category.setId(t.getId());
				category.setForumInstance(forumInstanceBean);
				List<Forum> forums = t.getForums();
				if (forums != null) {
					List<ForumBean> forumBeans = new ArrayList<ForumBean>();
					for (Forum forum : forums) {
						ForumBean forumBean = ForumToForumBean.apply(forum);
						forumBeans.add(forumBean);
					}
					category.setForums(forumBeans);
				}
				category.setOrder(t.getOrder());

				return category;
			} else
				return null;
		}
	};

	Function<CategoryBean, Category> CategoryBeanToCategory = new Function<CategoryBean, Category>() {

		public Category apply(CategoryBean t) {
			if (t != null) {
				Category category = new Category(t.getTitle());
				category.setId(t.getId());
				ForumInstanceBean forumInstanceBean = t.getForumInstance();
				if (forumInstanceBean != null) {
					ForumInstance forumInstance = new ForumInstance();
					forumInstance.setId(forumInstanceBean.getId());
					category.setForumInstance(forumInstance);
				}
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
			} else
				return null;
		}
	};

	Function<Forum, ForumBean> ForumToForumBean = new Function<Forum, ForumBean>() {

		public ForumBean apply(Forum t) {
			if (t != null) {
				CategoryBean categoryBean = new CategoryBean();
				categoryBean.setId(t.getCategory().getId());
				categoryBean.setOrder(t.getCategory().getOrder());
				categoryBean.setTitle(t.getCategory().getTitle());
				ForumBean forum = new ForumBean(t.getName(), t.getDescription(), categoryBean);
				forum.setId(t.getId());
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
			} else
				return null;
		}
	};

	Function<ForumBean, Forum> ForumBeanToForum = new Function<ForumBean, Forum>() {

		public Forum apply(ForumBean t) {
			if (t != null) {
				Category category = new Category();
				category.setId(t.getCategory().getId());
				category.setOrder(t.getCategory().getOrder());
				category.setTitle(t.getCategory().getTitle());
				Forum forum = new Forum(t.getName(), t.getDescription(), category);
				forum.setId(t.getId());
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
			} else
				return null;
		}
	};

	Function<ForumInstance, ForumInstanceBean> ForumInstanceToForumInstanceBean = new Function<ForumInstance, ForumInstanceBean>() {

		public ForumInstanceBean apply(ForumInstance t) {
			if (t != null) {
				ForumInstanceBean forumInstance = new ForumInstanceBean();
				Collection<Category> categories = t.getCategories();
				if (categories != null) {
					List<CategoryBean> categoryBeans = new ArrayList<CategoryBean>();
					for (Category category : categories) {
						CategoryBean categoryBean = new CategoryBean();
						categoryBean.setId(category.getId());
						categoryBean.setOrder(category.getOrder());
						categoryBean.setTitle(category.getTitle());
						categoryBeans.add(categoryBean);
					}
					forumInstance.setCategories(categoryBeans);
				}
				forumInstance.setId(t.getId());
				forumInstance.setName(t.getName());

				return forumInstance;
			} else
				return null;
		}
	};

	Function<ForumInstanceBean, ForumInstance> ForumInstanceBeanToForumInstance = new Function<ForumInstanceBean, ForumInstance>() {

		public ForumInstance apply(ForumInstanceBean t) {
			if (t != null) {
				ForumInstance forumInstance = new ForumInstance();
				Collection<CategoryBean> categoryBeans = t.getCategories();
				if (categoryBeans != null) {
					List<Category> categories = new ArrayList<Category>();
					for (CategoryBean categoryBean : categoryBeans) {
						Category category = new Category();
						category.setId(categoryBean.getId());
						category.setOrder(categoryBean.getOrder());
						category.setTitle(categoryBean.getTitle());
						categories.add(category);
					}
					forumInstance.setCategories(categories);
				}
				forumInstance.setId(t.getId());
				forumInstance.setName(t.getName());

				return forumInstance;
			} else
				return null;
		}
	};

	Function<ForumWatch, ForumWatchBean> ForumWatchToForumWatchBean = new Function<ForumWatch, ForumWatchBean>() {

		public ForumWatchBean apply(ForumWatch t) {
			if (t != null) {
				ForumWatchBean forumWatch = new ForumWatchBean();
				forumWatch.setId(t.getId());
				ForumBean forumBean = new ForumBean();
				forumBean.setId(t.getForum().getId());
				forumWatch.setForum(forumBean);
				forumWatch.setMode(t.getMode());
				PosterBean posterBean = PosterToPosterBean.apply(t.getPoster());
				posterBean.setId(t.getPoster().getId());
				forumWatch.setPoster(posterBean);

				return forumWatch;
			} else
				return null;
		}
	};

	Function<ForumWatchBean, ForumWatch> ForumWatchBeanToForumWatch = new Function<ForumWatchBean, ForumWatch>() {

		public ForumWatch apply(ForumWatchBean t) {
			if (t != null) {
				ForumWatch forumWatch = new ForumWatch();
				forumWatch.setId(t.getId());
				Forum forum = new Forum();
				forum.setId(t.getForum().getId());
				forumWatch.setForum(forum);
				forumWatch.setMode(t.getMode());
				Poster poster = PosterBeanToPoster.apply(t.getPoster());
				poster.setId(t.getPoster().getId());
				forumWatch.setPoster(poster);

				return forumWatch;
			} else
				return null;
		}
	};

	Function<Message, MessageBean> MessageToMessageBean = new Function<Message, MessageBean>() {

		public MessageBean apply(Message t) {
			if (t != null) {
				MessageBean message = new MessageBean(t.getText());
				message.setBBCodeEnabled(t.getBBCodeEnabled());
				message.setHTMLEnabled(t.getHTMLEnabled());
				message.setSignatureEnabled(t.getSignatureEnabled());
				message.setSmiliesEnabled(t.getSmiliesEnabled());
				message.setSubject(t.getSubject());

				return message;
			} else
				return null;
		}
	};

	Function<MessageBean, Message> MessageBeanToMessage = new Function<MessageBean, Message>() {

		public Message apply(MessageBean t) {
			if (t != null) {
				Message message = new Message(t.getText());
				message.setBBCodeEnabled(t.getBBCodeEnabled());
				message.setHTMLEnabled(t.getHTMLEnabled());
				message.setSignatureEnabled(t.getSignatureEnabled());
				message.setSmiliesEnabled(t.getSmiliesEnabled());
				message.setSubject(t.getSubject());

				return message;
			} else
				return null;
		}
	};

	Function<Poll, PollBean> PollToPollBean = new Function<Poll, PollBean>() {

		public PollBean apply(Poll t) {
			if (t != null) {
				PollBean poll = new PollBean(t.getTitle(),
						t.getOptions().stream().map(x -> PollOptionToPollOptionBean.apply(x)).collect(toList()),
						t.getLength());
				poll.setId(t.getId());
				poll.setCreationDate(t.getCreationDate());
				poll.setVoted(t.getVoted());

				return poll;
			} else
				return null;
		}
	};

	Function<PollBean, Poll> PollBeanToPoll = new Function<PollBean, Poll>() {

		public Poll apply(PollBean t) {
			if (t != null) {
				Poll poll = new Poll(t.getTitle(), null, t.getLength());
				poll.setCreationDate(t.getCreationDate());
				poll.setId(t.getId());
				poll.setVoted(t.getVoted());
				poll.setOptions(
						t.getOptions().stream().map(x -> PollOptionBeanToPollOption.apply(x)).collect(toList()));

				return poll;
			} else
				return null;
		}
	};

	Function<PollOption, PollOptionBean> PollOptionToPollOptionBean = new Function<PollOption, PollOptionBean>() {

		public PollOptionBean apply(PollOption t) {
			if (t != null) {
				PollBean pollBean = null;
				if (t.getPoll() != null) {
					pollBean = new PollBean();
					pollBean.setId(t.getPoll().getId());
				}
				PollOptionBean pollOption = new PollOptionBean(t.getQuestion());
				pollOption.setPoll(pollBean);
				pollOption.setPollOptionPosition(t.getPollOptionPosition());
				pollOption.setVotes(t.getVotes());

				return pollOption;
			} else
				return null;
		}
	};

	Function<PollOptionBean, PollOption> PollOptionBeanToPollOption = new Function<PollOptionBean, PollOption>() {

		public PollOption apply(PollOptionBean t) {
			if (t != null) {
				Poll poll = null;
				if (t.getPoll() != null) {
					poll = new Poll();
					poll.setId(t.getPoll().getId());
				}
				PollOption pollOption = new PollOption(t.getQuestion());
				pollOption.setPoll(poll);
				pollOption.setPollOptionPosition(t.getPollOptionPosition());
				pollOption.setVotes(t.getVotes());

				return pollOption;
			} else
				return null;
		}
	};

	Function<PollVoted, PollVotedBean> PollVotedToPollVotedBean = new Function<PollVoted, PollVotedBean>() {

		public PollVotedBean apply(PollVoted t) {
			if (t != null) {
				PollBean pollBean = new PollBean();
				pollBean.setId(t.getPoll().getId());
				PollVotedBean pollVoted = new PollVotedBean();
				pollVoted.setPoll(pollBean);
				pollVoted.setPoll2(t.getPk().getPoll2());
				pollVoted.setPollVoted(t.getPk().getPollVoted());

				return pollVoted;
			} else
				return null;
		}
	};

	Function<PollVotedBean, PollVoted> PollVotedBeanToPollVoted = new Function<PollVotedBean, PollVoted>() {

		public PollVoted apply(PollVotedBean t) {
			if (t != null) {
				Poll poll = new Poll();
				poll.setId(t.getPoll().getId());
				PollVoted pollVoted = new PollVoted();
				pollVoted.setPoll(poll);
				PollVotedPK pollVotedPK = new PollVotedPK();
				pollVotedPK.setPoll2(t.getPoll2());
				pollVotedPK.setPollVoted(t.getPollVoted());
				pollVoted.setPk(pollVotedPK);

				return pollVoted;
			} else
				return null;
		}
	};

	Function<Post, PostBean> PostToPostBean = new Function<Post, PostBean>() {

		public PostBean apply(Post t) {
			if (t != null) {
				Collection<Attachment> attachments = t.getAttachments();
				List<AttachmentBean> attachmentBeans = attachments == null ? null
						: attachments.stream().map(x -> AttachmentToAttachmentBean.apply(x)).collect(toList());
				TopicBean topicBean = TopicToTopicBean.apply(t.getTopic());
				PostBean post = new PostBean(topicBean, t.getMessage().getText(), attachmentBeans);
				post.setCreateDate(t.getCreateDate());
				post.setEditCount(t.getEditCount());
				post.setEditDate(t.getEditDate());
				post.setId(t.getId());
				MessageBean messageBean = MessageToMessageBean.apply(t.getMessage());
				post.setMessage(messageBean);
				PosterBean posterBean = PosterToPosterBean.apply(t.getPoster());
				post.setPoster(posterBean);
				post.setUser(t.getUser());

				return post;
			} else
				return null;
		}
	};

	Function<PostBean, Post> PostBeanToPost = new Function<PostBean, Post>() {

		public Post apply(PostBean t) {
			if (t != null) {
				Collection<AttachmentBean> attachmentBeans = t.getAttachments();
				List<Attachment> attachments = attachmentBeans == null ? null
						: attachmentBeans.stream().map(x -> AttachmentBeanToAttachment.apply(x)).collect(toList());
				Topic topic = new Topic();
				topic.setId(t.getTopic().getId());
				Post post = new Post(topic, t.getMessage().getText(), attachments);
				post.setCreateDate(t.getCreateDate());
				post.setEditCount(t.getEditCount());
				post.setEditDate(t.getEditDate());
				post.setId(t.getId());
				Message message = MessageBeanToMessage.apply(t.getMessage());
				post.setMessage(message);
				Poster poster = PosterBeanToPoster.apply(t.getPoster());
				post.setPoster(poster);
				post.setUser(t.getUser());

				return post;
			} else
				return null;
		}
	};

	Function<Poster, PosterBean> PosterToPosterBean = new Function<Poster, PosterBean>() {

		public PosterBean apply(Poster t) {
			if (t != null) {
				PosterBean poster = new PosterBean(t.getUserId());
				poster.setId(t.getId());

				return poster;
			} else
				return null;
		}
	};

	Function<PosterBean, Poster> PosterBeanToPoster = new Function<PosterBean, Poster>() {

		public Poster apply(PosterBean t) {
			if (t != null) {
				Poster poster = new Poster(t.getUserId());
				poster.setId(t.getId());

				return poster;
			} else
				return null;
		}
	};

	Function<Topic, TopicBean> TopicToTopicBean = new Function<Topic, TopicBean>() {

		public TopicBean apply(Topic t) {
			if (t != null) {
				ForumBean forumBean = ForumToForumBean.apply(t.getForum());
				PollBean pollBean = PollToPollBean.apply(t.getPoll());
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
				PosterBean posterBean = PosterToPosterBean.apply(t.getPoster());
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
			} else
				return null;
		}
	};

	Function<TopicBean, Topic> TopicBeanToTopic = new Function<TopicBean, Topic>() {

		public Topic apply(TopicBean t) {
			if (t != null) {
				Forum forum = ForumBeanToForum.apply(t.getForum());
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
				Poster poster = PosterBeanToPoster.apply(t.getPoster());
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
			} else
				return null;
		}
	};

	Function<TopicWatch, TopicWatchBean> TopicWatchToTopicWatchBean = new Function<TopicWatch, TopicWatchBean>() {

		public TopicWatchBean apply(TopicWatch t) {
			if (t != null) {
				TopicWatchBean topicWatch = new TopicWatchBean();
				TopicBean topicBean = new TopicBean();
				topicBean.setId(t.getTopic().getId());
				topicWatch.setTopic(topicBean);
				topicWatch.setMode(t.getMode());
				topicWatch.setId(t.getId());
				PosterBean posterBean = PosterToPosterBean.apply(t.getPoster());
				topicWatch.setPoster(posterBean);

				return topicWatch;
			} else
				return null;
		}
	};

	Function<TopicWatchBean, TopicWatch> TopicWatchBeanToTopicWatch = new Function<TopicWatchBean, TopicWatch>() {

		public TopicWatch apply(TopicWatchBean t) {
			if (t != null) {
				TopicWatch topicWatch = new TopicWatch();
				Topic topic = new Topic();
				topic.setId(t.getTopic().getId());
				topicWatch.setTopic(topic);
				topicWatch.setMode(t.getMode());
				topicWatch.setId(t.getId());
				Poster poster = PosterBeanToPoster.apply(t.getPoster());
				poster.setId(t.getPoster().getId());
				topicWatch.setPoster(poster);

				return topicWatch;
			} else
				return null;
		}
	};

	Function<Watch, WatchBean> WatchToWatchBean = new Function<Watch, WatchBean>() {

		public WatchBean apply(Watch t) {
			if (t != null) {
				WatchBean watch = new WatchBean();
				watch.setMode(t.getMode());
				watch.setId(t.getId());
				PosterBean posterBean = PosterToPosterBean.apply(t.getPoster());
				watch.setPoster(posterBean);

				return watch;
			} else
				return null;
		}
	};

	Function<WatchBean, Watch> WatchBeanToWatch = new Function<WatchBean, Watch>() {

		public Watch apply(WatchBean t) {
			if (t != null) {
				Watch watch = new Watch();
				watch.setMode(t.getMode());
				watch.setId(t.getId());
				Poster poster = PosterBeanToPoster.apply(t.getPoster());
				watch.setPoster(poster);

				return watch;
			} else
				return null;
		}
	};
}