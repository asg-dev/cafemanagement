package com.org.cafemgmt.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.views.CafeMenuView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class CafeMenus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    private long id;
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    private String name;
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    private String description;
    @Column(nullable = true)
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    private boolean active;
    @ElementCollection(targetClass = Long.class)
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    private List<Long> menu_items;
    @Transient
    private List<String> menu_item_names;
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    private Date created_at;
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    private Date updated_at;
}
