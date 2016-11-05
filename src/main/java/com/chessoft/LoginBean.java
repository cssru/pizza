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
package com.chessoft;


import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

import com.chessoft.constants.Outcome;
import com.chessoft.dao.LoginDao;
import com.chessoft.dao.impl.LoginDaoImpl;
import com.chessoft.entity.User;
import com.chessoft.util.SessionUtils;

@Getter
@Setter
@SessionScoped
@ManagedBean
public class LoginBean {
	
	private String login;
	private String password;
	
	private LoginDao loginDao;
	
	public LoginBean() {
		// in future Dependency Injection for "loginDao" field may be used
		this.loginDao = new LoginDaoImpl();
		this.login = StringUtils.EMPTY;
		this.password = StringUtils.EMPTY;
	}
	
	public String authenticate() {
		if (checkCredentials()) {
			SessionUtils
				.getSession(false)
				.setAttribute("login", login);
			return Outcome.OUTCOME_ORDERS;
		} else {
			FacesContext
				.getCurrentInstance()
				.addMessage(
					"password",
					new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"Неправильное имя пользователя или пароль",
						null
					)
				);
			return Outcome.OUTCOME_LOGIN;
		}
	}
	
	private boolean checkCredentials() {
		User user;
		try {
			user = loginDao.findUser(login);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		if (user == null) {
			return false;
		}
		
		return StringUtils.equals(password, user.getPassword());
	}
}
