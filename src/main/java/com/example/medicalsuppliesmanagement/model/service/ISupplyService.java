package com.example.medicalsuppliesmanagement.model.service;

import com.example.medicalsuppliesmanagement.model.dto.req.AddSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.req.UpdateSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.res.DailyExportResponse;
import com.example.medicalsuppliesmanagement.model.dto.res.TopExportResponse;
import com.example.medicalsuppliesmanagement.model.entity.Supply;

import java.util.List;

public interface ISupplyService {
    Supply addSupply(AddSupplyRequest request);

    Supply updateSupply(Long id, UpdateSupplyRequest request);

    void deleteSupply(Long id);

    List<Supply> getAllSupplies();

    List<Supply> searchSupplies(String name);

    Supply exportSupply(Long id, Integer amount);

    Supply importSupply(Long id, Integer amount);

    List<DailyExportResponse> getDailyExportStatistics();

    TopExportResponse getTopExportStatistics();
}
