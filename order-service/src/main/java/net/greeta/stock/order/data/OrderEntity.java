/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.greeta.stock.order.data;

import net.greeta.stock.order.model.OrderStatus;
import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 5313493413859894403L;

    @Id
    @Column(unique = true)
    public String orderId;
    private String productId;
    private int quantity;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
