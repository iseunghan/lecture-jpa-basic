package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable // 값 타입이라고 명시!
public class Address {

    @Column(length = 10) // 값타입의 장점.이런식으로 공통적으로 관리가 가능하다.
    private String city;
    @Column(length = 20)
    private String street;
    @Column(length = 5)
    private String zipcode;

    // 이런식으로 객체 지향적으로 잘 사용하고, 값타입을 의미 있게 사용할 수 있다.
    public String fullAddress() {
        return getCity() + " " + getStreet() + " " + getZipcode();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    private void setStreet(String street) {
        this.street = street;
    }

    private String getZipcode() {
        return zipcode;
    }

    private void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getStreet(), address.getStreet()) &&
                Objects.equals(getZipcode(), address.getZipcode());
    }

    /**
     * 위에 코드를 보면, 원래 equals를 오버라이딩 했을때는, 필드에 직접 접근했었는데, 오바라이딩 할때 use getters during ..을 체크했더니
     * 각 변수의 getter를 호출해서 불러오고있다. -> 이게 좋은 점이 : 만약 proxy객체일 경우에 getter를 호출하지않으면 실제 객체가 안담겨서 이상한 값이 담기게 된다.
     * proxy객체는 실제 값을 사용할 경우(getter)에 DB에서 조회해서 실제 객체를 담아주기때문에 getter를 사용하는게 좋다!!
     */

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
