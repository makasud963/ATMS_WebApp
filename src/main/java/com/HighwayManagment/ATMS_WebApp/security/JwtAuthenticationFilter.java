package com.HighwayManagment.ATMS_WebApp.security;
import com.HighwayManagment.ATMS_WebApp.entity.UserContext;
import com.HighwayManagment.ATMS_WebApp.repository.UserSessionRepository;
import com.HighwayManagment.ATMS_WebApp.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import io.jsonwebtoken.ExpiredJwtException; // Ensure this matches your JWT library
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserSessionRepository sessionRepository; // 1. Inject your Session Repository here

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                email = jwtService.extractEmail(token);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    Long userId = jwtService.extractUserId(token);
                    Long sessionId = jwtService.extractSessionId(token);

                    UserContextHolder.set(
                            new UserContext(
                                    userId,
                                    email,
                                    sessionId
                            )
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            // 1. Get claims directly from the exception object (Do not re-parse via jwtService)
            Claims claims = e.getClaims();

            // 2. Safely extract your custom claims out of the expired token packet
            Long sessionId = null;
            if (claims.get("sessionId") != null) {
                sessionId = Long.valueOf(claims.get("sessionId").toString());
            }

            // 3. Update the logout datetime in your database session table
            if (sessionId != null) {
                final Long finalSessionId = sessionId; // Needed for lambda stream scope
                sessionRepository.findById(finalSessionId).ifPresent(session -> {
                    session.setLogoutTime(LocalDateTime.now());
                    session.setActive(false);
                    sessionRepository.save(session);
                });
            }

            // 4. Send a clear 401 Unauthorized response back to the client
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Session has expired. Please login again.\"}");
            return; // Essential: Stops the filter chain from continuing
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }
    }
}

//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final CustomUserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        String token = null;
//        String email = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//
//            token = authHeader.substring(7);
//
//            email = jwtService.extractEmail(token);
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//            if (jwtService.validateToken(token, userDetails)) {
//
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities()
//                        );
//                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails(request)
//                );
//                Long userId = jwtService.extractUserId(token);
//                Long sessionId = jwtService.extractSessionId(token);
//                UserContextHolder.set(
//                        new UserContext(
//                                userId,
//                                email,
//                                sessionId
//                        )
//                );
//                SecurityContextHolder
//                        .getContext()
//                        .setAuthentication(authentication);
//            }
//        }
//        try {
//            filterChain.doFilter(request, response);
//
//        } finally {
//
//            UserContextHolder.clear();
//        }
//    }
//}