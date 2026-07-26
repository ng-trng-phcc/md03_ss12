package com.example.medicalsuppliesmanagement.model.dto.res;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TopExportResponse {
    private String topSupplyName;
    private Integer totalExportQuantity;
}
