package com.app.vdsp.repository;

import com.app.vdsp.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

//    @Modifying
//    @Query("UPDATE RefreshToken r SET r.revoked = TRUE WHERE r.user.id = :userId AND r.revoked = FALSE")
//    void revokeAllActiveTokensForUser(@Param("userId") Long userId);
}
