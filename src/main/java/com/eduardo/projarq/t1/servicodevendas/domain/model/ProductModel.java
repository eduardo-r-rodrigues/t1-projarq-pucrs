package com.eduardo.projarq.t1.servicodevendas.domain.model;

import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ProductModel {
    @Id
    private Long id;
    private String code;
    private String description;
    private BigDecimal unitPrice;
}
