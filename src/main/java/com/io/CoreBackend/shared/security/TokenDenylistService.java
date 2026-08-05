package com.io.CoreBackend.shared.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenDenylistService {

    private final RevokedTokenRepository revokedTokenRepository;

    @Transactional
    @CacheEvict(value = "revokedTokens", key = "#jti")
    public void revoke(String jti, Date expiration) {
        if (jti == null || revokedTokenRepository.existsById(jti)) {
            return;
        }
        LocalDateTime expiresAt = expiration.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        revokedTokenRepository.save(
                RevokedToken.builder().jti(jti).expiresAt(expiresAt).build());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "revokedTokens", key = "#jti")
    public boolean isRevoked(String jti) {
        return jti != null && revokedTokenRepository.existsById(jti);
    }

    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void purgeExpired() {
        revokedTokenRepository.deleteExpired(LocalDateTime.now());
    }
}