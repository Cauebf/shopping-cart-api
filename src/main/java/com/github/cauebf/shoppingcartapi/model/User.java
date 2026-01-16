package com.github.cauebf.shoppingcartapi.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId // unique
    private String email;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // if delete user, delete cart
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // if delete user, delete orders
    private List<Order> orders;

    @ManyToMany(
        fetch = FetchType.EAGER, // load roles together with user
        cascade = { 
            CascadeType.PERSIST, // saves new roles when saving user
            CascadeType.MERGE, // updates roles when updating user
            CascadeType.DETACH, // detaches roles when the user is detached
            CascadeType.REFRESH // refreshes roles when the user is refreshed
            // CascadeType.REMOVE // deletes roles when the user is deleted
        }
    ) 
    @JoinTable( // intermediate table between user and role
        name = "user_roles", // table name
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // foreign key of user
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) // foreign key of role
    private Collection<Role> roles = new HashSet<>();
}
