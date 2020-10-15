package helloJpa;

import javax.persistence.*;

@Entity
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToOne(mappedBy = "locker") // 읽기 전용으로 설정!
    //@JoinColumn(name = "MEMBER_ID") 이렇게 걸어주면 안되고 mappedBy 해줘야한다.
    private Member member;
}
