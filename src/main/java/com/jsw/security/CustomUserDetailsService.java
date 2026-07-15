package com.jsw.security;


import com.jsw.entity.AppUser;
import com.jsw.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with username: "+username));

        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(appUser.getRole()))
        );
    }

}
