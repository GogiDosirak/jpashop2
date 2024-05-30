package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional // 데이터 변경 필요
    public Long order(Long memberId, Long itemId, int count) {
        // 주문할 때 회원명, 상품명, 주문수량이 필요하므로 id값을 파라미터로 받음
        // 꺼내와야 되므로 Member, Item 리포지토리가 필요
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성 (간단하게 그냥 Member에서 받아옴)
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성 (생성메소드 사용)
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // 생성 메소드를 통해서만 생성해야하므로, OrderItem orderItem = new OrderItem();
        // 해서 .setPrice() 이런식으로 하는걸 방지해야함
        // => 기본생성자를 protected로 만들어놓으면 이를 막을 수 있음

        // 주문 생성 (생성메소드 사용)
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        // cascade로 매핑해놨기 떄문에 order만 save로 persist해줘도
        // delivery, orderItem 모두 persist됨
        // delivery와 orderItem은 order에서만 참조하기 때문에 cascade를 사용 가능 (라이프 사이클이 같은경우)
        // 만약, 얘네들이 매우 중요한 애들이고 다른데서도 쓴다면 cascade쓰면 안됨
        // -> 별도의 repository 생성해서 persist 다 해줘야함

        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) { // 취소할때 아이디값만 필요
        Order order = orderRepository.findOne(orderId); // 아이디로 Order찾아와서
        order.cancel();
        // order.cancel() -> Status를 cancel로 바꾸고 orderItem.cancel() -> 재고증가

    }


    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
