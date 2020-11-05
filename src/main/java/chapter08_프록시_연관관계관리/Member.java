package chapter08_프록시_연관관계관리;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {
    /**
     * 기본 키 매핑 방법
     * - 직접 할당 :  @Id만 사용
     * - 자동 생성 (@GeneratedValue)
     *      - strategy = GenerationType.IDENTITY : 데이터베이스에 위임. 알아서 증가
     *      - strategy = GenerationType.SEQUENCE : 테이블마다 따로 시퀀스를 관리하고 싶을땐 -> @SequenceGenerator를 이용해서 직접 시퀀스를 만들수도 있다.
     *              @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = sequence이름)
     *      ** 성능 최적화 ! : allocationSize = 50, call next value를 데이터 하나 마다 호출하면 성능 상에도 문제가 있으니까
     *                      딱 두번만 호출 시키게 할 수 있다.
     *                      50개씩 미리 DB에 올려놓고 메모리에서 . 미리 올려놓은 1부터 50까지 땡겨서 memory에 저장시킨 다음
     *                      호출 될때마다 call을 하지 않고, 메모리에서 땡겨서 사용하는 방식이다.
     *                      그리고 다음 또 insert를 날릴때는 다시 51번부터 100번대 까지 사용 하는 방식이다.
     */
    @Id @GeneratedValue
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    /**
     * fetch = FetchType.EAGER(즉시 로딩) or LAZY(지연 로딩)
     *      - LAZY 로딩을 하면, team은 프록시 가짜 객체가 담기게 되고, team을 실제로 사용하는 시점에 쿼리가 나가게 된다. ex) team.getName() 이 시점.
     *      - EAGER 로딩을 하게 되면, team은 진짜 객체를 넣어준다. 쿼리를 한방에 조인하여 땡겨오게 된다.
     */
    @ManyToOne(fetch = FetchType.LAZY) // LAZY로 설정하면, team에 프록시 객체를 넣어준다.
    @JoinColumn(name = "TEAM_ID") // 조인하는 컬럼을 적어준다!
    private Team team; // error가 나는 이유는 몇대몇인지 관계를 매핑해줘야한다.


    public void setTeam(Team team) {
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }
    // 연관관계 편의 메소드는 어느쪽에 넣어도 상관없지만, 무조건 한쪽에만 넣어야한다. 안그러면 무한루프에 빠짐.

    public void changeTeam(Team team) { // 메소드 명을 setTeam 보다는 changeTeam으로 바꿔주면, 뭔가 더 중요한 일을 하는구나! 라고 명시해 줄수 있기 때문에 더 좋다
        this.team = team;
        team.getMembers().add(this); // 이렇게하면, setTeam을 설정하는 시점에 양쪽에 데이터가 세팅이 되버리는 이점이 있다.
    }
}
