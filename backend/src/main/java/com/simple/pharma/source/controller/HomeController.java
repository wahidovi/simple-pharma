package com.simple.pharma.source.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simple.pharma.source.constants.ResponseCode;
import com.simple.pharma.source.constants.WebConstants;
import com.simple.pharma.source.exception.UserCustomException;
import com.simple.pharma.source.model.User;
import com.simple.pharma.source.response.ServerResponse;
import com.simple.pharma.source.service.MyUserDetailService;
import com.simple.pharma.source.util.JwtUtil;
import com.simple.pharma.source.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.pharma.source.repository.UserRepository;

@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailService userDetailService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtil jwtutil;

	@PostMapping("/auth")
	public ResponseEntity<ServerResponse> createAuthToken(@RequestBody HashMap<String, String> credential) {

		final String email = credential.get(WebConstants.USER_EMAIL);
		final String password = credential.get(WebConstants.USER_PASSWORD);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			throw new UserCustomException("Invalid User Credentials");
		}
		final UserDetails userDetails = userDetailService.loadUserByUsername(email);
		final String jwt = jwtutil.generateToken(userDetails);

		ServerResponse resp = new ServerResponse();
		resp.setStatus(ResponseCode.SUCCESS_CODE);
		resp.setMessage(ResponseCode.SUCCESS_MESSAGE);
		resp.setAuthToken(jwt);

		if (userDetails != null
				&& userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			resp.setUserType("ADMIN");
		} else {
			resp.setUserType("CUSTOMER");
		}

		return new ResponseEntity<ServerResponse>(resp, HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<ServerResponse> addUser(@RequestBody User user) {

		ServerResponse resp = new ServerResponse();
		try {
			if (Validator.isUserEmpty(user)) {
				resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
				resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
			} else if (!Validator.isValidEmail(user.getEmail())) {
				resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
				resp.setMessage(ResponseCode.INVALID_EMAIL_FAIL_MSG);
			} else {
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.CUST_REG);
				user.setEnabled(true);
				userRepo.save(user);
			}
		} catch (Exception e) {
			throw new UserCustomException("An error occured while saving user, please check details or try again");
		}
		return new ResponseEntity<ServerResponse>(resp, HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/logout")
	public void logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
	}
}
