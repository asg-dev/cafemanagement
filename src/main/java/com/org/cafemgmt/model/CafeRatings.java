package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CafeRatings {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long order_id;
    private long user_id;
    private long rating;
    @Column(length = 1024)
    private String feedback;
}
