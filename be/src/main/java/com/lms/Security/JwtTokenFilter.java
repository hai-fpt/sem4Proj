package com.lms.security;

import com.lms.models.Role;
import com.lms.models.User;
import com.lms.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lms.security.TokenVerifier.secretKey;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final TokenVerifier tokenVerifier;
    private final UserRepository userRepository;

    public JwtTokenFilter(TokenVerifier tokenVerifier, UserRepository userRepository) {
        this.tokenVerifier = tokenVerifier;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        if (!tokenVerifier.verifyJwt(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        User user = userRepository.findUserByEmail(claims.getSubject()).orElse(null);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user == null) {
            GrantedAuthority userAuthority = new SimpleGrantedAuthority("ROLE_USER");
            authorities.add(userAuthority);
        } else {
            List<Role.RoleEnum> roles = userRepository.getRolesOfUser(user.getId());
            for (Role.RoleEnum role : roles) {
                String roleString = "ROLE_" + role.toString();
                GrantedAuthority authority = new SimpleGrantedAuthority(roleString);
                authorities.add(authority);
            }
        }
        User userDetails = new User();
        userDetails.setEmail(claims.getSubject());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
