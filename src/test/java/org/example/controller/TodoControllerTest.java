package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    //MockBean으로 사용할 TodoService 필드 선언
    @MockBean
    TodoService todoService;

    private TodoEntity expected;
    @BeforeEach
    void setup() {
        //메소드가 실행되기 전 마다 초기화
        this.expected = new TodoEntity();
        this.expected.setId(123L);
        this.expected.setTitle("TEST TITLE");
        this.expected.setOrder(0L);
        this.expected.setCompleted(false);
    }
    @Test
    void create() throws Exception {
        when(this.todoService.add(any(TodoRequest.class)))
                .then((i) -> {
                    TodoRequest request = i.getArgument(0, TodoRequest.class);

                    return new TodoEntity(this.expected.getId(),
                                            request.getTitle(),
                                            this.expected.getOrder(),
                                            this.expected.getCompleted());
                });
        TodoRequest request = new TodoRequest();
        request.setTitle("ANY TITLE");

        //작성한 Request를 OpjectMapper를 통해 Requestbody에 넣어줌.
        ObjectMapper mapper = new ObjectMapper();
        //Request를 String 형태로 변환
        String content = mapper.writeValueAsString(request);

        this.mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title")
                    .value("ANY TITLE"));
    }

    @Test
    void readOne() {
    }
}