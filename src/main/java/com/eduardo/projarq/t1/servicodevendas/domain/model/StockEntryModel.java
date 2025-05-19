package com.eduardo.projarq.t1.servicodevendas.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class StockEntryModel {
    @EqualsAndHashCode.Include
    private Long id;

    private String productCode;
    private int quantity;
    private LocalDateTime arrivalDate;
}


