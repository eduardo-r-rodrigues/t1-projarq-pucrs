package com.eduardo.projarq.t1.servicodevendas.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class QuoteModel {

    @EqualsAndHashCode.Include
    private Long id;

    private String code;
    private LocalDateTime createdAt;
    private String customerName;
    private String state;
    private String country;

    private List<QuoteItemModel> items;

    private BigDecimal totalItemsValue;
    private BigDecimal stateTax;
    private BigDecimal federalTax;
    private BigDecimal totalDiscount;
    private BigDecimal finalValue;

    private QuoteStatus status;

}
