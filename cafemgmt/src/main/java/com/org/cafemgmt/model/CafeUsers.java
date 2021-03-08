package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class CafeUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(unique=true)
    private String emailAddress;
    private String password;
    private Date created_at;
    private Date updated_at;
    private String authority;
    private String name;
    private boolean internalUser;
    private boolean walkinCustomer;
    private String passwordRecoveryQuestion;
    private String passwordRecoveryAnswer;
}
