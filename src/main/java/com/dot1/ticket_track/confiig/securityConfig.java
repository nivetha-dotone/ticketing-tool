package com.dot1.ticket_track.confiig;
import com.dot1.ticket_track.webtoken.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.analysis.function.Abs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class securityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
//        security.csrf(cs-> cs.disable());
//        security.authorizeHttpRequests(au-> au.anyRequest().authenticated());
//        security.httpBasic(Customizer.withDefaults());
//        security.sessionManagement(ss->ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return security.build();
//    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private  final UserService userService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
           try {
               http.csrf(AbstractHttpConfigurer::disable)
                       .authorizeHttpRequests(request-> request
                               .requestMatchers("/auth/**").permitAll()
                               .requestMatchers("/Admin/**").hasAnyAuthority("ADMIN")
                               .requestMatchers("/Manager/**").hasAnyAuthority("MANAGER")
                               .requestMatchers("/Employee/**").hasAnyAuthority("CONSULTANT")
                               .requestMatchers("/Client/**").hasAnyAuthority("CLIENT")


                               .anyRequest().permitAll()
                       )
                       .sessionManagement(manager-> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                       .authenticationProvider(authecationProvider()).addFilterBefore(
                               jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                       );
            return http.build();
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
    }



    @Bean
    public AuthenticationProvider authecationProvider() {
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
