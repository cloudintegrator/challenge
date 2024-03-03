package com.ag.app.dao;

import com.ag.app.entity.MedData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedDataRepository extends JpaRepository<MedData, Integer> {
}
