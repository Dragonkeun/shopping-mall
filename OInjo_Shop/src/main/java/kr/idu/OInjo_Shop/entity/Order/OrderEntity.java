package kr.idu.OInjo_Shop.entity.Order;

import kr.idu.OInjo_Shop.constant.OrderStatus;
import kr.idu.OInjo_Shop.entity.BaseEntity;
import kr.idu.OInjo_Shop.entity.Member.MemberEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "order")
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;        //한명의 회원의 여러개의 주문을 할 수 있다.

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    //주문 상품 객체를 이용해 주문 객체를 만듦
    public void addOrderItem(OrderItemEntity orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static OrderEntity createOrder(MemberEntity member, List<OrderItemEntity> orderItemList) {
        OrderEntity order = new OrderEntity();

        order.setMember(member);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDER);
        for (OrderItemEntity orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        return order;
    }
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItemEntity orderItem : orderItems)
            totalPrice += orderItem.getTotalPrice();
        return totalPrice;
    }

}