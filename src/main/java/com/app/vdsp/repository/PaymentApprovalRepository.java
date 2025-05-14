package com.app.vdsp.repository;

import com.app.vdsp.entity.PaymentApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentApprovalRepository extends JpaRepository<PaymentApproval, Long> {
    @Query("SELECT pa FROM PaymentApproval pa WHERE pa.user.id = :userId")
    List<PaymentApproval> findByUserId(@Param("userId") Long userId);
}
