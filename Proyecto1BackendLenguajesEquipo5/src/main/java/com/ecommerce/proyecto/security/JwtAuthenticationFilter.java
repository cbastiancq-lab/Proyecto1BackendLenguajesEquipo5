package com.ecommerce.proyecto.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String servletPath = request.getServletPath();

        System.out.println("=== JWT FILTER ===");
        System.out.println("PATH: " + servletPath);

        if (servletPath.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String authHeader = request.getHeader("Authorization");

            System.out.println("HEADER: " + authHeader);

            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(BEARER_PREFIX.length());

            System.out.println("TOKEN: " + token);

            String username = jwtService.extractUsername(token);

            System.out.println("USERNAME: " + username);

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails
                        = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken
                            = UsernamePasswordAuthenticationToken.authenticated(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);

                    System.out.println("AUTH OK");
                }
            }

        } catch (Exception ex) {

            System.out.println("ERROR JWT:");
            ex.printStackTrace();

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
