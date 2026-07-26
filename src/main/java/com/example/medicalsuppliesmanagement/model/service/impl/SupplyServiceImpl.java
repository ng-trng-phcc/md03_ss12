package com.example.medicalsuppliesmanagement.model.service.impl;

import com.example.medicalsuppliesmanagement.model.dto.req.AddSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.req.UpdateSupplyRequest;
import com.example.medicalsuppliesmanagement.model.entity.Supply;
import com.example.medicalsuppliesmanagement.model.repository.ISupplyRepository;
import com.example.medicalsuppliesmanagement.model.service.ISupplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplyServiceImpl implements ISupplyService {
    private final ISupplyRepository supplyRepository;

    @Override
    public Supply addSupply(AddSupplyRequest request) {
        Supply supply = Supply.builder()
                .name(request.getName())
                .provider(request.getProvider())
                .unitOfCalculation(request.getUnitOfCalculation())
                .build();

        Supply savedSupply = supplyRepository.save(supply);

        log.info("Đã tạo mới vật tư: [{}] với ID: [{}]", supply.getName(), savedSupply.getId());
        return savedSupply;
    }

    @Override
    public Supply updateSupply(Long id, UpdateSupplyRequest request) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vật tư với id: " + id));

        if (request.getName() != null) supply.setName(request.getName());
        if (request.getSpecification() != null) supply.setSpecification(request.getSpecification());
        if (request.getProvider() != null) supply.setProvider(request.getProvider());

        return supplyRepository.save(supply);
    }


}
