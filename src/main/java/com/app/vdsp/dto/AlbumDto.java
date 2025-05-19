package com.app.vdsp.dto;

import com.app.vdsp.entity.Album;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDto {
    private Long id;
    private String name;
    private String coverPhoto;
    private List<ImageDto> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AlbumDto fromEntity(Album album) {
        return AlbumDto.builder()
                .id(album.getId())
                .name(album.getName())
                .coverPhoto(album.getCoverPhoto())
                .createdAt(album.getCreatedAt())
                .updatedAt(album.getUpdatedAt())
                .images(album.getImages() != null ? album.getImages().stream()
                        .map(ImageDto::fromEntity)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static Album toEntity(AlbumDto dto) {
        return Album.builder()
                .name(dto.getName())
                .coverPhoto(dto.getCoverPhoto())
                .build();
    }
}
