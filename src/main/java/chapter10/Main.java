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
            member.setUsername("관리자");
            member.setAge(10);
            member.changeTeam(team); /* 편의 메소드 생성함. */
            member.setType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("관리자");
            member2.setAge(10);
            member2.changeTeam(team); /* 편의 메소드 생성함. */
            member2.setType(MemberType.ADMIN);


            em.flush();
            em.clear();

//            String query = "select " +
//                    "case when m.age <= 10 then '학생요금'" +
//                        "when m.age >= 60 then '경로요금'" +
//                        "else '일반요금'" +
//                    "end " +
//                    "from Member m";

            String query = "select group_concat(m.username) from Member m";

            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();


            for (String integer : resultList) {
                System.out.println("integer = " + integer);
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