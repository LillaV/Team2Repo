package de.msg.javatraining.donationmanager.config.security;


import de.msg.javatraining.donationmanager.config.security.JwtAuthenticationFilter;
import de.msg.javatraining.donationmanager.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/auth/**", "/app/test/**", "/users/**").permitAll() //these requests are allowed
//                .authorizeHttpRequests(request -> request.requestMatchers("/auth/**", "/app/test/**", "/users/**", "/users/*").permitAll() //these requests are allowed
                        .anyRequest().authenticated()) //any other request must be authenticated
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS)) // we don't want sessions
                //.authenticationProvider(authenticationProvider())
                .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.addAllowedOrigin("http://localhost:4200"); // Replace with your frontend origin
                        corsConfiguration.addAllowedHeader("*");
                        corsConfiguration.addAllowedMethod("*");
                        corsConfiguration.setAllowCredentials(true);
                        return corsConfiguration;
                    });
                });
        //TODO: we need a filter before UsernamePasswordAuthenticationFilter.class
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //TODO: new DaoAuthenticationProvider with UserDetailsService and a PasswordEncoder
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }


}