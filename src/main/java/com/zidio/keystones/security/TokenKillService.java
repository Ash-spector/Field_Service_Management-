package com.zidio.keystones.security;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenKillService {

    private final Set<String> blockToken;

    public TokenKillService(){
        this.blockToken = Collections.newSetFromMap(
                new ConcurrentHashMap<String, Boolean>()
        );
    }

    public void blackListToken (String token) {
        blockToken.add(token);
    }

    public boolean isBlockedToken(String token){
        return blockToken.contains(token);
    }
}
