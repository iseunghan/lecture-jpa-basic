package chapter07_고급매핑;

import javax.persistence.*;

/**
 * @Inheritance - 3가지 전략
 * - JOINED : 조인 전략
 *      @DiscriominatorColumn을 적용해줘서 구별해주는것이 좋다!
 *      장점 :
 *          테이블 정규화
 *          외래키 참조 무결성 제약조건 활용가능
 *          저장공간 효율화
 *      단점 :
 *          조회시 조인을 많이 사용 -> 성능 저하
 *          조회 쿼리가 복잡함
 *
 * - SINGLE_TABLE(기본값) : 단일 테이블 전략
 *      @DiscriminatorColumn 기본적용!
 *      장점 :
 *          조인이 필요가 없으므로 조회 성능이 빠름
 *          조회 쿼리가 단순함
 *      단점 :
 *          자식 엔티티가 매핑한 컬럼은 모두 null 허용
 *          단일 테이블에 모든것을 저장하므로 테이블이 커질 수 있다.
 *
 * - (추천하지 않는 전략!!)TABLE_PER_CLASS : 구현 클래스마다 테이블 전략
 *      @DiscriminatorColumn : 테이블마다 구별되서 생성되었기때문에 필요가 없어진다.
 *      ** Item 클래스를 (abstract)추상클래스로 선언해줘야한다.(안그러면, item 테이블이 생성이됨)
 *      조회(select) 할때가 치명적인 단점이다. UNION SQL (모든 테이블을 조회하기 때문에 비효율적이다.)
 *      장점 :
 *          서브 타입을 명확하게 구분해서 처리할때 효과적
 *          not null 제약조건 사용 가능
 *      단점 :
 *          여러 자식 테이블을 함께 조회할때 성능이 느림(UNION SQL)
 *          자식 테이블을 통합해서 쿼리하기 어려움
 *
 * @DiscriminatorColumn
 * String name() default "DTYPE"; 컬럼명을 바꿀수도 있다 (name = "OOO") 이런식으로
 * 기본적으로 Dtype에는 엔티티 명이 찍히게 되는데, 만약 회사 규정상 Movie -> M 으로 표현을 해야한다면
 * Movie.class 에 @DiscriminatorValue("M") 이런식으로 애노테이션을 추가해주면 된다.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 조인 전략이기 때문에 joined로 설정해주어야한다.
@DiscriminatorColumn // SINGLE_TABLE 전략을 사용하면 기본적으로 DTYPE이 생성되기 때문에 해당 애노테이션은 삭제해도 된다.
public abstract class Item extends BaseEntity{
    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
