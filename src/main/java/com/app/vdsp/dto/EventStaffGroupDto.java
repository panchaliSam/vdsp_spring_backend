package com.app.vdsp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventStaffGroupDto {
    private String eventDate;
    private List<StaffDto> staffList;
}
