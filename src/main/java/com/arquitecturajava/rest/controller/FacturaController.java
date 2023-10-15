package com.arquitecturajava.rest.controller;

import com.arquitecturajava.rest.entity.Factura;
import com.arquitecturajava.rest.exception.FacturasExistenException;
import com.arquitecturajava.rest.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

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


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Factura> buscarFacturaById(@PathVariable Long id) {
        return facturaService.buscarFacturaById(id);
    }



    @GetMapping("numero/{numero}")
    public List<Factura> buscarFacturaPorNumero(@PathVariable int numero) {
        return facturaService.buscarFacturaPorNumero(numero);
    }

    @GetMapping("/ids")

    public Map<String, List<Map<String, Long>>> obtenerTodosLosIdsDeFacturas() {
        List<Long> ids = facturaService.obtenerTodosLosIdsDeFacturas();

        // la respuesta json
        Map<String, List<Map<String, Long>>> response = new HashMap<>();
        List<Map<String, Long>> facturas = new ArrayList<>();

        for (Long id : ids) {
            Map<String, Long> factura = new HashMap<>();
            factura.put("id", id);
            facturas.add(factura);
        }

        response.put("facturas", facturas);
        return response;
    }

    @PostMapping
    public ResponseEntity<?> crearFactura(@RequestBody Factura factura) {
        try {
            Factura nuevaFactura = facturaService.agregarFactura(factura);

            // Creamos la URL del recurso creado
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(nuevaFactura.getId())
                    .toUri();

            return ResponseEntity.created(location).body(nuevaFactura);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* @PostMapping("/bulk")
     public List<Factura> agregarFacturas(@RequestBody List<Factura> facturas) {
         return facturaService.agregarFacturas(facturas);
     }
 */

    @PostMapping("/bulk")
    public ResponseEntity<?> agregarFacturas(@RequestBody List<Factura> facturas) {
        try {
            List<Factura> nuevasFacturas = facturaService.agregarFacturas(facturas);

            List<URI> facturaUris = nuevasFacturas.stream()
                    .map(factura -> ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/facturas/bulk/{id}")
                            .buildAndExpand(factura.getId())
                            .toUri())
                    .collect(Collectors.toList());

            HttpHeaders headers = new HttpHeaders();
            headers.addAll(HttpHeaders.LOCATION, facturaUris.stream()
                    .map(URI::toString)
                    .collect(Collectors.toList()));


            return new ResponseEntity<>(nuevasFacturas, HttpStatus.CREATED);
        } catch (FacturasExistenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public Factura actualizarFacturaById(@PathVariable Long id, @RequestBody Factura nuevaFactura) {
        return facturaService.actualizarFacturaById(id, nuevaFactura);
    }



    @PutMapping("numero/{numero}")
    public Map<String, Object> actualizarFacturaPorNumero(@PathVariable int numero, @RequestBody Factura nuevaFactura) {
        return facturaService.actualizarFacturaPorNumero(numero, nuevaFactura);
    }

    @DeleteMapping("/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        return facturaService.eliminarFacturaById(id);
    }


}

