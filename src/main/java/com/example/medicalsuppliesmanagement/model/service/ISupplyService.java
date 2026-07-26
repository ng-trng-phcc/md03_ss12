package com.example.medicalsuppliesmanagement.model.service;

import com.example.medicalsuppliesmanagement.model.dto.req.AddSupplyRequest;
import com.example.medicalsuppliesmanagement.model.entity.Supply;

public interface ISupplyService {
    Supply addSupply(AddSupplyRequest request);
}
