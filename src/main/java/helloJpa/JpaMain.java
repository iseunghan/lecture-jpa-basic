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

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("MemberA");
            member.changeTeam(team); // 편의상 컬렉션에 member를 추가하는 코드는 setTeam메소드에 추가해주자.
            em.persist(member);

//            team.getMembers().add(member);
// 만약, 이 코드가 주석처리되고, em.flush, clear를 완전하게 안해주면,em.find(team..) .getMembers를 했을때 아무값도 들어있지 않게 된다.
//그리하여, 1차캐시에서 조회하는 경우를 고려해서, 양쪽다 데이터를 넣어주도록 하자.

            //만약 select 쿼리가 나가는걸 보고 싶을떈 아래 코드를 추가하면 된다.
            em.flush();
            em.clear();

            Team team1 = em.find(Team.class, team.getId());
            List<Member> members = team1.getMembers();

            System.out.println("=================");
            for (Member member1 : members) {
                System.out.println("m = " + member1.getUsername());
            }
            System.out.println("=================");

            tx.commit(); // 이때 쌓아뒀던 쿼리를 한방에 날린다.
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close(); // 항상 꼭 마지막에 닫아줘야함!
    }
}
