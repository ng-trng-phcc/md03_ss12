package com.example.medicalsuppliesmanagement.model.dto.req;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateSupplyRequest {
    private String name;

    private String specification;

    private String provider;

    /**
     * Cái này dùng để bắt những field nguy hiểm (id, quantity) nếu user nhập vào body
     * */
    @JsonAnySetter
    @Builder.Default
    private Map<String, Object> prohibitedFields = new HashMap<>();

    public Map<String, Object> getProhibitedFields() {
        if (prohibitedFields == null) {
            prohibitedFields = new HashMap<>();
        }
        return prohibitedFields;
    }
}
