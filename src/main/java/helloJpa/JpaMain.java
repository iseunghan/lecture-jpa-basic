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
            // 비영속 상태
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloJPA");

            // 영속 상태 ! entityManager를 통해 영속성컨텍스트에 접근
            System.out.println("=== BEFORE ===");
            em.persist(member); //이때 쿼리가 나가지 않고, 1차 캐시에 저장.
            System.out.println("=== AFTER ===");

            Member findMember = em.find(Member.class, 101L);
            System.out.println("findMember.id = " + findMember.getId());//이때 조회를 했지만, select쿼리가 안나간다.
            System.out.println("findMember.name = " + findMember.getName());//why? 1차캐시에서 조회했기 때문에

            tx.commit(); // 이때 쌓아뒀던 쿼리를 한방에 날린다.
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close(); // 항상 꼭 마지막에 닫아줘야함!
    }
}
