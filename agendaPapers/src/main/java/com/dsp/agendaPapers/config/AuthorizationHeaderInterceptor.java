/**
* 
* @author Mrinal Lakhanpal
* @version 1.0
*/
package com.dsp.agendaPapers.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHeaderInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		// Fetch the token dynamically (for example from SecurityContext or custom
		// context)
		String token = fetchTokenFromContext();
		System.out.println("### token - " + token);

		if (token != null && !token.isEmpty()) {
			System.out.println("Inside if - " + request.getHeaders());
			System.out.println("### request - " + request);
			// Add the Authorization header with Bearer token
			request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		}

		return execution.execute(request, body);
	}

	// You can implement this method to fetch the token from your context
	private String fetchTokenFromContext() {
		// For example, using Spring Security's SecurityContext:
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("### authentication - " + authentication);
		if (authentication != null && authentication.getCredentials() != null) {
			return (String) authentication.getCredentials(); // Assuming the token is stored in credentials
		}
		return null; // Return null if no token is found
	}
}