package com.goodbuy.store.configuration;

import com.goodbuy.store.dao.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration implements WebMvcConfigurer {
	@Autowired
	@Qualifier("productInterceptor")
	private HandlerInterceptor ProductAdminOnlyRouteInterceptor;
	@Autowired
	@Qualifier("userInterceptor")
	private HandlerInterceptor UserAdminOnlyRouteInterceptor;
	@Autowired
	@Qualifier("orderInterceptor")
	private HandlerInterceptor OrderAdminOnlyRouteInterceptor;
	private final UserDAO userDAO;

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userDAO.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(ProductAdminOnlyRouteInterceptor).addPathPatterns("/api/v1/products/","/api/v1/products/{id}");
		registry.addInterceptor(UserAdminOnlyRouteInterceptor).addPathPatterns("/api/v1/users/**").excludePathPatterns("/api/v1/users/profile");
		registry.addInterceptor(OrderAdminOnlyRouteInterceptor).addPathPatterns("/api/v1/orders/", "/api/v1/orders/{id}/deliver").excludePathPatterns("/api/v1/orders/myorders","/api/v1/orders/:id");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/v1/**");
	}

}
