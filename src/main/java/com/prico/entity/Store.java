package com.prico.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String website;

    @OneToMany(mappedBy = "store")
    private List<Price> prices;

    public Store(Long id, String name, String location, String website) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.website = website;
    }
}