package com.app.vdsp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "event_staff", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"staff_id", "event_date"}, name = "unique_staff_event_date")
})
public class EventStaff implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Event cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @NotNull(message = "Staff cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @NotNull(message = "Event date cannot be null")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @PrePersist
    @PreUpdate
    private void syncEventDate() {
        if (this.event != null) {
            this.eventDate = this.event.getEventDate();
        }
    }
}
