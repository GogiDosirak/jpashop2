package jpabook.jpashop.Service;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateDto {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

}
