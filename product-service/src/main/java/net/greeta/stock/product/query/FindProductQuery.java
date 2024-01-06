package net.greeta.stock.product.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindProductQuery {

    private final String productId;
}
