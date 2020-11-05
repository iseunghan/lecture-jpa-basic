package chapter08_프록시_연관관계관리;

import javassist.expr.Instanceof;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 프록시
 */
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter7"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

        EntityManager em = emf.createEntityManager();
        // code

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            /**
             * (가급적 지연 로딩만 사용! (특히 실무에서) )
             * 지연 로딩 LAZY 를 사용하면 프록시 객체를 가져옴
             * 즉시 로딩 EAGER를 사용하게 되면 실제 객체를 가져오게 된다.
             *
             * - 즉시 로딩을 사용 하면 안되는 이유
             *      - 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생한다.
             *      - 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.
             *      - @ManyToOne, @OneToOne은 기본이 즉시 로딩 -> LAZY로 설정
             *      - @OneToMany, @ManyToMany는 기본이 지연 로딩
            */
            Member member1 = new Member();
            member1.setUsername("hello");
            em.persist(member1);

            Team team = new Team();
            team.setName("team1");
            member1.setTeam(team);
            em.persist(team);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member1.getId());
            Team m_team = m.getTeam(); // 프록시 객체를 주기 때문에, 이때는 쿼리가 나가지 않는다.
            System.out.println("m = " + m.getClass());
            System.out.println("team = " + m_team.getClass());
            System.out.println("===============");
            m_team.getName();   // 이 시점에 쿼리가 나가게 된다! (DB조회)
            System.out.println("===============");

            /**
             *  JPQL N+1 문제를 fetch join으로 부분적으로 해결시킬수 있다!
             *  fetch join 을 사용하면 한방 쿼리를 날려서, 이후에 데이터를 조회해도 쿼리가 날라가지 않는다!
            */
            List<Member> list = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .getResultList();

            tx.commit(); // 이때 쌓아뒀던 쿼리를 한방에 날린다.
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close(); // 항상 꼭 마지막에 닫아줘야함!
    }

    // Member와 Team 을 땡겨올땐 아주 유용하지만, Member만 필요로 할 경우에 아주 큰 자원 낭비가 발생한다. 그러므로 printMember를 따로 생성한다.
    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getUsername());
    }
}
