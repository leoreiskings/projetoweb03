package br.com.kingsdevs.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtSecurity extends OncePerRequestFilter {

	private final String HEADER = "Authorization";	
	private final String PREFIX = "Bearer ";	
	public static final String SECRET = "6ce32551-c9ee-4856-9a71-f1a251a49b0f";						

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)		throws ServletException, IOException {
		
		try {
			if (checkJWTToken(request, response)) {
				Claims claims = validateToken(request);
				if (claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				} else {
					SecurityContextHolder.clearContext();
				}
			} else {
				SecurityContextHolder.clearContext();
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			return;
		}
	}

	private Claims validateToken(HttpServletRequest request) {
			
			String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
			return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
			
	}

	private void setUpSpringAuthentication(Claims claims) {
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<String> authorities = (List) claims.get("authorities");
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,	 authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);
		
	}

	private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
		
		String authenticationHeader = request.getHeader(HEADER);
		
		if (authenticationHeader == null 
				|| !authenticationHeader.startsWith(PREFIX))
				return false;
		return true;
	}
}
