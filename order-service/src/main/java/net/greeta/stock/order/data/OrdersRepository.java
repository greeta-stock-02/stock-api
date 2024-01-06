/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.greeta.stock.order.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository <OrderEntity, String>{
	OrderEntity findByOrderId(String orderId);
}