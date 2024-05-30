package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @GeneratedValue
    @Id
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // 실무에선 이넘타입을 무조건 STRING으로 해줘야함 (유지보수 용이)
    private OrderStatus status;

    // 연관관계 편의 메소드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메소드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { //...문법을 사용하면 여러개를 넘길 수 있음
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
        // 이렇게 하면 order가 생성될 때 연관관계가 쫙 걸리면서 생성되고, 나머지 값들도 세팅됨


    }

    // 비즈니스 로직
    // 주문 취소
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) { // 배송완료가 된 상태면 취소되면 안됨
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL); // 위의 Validation을 통과하면 Cancel로 바꿔줌
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); //오더 아이템별로 cancel 날려줘야함 -> orderItem에 메소드 생성
        }
    }

    // 조회 로직
    // 전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice(); //count * price 메소드 따로 생성
        }
        return totalPrice;
    }

    protected Order() {}
}
