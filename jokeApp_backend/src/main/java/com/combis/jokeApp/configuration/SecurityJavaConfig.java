package com.combis.jokeApp.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.combis.jokeApp.services.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter{

	RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	MySavedRequestAwareAuthenticationSuccessHandler success;
	MySavedRequestAwareAuthenticationFailureHandler failure;
	MyUserDetailsService myUserDetailsService;
	@Autowired
	SecurityJavaConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
						MySavedRequestAwareAuthenticationSuccessHandler success,
						MySavedRequestAwareAuthenticationFailureHandler failure,
						MyUserDetailsService myUserDetailsService){
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
		this.success = success;
		this.failure = failure;
		this.myUserDetailsService = myUserDetailsService;
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public PasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(encoder());
		daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
		return daoAuthenticationProvider;
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { 
		http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);
	    http
	    .cors().and().csrf().disable()
	    .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
	    .and()
	    .authorizeRequests()
	    .antMatchers("/jokes/**", "/actuator/**").authenticated()//.permitAll()
	    .antMatchers("/users/**").permitAll()
	    .and().formLogin().defaultSuccessUrl("/users", true)
	    .successHandler(success)
	    .failureHandler(failure)
        .and().logout().logoutSuccessUrl("/");
	}
	
	@Configuration
    @Order(2)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
        }

        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
	            .antMatchers("/jokes/**", "/actuator/**").authenticated()//.permitAll()
	    	    .antMatchers("/users/**").permitAll()
                .and()
                .httpBasic();
        }
    }
}


