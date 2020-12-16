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
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            /*// 과연 이 member가 영속성 컨텍스트에 의해 관리가 될까?
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            // 이 member를 가져와서 age를 20으로 바꾸면 바뀔까? -> 바뀐다. select로 조회한 모든 member들을 영속성컨텍스트가 전부 다 관리한다.
            Member findMember = result.get(0);
            findMember.setAge(20);*/

            List resultList = em.createQuery("select m.age, m.username from Member m")
                    .getResultList();

            // 1번째 방법 : Object객체를 Object[] 타입 캐스팅해서 사용하기
            Object o = resultList.get(0);
            Object[] result = (Object[]) o; // object배열에 [m.age , m.username] 이런식으로 들어있다.
            System.out.println("age = " + result[0]);    // age
            System.out.println("username = " + result[1]);    // username

            // 2번째 방법 제네릭에 Object[] 타입 선언
            List<Object[]> resultList1 = em.createQuery("select m.age, m.username from Member m")
                    .getResultList();
            Object[] result1 = resultList1.get(0);
            System.out.println("age = " + result1[0]);
            System.out.println("username = " + result1[1]);

            // 3번째 방법 : 간단한 DTO 생성해서 DTO타입으로 뽑는 방법
            List<MemberDTO> resultList2 = em.createQuery("select new chpater10_객체지향쿼리언어.MemberDTO(m.age, m.username) from Member m", MemberDTO.class)
                    .getResultList();       // m.age, m.username 을 가져올때는 저렇게 마치 생성자를 호출하는 것 처럼 넣어준다.(package명까지 다 적어야하는 귀찮음)
            MemberDTO memberDTO = resultList2.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());

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
