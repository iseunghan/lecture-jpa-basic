package chpater10_객체지향쿼리언어;

import chapter09_값타입.Address;
import chapter09_값타입.AddressEntity;

import javax.persistence.*;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter7"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

        EntityManager em = emf.createEntityManager();
        // code

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query1 = em.createQuery("select m.username from Member m", String.class); // username은 String이므로 String.class
            Query query2 = em.createQuery("select m.username, m.age from Member m"); // int. string 이 섞여 있어서 타입 정보를 받아 올 수 없음.


            TypedQuery<Member> query3 = em.createQuery("select m from Member m where m.username =:username", Member.class);
            query3.setParameter("username", "member1");
            Member singleResult = query3.getSingleResult();
            System.out.println("singleResult = " + singleResult.getUsername());

            Member result = em.createQuery("select m from Member m where m.username =:username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("result = " + result.getUsername());

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
