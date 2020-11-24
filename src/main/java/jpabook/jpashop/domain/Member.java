package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID") // 애매한 컬럼만 이름 매핑해준다. java에서는 memberId / DB에서는 member_id or MEMBER_ID 이런식으로 "_"를 선호하는 편이다.
    private Long id;

    private String name;

    @Embedded // 값 타입이라는것을 명시! (생략 가능)
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    // 편의 메소드
    public void addOrders(Order order) {
        this.getOrders().add(order);
        order.setMember(this);
    }

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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
