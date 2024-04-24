package com.perpusonline.repository;

import com.perpusonline.domain.MstBook;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface MstBookRepository extends JpaRepository<MstBook, Integer> {
    @Query(value = "select * from MST_BOOK", nativeQuery = true)
    MstBook findAllBy(@Param("engineNumber") String engineNumber);
}