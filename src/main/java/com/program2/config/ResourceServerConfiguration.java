package com.program2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * @author mitradev
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{

	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http
		.authorizeRequests()
//		.antMatchers("/merchants").permitAll()
//		.antMatchers("/merchants/reset/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.exceptionHandling()
		.accessDeniedHandler(new OAuth2AccessDeniedHandler());
		
//		http
//		.authorizeRequests()
//		.antMatchers("/merchants").permitAll()
//		.antMatchers("/**").permitAll()
//		.anyRequest().authenticated()
//		.and()
//		.exceptionHandling()
//		.accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
	
}

