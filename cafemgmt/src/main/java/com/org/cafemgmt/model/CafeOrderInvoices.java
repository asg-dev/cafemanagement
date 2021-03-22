package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class CafeOrderInvoices {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long orderId;
    private Date createdAt;
    private Date updatedAt;
}
