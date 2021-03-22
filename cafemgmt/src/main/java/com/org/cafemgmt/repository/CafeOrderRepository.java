package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeCarts;
import com.org.cafemgmt.model.CafeOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CafeOrderRepository extends JpaRepository<CafeOrders, Long> {
    @Query("SELECT t FROM CafeOrders t WHERE t.status = 1")
    public List<CafeOrders> listAllPendingOrders();

    @Query("SELECT t FROM CafeOrders t WHERE t.customerId = ?1")
    public List<CafeOrders> findAllByUserId(long user_id);

}
