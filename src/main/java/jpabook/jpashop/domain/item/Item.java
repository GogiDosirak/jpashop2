package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속 전략을 적어줘야함
@DiscriminatorColumn(name = "dtype") //구분자를 정해줘야함, 자식 클래스에 별칭 지정 가능(안해주면 엔티티명으로들어감)
@Getter @Setter
public abstract class Item { // 구현체들을 갖고 할거기때문에 추상클래스
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;  //이 셋은 공통속성.. 상속한 애들을 만들면 관리하기 편함

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비지니스 로직 추가
    // stock 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // stock 감소
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        } // 재고가 0보다 적다면 예외를 터트려야함 (사용자 정의 예외)
        this.stockQuantity = restStock;
    }
}
