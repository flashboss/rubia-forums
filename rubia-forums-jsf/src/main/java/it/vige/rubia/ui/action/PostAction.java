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
package it.vige.rubia.ui.action;

import static it.vige.rubia.model.TopicType.NORMAL;
import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.ForumUtil.getSDF;
import static it.vige.rubia.ui.JSFUtil.getRequestParameter;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static it.vige.rubia.ui.action.MessageValidationException.INVALID_POST_SUBJECT;
import static it.vige.rubia.ui.action.MessageValidationException.INVALID_POST_TEXT;
import static it.vige.rubia.ui.action.PollValidationException.INVALID_POLL_OPTION;
import static it.vige.rubia.ui.action.PollValidationException.INVALID_POLL_TITLE;
import static it.vige.rubia.ui.action.PollValidationException.TOO_FEW_POLL_OPTION;
import static it.vige.rubia.ui.action.PollValidationException.TOO_MANY_POLL_OPTION;
import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.interceptor.Interceptors;

import org.richfaces.component.UIFileUpload;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.model.Attachment;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.TopicType;
import it.vige.rubia.ui.BaseController;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
public abstract class PostAction extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 295137515236191246L;

	@EJB
	private ForumsModule forumsModule;

	// post related view data
	protected String subject;
	protected String message;
	protected TopicType topicType;

	// poll related data view data
	protected String question;
	protected Map<String, String> options = new TreeMap<String, String>();
	protected int activeDuration;
	protected String option;

	// attachment related view data
	protected String attachmentComment;
	protected Collection<Attachment> attachments = new ArrayList<Attachment>();

	// navigation control related data
	protected boolean isPreview;
	protected int forumId = -1;
	protected int topicId = -1;
	protected int postId = -1;

	// business state

	// ui related data
	// accessors----------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return Returns the topicType.
	 */
	public TopicType getTopicType() {
		return topicType;
	}

	/**
	 * @param topicType
	 *            The topicType to set.
	 */
	public void setTopicType(TopicType topicType) {
		this.topicType = topicType;
	}

	/**
	 * 
	 * @return
	 */
	public String getPostDate() {
		String dateStr = null;

		SimpleDateFormat dateFormat = getSDF();

		dateStr = dateFormat.format(new Date());

		return dateStr;
	}

	/**
	 * 
	 * @return
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * 
	 * @param question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return Returns the activeDuration.
	 */
	public int getActiveDuration() {
		return activeDuration;
	}

	/**
	 * @param activeDuration
	 *            The activeDuration to set.
	 */
	public void setActiveDuration(int activeDuration) {
		this.activeDuration = activeDuration;
	}

	/**
	 * @return
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param
	 */
	public void setOption(String option) {
		this.option = option;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String addOption() {
		if (options == null) {
			options = new TreeMap<String, String>();
		}
		if (option != null && option.trim().length() > 0) {
			TreeMap<String, String> map = (TreeMap<String, String>) options;
			if (map.isEmpty()) {
				options.put("1", option);
			} else {
				options.put(Integer.toString(parseInt((String) map.lastKey()) + 1), option);
			}
		}
		option = null;
		return null;
	}

	public Map<String, String> getOptions() {
		if (options == null) {
			options = new TreeMap<String, String>();
		}
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	public Set<Map.Entry<String, String>> getEntrySet() {
		if (options == null) {
			options = new TreeMap<String, String>();
		}
		return options.entrySet();
	}

	/**
	 * @return Returns the attachmentComment.
	 */
	public String getAttachmentComment() {
		return attachmentComment;
	}

	/**
	 * @param attachmentComment
	 *            The attachmentComment to set.
	 */
	public void setAttachmentComment(String attachmentComment) {
		this.attachmentComment = attachmentComment;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumberOfAttachments() {
		int numberOfAttachments = 0;

		if (attachments != null) {
			numberOfAttachments = attachments.size();
		}

		return numberOfAttachments;
	}

	// -----navigation related
	// data---------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public boolean isPreview() {
		return isPreview;
	}

	/**
	 * 
	 *
	 */
	public boolean isPollPresent() {
		boolean isPollPresent = false;

		if (question != null && question.trim().length() > 0) {
			isPollPresent = true;
		}

		return isPollPresent;
	}

	/**
	 * 
	 *
	 */
	public int getForumId() {
		return forumId;
	}

	/**
	 * 
	 *
	 */
	public int getTopicId() {
		return topicId;
	}

	/**
	 * 
	 *
	 */
	public int getPostId() {
		return postId;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @author sshah since this controller is at session scope, must cleanup its
	 *         state when not needed this will help optimize memory usage...
	 *         puts the bean in an uninitialized state in that user's session
	 * 
	 */
	protected void cleanup() {
		// puts this controller in an cleaned up state for the next time it will
		// be used in the session
		subject = null;
		message = null;
		topicType = NORMAL;
		isPreview = false;
		forumId = -1;
		topicId = -1;
		postId = -1;

		// cleanup poll related data
		question = null;
		options = new TreeMap<String, String>();
		activeDuration = 0;

		// cleanup attachment related data
		attachmentComment = null;
		attachments = new ArrayList<Attachment>();
	}

	/**
	 * sets the poll information of a post for the ui from the business object
	 * 
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	protected void setupPoll(Poll poll) {
		if (poll != null) {
			question = poll.getTitle();
			activeDuration = poll.getLength();
			List<PollOption> pollOptions = poll.getOptions();
			if (pollOptions != null && pollOptions.size() > 0) {
				options = new TreeMap<String, String>();
				int counter = 1;
				for (PollOption cour : pollOptions) {
					options.put(Integer.toString(counter), cour.getQuestion());
					counter++;
				}
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @author sshah
	 * 
	 */
	public abstract String start();

	// poll related
	// operations--------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @author sshah
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String deleteOption() {
		String navState = null;
		try {
			String optionIndex = getParameter(p_option);
			options.remove(optionIndex);
			Iterator<String> it = options.keySet().iterator();
			Map<String, String> temporary = new TreeMap<String, String>();
			byte counter = 1;
			while (it.hasNext()) {
				temporary.put(Byte.toString(counter++), options.get(it.next()));
			}
			options = temporary;
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String updateOption() {
		String navState = null;
		try {
			String optionIndex = getParameter(p_option);
			String value = getParameter("post:option_" + optionIndex);
			options.put(optionIndex, value);
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	public void validatePoll(Poll poll) throws PollValidationException {
		if (poll.getOptions().size() > 10) {
			throw new PollValidationException(TOO_MANY_POLL_OPTION);
		}
		if (poll.getOptions().size() < 2) {
			throw new PollValidationException(TOO_FEW_POLL_OPTION);
		}
		if (poll.getTitle() == null || poll.getTitle().trim().length() == 0) {
			throw new PollValidationException(INVALID_POLL_TITLE);
		}
		for (PollOption option : poll.getOptions()) {
			if (option.getQuestion() == null || option.getQuestion().trim().length() == 0) {
				throw new PollValidationException(INVALID_POLL_OPTION);
			}
		}

	}

	// -----------------message
	// related------------------------------------------------------------------------------------------

	public void validateMessage(Message message) throws MessageValidationException {
		String subject = message.getSubject();
		if (subject == null || subject.trim().length() == 0) {
			throw new MessageValidationException(INVALID_POST_SUBJECT);
		}

		String text = message.getText();
		if (text == null || text.trim().length() == 0) {
			throw new MessageValidationException(INVALID_POST_TEXT);
		}
	}

	// -----------------attachment
	// related------------------------------------------------------------------------------------------

	/**
	 * 
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String addAttachment() {
		return "";
	}

	/**
	 * 
	 * @author sshah
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String deleteAttachment() {
		String navState = null;
		try {
			int attachmentIndex = parseInt(getParameter(p_attachment));

			attachments.remove(attachmentIndex);
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	/**
	 * 
	 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String updateAttachment() {
		return null;
	}

	// ------------preview-------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public String preview() {
		String navState = null;
		try {
			isPreview = true;
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}

	// ---------------------------cancel-----------------------------------------------------------------------------------------
	/**
	 * 
	 * @author sshah
	 * 
	 */
	public String cancel() {
		cleanup();
		return CANCEL;
	}

	// --------execute-------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public abstract String execute();

	// utility
	// methods-------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public Post getPost() throws Exception {
		Post post = null;

		post = forumsModule.findPostById(new Integer(getRequestParameter(p_postId)));

		return post;
	}

	public void upload(FileUploadEvent event) throws Exception {
		UploadedFile item = event.getUploadedFile();
		Attachment file = new Attachment();
		file.setComment(attachmentComment);
		file.setContent(item.getData());
		file.setContentType(item.getContentType());
		file.setName(item.getName());
		file.setSize(item.getSize());
		for (Attachment attachment : attachments)
			attachment.setPost(null);
		attachments.add(file);
	}

	public void clearUpload(AjaxBehaviorEvent event) throws Exception {
		@SuppressWarnings("unchecked")
		List<Attachment> attachments = (List<Attachment>) ((UIFileUpload) event.getSource()).getData();
		this.attachments.removeAll(attachments);
	}

	public void clearUpload(Attachment attachment) throws Exception {
		this.attachments.remove(attachment);
	}

	public void paint(OutputStream stream, Object object) throws IOException {
		stream.write(attachments.toArray(new Attachment[0])[(Integer) object].getContent());
		stream.close();
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}
}
