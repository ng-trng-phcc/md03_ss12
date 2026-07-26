package com.example.medicalsuppliesmanagement.model.service.impl;

import com.example.medicalsuppliesmanagement.exception.InsufficientStockException;
import com.example.medicalsuppliesmanagement.exception.ResourceNotFoundException;
import com.example.medicalsuppliesmanagement.model.dto.req.AddSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.req.UpdateSupplyRequest;
import com.example.medicalsuppliesmanagement.model.dto.res.DailyExportResponse;
import com.example.medicalsuppliesmanagement.model.dto.res.TopExportResponse;
import com.example.medicalsuppliesmanagement.model.entity.Supply;
import com.example.medicalsuppliesmanagement.model.entity.Transaction;
import com.example.medicalsuppliesmanagement.model.repository.ISupplyRepository;
import com.example.medicalsuppliesmanagement.model.repository.ITransactionRepository;
import com.example.medicalsuppliesmanagement.model.service.ISupplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class SupplyServiceImpl implements ISupplyService {
    private final ISupplyRepository supplyRepository;
    private final ITransactionRepository transactionRepository;

    private static final Logger historyLog = LoggerFactory.getLogger("history");

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
        Supply supply = supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với id: " + id));

        if (request.getName() != null) supply.setName(request.getName());
        if (request.getSpecification() != null) supply.setSpecification(request.getSpecification());
        if (request.getProvider() != null) supply.setProvider(request.getProvider());

        return supplyRepository.save(supply);
    }

    @Override
    @Transactional
    public void deleteSupply(Long id) {
        Supply supply = supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với id: " + id));

        supply.setIsDeleted(true);
        supplyRepository.save(supply);
    }

    @Override
    public List<Supply> getAllSupplies() {
        List<Supply> supplies = supplyRepository.findByIsDeletedFalse();
        log.debug("Truy vấn danh sách vật tư - Số lượng bản ghi: {}", supplies.size());
        return supplies;
    }

    @Override
    public List<Supply> searchSupplies(String name) {
        List<Supply> supplies = supplyRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);

        if (supplies.isEmpty()) {
            log.info("Không tìm thấy vật tư nào khớp với từ khóa: {}", name);
        }

        return supplies;
    }

    @Override
    @Transactional
    public Supply exportSupply(Long id, Integer amount) {
        Supply supply = supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với id: " + id));

        if (supply.getQuantity() < amount) {
            log.error("Thất bại khi xuất kho ID [{}]: Yêu cầu [{}], hiện có [{}]", id, amount, supply.getQuantity());
            throw new InsufficientStockException("Số lượng tồn kho không đủ để xuất");
        }

        supply.setQuantity(supply.getQuantity() - amount);
        Supply savedSupply = supplyRepository.save(supply);

        Transaction transaction = Transaction.builder()
                .supplyId(id)
                .type("EXPORT")
                .quantity(amount)
                .build();
        transactionRepository.save(transaction);

        log.info("Xuất kho thành công ID [{}], số lượng [{}], tồn mới [{}]", id, amount, savedSupply.getQuantity());
        return savedSupply;
    }

    @Override
    @Transactional
    public Supply importSupply(Long id, Integer amount) {
        Supply supply = supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với id: " + id));

        Integer oldQuantity = supply.getQuantity();
        supply.setQuantity(oldQuantity + amount);
        Supply savedSupply = supplyRepository.save(supply);

        Transaction transaction = Transaction.builder()
                .supplyId(id)
                .type("IMPORT")
                .quantity(amount)
                .build();
        transactionRepository.save(transaction);

        historyLog.info("Nhập kho ID [{}], số lượng [+{}], tồn cũ [{}]", id, amount, oldQuantity);
        return savedSupply;
    }

    @Override
    public List<DailyExportResponse> getDailyExportStatistics() {
        log.info("Bắt đầu thống kê vật tư xuất kho trong ngày");

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<Object[]> results = transactionRepository.getDailyExportStatistics(startOfDay, endOfDay);
        List<DailyExportResponse> responseList = new ArrayList<>();

        for (Object[] row : results) {
            Long supplyId = (Long) row[0];
            Long totalQuantity = (Long) row[1];

            Supply supply = supplyRepository.findById(supplyId).orElse(null);
            String supplyName = (supply != null) ? supply.getName() : "Không xác định";

            responseList.add(DailyExportResponse.builder()
                    .supplyId(supplyId)
                    .supplyName(supplyName)
                    .totalExportQuantity(totalQuantity.intValue())
                    .build());
        }

        log.info("Hoàn thành thống kê vật tư xuất kho trong ngày - Số lượng: {}", responseList.size());
        return responseList;
    }

    @Override
    public TopExportResponse getTopExportStatistics() {
        long transactionCount = transactionRepository.countByType("EXPORT");

        if (transactionCount == 0) {
            throw new ResourceNotFoundException("Chưa có dữ liệu giao dịch để thống kê");
        }

        List<Object[]> results = transactionRepository.findTopExportSupply();

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("Chưa có dữ liệu giao dịch để thống kê");
        }

        Object[] topRow = results.get(0);
        Long supplyId = (Long) topRow[0];
        Long totalQuantity = (Long) topRow[1];

        Supply supply = supplyRepository.findById(supplyId).orElse(null);
        String topSupplyName = (supply != null) ? supply.getName() : "Không xác định";

        return TopExportResponse.builder()
                .topSupplyName(topSupplyName)
                .totalExportQuantity(totalQuantity.intValue())
                .build();
    }
}
