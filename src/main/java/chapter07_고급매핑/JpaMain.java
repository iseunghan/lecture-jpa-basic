package chapter07_고급매핑;

import helloJpa.Member;
import helloJpa.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter7"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

        EntityManager em = emf.createEntityManager();
        // code

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Movie movie = new Movie();
            movie.setDirector("aaaa");
            movie.setActor("bbbb");
            movie.setName("바람과함께사라지다");
            movie.setPrice(1000);
            em.persist(movie);

            em.flush();
            em.clear(); // db 영속성 컨텍스트를 날려주고, 1차 캐시를 비워준다. (쿼리를 보기 위함)

            Movie findMovie = em.find(Movie.class, movie.getId()); // jpa가 알아서 join을 해서 값을 가져온다.
            System.out.println("findMovie = " + findMovie);

            tx.commit(); // 이때 쌓아뒀던 쿼리를 한방에 날린다.
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close(); // 항상 꼭 마지막에 닫아줘야함!
    }
}
