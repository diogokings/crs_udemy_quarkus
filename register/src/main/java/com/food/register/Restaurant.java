package com.food.register;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "RESTAURANT")
public class Restaurant extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String owner;

    public String name;

    public String document;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GEO_LOCATION_ID", foreignKey = @ForeignKey(name = "FK_RESTAURANT_GEOLOCATION"))
    public GeoLocation geoLocation;

    @CreationTimestamp
    public Date createDate;

    @UpdateTimestamp
    public Date updateDate;

}
