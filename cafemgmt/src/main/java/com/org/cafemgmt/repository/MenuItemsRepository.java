package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeMenuItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemsRepository extends JpaRepository<CafeMenuItems, Long> {
    @Query("SELECT t FROM CafeMenuItems t WHERE t.id = ?1")
    public List<CafeMenuItems> findMenuItemsByMenuId(long cafeMenusId);
}
