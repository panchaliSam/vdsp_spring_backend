package com.app.vdsp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresignedResponseDto {
    String objectKey;
    String presignedUrl;
    String viewUrl;
}
