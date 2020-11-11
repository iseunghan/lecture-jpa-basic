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
                Member member = new Member();
                member.setUsername("hello");
                member.setHomeAddress(new Address("city", "street", "zipcode"));
                member.setWorkAddress(new Address("work_city", "work_street", "work_zipcode"));
                member.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now()));
                em.persist(member);

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