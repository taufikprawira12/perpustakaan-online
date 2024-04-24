package com.perpusonline.repository;

import com.perpusonline.domain.MstMember;
import com.perpusonline.domain.TrxOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TrxOrderRepository extends JpaRepository<TrxOrder, Integer> {
    @Query(value = "select * from trx_order where id_book=:idBook and id_member=:idMember", nativeQuery = true)
    TrxOrder findByIdBook(@Param("idBook") Integer idBook, @Param("idMember") String idMember);
}
