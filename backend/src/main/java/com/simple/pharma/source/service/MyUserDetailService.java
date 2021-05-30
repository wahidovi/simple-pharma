package com.simple.pharma.source.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.simple.pharma.source.model.User;
import com.simple.pharma.source.repository.UserRepository;

@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findByUsername(s);

		user.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return user.map(MyUserDetails::new).get();
	}

}
