package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeMenus;
import com.org.cafemgmt.model.CafeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<CafeMenus, Long> {

    @Query("SELECT t FROM CafeMenus t WHERE t.active = true")
    public List<CafeMenus> listAllActiveMenus();

}
