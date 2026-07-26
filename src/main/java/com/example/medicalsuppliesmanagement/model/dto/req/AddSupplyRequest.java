package com.example.medicalsuppliesmanagement.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddSupplyRequest {
    @NotBlank(message = "Tên vật tư không được để trống")
    private String name;

    private String provider;

    private String unitOfCalculation;

}
