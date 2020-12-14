package chpater10_객체지향쿼리언어;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 값 타입 -> 엔티티로 승격
 *
 * ID가 있어서 식별자가 생겨서 이제 마음껏 수정하고 삭제할 수있다.
 * 값 타입 컬렉션 대신에 일대다 관계를 사용하는게 제일 맘 편하다.
 * 일대다 관계를 위한 엔티티 -> AddressEntity 를 만들고
 * 여기서 private Address address; -> 값 타입을 사용한다.
 * 영속성 전이(Cascade.ALL) + orphanRemoval 를 사용해서 값타입 컬렉션 처럼 사용해라
 */
@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

    @Id
    @GeneratedValue
    private Long id; // ID 식별자 생김!!!!

    private Address address; // 값 타입 을 매핑해줄 것이다.

    public AddressEntity() {
    }

    // 이런식으로 값 타입에 생성자를 이용해서 인스턴스를 생성해 넣어준다.
    public AddressEntity(String city, String street, String zipcode) {
        this.address = new Address(city, street, zipcode);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
