package com.simplepharma.backend.controller;


import com.simplepharma.backend.constants.ResponseCode;
import com.simplepharma.backend.constants.WebConstants;
import com.simplepharma.backend.dto.ServerDto;
import com.simplepharma.backend.exception.UserCustomException;
import com.simplepharma.backend.model.User;
import com.simplepharma.backend.repository.UserRepository;
import com.simplepharma.backend.service.MyUserDetailService;
import com.simplepharma.backend.util.JwtUtil;
import com.simplepharma.backend.util.Validator;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

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
	public ResponseEntity<ServerDto> createAuthToken(@RequestBody HashMap<String, String> credential) {

		final String email = credential.get(WebConstants.USER_EMAIL);
		final String password = credential.get(WebConstants.USER_PASSWORD);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			throw new UserCustomException("Invalid User Credentials");
		}
		final UserDetails userDetails = userDetailService.loadUserByUsername(email);
		final String jwt = jwtutil.generateToken(userDetails);

		ServerDto resp = new ServerDto();
		resp.setStatus(ResponseCode.SUCCESS_CODE);
		resp.setMessage(ResponseCode.SUCCESS_MESSAGE);
		resp.setAuthToken(jwt);

		if (userDetails != null
				&& userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			resp.setUserType("ADMIN");
		} else {
			resp.setUserType("CUSTOMER");
		}

		return new ResponseEntity<ServerDto>(resp, HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<ServerDto> addUser(@RequestBody User user) {

		ServerDto resp = new ServerDto();
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
				user.setUserType("CUSTOMER");
				userRepo.save(user);
			}
		} catch (Exception e) {
			throw new UserCustomException("An error occurred while saving user, please check details or try again");
		}
		return new ResponseEntity<ServerDto>(resp, HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/logout")
	public void logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
	}
}
