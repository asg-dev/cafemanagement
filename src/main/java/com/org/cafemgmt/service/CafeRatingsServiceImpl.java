package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeRatings;
import com.org.cafemgmt.repository.CafeRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CafeRatingsServiceImpl implements CafeRatingsService {

    @Autowired
    CafeRatingRepository cafeRatingRepository;

    @Override
    public CafeRatings saveRating(CafeRatings cafeRating) {
        return cafeRatingRepository.save(cafeRating);
    }
}
