package com.example.pr_ab_ecommerceproooject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="wishlists")
public class Wishlist {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user; // Reference to user who owns this wishlist

    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(
            name="wishlist_items",
            joinColumns=@JoinColumn(name="wishlist_id"),
            inverseJoinColumns=@JoinColumn(name="product_id")
    )
    private List<Product> products; // Products in this wishlist

}
