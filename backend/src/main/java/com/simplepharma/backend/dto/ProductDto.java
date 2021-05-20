package com.simplepharma.backend.dto;


import com.simplepharma.backend.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductDto {
	private String status;
	private String message;
	private String AUTH_TOKEN;
	private List<Product> oblist;
}