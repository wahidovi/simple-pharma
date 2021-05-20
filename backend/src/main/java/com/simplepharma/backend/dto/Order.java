package com.simplepharma.backend.dto;


import com.simplepharma.backend.model.Cart;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
	private int orderId;
	private String orderBy;
	private String orderStatus;
	private List<Cart> products = new ArrayList<>();
}