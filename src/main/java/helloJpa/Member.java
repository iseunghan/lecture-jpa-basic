package helloJpa;

import javax.persistence.*;
import java.util.Date;

@Entity
// @Table(name = "USER") -> 만약 테이블 명이 USER 일 경우
public class Member {
    @Id
    private int id;

    /**
     * @Column(속성 소개)
     * name : 필드와 매핑할 테이블의 컬럼 이름
     * insertable, updateable : 등록, 변경 가능 여부
     * nullable :  기본값:true, null값의 허용 여부를 설정, false -> not null 제약 조건 붙는다.
     * unique : @Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제약을 걸때 사용. 잘 안씀(unique name이 랜덤이라서)
     * columnDefinition : 직접 컬럼 정보 줄수 있음 ex) varchar(100) default 'EMPTY'
     * length : 문자 길이 제약, string 타입에만 사용
     *
     */
    @Column(name = "name", nullable = false) // 컬럼 매핑 : 객체에서는 username, DB에서는 name으로 사용
    private String username;

    private Integer age;

    /**
     *  EnumType.ORDINAL 사용 금지!!
     *  ORIDINAL :  enum 순서를 데이터베이스에 저장
     *  STRING : enum 이름을 데이터베이스에 저장
     *  나중에 enum 타입을 추가했을때 순서가 바뀌기 때문에 큰 오류를 발생시키므로 사용 금지!
     */
    @Enumerated(EnumType.STRING) // enum타입 매핑! (varchar로 매핑이 됨)
    private RoleType roleType;

    /**
     * @Temporal 은 예전 버전에서만 사용하고, 요즘은 java8 기능 중 LocalDateTime을 사용한다.
     * private LocalDate localDate; -> date로 자동 매핑
     * private LocalDateTime localDateTime; -> timeStamp로 자동 매핑 시켜준다.
     */
    @Temporal(TemporalType.TIMESTAMP) // 날짜 타입 매핑(3가지 종류 : DATE, TIME, TIMESTAMP(DATE+TIME))
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob // BLOB, CLOB (거대한 문자열을 넣을때 사용)
    private String description;

    @Transient // DB와 매핑을 하지 않겠다.
    private int temp;

    public Member() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
