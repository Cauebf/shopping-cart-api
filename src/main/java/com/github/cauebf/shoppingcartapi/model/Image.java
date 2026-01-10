package com.github.cauebf.shoppingcartapi.model;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    private String fileName;
    private String fileType;

    @Lob // large object
    private Blob image; // Blob is a Java type for binary data
    private String downloadUrl;
    
    @ManyToOne // many images to one product
    @JoinColumn(name = "product_id") // foreign key
    private Product product;
}
