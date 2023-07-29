package com.example.baggage.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "LODGING")
public class LodgmentDTO {

    @Id
    @Column(name = "ADDRESS")
    private String ADDRESS;

    @Column(name = "LICENSE")
    private String LICENSE;

    @Column(name = "NAME")
    private String NAME;

    private int code;
}
