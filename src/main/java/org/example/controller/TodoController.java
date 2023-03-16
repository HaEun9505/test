package org.example.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.TodoModel;
import org.example.model.TodoRequest;
import org.example.model.TodoResponse;
import org.example.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/")    //base url 설정
public class TodoController {

    private final TodoService service;

    //list에 item을 추가
    @PostMapping
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest request) {
        log.info("CREATE");
        if(ObjectUtils.isEmpty(request.getTitle())) //title이 없으면
            return ResponseEntity.badRequest().build(); //잘못된 요청이라는 응답을 내려줌

        if(ObjectUtils.isEmpty(request.getOrder())) //order값이 없으면
            request.setOrder(0L);   //기본 default값 설정

        if(ObjectUtils.isEmpty(request.getCompleted()))
            request.setCompleted(false);

        //TodoEntity를 result 값으로 받음
        TodoModel result = this.service.add(request);
        //받은 result 값을 TodoResponse에 매핑해서 내려줌(리턴)
        return ResponseEntity.ok(new TodoResponse(result));
    }

    //item을 하나씩 조회하는 API
    @GetMapping("{id}")
    //경로로 받은 id값을 쓰기 위해서 파라미터(@PathVariable)로 받음
    public ResponseEntity<TodoResponse> readOne(@PathVariable Long id) {
        log.info("READ ONE");
        TodoModel result = this.service.searchById(id);
        return ResponseEntity.ok(new TodoResponse(result));
    }
    @GetMapping
    public ResponseEntity<List<TodoResponse>> readAll() {   //전체 조회 메소드
        log.info("READ ALL");
        List<TodoModel> list = this.service.searchAll();
        //받은 list를 TodoResponse로 매핑
        List<TodoResponse> response = list.stream().map(TodoResponse::new)
                                                    .collect(Collectors.toList());
        //받은 response값을 ResponseEntity에 보냄
        return ResponseEntity.ok(response);
    }
    @PatchMapping("{id}")   //경로에 id를 받아서 id값을 가진 Entity를 update
    //
    public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request) {
        log.info("UPDATE");
        TodoModel result = this.service.updateById(id, request);
        return ResponseEntity.ok(new TodoResponse(result));
    }

    @DeleteMapping("{id}")  //특정 id를 받아서 하나만 삭제
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {
        log.info("DELETE");
        this.service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    //모든 리스트를 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        log.info("DELETE ALL");
        this.service.deleteAll();
        return ResponseEntity.ok().build();
    }
}
