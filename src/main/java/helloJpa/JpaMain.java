package helloJpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 저장 persist :
 *             Member member = new Member();
 *             member.setId(1L);
 *             member.setName("Hello1");
 *
 *             em.persist(member);
 *
 * 조회 select :
 *             Member findMember = em.find(Member.class, 1L);
 *             System.out.println("findMember.id = " + findMember.getId());
 *             System.out.println("findMember.name = " + findMember.getName());
 *
 * 삭제 delete :
 *             Member findMember = em.find(Member.class, 1L);
 *             em.remove(findMember);
 *
 * 수정 update :
 *             Member findMember = em.find(Member.class, 1L);
 *             findMember.setName("HelloJPA");
 *             em.persist(findMember) -> X 할 필요 없다. 알아서 update query를 날려줘서 db에 반영이 된다.
 */
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

        EntityManager em = emf.createEntityManager();
        // code

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            // JPQL : 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
            List<Member> resultList = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();
            for (Member member : resultList) {
                System.out.println("member.getName() = " + member.getName());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close(); // 항상 꼭 마지막에 닫아줘야함!
    }
}
