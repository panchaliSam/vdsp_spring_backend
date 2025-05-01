package com.app.vdsp.repository;

import com.app.vdsp.entity.PaymentApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentApprovalRepository extends JpaRepository<PaymentApproval, Long> {

    @Query("SELECT pa.payment.reservation.id FROM PaymentApproval pa WHERE pa.status = TRUE")
    List<Long> findApprovedReservationIds();
}
