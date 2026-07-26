package com.example.medicalsuppliesmanagement.controller;

import com.example.medicalsuppliesmanagement.model.dto.req.AddSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.req.AmountRequest;
import com.example.medicalsuppliesmanagement.model.dto.req.UpdateSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.res.ApiResponse;
import com.example.medicalsuppliesmanagement.model.dto.res.DailyExportResponse;
import com.example.medicalsuppliesmanagement.model.dto.res.TopExportResponse;
import com.example.medicalsuppliesmanagement.model.entity.Supply;
import com.example.medicalsuppliesmanagement.model.service.ISupplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
        supplyService.deleteSupply(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Supply>>> getAllSupplies() {
        List<Supply> supplies = supplyService.getAllSupplies();
        return ResponseEntity.ok(ApiResponse.success(supplies));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Supply>>> searchSupplies(@RequestParam("name") String name) {
        List<Supply> supplies = supplyService.searchSupplies(name);
        return ResponseEntity.ok(ApiResponse.success(supplies));
    }

    @PatchMapping("/{id}/export")
    public ResponseEntity<Supply> exportSupply(@PathVariable Long id,
                                               @RequestBody @Valid AmountRequest request) {
        Supply updated = supplyService.exportSupply(id, request.getAmount());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/import")
    public ResponseEntity<Supply> importSupply(@PathVariable Long id,
                                               @RequestBody @Valid AmountRequest request) {
        Supply updated = supplyService.importSupply(id, request.getAmount());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/statistics/daily-export")
    public ResponseEntity<ApiResponse<List<DailyExportResponse>>> getDailyExportStatistics() {
        List<DailyExportResponse> stats = supplyService.getDailyExportStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/statistics/top-export")
    public ResponseEntity<TopExportResponse> getTopExportStatistics() {
        TopExportResponse response = supplyService.getTopExportStatistics();
        return ResponseEntity.ok(response);
    }
}
