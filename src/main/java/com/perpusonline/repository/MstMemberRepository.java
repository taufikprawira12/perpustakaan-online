package com.perpusonline.repository;

import com.perpusonline.domain.MstMember;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface MstMemberRepository extends JpaRepository<MstMember, Integer> {
}
