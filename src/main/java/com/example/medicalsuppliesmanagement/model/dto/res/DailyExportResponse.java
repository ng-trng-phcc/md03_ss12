package com.example.medicalsuppliesmanagement.model.dto.res;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DailyExportResponse {
    private Long supplyId;
    private String supplyName;
    private Integer totalExportQuantity;
}
