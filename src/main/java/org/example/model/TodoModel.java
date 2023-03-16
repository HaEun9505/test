package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
//데이터베이스와 데이터를 주고 받기 위한 클래스
public class TodoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)   //Not Null
    private String title;

    //order 키워드는 h2database에서 예약어로 사용되고 있으므로 별도로 컬럼명 지정
    @Column(name="todoOrder", nullable = false)
    private Long order;

    @Column(nullable = false)
    private Boolean completed;
}
