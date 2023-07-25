package com.arquitecturajava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/facturas")
public class FacturaController {
    private final FacturaService facturaService;

    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public List<Factura> obtenerTodasLasFacturas() {
        return facturaService.buscarTodas();
    }

    @GetMapping("numero/{numero}")
    public List<Factura> buscarFacturaPorNumero(@PathVariable int numero) {
        return facturaService.buscarFacturaPorNumero(numero);
    }

    @GetMapping("/ids")
    public List<Long> obtenerTodosLosIdsDeFacturas() {
        return facturaService.obtenerTodosLosIdsDeFacturas();
    }

    @PostMapping
    public ResponseEntity<?> crearFactura(@RequestBody Factura factura) {
        try {
            Factura nuevaFactura = facturaService.agregarFactura(factura);
            return new ResponseEntity<>(nuevaFactura, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bulk")
    public List<Factura> agregarFacturas(@RequestBody List<Factura> facturas) {
        return facturaService.agregarFacturas(facturas);
    }


    @PutMapping("/{id}")
    public Factura actualizarFactura(@PathVariable Long id, @RequestBody Factura nuevaFactura) {
        return facturaService.actualizarFactura(id, nuevaFactura);
    }


    @PutMapping("numero/{numero}")
    public Map<String, Object> actualizarFactura(@PathVariable int numero, @RequestBody Factura nuevaFactura) {
        return facturaService.actualizarFacturaByNumero(numero, nuevaFactura);
    }

    @DeleteMapping("/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        return facturaService.eliminarFacturaPorId(id);
    }


}
