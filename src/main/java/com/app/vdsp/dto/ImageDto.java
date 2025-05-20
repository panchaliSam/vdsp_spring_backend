package com.app.vdsp.dto;

import com.app.vdsp.entity.Album;
import com.app.vdsp.entity.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {

    @JsonProperty("image_id")
    private String id;

    @NotBlank(message = "Image path cannot be blank")
    private String path;

    @NotNull(message = "Order is required")
    @JsonProperty("order")
    private int orderId;

    @NotNull(message = "Album ID cannot be null")
    @JsonProperty("album_id")
    private Long albumId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ImageDto fromEntity(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .path(image.getPath())
                .orderId(image.getOrderId())
                .albumId(image.getAlbum() != null ? image.getAlbum().getId() : null)
                .createdAt(image.getCreatedAt())
                .updatedAt(image.getUpdatedAt())
                .build();
    }

    public static Image toEntity(ImageDto dto, Album album) {
        return Image.builder()
                .id(dto.getId())
                .path(dto.getPath())
                .orderId(dto.getOrderId())
                .album(album)
                .build();
    }
}