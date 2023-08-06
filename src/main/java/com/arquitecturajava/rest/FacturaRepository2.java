package com.arquitecturajava.rest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacturaRepository2 extends JpaRepository<Factura, Long> {

    @Query("SELECT f.id FROM Factura f")
    List<Long> findAllIds();

    List<Factura> findByNumero(int numero);
    Factura findByid(Long id);

}
