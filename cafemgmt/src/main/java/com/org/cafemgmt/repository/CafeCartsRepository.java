package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeCarts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CafeCartsRepository extends JpaRepository<CafeCarts, Long> {
    @Query("SELECT t FROM CafeCarts t WHERE t.userId = ?1")
    public Optional<CafeCarts> checkCartItemForUser(long user_id);

}
