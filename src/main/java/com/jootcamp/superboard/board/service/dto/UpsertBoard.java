package com.jootcamp.superboard.board.service.dto;

import com.jootcamp.superboard.board.repository.Entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UpsertBoard {

    private String title;
    private String description;
    private long id;
    private String userName;

    public BoardEntity toEntity() {
        return BoardEntity.builder()
                .title(title)
                .description(description)
                .userName(userName)
                .build();
    }
}