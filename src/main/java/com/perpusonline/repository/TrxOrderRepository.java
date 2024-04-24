package com.perpusonline.repository;

import com.perpusonline.domain.TrxOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TrxOrderRepository extends JpaRepository<TrxOrder, Integer> {
}
