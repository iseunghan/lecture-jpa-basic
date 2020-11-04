package chapter08_프록시_연관관계관리;

import javassist.expr.Instanceof;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
            Member member1 = new Member();
            member1.setUsername("hello");

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("hello2");

            em.persist(member2);

            em.flush();
            em.clear();

            /*Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember.getId());
            System.out.println("findMember = " + findMember.getUsername());*/
            /**
             * Proxy 객체의 초기화 과정!
             * 1) Member.getName()을 호출하면, Member target에는 처음에 값이 null이기 때문에
             * 2) 영속성컨텍스트에 실제 값을 달라고 요청을 한다.
             * 3) 그러면 db를 조회해서 진짜 객체를 가져와 엔티티를 생성하게 되고, target은 그 실제 엔티티를 가리키게 된다.
             * 4) 이제 getName()은 target.getName()(..실제론, member1.getName()..)이 호출되어서 우리한테 값이 전달되는 것이다
             */
            Member findMember = em.getReference(Member.class, member1.getId()); // 이 시점에는 select쿼리가 안나간다! 가짜 객체이기 때문에!
            System.out.println("findMember = " + findMember.getClass()); // "class chapter08_프록시_연관관계관리.Member$HibernateProxy$waCRsb9J" HibernateProxy.. 라고 찍히게 된다. (프록시가 만든 가짜 클래스라는 말이다!)
            System.out.println("findMember = " + findMember.getId());
            System.out.println("findMember = " + findMember.getUsername()); // 실제로 findMember를 사용하는 시점에!! 가짜 객체 이기 때문에, select쿼리가 날라가게 된다!
            System.out.println("findMember = " + findMember.getUsername()); // 한번 초기화를 한 프록시는 이후에는 초기화 없이 쿼리 없이 사용 가능하다!

            /**
             * 프록시 객체와 타입 비교시 절대 == 비교 금지!!!! instance of 를 사용해라!
             */
            Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.getReference(Member.class, member2.getId());
            System.out.println("m1, m2 : " + (m1 instanceof Member));
            System.out.println("m1, m2 : " + (m2 instanceof Member));

            /**
             * 영속성 컨텍스트에 엔티티가 이미 올라가있으면? em.getReference()를 호출해 proxy객체가 아닌 실제 객체가 반환됨!!!
             *  왜 그럴까?? em.find를 했을때 이미 영속성 컨텍스트에는 Member엔티티가 올라가 있게 된다.
             *  근데 거기서 굳이 있는데도 proxy객체로 가져오면 성능상 이점도 없는 이유 하나와..
             *  jpa에서는 자바 컬렉션 비교 처럼 == 비교가 항상 true가 나오는것을 보장해야 하기 때문이다.
             *  그리하여 proxy객체를 가져오지 않고, 실제 객체를 가져오게 되는것이다.
             */
            Member member = em.find(Member.class, member1.getId());
            Member reference = em.getReference(Member.class, member1.getId());
            System.out.println("a = a : " + (member == reference)); // true

            /**
             * em.getReference 후에 em.find를 하게 되면??
             * 우리 생각으로는 em.getReference를 하면 proxy객체, em.find를 하면 Member객체가 나와야한다고 생각한다.
             * 하지만, jpa는 ==비교를 참인 결과를 보장해줘야 하기 때문에, em.find를 해도 proxy객체를 반환시켜준다.
             */
            Member refMember = em.getReference(Member.class, member.getId());
            Member findMember1 = em.find(Member.class, member.getId());
            System.out.println("refMember = " + refMember.getClass()); // proxy객체
            System.out.println("findMember1 = " + findMember1.getClass()); // proxy객체
            System.out.println("refMember == findMember1 : " + (refMember == findMember1)); // true

            em.flush();
            em.clear();
            /**
             * org.hibernate.LazyInitializationException
             *      -> em.getReference 이후에 영속성 컨텍스트를 clear, close, detach 를 하고 난뒤에
             *      reference를 사용하여 접근을 시도하면 Exception이 터지게 된다.
             */
            Member reference1 = em.getReference(Member.class, member1.getId());
            System.out.println("reference1 : " + reference1.getClass());

//            em.detach(reference1);
//            em.clear();
//            em.close();  하이버네이트가 업데이트가 되면서, 트랜잭션이 살아있으면 em.close()를 호출해도 완전히 닫히지 않은 읽기 가능 상태가 됩니다. 그리하여 조회가 가능하당

            System.out.println("reference1.name = " + reference1.getUsername());






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
