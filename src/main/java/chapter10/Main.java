package chapter10;

import javax.persistence.*;
import java.util.Collection;
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
            member.setUsername("관리자1");
            member.setAge(10);
            member.changeTeam(team); /* 편의 메소드 생성함. */
            member.setType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setAge(10);
            member2.changeTeam(team); /* 편의 메소드 생성함. */
            member2.setType(MemberType.ADMIN);

            em.flush();
            em.clear();

            /**
             * 경로 표현식 (왠만하면 사용하지 마라)
             *  - 상태 필드 : m.username
             *      - 경로 탐색의 끝 지점. 더 이상 탐색 X
             *  - 단일 값 연관 경로 : m.team
             *      - 묵시적 내부 조인이 발생(성능에 큰 지장을 줌), 탐색은 team.username 이런식으로 가능.
             *  - 컬렉션 값 연관 경로 : t.members
             *      - 묵시적 내부 조인이 발생(성능에 큰 지장을 줌), 더 이상의 탐색은 불가. 가능하다면 t.members.size 정도까지.
             *      - 탐색을 더 하고 싶다면 FROM 절에서 명시적 조인을 통해 별칭 얻어서 탐색 가능
             *      ex) SELECT m.username FROM Team t JOIN t.members m
             *
             */
            String query = "select m.username from Team t join t.mebmers m";

            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList) {
                System.out.println("s = " + s);
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