package it.vige.rubia;

import static java.util.stream.Collectors.toList;

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
			AttachmentBean attachment = new AttachmentBean(t.getName(), t.getComment(), t.getContent(),
					PostToPostBean.apply(t.getPost()), t.getContentType(), t.getSize());
			attachment.setId(t.getId());

			return attachment;
		}
	};

	Function<AttachmentBean, Attachment> AttachmentBeanToAttachment = new Function<AttachmentBean, Attachment>() {

		public Attachment apply(AttachmentBean t) {
			Attachment attachment = new Attachment(t.getName(), t.getComment(), t.getContent(),
					PostBeanToPost.apply(t.getPost()), t.getContentType(), t.getSize());
			attachment.setId(t.getId());

			return attachment;
		}
	};

	Function<Category, CategoryBean> CategoryToCategoryBean = new Function<Category, CategoryBean>() {

		public CategoryBean apply(Category t) {
			CategoryBean category = new CategoryBean(t.getTitle());
			category.setForumInstance(ForumInstanceToForumInstanceBean.apply(t.getForumInstance()));
			category.setForums(t.getForums().stream().map(x -> ForumToForumBean.apply(x)).collect(toList()));
			category.setOrder(t.getOrder());

			return category;
		}
	};

	Function<CategoryBean, Category> CategoryBeanToCategory = new Function<CategoryBean, Category>() {

		public Category apply(CategoryBean t) {
			Category category = new Category(t.getTitle());
			category.setForumInstance(ForumInstanceBeanToForumInstance.apply(t.getForumInstance()));
			category.setForums(t.getForums().stream().map(x -> ForumBeanToForum.apply(x)).collect(toList()));
			category.setOrder(t.getOrder());

			return category;
		}
	};

	Function<Forum, ForumBean> ForumToForumBean = new Function<Forum, ForumBean>() {

		public ForumBean apply(Forum t) {
			ForumBean forum = new ForumBean(t.getName(), t.getDescription(),
					CategoryToCategoryBean.apply(t.getCategory()));
			forum.setForumWatch(t.getForumWatch().stream().map(x -> WatchToWatchBean.apply(x)).collect(toList()));
			forum.setOrder(t.getOrder());
			forum.setPostCount(t.getPostCount());
			forum.setPruneEnable(t.getPruneEnable());
			forum.setPruneNext(t.getPruneNext());
			forum.setStatus(t.getStatus());
			forum.setTopicCount(t.getTopicCount());
			forum.setTopics(t.getTopics().stream().map(x -> TopicToTopicBean.apply(x)).collect(toList()));
			forum.setWatches(t.getWatches().stream().map(x -> WatchToWatchBean.apply(x)).collect(toList()));

			return forum;
		}
	};

	Function<ForumBean, Forum> ForumBeanToForum = new Function<ForumBean, Forum>() {

		public Forum apply(ForumBean t) {
			Forum forum = new Forum(t.getName(), t.getDescription(), CategoryBeanToCategory.apply(t.getCategory()));
			forum.setForumWatch(t.getForumWatch().stream().map(x -> WatchBeanToWatch.apply(x)).collect(toList()));
			forum.setOrder(t.getOrder());
			forum.setPostCount(t.getPostCount());
			forum.setPruneEnable(t.getPruneEnable());
			forum.setPruneNext(t.getPruneNext());
			forum.setStatus(t.getStatus());
			forum.setTopicCount(t.getTopicCount());
			forum.setTopics(t.getTopics().stream().map(x -> TopicBeanToTopic.apply(x)).collect(toList()));
			forum.setWatches(t.getWatches().stream().map(x -> WatchBeanToWatch.apply(x)).collect(toList()));

			return forum;
		}
	};

	Function<ForumInstance, ForumInstanceBean> ForumInstanceToForumInstanceBean = new Function<ForumInstance, ForumInstanceBean>() {

		public ForumInstanceBean apply(ForumInstance t) {
			ForumInstanceBean forumInstance = new ForumInstanceBean();
			forumInstance.setCategories(
					t.getCategories().stream().map(x -> CategoryToCategoryBean.apply(x)).collect(toList()));
			forumInstance.setId(t.getId());
			forumInstance.setName(t.getName());

			return forumInstance;
		}
	};

	Function<ForumInstanceBean, ForumInstance> ForumInstanceBeanToForumInstance = new Function<ForumInstanceBean, ForumInstance>() {

		public ForumInstance apply(ForumInstanceBean t) {
			ForumInstance forumInstance = new ForumInstance();
			forumInstance.setCategories(
					t.getCategories().stream().map(x -> CategoryBeanToCategory.apply(x)).collect(toList()));
			forumInstance.setId(t.getId());
			forumInstance.setName(t.getName());

			return forumInstance;
		}
	};

	Function<ForumWatch, ForumWatchBean> ForumWatchToForumWatchBean = new Function<ForumWatch, ForumWatchBean>() {

		public ForumWatchBean apply(ForumWatch t) {
			ForumWatchBean forumWatch = new ForumWatchBean();
			forumWatch.setForum(ForumToForumBean.apply(t.getForum()));
			forumWatch.setMode(t.getMode());
			forumWatch.setPoster(PosterToPosterBean.apply(t.getPoster()));

			return forumWatch;
		}
	};

	Function<ForumWatchBean, ForumWatch> ForumWatchBeanToForumWatch = new Function<ForumWatchBean, ForumWatch>() {

		public ForumWatch apply(ForumWatchBean t) {
			ForumWatch forumWatch = new ForumWatch();
			forumWatch.setForum(ForumBeanToForum.apply(t.getForum()));
			forumWatch.setMode(t.getMode());
			forumWatch.setPoster(PosterBeanToPoster.apply(t.getPoster()));

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
			PollBean poll = new PollBean(t.getTitle(),
					t.getOptions().stream().map(x -> PollOptionToPollOptionBean.apply(x)).collect(toList()),
					t.getLength());
			poll.setCreationDate(t.getCreationDate());
			poll.setTitle(t.getTitle());

			return poll;
		}
	};

	Function<PollBean, Poll> PollBeanToPoll = new Function<PollBean, Poll>() {

		public Poll apply(PollBean t) {
			Poll poll = new Poll(t.getTitle(),
					t.getOptions().stream().map(x -> PollOptionBeanToPollOption.apply(x)).collect(toList()),
					t.getLength());
			poll.setCreationDate(t.getCreationDate());
			poll.setTitle(t.getTitle());

			return poll;
		}
	};

	Function<PollOption, PollOptionBean> PollOptionToPollOptionBean = new Function<PollOption, PollOptionBean>() {

		public PollOptionBean apply(PollOption t) {
			PollOptionBean pollOption = new PollOptionBean(t.getQuestion());
			pollOption.setPoll(PollToPollBean.apply(t.getPoll()));
			pollOption.setPollOptionPosition(t.getPollOptionPosition());
			pollOption.setVotes(t.getVotes());

			return pollOption;
		}
	};

	Function<PollOptionBean, PollOption> PollOptionBeanToPollOption = new Function<PollOptionBean, PollOption>() {

		public PollOption apply(PollOptionBean t) {
			PollOption pollOption = new PollOption(t.getQuestion());
			pollOption.setPoll(PollBeanToPoll.apply(t.getPoll()));
			pollOption.setPollOptionPosition(t.getPollOptionPosition());
			pollOption.setVotes(t.getVotes());

			return pollOption;
		}
	};

	Function<PollVoted, PollVotedBean> PollVotedToPollVotedBean = new Function<PollVoted, PollVotedBean>() {

		public PollVotedBean apply(PollVoted t) {
			PollVotedBean pollVoted = new PollVotedBean();
			pollVoted.setPoll(PollToPollBean.apply(t.getPoll()));
			pollVoted.setPoll2(t.getPk().getPoll2());
			pollVoted.setPollVoted(t.getPk().getPollVoted());

			return pollVoted;
		}
	};

	Function<PollVotedBean, PollVoted> PollVotedBeanToPollVoted = new Function<PollVotedBean, PollVoted>() {

		public PollVoted apply(PollVotedBean t) {
			PollVoted pollVoted = new PollVoted();
			pollVoted.setPoll(PollBeanToPoll.apply(t.getPoll()));
			PollVotedPK pollVotedPK = new PollVotedPK();
			pollVotedPK.setPoll2(t.getPoll2());
			pollVotedPK.setPollVoted(t.getPollVoted());
			pollVoted.setPk(pollVotedPK);

			return pollVoted;
		}
	};

	Function<Post, PostBean> PostToPostBean = new Function<Post, PostBean>() {

		public PostBean apply(Post t) {
			PostBean post = new PostBean(TopicToTopicBean.apply(t.getTopic()), t.getMessage().getText(),
					t.getAttachments().stream().map(x -> AttachmentToAttachmentBean.apply(x)).collect(toList()));
			post.setCreateDate(t.getCreateDate());
			post.setEditCount(t.getEditCount());
			post.setEditDate(t.getEditDate());
			post.setId(t.getId());
			post.setMessage(MessageToMessageBean.apply(t.getMessage()));
			post.setPoster(PosterToPosterBean.apply(t.getPoster()));
			post.setUser(t.getUser());

			return post;
		}
	};

	Function<PostBean, Post> PostBeanToPost = new Function<PostBean, Post>() {

		public Post apply(PostBean t) {
			Post post = new Post(TopicBeanToTopic.apply(t.getTopic()), t.getMessage().getText(),
					t.getAttachments().stream().map(x -> AttachmentBeanToAttachment.apply(x)).collect(toList()));
			post.setCreateDate(t.getCreateDate());
			post.setEditCount(t.getEditCount());
			post.setEditDate(t.getEditDate());
			post.setId(t.getId());
			post.setMessage(MessageBeanToMessage.apply(t.getMessage()));
			post.setPoster(PosterBeanToPoster.apply(t.getPoster()));
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
			TopicBean topic = new TopicBean(ForumToForumBean.apply(t.getForum()), t.getSubject(),
					t.getPosts().stream().map(x -> PostToPostBean.apply(x)).collect(toList()), t.getType(),
					PollToPollBean.apply(t.getPoll()));
			topic.setId(t.getId());
			topic.setLastPostDate(t.getLastPostDate());
			topic.setPoster(PosterToPosterBean.apply(t.getPoster()));
			topic.setReplies(t.getReplies());
			topic.setStatus(t.getStatus());
			topic.setViewCount(t.getViewCount());
			topic.setWatches(t.getWatches().stream().map(x -> WatchToWatchBean.apply(x)).collect(toList()));

			return topic;
		}
	};

	Function<TopicBean, Topic> TopicBeanToTopic = new Function<TopicBean, Topic>() {

		public Topic apply(TopicBean t) {
			Topic topic = new Topic(ForumBeanToForum.apply(t.getForum()), t.getSubject(),
					t.getPosts().stream().map(x -> PostBeanToPost.apply(x)).collect(toList()), t.getType(),
					PollBeanToPoll.apply(t.getPoll()));
			topic.setId(t.getId());
			topic.setLastPostDate(t.getLastPostDate());
			topic.setPoster(PosterBeanToPoster.apply(t.getPoster()));
			topic.setReplies(t.getReplies());
			topic.setStatus(t.getStatus());
			topic.setViewCount(t.getViewCount());
			topic.setWatches(t.getWatches().stream().map(x -> WatchBeanToWatch.apply(x)).collect(toList()));

			return topic;
		}
	};

	Function<TopicWatch, TopicWatchBean> TopicWatchToTopicWatchBean = new Function<TopicWatch, TopicWatchBean>() {

		public TopicWatchBean apply(TopicWatch t) {
			TopicWatchBean topicWatch = new TopicWatchBean();
			topicWatch.setTopic(TopicToTopicBean.apply(t.getTopic()));
			topicWatch.setMode(t.getMode());
			topicWatch.setPoster(PosterToPosterBean.apply(t.getPoster()));

			return topicWatch;
		}
	};

	Function<TopicWatchBean, TopicWatch> TopicWatchBeanToTopicWatch = new Function<TopicWatchBean, TopicWatch>() {

		public TopicWatch apply(TopicWatchBean t) {
			TopicWatch topicWatch = new TopicWatch();
			topicWatch.setTopic(TopicBeanToTopic.apply(t.getTopic()));
			topicWatch.setMode(t.getMode());
			topicWatch.setPoster(PosterBeanToPoster.apply(t.getPoster()));

			return topicWatch;
		}
	};

	Function<Watch, WatchBean> WatchToWatchBean = new Function<Watch, WatchBean>() {

		public WatchBean apply(Watch t) {
			WatchBean watch = new WatchBean();
			watch.setMode(t.getMode());
			watch.setPoster(PosterToPosterBean.apply(t.getPoster()));

			return watch;
		}
	};

	Function<WatchBean, Watch> WatchBeanToWatch = new Function<WatchBean, Watch>() {

		public Watch apply(WatchBean t) {
			Watch watch = new Watch();
			watch.setMode(t.getMode());
			watch.setPoster(PosterBeanToPoster.apply(t.getPoster()));

			return watch;
		}
	};
}