package helloJpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // 다대다 매핑은 왠만하면 안쓰는게 최선이다!
    // 첫번째 방법(중간 테이블 생성되는것)
    /*@ManyToMany(mappedBy = "products") // 이렇게 하면 Member_Product 중간 테이블이 생성이 된다.
    private List<Member> members = new ArrayList<>();*/

    // 다대다 매핑 : 두번째 방법
    @OneToMany(mappedBy = "product")
    private List<Member_Product> member_products = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
