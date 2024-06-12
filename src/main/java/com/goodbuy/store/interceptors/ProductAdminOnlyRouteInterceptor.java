package com.goodbuy.store.interceptors;

import com.goodbuy.store.utils.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@Qualifier("productInterceptor")
public class ProductAdminOnlyRouteInterceptor implements HandlerInterceptor {
	@Autowired
	private JwtService jwtService;
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
		if (request.getMethod().equals("GET") || request.getMethod().equals("OPTIONS")) {
			return true;
		}
		if(request.getHeader("Authorization") != null) {
			final String authorizationHeader = request.getHeader("Authorization");
			final String token = authorizationHeader.substring(7);
			Claims claims = jwtService.extractClaims(token);
			if(claims.containsKey("isAdmin") && claims.get("isAdmin").equals(true)) {
				return true;
			}
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return false;
	}
}
