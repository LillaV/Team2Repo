package de.msg.javatraining.donationmanager.controller.auth;


import de.msg.javatraining.donationmanager.config.notifications.events.UserDeactivatedEvent;
import de.msg.javatraining.donationmanager.config.security.JwtUtils;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.RoleRepository;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.security.RefreshTokenService;
import de.msg.javatraining.donationmanager.service.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthController {
  private static final String REFRESHTOKEN_COOKIE_NAME = "RefreshTokenCookie";
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;
  @Autowired
  private ApplicationEventPublisher eventPublisher;


  private ResponseCookie createCookie(String token) {
    return ResponseCookie.from(REFRESHTOKEN_COOKIE_NAME, token)
            .httpOnly(true)
            .maxAge(Duration.ofDays(1))
            .sameSite("false")
            .path("/auth/refreshToken")
            .build();
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

    try{
      Authentication authentication = authenticationManager
              .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      User user = this.userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
      String jwt = jwtUtils.generateJwtToken(userDetails);
      List<String> permissions = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
              .collect(Collectors.toList());
      String refreshToken = UUID.randomUUID().toString();
      refreshTokenService.deleteRefreshTokenForUser(userDetails.getId());
      refreshTokenService.createRefreshToken(refreshToken, userDetails.getId());
      HttpHeaders headers = new HttpHeaders();
      user.setFailedLoginAttempts(0);
      headers.add(HttpHeaders.SET_COOKIE, createCookie(refreshToken).toString());
      return new ResponseEntity<>(new SignInResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), permissions,userDetails.isNewUser(),userDetails.isEnabled()), headers, HttpStatus.OK);
    } catch (BadCredentialsException e){
        User user = this.userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        user.setFailedLoginAttempts(user.getFailedLoginAttempts()+1);
        if(user.getFailedLoginAttempts() >= 5){
            user.setActive(false);
            user.setFailedLoginAttempts(0);
            this.userRepository.save(user);
            eventPublisher.publishEvent(new UserDeactivatedEvent(user));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your account is currently deactivated!");
        }
        this.userRepository.save(user);
        throw e;
    }
  }

  @GetMapping("/refreshToken")
  public ResponseEntity<?> checkCookie(HttpServletRequest request) {
    Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(REFRESHTOKEN_COOKIE_NAME)).findFirst();
    if(cookie.isPresent()) {
      return new ResponseEntity<>(new RefreshTokenResponse(refreshTokenService.exchangeRefreshToken(cookie.get().getValue())),HttpStatus.OK);
    }
    throw new RuntimeException("Cookie was not set");
  }

  @DeleteMapping("/logout/{userId}")
  public ResponseEntity<String> performLogout(@PathVariable("userId")Long id) {
    SecurityContextHolder.clearContext();
    refreshTokenService.deleteRefreshTokenForUser(id);
    return new ResponseEntity<>("Successful Logout",HttpStatus.OK);
  }


}
