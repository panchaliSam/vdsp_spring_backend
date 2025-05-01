package com.app.vdsp.entity;

import com.app.vdsp.type.EventType;
import com.app.vdsp.type.SessionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name="reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message="User cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @NotNull(message = "Event type cannot be null")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @NotNull(message = "Package cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id", nullable = false)
    private Package eventPackage;

    @NotBlank(message = "Event location cannot be blank")
    @Column(name = "event_location", nullable = false, columnDefinition = "TEXT")
    private String eventLocation;

    @NotNull(message = "Event date cannot be null")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @NotNull(message = "Event start time cannot be null")
    @Column(name = "event_start_time", nullable = false)
    private LocalTime eventStartTime;

    @NotNull(message = "Event end time cannot be null")
    @Column(name = "event_end_time", nullable = false)
    private LocalTime eventEndTime;

    @NotNull(message = "Session type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false)
    private SessionType sessionType;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
