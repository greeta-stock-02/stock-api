package net.greeta.stock.restaurant.rest.dto;

import net.greeta.stock.restaurant.model.OrderItem;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

public record RestaurantOrderResponse(String id, String customerName, String customerAddress, String status,
                                      BigDecimal total, ZonedDateTime createdAt, Set<OrderItem> items) {
}
