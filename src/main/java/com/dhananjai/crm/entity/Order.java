package com.dhananjai.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="`order`") // order is a reserved keyword in SQL, escaped using ` ` in MySQL
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="cust_id")
    private int custId;

    @Column(name="product")
    private String product;

    @Column(name="review")
    private String review;


}
