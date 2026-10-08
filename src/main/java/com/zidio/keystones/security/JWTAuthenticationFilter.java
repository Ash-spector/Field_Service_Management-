package com.zidio.keystones.security;

import com.zidio.keystones.entity.UserAuth;
import com.zidio.keystones.repository.UserAuthRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenUtil jwtTokenUtil;

    private final UserAuthRepo userAuthRepository;

    private final TokenKillService tokenKillService;

    public JWTAuthenticationFilter(
            JWTTokenUtil jwtTokenUtil,
            UserAuthRepo userAuthRepository,
            TokenKillService tokenKillService) {

        this.jwtTokenUtil = jwtTokenUtil;
        this.userAuthRepository = userAuthRepository;
        this.tokenKillService = tokenKillService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtTokenUtil.exractToken(authHeader);

        // Check whether token has been revoked
        if (tokenKillService.isBlockedToken(token)) {

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Token has been revoked. Please login again.\"}"
            );

            return;
        }

        try {

            // Validate JWT
            if (!jwtTokenUtil.validateToken(token)) {

                filterChain.doFilter(request, response);
                return;
            }

            // Get email from JWT
            String email = jwtTokenUtil.getEmailFromToken(token);

            // Avoid setting authentication again
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                UserAuth user = userAuthRepository.findByEmail(email)
                        .orElse(null);

                if (user == null) {

                    filterChain.doFilter(request, response);
                    return;
                }

                List<SimpleGrantedAuthority> authorities =
                        new ArrayList<>();

                // Add role
                authorities.add(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )
                );

                // Add permissions
                Set<String> permissions =
                        jwtTokenUtil.getPermissionsFromToken(token);

                if (permissions != null) {

                    permissions.forEach(permission ->
                            authorities.add(
                                    new SimpleGrantedAuthority(permission)
                            )
                    );
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                authorities
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}