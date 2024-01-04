/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.greeta.stock.core.events.order;

import lombok.Value;
 
@Value
public class PaymentProcessedEvent {
    private final String orderId;
    private final String paymentId;
}
