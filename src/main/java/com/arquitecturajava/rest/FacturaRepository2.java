package com.arquitecturajava.rest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository2 extends JpaRepository<Factura, Long> {

    @Query("SELECT f.id FROM Factura f")
    List<Long> findAllIds();

    Optional< Factura> findById(Long id);

    List<Factura> findByNumero(int numero);

}
