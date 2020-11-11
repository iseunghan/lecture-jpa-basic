package chapter09_값타입;

import javax.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    // 기간 period
    /**
     * 임베디드 타입 사용법
     *
     *      - @Embeddedable : 값 타입을 정의 하는 곳에 표시
     *      - @Embedded : 값 타입을 사용 하는 곳에 표시
     *      - 기본 생성자 필수
     *      - 임베디드 타입의 값이 null이면 매핑한 컬럼 값은 모두 null  -> ex)@Embedded private Period period = null; -> 모든 컬럼 = null;
     *
     * 임베디드 타입과 테이블 매핑
     *
     *      - 임베디드 타입은 엔티티의 값이 뿐이다.
     *      - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
     *      - 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능
     *      - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음 -> 임베디드 타입을 더 많이 사용하는것이 좋다.
     *
     */
    @Embedded
    private Period workPeriod;

    // 주소 address
    @Embedded
    private Address homeAddress;
    /**
     * 한 엔티티에서 같은 값 타입을 사용하고 싶을때! -> @AttributeOverrides, @AttributeOverride를 사용해서 컬럼 명 속성을 재정의!
     *
     * - 사용법
     *      @AttributeOverrides({@AttributeOverride(name = "city",
     *             column = @Column(name = "WORK_STREET")),
     *             @AttributeOverride(name = "street",
     *                     column = @Column(name = "WORK_STREET")),
     *             @AttributeOverride(name = "zipcode",
     *                     column = @Column(name = "WORK_ZIPCODE"))
     *     })
     *     컬럼 명을 하나하나 다 매핑 시켜서 재정의까지 시켜주면 끝!
     */
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "city",
            column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street",
                    column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode",
                    column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
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

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }
}
