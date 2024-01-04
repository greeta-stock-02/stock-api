package net.greeta.stock.foodordering.order.mapper;

import net.greeta.stock.foodordering.order.model.Order;
import net.greeta.stock.foodordering.order.rest.dto.OrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);
}
