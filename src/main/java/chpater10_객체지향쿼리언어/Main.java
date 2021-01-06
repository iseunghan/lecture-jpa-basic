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
             * 서브 쿼리
             *  - 쿼리 안에 또 다른 쿼리
             *
             *  서브 쿼리 지원 함수
             *  - exists : 서브쿼리에 결과가 존재하면 참
             *  - ALL, ANY, SOME : ALL은 모두 만족하면 참,
             *                   ANY, SOME은 조건을 하나라도 만족하면 참
             *  - IN : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
             */
            /* 나이가 평균보다 많은 회원
            *   서브 쿼리를 보면 Member m 을 가져와서 사용하지 않고, 새로운 m2를 만들어서 조회를 했다.
            * (이게 성능이 잘 나오게 된다.)
            */
            String sql = "select m from Member m " +
                    "where m.age > (select avg(m2.age) from Member m2)";

            /* 한 건이라도 주문한 고객 */
//            String sql2 = "select m from Member m " +
//                    "where (select count(o) from Order o where m = o.member) > 0";

            /* 서브쿼리 지원 함수 */
            // 팀A에 속한 멤버 exists가 리턴하는 값이 true인 경우에만 메인 쿼리를 실행한다는 뜻!
            String sup1 = "select m from Member m where exists (select t from m.team t where t.name = 'teamA')";

            // 전체 상품 각각의 재고보다 주문량이 많은 주문들
            String sup2 = "select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)";

            // 어떤 팀이든 팀에 소속된 회원
            String sup3 = "select m from Member m where m.team = ANY(select t from Team t)";


//            List<Member> resultList = em.createQuery(sql, Member.class)
//                    .getResultList();


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