package com.example.chopar_1.config;

import com.example.chopar_1.dto.JwtDTO;
import com.example.chopar_1.util.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
       @Autowired
        private UserDetailsService userDetailsService;

     @Override
        protected  boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            return Arrays
                    .stream(SpringSecurityConfig.AUTH_WHITELIST)
                    .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain) throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authHeader.substring(7);
            JwtDTO jwtDto;
            try {
                UserDetails userDetails;
                jwtDto = JWTUtil.decode(token);
                if (jwtDto.getEmail()!=null) {
                     userDetails = userDetailsService.loadUserByUsername(jwtDto.getEmail());
                }else {
                    userDetails = userDetailsService.loadUserByUsername(jwtDto.getPhone());

                }
                UsernamePasswordAuthenticationToken
                        authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("Message", "Token Not Valid");
                return;
            }
        }
    }

