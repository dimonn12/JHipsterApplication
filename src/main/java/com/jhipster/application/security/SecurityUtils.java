package com.jhipster.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     */
    public static String getCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails springSecurityUser = null;
        String userName = null;
        if(authentication != null) {
            if(authentication.getPrincipal() instanceof UserDetails) {
                springSecurityUser = (UserDetails)authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else {
                if(authentication.getPrincipal() instanceof String) {
                    userName = (String)authentication.getPrincipal();
                }
            }
        }
        return userName;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();
        if(null != auth) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            if(null != authorities) {
                return !containsAnonymous(authorities);
            }
            return true;
        }
        return false;
    }

    /**
     * Check if a user is admin.
     *
     * @return true if the user is admin, false otherwise
     */
    public static boolean isAdmin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();
        if(null != auth) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            if(authorities != null) {
                return containsAdmin(authorities);
            }
        }
        return false;
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
     */
    public static boolean isUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();
        if(null != auth) {
            if(auth.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails)auth.getPrincipal();
                Collection<? extends GrantedAuthority> authorities = springSecurityUser.getAuthorities();
                if(authorities != null) {
                    return containsRole(authorities, authority);
                }
            } else {
                Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
                if(authorities != null) {
                    return containsRole(authorities, authority);
                }
            }
        }
        return false;
    }

    private static boolean containsAnonymous(Collection<? extends GrantedAuthority> authorities) {
        return containsRole(authorities, AuthoritiesConstants.ANONYMOUS);
    }

    private static boolean containsAdmin(Collection<? extends GrantedAuthority> authorities) {
        return containsRole(authorities, AuthoritiesConstants.ADMIN);
    }

    private static boolean containsRole(Collection<? extends GrantedAuthority> authorities, String role) {
        for(GrantedAuthority authority : authorities) {
            if(authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }

}
