package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")                          //둘 다 서로를 가지고있으면, JPA입장에선 어디가 변경돼야 포린키 값을 바꿔야하는지 헷갈림 -> 주인을 정해서 걔가 변경되면 포린키 값이 변경되게끔 약속, order테이블에 있는 member 필드에 의해 매핑이 되었으므로, member를 적어줌 => 읽기 전용이됨
    private List<Order> orders = new ArrayList<>();




}
