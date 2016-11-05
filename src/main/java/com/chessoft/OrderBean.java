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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.myfaces.tobago.model.SheetState;

import lombok.Getter;
import lombok.Setter;

import com.chessoft.constants.Outcome;
import com.chessoft.constants.UI;
import com.chessoft.dao.OrderDao;
import com.chessoft.dao.impl.OrderDaoImpl;
import com.chessoft.entity.Order;
import com.chessoft.entity.enums.OrderState;

@Getter
@Setter
@SessionScoped
@ManagedBean
public class OrderBean {
	
	private OrderDao orderDao;
	private SheetState selectedOrders;
	private boolean renderOrderEditor;
	private Order currentOrder;
	private Converter orderStateConverter = new OrderStateConverter();
	
	public OrderBean() {
		this.orderDao = new OrderDaoImpl();
	}
	
	public static String getReadableStateName(OrderState state) {
		switch (state) {
			case NEW:
				return UI.UI_NEW_ORDER_STATE;
			case EXECUTION:
				return UI.UI_EXECUTION_ORDER_STATE;
			case DONE:
				return UI.UI_DONE_ORDER_STATE;
		}
		return null;
	}
	
	public List<Order> getOrderList() {
		try {
			return orderDao.list();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	public String addOrder() {
		currentOrder = new Order();
		currentOrder.setId(-1);
		currentOrder.setDate(new Date());
		
		setRenderOrderEditor(true);
		return Outcome.OUTCOME_ORDERS;
	}
	
	public String editOrder() {
		final List<Integer> selection = selectedOrders.getSelectedRows();
		if (selection.size() != 1) {
			return null;
		}
		currentOrder = getOrderList().get(selection.get(0));
		setRenderOrderEditor(true);
		return Outcome.OUTCOME_ORDERS;
	}
	
	public String save() {
		try {
			if (currentOrder.getId() < 0) {
				orderDao.create(currentOrder);
			} else {
				orderDao.update(currentOrder);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		setRenderOrderEditor(false);
		return Outcome.OUTCOME_ORDERS;
	}
	
	public String cancel() {
		setRenderOrderEditor(false);
		return Outcome.OUTCOME_ORDERS;
	}
	
	public OrderState[] getOrderStates() {
		return OrderState.values();
	}
	
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}
	
	private static class OrderStateConverter implements Converter {
		
		@Override
		public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
			switch (value) {
				case UI.UI_NEW_ORDER_STATE:
					return OrderState.NEW;
				case UI.UI_EXECUTION_ORDER_STATE:
					return OrderState.EXECUTION;
				case UI.UI_DONE_ORDER_STATE:
					return OrderState.DONE;
			}
			return null;
		}
		
		@Override
		public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
			if (!(value instanceof OrderState)) {
				return null;
			}
			return getReadableStateName((OrderState) value);
		}
	}
}
