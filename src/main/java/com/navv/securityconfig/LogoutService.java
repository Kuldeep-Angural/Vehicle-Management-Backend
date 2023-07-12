package com.navv.securityconfig;

import com.navv.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader=request.getHeader("AUTHORIZATION");
        final String jwtToken;
        if(authHeader==null && !authHeader.startsWith("Bearer "))
        {
            return;
        }
        jwtToken=authHeader.substring(7);
        System.out.println(jwtToken);
        var savedToken=tokenRepository.findByToken(jwtToken).orElse(null);
        if(savedToken!=null){
            savedToken.setExpired(true);
            savedToken.setActive(false);
            tokenRepository.save(savedToken);
        }
    }
}
