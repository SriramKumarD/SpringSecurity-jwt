package com.srird.springsecurityjwt.resource;

import java.net.Authenticator.RequestorType;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.srird.springsecurityjwt.model.AuthenticationRequest;
import com.srird.springsecurityjwt.model.AuthenticationResponse;
import com.srird.springsecurityjwt.security.service.MyUserDetailsService;
import com.srird.springsecurityjwt.util.JwtUtil;

@RestController
public class HelloController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
 
	@RequestMapping("/hello")
	public String firstPage() {
		return "Hello World";
	}
	
	
	@RequestMapping(value ="/authentication", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassWord()));
		}
		catch(BadCredentialsException e) {
			throw new Exception("Incorrect UserName and Password....", e);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUserName());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
