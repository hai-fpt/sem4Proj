package com.lms.security;

import com.lms.models.Role;
import com.lms.models.User;
import com.lms.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
//        prePostEnabled = true
//)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //TODO: Uncomment these when needed
    private final UserRepository userRepository;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(UserRepository userRepository, JwtTokenFilter jwtTokenFilter) {
        this.userRepository = userRepository;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(
                username -> userRepository.findUserByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"))
        );
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .anyRequest().permitAll()
//                .and()
//                .cors()
//                .and()
//                .csrf().disable();
        http = http.cors().and().csrf().disable();

        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        ((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                        })
                )
                .and();

        http
                .authorizeRequests()
                .antMatchers("/verify").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user_leave").permitAll()
                .antMatchers(HttpMethod.GET, "/api/leave").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/user/{id}").permitAll()
                .antMatchers("/api/avatar/**").permitAll()
                .antMatchers("/api/admin/**").hasRole(Role.RoleEnum.ADMIN.toString())
                .antMatchers("/api/manager/**").hasRole(Role.RoleEnum.MANAGER.toString())
                .antMatchers("/api/file/**", "/api/comment/**", "/api/leave/**").hasAnyRole(Role.RoleEnum.ADMIN.toString(), Role.RoleEnum.MANAGER.toString(), Role.RoleEnum.USER.toString())
                .antMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole(Role.RoleEnum.ADMIN.toString(), Role.RoleEnum.MANAGER.toString())
                .antMatchers(HttpMethod.POST, "/api/user/**").hasRole(Role.RoleEnum.ADMIN.toString())
                .antMatchers(HttpMethod.DELETE, "/api/user/**").hasRole(Role.RoleEnum.ADMIN.toString())
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
