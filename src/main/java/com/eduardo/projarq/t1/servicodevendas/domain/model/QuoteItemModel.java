package com.eduardo.projarq.t1.servicodevendas.domain.model;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class QuoteItemModel {

    @EqualsAndHashCode.Include
    private Long id;

    private String quoteCode;
    private String productCode;
    private int quantity;
    private BigDecimal totalValue;
    private BigDecimal discount;
}