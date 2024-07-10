package com.dhananjai.crm.controller;

import java.util.Optional;

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
public class LoginSignupController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private JwtService jwtService;
	
	@GetMapping("/")
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
	public String showSignupForm(Model theModel) {
		User user = new User();
		theModel.addAttribute("user", user);
		return "sign-up-form";
	}
	
	@PostMapping("/processSignUpForm")
	public String processSignUpForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			Model model) {

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
			
			AuthenticationResponse response = authenticationService.register(user);
			model.addAttribute("accountCreated", true);
			return "success ";
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			model.addAttribute("error", "There already exists an account with this email");
			return "sign-up-form";
		}
	}
}
