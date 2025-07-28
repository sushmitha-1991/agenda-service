package com.dsp.agendaPapers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dsp.agendaPapers.dto.UserDetailsDTO;
import com.dsp.agendaPapers.entity.BaseUser;
import com.dsp.agendaPapers.repository.MyUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private MyUserRepository myUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		BaseUser baseUser = myUserRepository.findByUsername(username);
		if (baseUser == null) {
			throw new UsernameNotFoundException("User not found");
		}

		// Create a UserDetailsDTO object from BaseUser
		return new UserDetailsDTO(baseUser.getId(), baseUser.getUsername(), baseUser.getPhone(), baseUser.getEmail(),
				baseUser.getPassword(), baseUser.getRole(), baseUser.getHouseId(), baseUser.getIsActive());
	}
}
