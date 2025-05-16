package com.app.vdsp.dto;

import com.app.vdsp.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StaffDto fromEntity(Staff staff) {
        return StaffDto.builder()
                .id(staff.getId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .email(staff.getEmail())
                .phoneNumber(staff.getPhoneNumber())
                .userId(staff.getUserId())
                .createdAt(staff.getCreatedAt())
                .updatedAt(staff.getUpdatedAt())
                .build();
    }
}
