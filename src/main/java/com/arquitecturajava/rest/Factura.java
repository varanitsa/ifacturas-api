package com.arquitecturajava.rest;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Objects;

//javax is now jakarta
@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true) // Asegura que el campo numero sea único
    private int numero;
    private String concepto;
    private double importe;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    // Constructor con todos los campos
    public Factura(Long id, int numero, String concepto, double importe) {
        this.id = id;
        this.numero = numero;
        this.concepto = concepto;
        this.importe = importe;
    }

    // Constructor vacío
    public Factura() {

    }


}
