package chpater10_객체지향쿼리언어;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpaMain {
        public static void main(String[] args) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter7"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

            EntityManager em = emf.createEntityManager();
            // code

            EntityTransaction tx = em.getTransaction();

            tx.begin();

            try {

                // Criteria 사용 준비
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Member> query = cb.createQuery(Member.class); // member에 대한 쿼리를 생성할 거야

                // 루트 클래스(조회를 시작할 클래스)
                Root<Member> m = query.from(Member.class);

                // 쿼리 생성
                CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
                List<Member> resultList = em.createQuery(cq).getResultList();


                em.createNativeQuery("select MEMBER_ID,city,street, zipcode, USERNAME from MEMBER").getResultList();

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