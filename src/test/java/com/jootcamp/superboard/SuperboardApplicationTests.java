package com.jootcamp.superboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jootcamp.superboard.board.repository.entity.BoardEntity;
import com.jootcamp.superboard.board.controller.dto.UpsertBoardRequest;
import com.jootcamp.superboard.board.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.jootcamp.superboard.common.constants.UserConstant.USER_ID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SuperboardApplicationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected BoardRepository boardRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        boardRepository.deleteAll();
    }

    @DisplayName("addBoard: 게시판 추가에 성공한다.")
    @Test
    public void addBoard() throws Exception {
        //Given
        final String url = "/boards";
        final String title = "유머게시판";
        final String content = "웃긴 게시글만 모음";
        final UpsertBoardRequest userRequest = new UpsertBoardRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        //when
        ResultActions result = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody));

        //then
        result.andExpect(status().isOk());

        List<BoardEntity> boards = boardRepository.findAll();

        assertThat(boards.size()).isEqualTo(1);
        assertThat(boards.get(0).getTitle()).isEqualTo(title);
        assertThat(boards.get(0).getDescription()).isEqualTo(content);
        assertThat(boards.get(0).getCreatedBy()).isEqualTo(2024L);

    }


    @DisplayName("findBoard: 게시판 조회에 성공한다.")
    @Test
    public void findBoard() throws Exception {
        //Given
        final String url = "/boards/{id}";
        final String title = "유머게시판";
        final String content = "웃긴 게시글만 모음";

        BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                .title(title)
                .description(content)
                .userId(USER_ID)
                .build());

        //when
        final ResultActions resultActions = mockMvc.perform(get(url, boardEntity.getId()));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(content))
                .andExpect(jsonPath("$.title").value(title))
                .andDo(print());

    }

    @DisplayName("findBoard: 게시판 목록 조회에 성공한다.")
    @Test
    public void findAllBoard() throws Exception {
        //Given
        final String url = "/boards";
        final String title = "유머게시판";
        final String content = "웃긴 게시글만 모음";

        BoardEntity boardEntity1 = boardRepository.save(BoardEntity.builder()
                .title(title+1)
                .description(content)
                .userId(USER_ID)
                .build());

        BoardEntity boardEntity2 = boardRepository.save(BoardEntity.builder()
                .title(title+2)
                .description(content)
                .userId(USER_ID)
                .build());

        BoardEntity boardEntity3 = boardRepository.save(BoardEntity.builder()
                .title(title+3)
                .description(content)
                .userId(USER_ID)
                .build());

        //when
        final ResultActions resultActions = mockMvc.perform(get(url));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[1].title").value(title+2))
                .andDo(print());

    }

    @DisplayName("deleteBoard: 게시판 삭제에 성공한다")
    @Test
    public void deleteBoard() throws Exception {
        //Given
        final String url = "/boards/{id}";
        final String title = "유머게시판";
        final String content = "웃긴 게시글만 모음";

        BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                .title(title)
                .description(content)
                .userId(USER_ID)
                .build());

        //when
        mockMvc.perform(delete(url, boardEntity.getId())).andExpect(status().isNoContent());

        //then
        List<BoardEntity> boardResponses = boardRepository.findAllByIsDeletedIsFalse();
        assertThat(boardResponses).isEmpty();
    }


}
