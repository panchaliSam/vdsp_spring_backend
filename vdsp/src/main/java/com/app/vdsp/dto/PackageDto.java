package com.app.vdsp.dto;

import com.app.vdsp.entity.Package;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageDto {

    @NotBlank(message = "Package name cannot be blank")
    private String name;

    @NotBlank(message = "Package description cannot be blank")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    public static PackageDto fromEntity(Package packageEntity) {
        return PackageDto.builder()
                .name(packageEntity.getName())
                .description(packageEntity.getDescription())
                .price(packageEntity.getPrice())
                .build();
    }
}
