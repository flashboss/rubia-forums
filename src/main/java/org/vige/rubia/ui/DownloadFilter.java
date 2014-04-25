/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.vige.rubia.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vige.rubia.ForumsModule;
import org.vige.rubia.model.Attachment;

/**
 * @author sohil shah
 */
public class DownloadFilter implements Filter {
	/**
     * 
     */
	private final static String WRONG_REQ_RESP = "Error accessing the requested resource.";

	@Inject
	private ForumsModule forumsModule;

	/**
     * 
     */
	public void init(FilterConfig conf) {
	}

	/**
     * 
     */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException {
		try {
			if ((request instanceof HttpServletRequest)
					&& (response instanceof HttpServletResponse)) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				// get this attachment data
				int attachmentId = Integer.parseInt(request.getParameter("id"));
				Attachment attachment = forumsModule
						.findFindAttachmentById(attachmentId);

				// set the attachment headers
				httpResponse.setContentLength((int) attachment.getSize());
				httpResponse.setContentType(attachment.getContentType());
				httpResponse.setHeader("Content-Disposition",
						"attachment; filename=" + attachment.getName());

				// now send the actual content down
				InputStream is = new ByteArrayInputStream(
						attachment.getContent());
				OutputStream os = httpResponse.getOutputStream();
				transferBytes(is, os);
				os.flush();

				// cleanup
				if (is != null) {
					is.close();
				}
			} else {
				response.setContentType("text/html");
				response.getWriter().write(WRONG_REQ_RESP);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void transferBytes(InputStream srcStream,
			OutputStream destStream) throws IOException {
		try {
			byte[] buffer = new byte[1024];
			int read = -1;
			while ((read = srcStream.read(buffer)) != -1) {
				destStream.write(buffer, 0, read);
			}
		} finally {
			if (srcStream != null) {
				srcStream.close();
			}
			if (destStream != null) {
				destStream.close();
			}
		}
	}

	/**
     * 
     */
	public void destroy() {
	}
}
