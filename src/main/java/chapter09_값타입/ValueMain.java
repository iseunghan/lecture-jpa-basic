package chapter09_값타입;

public class ValueMain {
    public static void main(String[] args) {
        int a = 10;
        int b = 10;

        Address address = new Address("city", "street", "zipcode");
        Address address1 = new Address("city", "street", "zipcode");

        System.out.println("a == b: " + (a == b));//true
        System.out.println("address == address1: " + (address == address1));//false
        System.out.println("address equals address1: " + (address.equals(address1)));//false
        // 하지만, equals() 메소드를 재정의 해주면 -> true가 된다.

    }
}
