package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class CafeMenus {
    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private long id;
    private String name;
    private String description;
    private Blob picture;
    @ElementCollection(targetClass = Long.class)
    private List<Long> menu_items;
    private Date created_at;
    private Date updated_at;
}
