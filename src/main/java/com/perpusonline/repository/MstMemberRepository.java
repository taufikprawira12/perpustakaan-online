package com.perpusonline.repository;

import com.perpusonline.domain.MstMember;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public interface MstMemberRepository extends JpaRepository<MstMember, Integer> {
    @Query(value = "select * from mst_member where email = :email", nativeQuery = true)
    MstMember findMstMemberByEmail(@Param("email") String email);

    @Query(value = "select * from mst_member where email = :email and password=:password", nativeQuery = true)
    MstMember findMstMemberByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Query(value = "select * from mst_member where is_login=:isLogin", nativeQuery = true)
    MstMember findByIsLogin(@Param("isLogin") String isLogin);
}
