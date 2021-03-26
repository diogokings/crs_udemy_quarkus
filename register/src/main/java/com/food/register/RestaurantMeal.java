package com.food.register;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "RESTAURANT_MEAL")
public class RestaurantMeal extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID", foreignKey = @ForeignKey(name = "FK_MEAL_RESTAURANT"))
    public Restaurant restaurant;

    public String name;

    public String description;

    public BigDecimal price;
}
