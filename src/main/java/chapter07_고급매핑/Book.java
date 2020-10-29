package chapter07_고급매핑;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
/**
 * @DiscriminatorColumn
 * String name() default "DTYPE"; 컬럼명을 바꿀수도 있다 (name = "OOO") 이런식으로
 * 기본적으로 Dtype에는 엔티티 명이 찍히게 되는데, 만약 회사 규정상 Movie -> M 으로 표현을 해야한다면
 * Movie.class 에 @DiscriminatorValue("M") 이런식으로 애노테이션을 추가해주면 된다.
 */
@Entity
@DiscriminatorValue("B")
public class Book extends Item{
    private String author;
    private String isbn;
}
