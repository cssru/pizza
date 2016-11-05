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

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import com.chessoft.dao.OrderDao;
import com.chessoft.entity.Order;
import com.chessoft.util.ConnectionUtils;
import com.chessoft.util.DbUtils;

public class OrderDaoImpl implements OrderDao {
	public void create(Order order) throws SQLException, ClassNotFoundException {
		Connection connection = ConnectionUtils.getConnection();
		
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO orders (customer, address, order_date, state) VALUES (?,?,?,?);");
		
		stmt.setString(1, order.getCustomer());
		stmt.setString(2, order.getAddress());
		stmt.setTimestamp(
			3,
			new Timestamp(order.getDate().getTime())
		);
		stmt.setString(
			4,
			order.getState().name()
		);
		
		stmt.executeUpdate();
		
		stmt.close();
		connection.close();
	}
	
	public List<Order> list() throws SQLException, ClassNotFoundException {
		Connection connection = ConnectionUtils.getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT id, customer, address, order_date, state FROM orders;");
		
		ResultSet rs = stmt.executeQuery();
		List<Order> result = new LinkedList<>();
		while (rs.next()) {
			result.add(DbUtils.createOrder(rs));
		}
		
		rs.close();
		stmt.close();
		connection.close();
		return result;
	}
	
	public void update(Order order) throws SQLException, ClassNotFoundException {
		Connection connection = ConnectionUtils.getConnection();
		
		PreparedStatement stmt = connection.prepareStatement("UPDATE orders SET customer=?, address=?, order_date=?, state=? WHERE id=?;");
		
		stmt.setString(1, order.getCustomer());
		stmt.setString(2, order.getAddress());
		stmt.setTimestamp(
			3,
			new Timestamp(order.getDate().getTime())
		);
		stmt.setString(
			4,
			order.getState().name()
		);
		stmt.setInt(5, order.getId());
		stmt.executeUpdate();
		
		stmt.close();
		connection.close();
	}
}
