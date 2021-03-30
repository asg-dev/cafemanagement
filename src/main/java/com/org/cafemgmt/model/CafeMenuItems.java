package com.org.cafemgmt.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.views.CafeMenuItemView;
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
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    private Long id;
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    private String name;
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    private String description;
    private String imagePath;
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    private double price;
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    private Date createdAt;
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    private Date updatedAt;
}
