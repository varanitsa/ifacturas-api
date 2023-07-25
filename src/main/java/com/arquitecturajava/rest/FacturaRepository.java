package com.arquitecturajava.rest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class FacturaRepository {
    @PersistenceContext
    private EntityManager em;
    public List<Factura> buscarTodas() {
        return em.createQuery("select f from Factura f", Factura.class).getResultList();

    }
}
