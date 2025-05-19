package com.app.vdsp.dto;

import com.app.vdsp.entity.Album;
import com.app.vdsp.entity.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {

    private UUID id;

    @NotBlank(message = "Image path cannot be blank")
    private String path;

    @NotNull(message = "Album ID cannot be null")
    private Long albumId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ImageDto fromEntity(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .path(image.getPath())
                .albumId(image.getAlbum() != null ? image.getAlbum().getId() : null)
                .createdAt(image.getCreatedAt())
                .updatedAt(image.getUpdatedAt())
                .build();
    }

    public static Image toEntity(ImageDto dto, Album album) {
        return Image.builder()
                .path(dto.getPath())
                .album(album)
                .build();
    }
}