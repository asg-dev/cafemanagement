package com.org.cafemgmt.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.views.CafeUserView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class CafeUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonView(CafeUserView.ViewToReturnUsers.class)
    private long id;
    @JsonView(CafeUserView.ViewToReturnUsers.class)
    @Column(unique=true)
    private String emailAddress;
    @Column(name = "active", nullable = true)
    private boolean active;
    private String password;
    @JsonView(CafeUserView.ViewToReturnUsers.class)
    private Date created_at;
    @JsonView(CafeUserView.ViewToReturnUsers.class)
    private Date updated_at;
    @JsonView(CafeUserView.ViewToReturnUsers.class)
    private String authority;
    @JsonView(CafeUserView.ViewToReturnUsers.class)
    private String name;
    private UUID validityToken;
    private boolean internalUser;
    private boolean walkinCustomer;
}
