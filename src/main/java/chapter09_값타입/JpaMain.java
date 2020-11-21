package chapter09_값타입;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {
        public static void main(String[] args) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter7"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

            EntityManager em = emf.createEntityManager();
            // code

            EntityTransaction tx = em.getTransaction();

            tx.begin();

            try {
                Address address = new Address("city", "street", "zipcode");

                Member member = new Member();
                member.setUsername("member");
                member.setHomeAddress(address);
                em.persist(member);

                Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

                Member member1 = new Member();
                member1.setUsername("member1");
                member1.setHomeAddress(copyAddress);
                em.persist(member1);

                /**
                 * 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험하다!!
                 * 부작용(side effect) 발생
                 *
                 *  member.city = "city"
                 *  member1.city = "city"
                 */
                // -------------------------------------------
                member.getHomeAddress().setCity("newCity");

                /**
                 * 임베디드 처럼 직접 정의한 값 타입은 자바에서의 기본 타입(primitive Type)이 아니라, 객체 타입이다.
                 * 그러므로, 값이 복사가 되는게 아니라 -> 주소 값이 복사가 되므로 member1의 값을 바꾸면 member의 값이 함께 바뀌어 버리는것이다!
                 *
                 * member.city = "newCity"
                 * member1.city = "newCity"
                 * member.city값만 newCity로 바꾸려했지만, 의도치 않게 member1까지 바꿔버렸다.
                 *
                 * 이럴땐, Address 객체를 복사해서 member1에 따로 넣어주도록 하자!
                 *
                 * <불변 제약 설정!>
                 * 아니면 생성자로만 값을 생성하고, setter를 만들지 않거나, setter를 private으로 선언하자!
                 */

                /**
                 * 만약 값을 바꾸고 싶다면??
                 * 귀찮지만, 새로 생성해서 넣어줘야함.
                 *
                 * ex) Address address = new Address("city" , "street", "zipcode");
                 * member.setHomeAddress(address);
                 * !-- 여기서 내가 city를 newCity로 바꾸고 싶다! --!
                 * Address newAddress = new Address("newCity", address.getStreet(), address.getZipcode() );
                 * member.setHomeAddress(newAddress);
                 * 이런식 으로 변경해 줘야만 함.
                 *  귀찮은 것 같아도 이렇게 하지 않으면 나중에 큰 문제가 생길 수 있음.
                 */



                tx.commit(); // 이때 쌓아뒀던 쿼리를 한방에 날린다.
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            } finally {
                em.close();
            }

            emf.close(); // 항상 꼭 마지막에 닫아줘야함!
        }
}