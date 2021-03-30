package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeOrders;
import com.org.cafemgmt.model.CafeRatings;
import com.org.cafemgmt.service.CafeOrderService;
import com.org.cafemgmt.service.CafeRatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SurveyRatingController {

    @Autowired
    CafeRatingsService cafeRatingsService;

    @Autowired
    CafeOrderService cafeOrderService;

    @RequestMapping(value = "/orders/{id}/rate", method = RequestMethod.POST)
    public String rateOrder(@PathVariable("id") long id, @ModelAttribute("ratings") CafeRatings cafeRating) {
        CafeRatings rating = new CafeRatings();
        CafeOrders cafeOrder = cafeOrderService.getOrderById(id);
        rating.setOrder_id(id);
        rating.setFeedback(cafeRating.getFeedback());
        rating.setRating(cafeRating.getRating());
        cafeRatingsService.saveRating(rating);
        cafeOrder.setRating(rating.getRating());
        cafeOrderService.saveCafeOrder(cafeOrder);
        return "redirect:/site/my_orders";
    }
}
