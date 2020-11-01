package chapter07_고급매핑;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @MappedSuperclass : 공통의 매핑 정보가 필요할 때
 * 내가 원하는 테이블에 누가 생성했고(createdBy), 언제 생성했는지(createdByDate), 등등
 * 추가 하고 싶을때, 귀찮음을 덜어주는 애노테이션이다.
 *
 * (*) 이 클래스는 생성해서 사용할 일이 없으므로 추상클래스로 선언!!
 *
 * 방법. BaseEntity라는 클래스를 생성해서 추가하고 싶은 정보들을 넣고, @MappedSuperclass 만 붙여주면 끝!
 * 그리고 추가 하고 싶은 엔티티에 상속을 시켜주면 된다.
 * 예) Book 에 추가하고싶다 -> public class Book extends BaseEntity ..
 */
@MappedSuperclass
public abstract class BaseEntity {

    private String createdBy;
    private LocalDateTime createdByDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedByDate() {
        return createdByDate;
    }

    public void setCreatedByDate(LocalDateTime createdByDate) {
        this.createdByDate = createdByDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
