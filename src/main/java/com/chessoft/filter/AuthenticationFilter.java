/*
 * Copyright (C) 2016 Sergey Chechenev <cssru@mail.ru>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.chessoft.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

@WebFilter(
	filterName = "authenticationFilter",
	urlPatterns = {"*.xhtml"}
)
public class AuthenticationFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void doFilter(
		ServletRequest request, ServletResponse response, FilterChain chain
	) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		String uri = httpRequest.getRequestURI();
		
		if (StringUtils.contains(uri, "/login.xhtml")
			|| (session != null && session.getAttribute("login") != null)
			|| StringUtils.contains(uri, "javax.faces.resource")
			|| StringUtils.contains(uri, "org/apache/myfaces/tobago/renderkit")
			) {
			chain.doFilter(request, response);
		} else {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
		}
	}
	
	@Override
	public void destroy() {
		
	}
}
