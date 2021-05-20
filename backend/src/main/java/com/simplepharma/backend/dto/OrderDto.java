package com.simplepharma.backend.dto;


import com.simplepharma.backend.model.Cart;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderDto {
	private int orderId;
	private List<Cart> cartList;
}