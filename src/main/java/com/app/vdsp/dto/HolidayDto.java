package com.app.vdsp.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HolidayDto {
    private String name;
    private LocalDate date;
}