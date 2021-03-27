package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeCarts;
import com.org.cafemgmt.model.CafeOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CafeOrderRepository extends JpaRepository<CafeOrders, Long> {
    @Query("SELECT t FROM CafeOrders t WHERE t.status = 1 ORDER BY t.createdAt DESC")
    public List<CafeOrders> listAllPendingOrders();

    @Query("SELECT t FROM CafeOrders t WHERE t.status = 2 ORDER BY t.createdAt DESC")
    public List<CafeOrders> listAllApprovedOrders();

    @Query("SELECT t FROM CafeOrders t WHERE t.customerId = ?1 ORDER BY t.createdAt DESC")
    public List<CafeOrders> findAllByUserId(long user_id);

    // created within
    // customerId --> Builder
    // approverId --> Builder
    // created within + cid
    // created within + aid
    //  cid + aid --> Biilder

    @Query("SELECT t FROM CafeOrders t WHERE t.status = 2 AND t.createdAt > ?1 AND t.createdAt < ?2")
    public List<CafeOrders> findWithinDateRange(Date startRange, Date endRange);

    @Query("SELECT t FROM CafeOrders t WHERE t.status = 2 AND t.createdAt > ?1 AND t.createdAt < ?2 AND t.approvedUser = ?3")
    public List<CafeOrders> findWithinDateRangeApproverId(Date startRange, Date endRange, long approvedBy);

    @Query("SELECT t FROM CafeOrders t WHERE t.status = 2 AND t.createdAt > ?1 AND t.createdAt < ?2 AND t.customerId = ?3")
    public List<CafeOrders> findWithinDateRangeCustomerId(Date startRange, Date endRange, long customerId);

}
