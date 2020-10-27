package helloJpa;

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

    /*@Column(name = "TEAM_ID")
    private Long teamId;*/


    // 의존관계의 주인(Owner)이다. (거의 Many쪽이 주인이 된다)
    @ManyToOne // 1 :Team <-> n: Member 이것을 Member입장에서 매핑!
    @JoinColumn(name = "TEAM_ID") // 조인하는 컬럼을 적어준다!
    private Team team; // error가 나는 이유는 몇대몇인지 관계를 매핑해줘야한다.

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    /**
     * 다대다 매핑은 왠만하면 안쓰는게 최선이다.
     *
     * 첫번째 방법. @ManyToMany 사용
     */
    /*@ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT") // 이렇게 하면 MEMBER_PRODUCT라는 테이블이 생성이 된다. (중간 테이블)
    private List<Product> products = new ArrayList<>();*/

    /**
     * 다대다 매핑 두번째 방법.
     * 연결 테이블용 엔티티 추가(연결 테이블을 엔티티로 승격)
     *
     * @ManyToMany 를 -> @OneToMany 두개로 풀어서 관계 매핑
     */
    @OneToMany(mappedBy = "member")
    private List<Member_Product> products = new ArrayList<>();

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
