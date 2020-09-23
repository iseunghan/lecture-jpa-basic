package helloJpa;

import javax.persistence.*;

@Entity
// @Table(name = "USER") -> 만약 테이블 명이 USER 일 경우
public class Member {

    @Id
    private Long id;
    // @Column(name = "username") -> 만약 컬럼 명이 username 일 경우
    // @Column(unique = true, length = 10) -> 제약조건 : unique, 길이 10 제한.
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
