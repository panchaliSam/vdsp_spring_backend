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
public class AlbumSummaryDto {

    private Long id;
    private String name;
    private String coverPhoto;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AlbumSummaryDto fromEntity(Album album) {
        return AlbumSummaryDto.builder()
                .id(album.getId())
                .name(album.getName())
                .coverPhoto(album.getCoverPhoto())
                .createdAt(album.getCreatedAt())
                .updatedAt(album.getUpdatedAt())
                .images(album.getImages() != null
                        ? album.getImages().stream()
                        .map(image -> image.getId())
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}