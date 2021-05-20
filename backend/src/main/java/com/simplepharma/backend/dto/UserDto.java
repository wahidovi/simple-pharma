package com.simplepharma.backend.dto;


import com.simplepharma.backend.model.Address;
import com.simplepharma.backend.model.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserDto implements Serializable {

	private static final long serialVersionUID = 4744643015194204171L;
	private String status;
	private String message;
	private String AUTH_TOKEN;
	private User user;
	private Address address;
}