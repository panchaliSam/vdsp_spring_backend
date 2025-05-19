package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.DashboardStatsDto;
import com.app.vdsp.entity.ApiResponse;
import com.app.vdsp.entity.Payment;
import com.app.vdsp.helpers.AuthorizationHelper;
import com.app.vdsp.repository.EventRepository;
import com.app.vdsp.repository.PaymentRepository;
import com.app.vdsp.repository.StaffRepository;
import com.app.vdsp.repository.UserRepository;
import com.app.vdsp.service.AdminDashboardService;
import com.app.vdsp.type.PaymentStatus;
import com.app.vdsp.type.RoleType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final PaymentRepository paymentRepository;
    private final EventRepository eventRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;

    public AdminDashboardServiceImpl(PaymentRepository paymentRepository, EventRepository eventRepository, StaffRepository staffRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.eventRepository = eventRepository;
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse<DashboardStatsDto> getDashboardStats(String authHeader, int year, int month) {
        AuthorizationHelper.ensureAuthorizationHeader(authHeader);

        BigDecimal totalPayments = paymentRepository.findAll().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS)
                .map(Payment::getPayhereAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalEvents = eventRepository.count();
        long totalStaff = staffRepository.count();
        long totalCustomers = userRepository.countByRole(RoleType.ROLE_CUSTOMER);

        long yearlyCount = eventRepository.findAll().stream()
                .filter(e -> e.getEventDate().getYear() == year)
                .count();

        long monthlyCount = eventRepository.findAll().stream()
                .filter(e -> e.getEventDate().getYear() == year && e.getEventDate().getMonthValue() == month)
                .count();

        DashboardStatsDto stats = DashboardStatsDto.builder()
                .totalSuccessfulPayments(totalPayments)
                .totalEvents(totalEvents)
                .totalStaff(totalStaff)
                .totalCustomers(totalCustomers)
                .yearlyEventCount(yearlyCount)
                .monthlyEventCount(monthlyCount)
                .build();

        return new ApiResponse<>(true, "Dashboard statistics retrieved successfully", stats);
    }
}
