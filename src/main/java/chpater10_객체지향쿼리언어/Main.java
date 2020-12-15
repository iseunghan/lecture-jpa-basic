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
            Member member = new Member();
            member.setUsername("son");
            member.setAge(24);
//            em.persist(member);

            Member member1 = new Member();
            member1.setUsername("father");
            member1.setAge(50);
//            em.persist(member1);

            Team team = new Team();
            team.setName("Family");
            team.getMebmers().add(member);
            team.getMebmers().add(member1);
            member.setTeam(team);
            member1.setTeam(team);
            em.persist(team);

            em.flush(); // 쿼리를 다 날리고
            em.clear(); // 영속성 컨텍스트를 초기화 시킨다.

            System.out.println("======================");
            Team findTeam = em.find(Team.class, team.getId());
            findTeam.getMebmers().remove(0);

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
