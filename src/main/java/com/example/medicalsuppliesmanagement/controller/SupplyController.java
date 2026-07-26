package com.example.medicalsuppliesmanagement.controller;

import com.example.medicalsuppliesmanagement.model.dto.req.AddSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.req.UpdateSupplyRequest;
import com.example.medicalsuppliesmanagement.model.entity.Supply;
import com.example.medicalsuppliesmanagement.model.service.ISupplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/supplies")
@RequiredArgsConstructor
@Slf4j
public class SupplyController {
    private final ISupplyService supplyService;

    @PostMapping
    public ResponseEntity<Supply> addSupply(@RequestBody @Valid AddSupplyRequest request) {
        return ResponseEntity.ok(
                supplyService.addSupply(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupply(@PathVariable Long id,
                                          @RequestBody UpdateSupplyRequest request) {
        if (!request.getProhibitedFields().isEmpty()) {
            log.warn("Phát hiện dữ liệu cấm: {}", request.getProhibitedFields().keySet());
            return ResponseEntity.badRequest()
                    .body("Không được gửi các trường cấm: " + request.getProhibitedFields().keySet());
        }

        Supply updated = supplyService.updateSupply(id, request);
        return ResponseEntity.ok(updated);
    }
}
