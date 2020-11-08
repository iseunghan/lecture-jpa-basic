package chapter08_프록시_연관관계관리;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {
    @Id @GeneratedValue
    private Long id;

    private String name;

    /**
     * CASCADE의 종류
     *      - ALL : 모두 적용 (전부 다 lifecycle에 맞춰야할때)
     *      사용해도 되는 조건 2가지
     *          parent와 child의 lifecycle이 유사할때! (등록, 수정 삭제 시점이 같을때)
     *          단일 소유자 일때! (parent만 child를 소유할때)
     *
     *      - PESIST : 영속 (저장할때만 맞춰야할때)
     *      - REMOVE :  삭제
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChile(Child child) {
        childList.add(child);
        child.setParent(this);
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
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
}
