package com.simplepharma.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServerDto {
	private String status;
	private String message;
	private String authToken;
	private String userType;
}
