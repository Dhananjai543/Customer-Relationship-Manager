package com.dhananjai.crm.controller;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dhananjai.crm.authentication.AuthenticationRequest;
import com.dhananjai.crm.authentication.AuthenticationResponse;
import com.dhananjai.crm.authentication.AuthenticationService;
import com.dhananjai.crm.config.JwtService;
import com.dhananjai.crm.entity.User;
import com.dhananjai.crm.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
@Tag(name = "Login/Signup APIs", description = "APIs for logging and signup of users.")
public class LoginSignupController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private JwtService jwtService;
	
	@GetMapping("/")
	@Operation(summary = "Show the login form", description = "Displays the login form and clears any existing JWT cookies.")
	public String showLoginForm(Model theModel, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("inside");
		// Clear JWT cookie
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/"); // Ensure the path matches where the cookie was set
        jwtCookie.setMaxAge(0); // Set max age to 0 to delete the cookie

        // Add the expired cookie to the response
        response.addCookie(jwtCookie);
		return "login-form";
	}
	
	@PostMapping("/processLoginForm")
	@Operation(summary = "Process the login form", description = "Authenticates the user and sets a JWT cookie upon successful login.")
	public String processLoginForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			Model model, HttpServletRequest hrequest, HttpServletResponse response) {
		
		
		//--------
		
		AuthenticationRequest request = new AuthenticationRequest();
		request.setUsername(user.getUsername());
		request.setPassword(user.getPassword());
		System.out.println(request.toString());
		ResponseEntity<AuthenticationResponse> currResponse;
		try {
			currResponse = ResponseEntity.ok(authenticationService.login(request, response));
			System.out.println(currResponse.getBody().gettoken());
			
			Cookie jwtCookie = new Cookie("jwtToken", currResponse.getBody().gettoken());
	        jwtCookie.setHttpOnly(true); // To prevent JavaScript access to the cookie
	        jwtCookie.setSecure(true); // To ensure the cookie is sent over HTTPS
	        jwtCookie.setPath("/"); // The path to which the cookie applies
	        jwtCookie.setMaxAge(60 * 60 * 24); // Cookie valid for 1 day

	        // Add the cookie to the response
	        response.addCookie(jwtCookie);
			return "redirect:/customer/list";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			model.addAttribute("error","Sorry! Bad credentials.");
			return "login-form";
		}
	}
	
	@GetMapping("/showSignUpForm")
	@Operation(summary = "Show the signup form", description = "Displays the signup form and clears any existing JWT cookies.")
	public String showSignupForm(Model theModel, HttpServletResponse response) {
		Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/"); // Ensure the path matches where the cookie was set
        jwtCookie.setMaxAge(0); // Set max age to 0 to delete the cookie

        // Add the expired cookie to the response
        response.addCookie(jwtCookie);
		User user = new User();
		theModel.addAttribute("user", user);
		return "sign-up-form";
	}
	
	@PostMapping("/processSignUpForm")
	@Operation(summary = "Process the signup form", description = "Registers a new user and authenticates them, setting a JWT cookie upon successful signup.")
	public String processSignUpForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			Model model, HttpServletResponse response) {

		// validation errors from user Entity
		if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				System.out.println(error.getDefaultMessage());
				model.addAttribute("error", error.getDefaultMessage());
			}
			return "sign-up-form";
		}

		try {
			
			user.setRole("USER");
			System.out.println(user.toString());

			Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
			if(existingUser.isPresent() && existingUser.get() != null){
				model.addAttribute("error", "OOPS!! This username is already taken. Please use some other username");
				return "sign-up-form";
			}
			
			Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
			if(existingUserByEmail.isPresent() && existingUserByEmail.get() != null){
				model.addAttribute("error", "OOPS!! There already exists an account with this email.");
				return "sign-up-form";
			}

			String rawPassword = user.getPassword();
			if (rawPassword.length() < 6 || rawPassword.length() > 12) {
				model.addAttribute("error", "Password must be between 6 and 12 characters");
				return "sign-up-form";
			}
			
			AuthenticationResponse authResponse = authenticationService.register(user);
			
			// -----
			
			AuthenticationRequest request = new AuthenticationRequest();
			request.setUsername(user.getUsername());
			request.setPassword(user.getPassword());
			System.out.println(request.toString());
			ResponseEntity<AuthenticationResponse> currResponse;
			
			try {
				currResponse = ResponseEntity.ok(authenticationService.login(request, response));
				System.out.println(currResponse.getBody().gettoken());
				
				Cookie jwtCookie = new Cookie("jwtToken", currResponse.getBody().gettoken());
		        jwtCookie.setHttpOnly(true); // To prevent JavaScript access to the cookie
		        jwtCookie.setSecure(true); // To ensure the cookie is sent over HTTPS
		        jwtCookie.setPath("/"); // The path to which the cookie applies
		        jwtCookie.setMaxAge(60 * 60 * 24); // Cookie valid for 1 day

		        // Add the cookie to the response
		        response.addCookie(jwtCookie);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				model.addAttribute("error","Sorry! Error occured while signing up.");
				return "login-form";
			}
			
			// -----
			
			model.addAttribute("accountCreated", true);
			return "redirect:/customer/list";
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			model.addAttribute("error", "There already exists an account with this email");
			return "sign-up-form";
		}
	}
}
