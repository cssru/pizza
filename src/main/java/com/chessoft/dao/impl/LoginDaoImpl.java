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
package com.chessoft.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.chessoft.dao.LoginDao;
import com.chessoft.entity.User;
import com.chessoft.util.ConnectionUtils;

public class LoginDaoImpl implements LoginDao {
	
	@Override
	public User findUser(String login) throws SQLException, ClassNotFoundException {
		Connection connection = ConnectionUtils.getConnection();
		
		PreparedStatement stmt = connection.prepareStatement("SELECT login, password FROM users where login = ?");
		stmt.setString(1, login);
		
		User user = null;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			user = new User(rs.getString("login"), rs.getString("password"));
		}
		rs.close();
		connection.close();
		return user;
	}
	
}
