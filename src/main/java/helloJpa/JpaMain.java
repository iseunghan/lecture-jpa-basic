package helloJpa;

import javax.persistence.*;
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

            // 영속
            Member member1 = new Member();
            member1.setId(5L);
            member1.setName("member52");
            Member member2 = new Member();
            member2.setId(6L);
            member2.setName("member53");
            Member member3 = new Member();
            member3.setId(7L);
            member3.setName("member54");

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            em.flush();

            System.out.println("===========================");
            tx.commit(); // 이때 쌓아뒀던 쿼리를 한방에 날린다.
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close(); // 항상 꼭 마지막에 닫아줘야함!
    }
}
