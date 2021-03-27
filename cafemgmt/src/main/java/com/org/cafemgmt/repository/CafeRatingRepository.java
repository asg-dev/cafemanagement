package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeRatings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRatingRepository extends JpaRepository<CafeRatings, Long> {
}
