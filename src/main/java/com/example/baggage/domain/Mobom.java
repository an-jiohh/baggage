package com.example.baggage.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@Entity
public class Mobom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column
    private String SHOPNAME;

    @Column
    private String ADDRESS;

    @Column
    private String HEADMENU;

    public Mobom(String shopName, String address, String headMenu) {
        this.SHOPNAME = shopName;
        this.ADDRESS = address;
        this.HEADMENU = headMenu;
    }
}
