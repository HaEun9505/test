package org.example.service;

import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    //직접적으로 Mock 객체를 사용
    private TodoRepository todoRepository;

    @InjectMocks
    //Mock을 주입받아서 사용하기 위해 필드 추가
    private TodoService todoService;

    @Test
    void add() {
        //todoRepository가 save를 호출해서 TodoModel 값을 받으면
        when(this.todoRepository.save(any(TodoEntity.class)))
                //받은 Model값을 반환
                .then(AdditionalAnswers.returnsFirstArg());
        //값 셋팅
        TodoRequest expected = new TodoRequest();
        expected.setTitle("Test Title");

        //Service에 request를 보냄(요청)
        TodoEntity actual = this.todoService.add(expected);

        //직접 넣은 값과 실제로 반환 받은 값이 일치하는지 확인
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    void searchById() {
        TodoEntity entity = new TodoEntity();
        entity.setId(123L);
        entity.setTitle("TITLE");
        entity.setOrder(0L);
        entity.setCompleted(false);

        //Optional로 리턴(entity)값을 넣어줌(findById - Optional 반환 메소드)
        Optional<TodoEntity> optional = Optional.of(entity);
        //findById로 id값이 주어졌을때 optional 값을 리턴할 수 있도록 설정
        given(this.todoRepository.findById(anyLong()))
                .willReturn(optional);

        //직접 넣은 값(optional) 가져오기
        TodoEntity expected = optional.get();

        //Service에서 searchById를 했을 때 실제 반환받은 값을 TodoEntity에 보냄
        TodoEntity actual = this.todoService.searchById(123L);

        //값 비교
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCompleted(), actual.getCompleted());
    }

    @Test
    public void searchByIdFailed() {
        //에러가 잘 발생하는지 테스트
        given(this.todoRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            this.todoService.searchById(123L);
        });
    }
}