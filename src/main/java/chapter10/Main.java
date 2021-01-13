package chapter10;

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
            member.setType(MemberType.ADMIN);

            em.persist(member);

            em.flush();
            em.clear();

            String sql = "select m.username, 'HELLO', true From Member m where m.type = chapter10.MemberType.ADMIN";

            List<Object[]> result = em.createQuery(sql)
                    .getResultList();

            for (Object[] objects : result) {
                System.out.println(objects[0]);
                System.out.println(objects[1]);
                System.out.println(objects[2]);
            }


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