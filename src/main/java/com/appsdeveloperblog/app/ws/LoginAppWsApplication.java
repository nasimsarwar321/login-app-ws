package com.appsdeveloperblog.app.ws;

import javax.servlet.http.HttpServlet;

import com.appsdeveloperblog.app.ws.service.EmailService;
import com.appsdeveloperblog.app.ws.ui.controller.UserController;
import com.appsdeveloperblog.app.ws.ui.model.request.MailRequest;
import com.appsdeveloperblog.app.ws.ui.model.response.MailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.security.AppProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class LoginAppWsApplication {



	public static void main(String[] args) {
		SpringApplication.run(LoginAppWsApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean 
	public SpringApplicationContext springApplicationContext()
	{
		return new SpringApplicationContext();
	}
	
	@Bean(name="AppProperties")
	public AppProperties getAppProperties()
	{
		return new AppProperties();
	}

	/*@Bean
	public  UserController userController(){
		return new UserController();
	}
	*/
}
