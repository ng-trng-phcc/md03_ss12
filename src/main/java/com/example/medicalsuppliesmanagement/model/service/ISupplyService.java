package com.example.medicalsuppliesmanagement.model.service;

import com.example.medicalsuppliesmanagement.model.dto.req.AddSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.req.UpdateSupplyRequest;
import com.example.medicalsuppliesmanagement.model.entity.Supply;
import org.springframework.http.ResponseEntity;

public interface ISupplyService {
    Supply addSupply(AddSupplyRequest request);
    Supply updateSupply(Long id, UpdateSupplyRequest request);
}
