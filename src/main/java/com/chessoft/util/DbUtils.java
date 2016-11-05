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
package com.chessoft.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.chessoft.entity.Order;
import com.chessoft.entity.enums.OrderState;

public class DbUtils {
	private DbUtils() {
		// utilities class
	}
	
	public static Order createOrder(ResultSet rs) throws SQLException {
		return new Order(
			rs.getInt("id"),
			rs.getString("customer"),
			rs.getString("address"),
			new Date(rs.getTimestamp("order_date").getTime()),
			OrderState.valueOf(rs.getString("state"))
		);
	}
}
