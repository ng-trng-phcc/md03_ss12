package com.example.medicalsuppliesmanagement.model.repository;

import com.example.medicalsuppliesmanagement.model.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISupplyRepository extends JpaRepository<Supply, Long> {
}
