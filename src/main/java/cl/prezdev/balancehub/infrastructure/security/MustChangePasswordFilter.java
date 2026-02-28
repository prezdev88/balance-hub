package cl.prezdev.balancehub.infrastructure.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MustChangePasswordFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser user) {
            if (user.mustChangePassword() && !isAllowedPath(request.getRequestURI())) {
                response.sendError(HttpStatus.FORBIDDEN.value(), "Password change required");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAllowedPath(String path) {
        return path.equals("/api/auth/change-password")
            || path.equals("/api/auth/logout")
            || path.startsWith("/swagger-ui")
            || path.startsWith("/v3/api-docs");
    }
}
