package io.antmedia.test.filter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import io.antmedia.AppSettings;
import io.antmedia.filter.IPFilter;
import io.antmedia.filter.JWTFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTFilterTest {
	
	protected static Logger logger = LoggerFactory.getLogger(JWTFilterTest.class);
	
    @Test
    public void testDoFilterPass() throws IOException, ServletException {
    	   	
    	JWTFilter jwtFilter = Mockito.spy(new JWTFilter());
        
        MockHttpServletResponse httpServletResponse;
        MockHttpServletRequest httpServletRequest;
        MockFilterChain filterChain;
        
        AppSettings appSettings = new AppSettings();
        appSettings.setRemoteAllowedCIDR("127.0.0.1/8");
        appSettings.setJwtSecretKey("testtesttesttesttesttesttesttesttesttest");
        appSettings.setJwtControlEnabled(true);
        
        Mockito.doReturn(appSettings).when(jwtFilter).getAppSettings();
        
		SecretKey key = Keys.hmacShaKeyFor(appSettings.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
		SecretKey invalidKey = Keys.hmacShaKeyFor("invalid-key-invalid-key-invalid-key".getBytes(StandardCharsets.UTF_8));
		
		String token = Jwts.builder().setSubject("token").signWith(key).compact();
		String invalidToken = Jwts.builder().setSubject("token").signWith(invalidKey).compact();
		
		String nullToken = null;
        
        // JWT Token enable and invalid token scenario
        {   
        	//reset filterchain
        	filterChain = new MockFilterChain();
        	
        	//reset httpServletResponse
        	httpServletResponse = new MockHttpServletResponse();
        	
        	//reset httpServletRequest
        	httpServletRequest = new MockHttpServletRequest();
        	
            appSettings.setJwtSecretKey("testtesttesttesttesttesttesttesttesttest");
            appSettings.setJwtControlEnabled(true);
            
            Mockito.doReturn(appSettings).when(jwtFilter).getAppSettings();
            
            httpServletRequest.addHeader("jwtToken", invalidToken);

            jwtFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
            assertEquals(HttpStatus.FORBIDDEN.value(),httpServletResponse.getStatus());
            
        }
        
        // JWT Token disable and passed token scenario
        {
        	//reset filterchains
        	filterChain = new MockFilterChain();
        	
        	//reset httpServletResponses
        	httpServletResponse = new MockHttpServletResponse();
        	
        	//reset httpServletRequest
        	httpServletRequest = new MockHttpServletRequest();
        	
           // appSettings.setRemoteAllowedCIDR("127.0.0.1/8");
            appSettings.setJwtSecretKey("testtesttesttesttesttesttesttesttesttest");
            appSettings.setJwtControlEnabled(false);
            
            Mockito.doReturn(appSettings).when(jwtFilter).getAppSettings();
            
            httpServletRequest.addHeader("jwtToken", token);
            
            jwtFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
            assertEquals(HttpStatus.OK.value(),httpServletResponse.getStatus());
            
        }
        
        // JWT Token enable and invalid token scenario
        {
        	//reset filterchains
        	filterChain = new MockFilterChain();
        	
        	//reset httpServletResponses
        	httpServletResponse = new MockHttpServletResponse();
        	
        	//reset httpServletRequest
        	httpServletRequest = new MockHttpServletRequest();
        	
            appSettings.setJwtSecretKey("testtesttesttesttesttesttesttesttesttest");
            appSettings.setJwtControlEnabled(true);
            
            Mockito.doReturn(appSettings).when(jwtFilter).getAppSettings();
            
            httpServletRequest.addHeader("jwtToken", invalidToken);
            
            jwtFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
            assertEquals(HttpStatus.FORBIDDEN.value(),httpServletResponse.getStatus());
        }
        
        // JWT Token enable and invalid token scenario
        {
        	//reset filterchains
        	filterChain = new MockFilterChain();
        	
        	//reset httpServletResponses
        	httpServletResponse = new MockHttpServletResponse();
        	
        	//reset httpServletRequest
        	httpServletRequest = new MockHttpServletRequest();
        	
            appSettings.setJwtSecretKey("testtesttesttesttesttesttesttesttesttest");
            appSettings.setJwtControlEnabled(true);
            
            Mockito.doReturn(appSettings).when(jwtFilter).getAppSettings();
            
            jwtFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
            assertEquals(HttpStatus.FORBIDDEN.value(),httpServletResponse.getStatus());
        }
        
    }

}