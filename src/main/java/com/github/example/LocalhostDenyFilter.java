package com.github.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter denies access from localhost.
 * However, access from localhost is allowed while the ReadinessState is REFUSING_TRAFFIC.
 * This mechanism is mainly provided for warming up the application during startup.
 */
@Component
public class LocalhostDenyFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(LocalhostDenyFilter.class);
    private final ApplicationAvailability availability;

    public LocalhostDenyFilter(ApplicationAvailability availability) {
        this.availability = availability;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        logger.info("Deny localhost access. remoteAddr: {}, path: {}", request.getRemoteAddr(), request.getRequestURI());
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (availability.getReadinessState() == ReadinessState.REFUSING_TRAFFIC) {
            logger.info("Allow localhost access because readiness state is still REFUSING_TRAFFIC.");
            return true;
        }

        // This is sample, so only specified IPv4.
        var remoteAddr = request.getRemoteAddr();
        return !remoteAddr.equals("127.0.0.1") && !remoteAddr.equals("localhost");
    }
}
