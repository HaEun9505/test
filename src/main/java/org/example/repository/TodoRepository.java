package org.example.repository;

import org.example.model.TodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoModel,Long> {
//<데이터베이스의 테이블과 연결될 객체인 TodoEntity, 해당 객체의 필드 타입>

}
