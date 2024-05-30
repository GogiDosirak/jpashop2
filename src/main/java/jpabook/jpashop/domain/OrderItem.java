package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        // 할인같은게 있을수도 있기 때문에 orderPrice를 따로 가져가는게 맞음
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 재고 - 해줘야함
        return orderItem;

    }

    // 비즈니스 로직
    public void cancel() {
        getItem().addStock(count); // 재고를 다시 주문수량만큼 늘려줘야함
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

    protected OrderItem() {};
}
