package com.simplepharma.backend.service;

import com.simplepharma.backend.model.User;
import com.simplepharma.backend.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

	private static final Logger logger = LogManager.getLogger(MyUserDetailService.class);

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

		Optional<User> user = userRepository.findByUsername(s);
		logger.info("finding user");
		user.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return user.map(MyUserDetails::new).get();
	}

}
