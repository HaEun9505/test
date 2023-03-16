package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.TodoModel;
import org.example.model.TodoRequest;
import org.example.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class TodoService {

/*1	todo 리스트 목록에 아이템을 추가
2	todo  리스트 목록 중 특정 아이템을 조회
3	todo 리스트 전체 목록을 조회
4	todo 리스트 목록 중 특정 아이템을 수정
5	todo 리스트 목록 중 특정 아이템을 삭제
6	todo 리스트 전체 목록을 삭제 */

    private final TodoRepository todoRepository;

    //method 시그니처 정의
    //item을 추가하는 메소드(Entity 반환하는 메소드)
    public TodoModel add(TodoRequest request) {
        //request를 받아서 Entity 객체 생성
        TodoModel todoModel = new TodoModel();
        todoModel.setTitle(request.getTitle());
        todoModel.setOrder(request.getOrder());
        todoModel.setCompleted(request.getCompleted());
        //repository로 database의 값이 입력되도록 save에 Entity를 넣어줌
        //save - generic으로 받은 타입을 반환
        return this.todoRepository.save(todoModel);
    }

    //조회된 item을 반환(search할 기준인 id를 파라미터로 입력받아 반환해주는 메소드)
    public TodoModel searchById(Long id) {
        return this.todoRepository.findById(id)
                //값이 없으면 NotFound Exception
                .orElseThrow (() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //전체 목록을 조회하는 메소드
    public List<TodoModel> searchAll() {
        return this.todoRepository.findAll();
    }

    //수정된 결과 Entity를 반환(수정할 id값을 파라미터로 입력받아 반환해주는 메소드)
    public TodoModel updateById(Long id, TodoRequest request) {
        //기존에 있는 id(Entity객체)를 가져와서 update
        TodoModel todoModel = this.searchById(id);
        if(request.getTitle() != null) {
            todoModel.setTitle(request.getTitle());
        }
        if(request.getOrder() != null) {
            todoModel.setOrder(request.getOrder());
        }
        if(request.getCompleted() != null) {
            todoModel.setCompleted(request.getCompleted());
        }
        return this.todoRepository.save(todoModel);
        //TodoEntity를 save에 담아서 저장된 결과값을 리턴
    }

    //삭제 메소드(삭제할 Entity하나만 입력받아 반환하는 메소드)
    public void deleteById(Long id) {
        this.todoRepository.deleteById(id);
    }

    //전체 삭제 메소드
    public void deleteAll() {
        this.todoRepository.deleteAll();
    }
}
