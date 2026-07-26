package com.example.medicalsuppliesmanagement.controller;

import com.example.medicalsuppliesmanagement.model.dto.req.AddSupplyRequest;
import com.example.medicalsuppliesmanagement.model.entity.Supply;
import com.example.medicalsuppliesmanagement.model.service.ISupplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supplies")
@RequiredArgsConstructor
public class SupplyController {
    private final ISupplyService supplyService;

    @PostMapping
    public ResponseEntity<Supply> addSupply(@RequestBody @Valid AddSupplyRequest request) {
        return ResponseEntity.ok(
                supplyService.addSupply(request)
        );
    }
}
