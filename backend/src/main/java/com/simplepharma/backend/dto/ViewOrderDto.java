package com.simplepharma.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class ViewOrderDto {
	private String status;
	private String message;
	private String AUTH_TOKEN;
	private List<Order> orderlist = new ArrayList<>();
}
