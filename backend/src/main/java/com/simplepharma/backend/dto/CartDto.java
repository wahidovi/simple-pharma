package com.simplepharma.backend.dto;

import com.simplepharma.backend.model.Cart;
import lombok.Data;

import java.util.List;

@Data
public class CartDto {
	private String status;
	private String message;
	private String AUTH_TOKEN;
	private List<Cart> oblist;
}