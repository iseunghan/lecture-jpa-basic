package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order extends BaseEntity{

    @Id  @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    //  이 memberId는 객체지향스럽지 않다. 왜냐하면, order에서 바로 member를 꺼내와서 member.getName..등등
    // 바로 접근할 수 있어야하는데, 이렇게 되버리면 Long으로 id받아서 다시 또 id로 find(Member.class...) 하고......
    // 아래와 같은 방식을 관계형 DB의 맞춤 설계 라고 한다.
    // 이런 데이터 중심 설계의 문제점
    // - 현재 방식은 객체 설계를 테이블 설계에 맞춘 방식
    // - 테이블의 외래키를 객체에 그대로 가져옴
    // - 객체 그래프 탐색이 불가능
    // - 참조가 없으므로 UML도 잘못됨 ( n:m 관계 설정이 다 끊긴다.)

//    @Column(name = "MEMBER_ID")
//    private Long memberId;
    /**
     * 이제는 객체 지향적으로 매핑을 하기 위해서, Long id 대신에 Member 객체를 저장한다.
     * @ManyToOne : Order입장에서는 하나의 회원이 여러개의 주문을 할수 있으니 Many에 해당한다.
     * @JoinColumn : MEMBER_ID와 조인 시킨다.
     */
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    //    @Column(name = "ORDER_DATE") 이런식으로 name을 매핑시켜주는것이 더 좋다!
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) //ORDINAL 사용 금지 !
    private OrderStatus status;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
