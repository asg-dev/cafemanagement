package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class CafeMenuItems {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private String imagePath;
    private double price;
    @ElementCollection(targetClass = Long.class)
    private List<Long> mapped_menus;
    private Date createdAt;
    private Date updatedAt;
}
