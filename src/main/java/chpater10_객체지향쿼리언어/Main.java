package chpater10_객체지향쿼리언어;

import chapter09_값타입.Address;
import chapter09_값타입.AddressEntity;

import javax.persistence.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter7"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

        EntityManager em = emf.createEntityManager();
        // code

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.changeTeam(team); /* 편의 메소드 생성함. */

            em.persist(member);

            em.flush();
            em.clear();

            /**
             * inner 조인 - (inner 생략 가능)
             *      select m from Member m (inner) join m.team t"
             * outer 조인 - (outer 생략 가능)
             *      select m from Member m left (outer) join m.team t"
             * 세타 조인
             *      select m from Member m, Team t where m.username = t.name"
             */

            /* teamA인 팀만 필터링 */
//            String qlString = "select m from Member m left join m.team t on t.name = 'teamA'";
            /* 연관관계 없는 엔티티 외부 조인 */
            String qlString = "select m from Member m left join Team t on m.username = t.name";
            /* on절 대신 where절 사용 */
//            String qlString = "select m from Member m left join m.team t where t.name = 'teamA'";
            List<Member> resultList = em.createQuery(qlString, Member.class)
                    .getResultList();// 결과값을 List

            System.out.println("resultList.size : " + resultList.size());


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