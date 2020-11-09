package jpabook.jpashop;


import com.sun.tools.corba.se.idl.constExpr.Or;
import jpabook.jpashop.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaShopMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop"); //persistenceName : resource/META-INF/persistence.xml 파일에 설정한 name

        EntityManager em = emf.createEntityManager();
        // code

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setName("이승한");
            member.setCity("Daejeon");
            member.setStreet("Cheong-Sa-Ro 1");
            member.setZipcode("3xxxx");
            em.persist(member);

            Member member1 = new Member();
            member1.setName("양준혁");
            member1.setCity("Daejeon");
            member1.setStreet("Cheong-Sa-Ro 2");
            member1.setZipcode("4xxxx");
            em.persist(member1);

            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.ORDER);
            member.addOrders(order);
            member1.addOrders(order);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderPrice(5000);
            orderItem.setCreatedBy("YDG");
            orderItem.setCreatedDate(LocalDateTime.now());
            orderItem.setLastModifiedBy("LLL");
            orderItem.setLastModifiedDate(LocalDateTime.now());
            order.getOrderItems().add(orderItem);
            orderItem.setOrder(order);
            em.persist(order);
            em.persist(orderItem);

            tx.commit(); // 이때 쌓아뒀던 쿼리를 한방에 날린다.
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close(); // 항상 꼭 마지막에 닫아줘야함!
    }
}
