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
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChile(child1);
            parent.addChile(child2);

            /**
             * CASCADE
             * - ALL : 모두 적용 (lifecycle이 같을때)
             * - PERSIST : 저장
             *
             * 코드 :
             *      @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
             *      private Parent parent;
             *      부모 엔티티를 저장할때, 자식 엔티티도 함께 저장이 된다!!
             *      parent가 persist가 되면 연관관계가 설정된 child가 함께 persist가 된다.
             */
            em.persist(parent); // cascadeType.ALL 을 사용하면, 부모 엔티티만 persist해도 된다!
//            em.persist(child1); 이렇게 매번 persist를 하기가 귀찮으니까 -> cascade옵션을 사용하면 된다.
//            em.persist(child2);

            em.flush();
            em.clear();

            /**
             * 고아 객체 제거 : 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
             * (부모 엔티티에서 자식 엔티티 컬렉션에서 자식 엔티티를 삭제하면 해당 엔티티를 삭제를 시켜준다!
             * -> 원래는 연관관계의 주인이 아닌 Parent가 orphanremoval로 인해 삭제가 가능해진 것이다.)
             * cascadeType.REMOVE 와 유사하게 동작
             * (주의!) 특정 엔티티가 개인 소유할 때 사용!
             * - 방법 :
             *      orphanRemoval = true 설정
             * - 코드 :
             *      Parent p1 = em.find(Parent.class, id);
             *      p1.getChildList().remove(0);
             *      이때! orphanRemoval에 의해 child 엔티티까지 삭제가 돼버린다.
             */
            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);
//            em.remove(findParent); parent 자체를 삭제시켜도 orphanremoval가 작동된다.
            /**
             * CascadeType.ALL , orphanRemoval = true 를 둘다 사용하게 되면,,,, "부모 엔티티를 통해서 자식의 생명주기를 관리 할 수 있다."
             * 원래 연관관계 주인인 Child에서의 parent객체가 수정 삭제를 할수 있는데...
             * 저 두 옵션을 사용함으로써 어떻게 보면 부모 엔티티인 Parent가 Cascade로 영속화, orphanremoval로 삭제를
             * 하고 있다.
             */

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
