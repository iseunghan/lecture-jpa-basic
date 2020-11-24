package chapter09_값타입;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JpaMain {
        public static void main(String[] args) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter7"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

            EntityManager em = emf.createEntityManager();
            // code

            EntityTransaction tx = em.getTransaction();

            tx.begin();

            try {
                Member member = new Member();
                member.setUsername("member1");
                member.setHomeAddress(new Address("homeCity", "street", "zipcode"));

                member.getFavoriteFoods().add("치킨");
                member.getFavoriteFoods().add("족발");
                member.getFavoriteFoods().add("피자");

                member.getAddressHistory().add(new AddressEntity("old1", "street", "zipcode"));
                member.getAddressHistory().add(new AddressEntity("old2", "street", "zipcode"));

                // member 만 persist 해줘도, 값타입 컬렉션들까지 한번에 다같이 insert 쿼리가 나가게 된다. 왜? 값타입이니까
                // 값 타입 컬렉션은 영속성 전이(CASCADE) + 고아 객체 제거 기능이 필수로 설정 되어있다.
                // OneToMany(Cascade = CASCADETYPE.ALL, OrpharRemoval = true)
                em.persist(member);

                em.flush();
                em.clear();

                System.out.println("===================================");
                Member findMember = em.find(Member.class, member.getId()); // 이때 값 컬렉션에 대해서는 쿼리가 안나간다! 그 말은? -> fetch = LAZY : 지연로딩 전략이라는 뜻!

                /**
                 * 조회
                 */
                /* 실제 값을 사용 할때 쿼리가 나가게 된다!
                List<Address> addressHistory = findMember.getAddressHistory();
                for (Address address : addressHistory) {
                    System.out.println("address = " + address.getCity());
                }

                Set<String> favoriteFoods = findMember.getFavoriteFoods();
                for (String favoriteFood : favoriteFoods) {
                    System.out.println("favoriteFood = " + favoriteFood);
                }*/

                /**
                 * 수정
                 * homeCity -> newCity 로 수정하기
                 */
//                findMember.getHomeAddress().setCity("newCity"); XXX 이렇게 하면 안됨! 값타입은 항상 immutable(불변) 해야하기 때문에 안그러면 side effect(부작용) 발생!
                // 이런식으로 인스턴스를 통째로 갈아 끼워야 한다!!
                findMember.setHomeAddress(new Address("newCitu", findMember.getHomeAddress().getStreet(), findMember.getHomeAddress().getZipcode()));

                // 치킨 -> 한식
                findMember.getFavoriteFoods().remove("치킨");
                findMember.getFavoriteFoods().add("한식");

                // 주소 바꾸기
                // 치명적인 오류!! 이렇게 하면 값 타입 테이블엔 식별자가 따로 없기 때문에 해당 memberId에 대한 테이블을 삭제하고 다시 저장하는 일이 벌어진다.(주의!)
                findMember.getAddressHistory().remove(new Address("old1", "street", "zipcode"));
                findMember.getAddressHistory().remove(new Address("newCity1", "street", "zipcode"));


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