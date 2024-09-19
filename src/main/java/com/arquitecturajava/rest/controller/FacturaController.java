package com.arquitecturajava.rest.controller;

import com.arquitecturajava.rest.service.FacturaService;
import com.arquitecturajava.rest.exception.FacturasExistenException;
import com.arquitecturajava.rest.entity.Factura;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/facturas")
@Tag(name = "")
public class FacturaController {
    private final FacturaService facturaService;

    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    @Operation(
            summary = "Obtener todas las facturas",
            description = "Obtiene una lista de todas las facturas almacenadas en el sistema."
    )
    public List<Factura> obtenerTodasLasFacturas() {
        return facturaService.buscarTodas();
    }


   @GetMapping("/{id}")
    @Operation(
            summary = "Buscar factura por ID",
            description = "Busca una factura por su identificador único (ID)."
    )
    public ResponseEntity<Factura> buscarFacturaById(
            @Parameter(description = "ID de la factura que se desea buscar", required = true) @PathVariable Long id) {
        Optional<Factura> facturaOptional = facturaService.buscarFacturaById(id);

        if (facturaOptional.isPresent()) {
            Factura factura = facturaOptional.get();
            return ResponseEntity.ok(factura);
        } else {
            return ResponseEntity.notFound().build(); // Devuelve una respuesta 404 si la factura no se encuentra.
        }
    }

    @GetMapping("numero/{numero}")
    @Operation(
            summary = "Buscar facturas por número",
            description = "Busca facturas por su número."
    )
    public List<Factura> buscarFacturaPorNumero(
            @Parameter(description = "Número de factura que se desea buscar", required = true) @PathVariable int numero) {
        return facturaService.buscarFacturaPorNumero(numero);
    }

    @GetMapping("/ids")
    @Operation(
            summary = "Obtener todos los IDs de facturas",
            description = "Obtiene una lista de todos los IDs de facturas almacenados."
    )
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
    @Operation(summary = "Crear una factura", description = "Crea una nueva factura en el sistema.")
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
    @Operation(
            summary = "Agregar múltiples facturas",
            description = "Agrega múltiples facturas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facturas agregadas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al agregar facturas")
    })
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
    @Operation(summary = "Actualizar factura por ID", description = "Actualiza una factura existente por su ID.")
    public Factura actualizarFacturaById (
            @Parameter(description = "ID de la factura que se desea actualizar", required = true)@PathVariable Long id, @RequestBody Factura nuevaFactura) {
        return facturaService.actualizarFacturaById(id, nuevaFactura);
    }


    @PutMapping("numero/{numero}")
    @Operation(summary= "Actualizar factura por número", description = "Actualiza una factura existente por su número.")
    public Map<String, Object> actualizarFacturaPorNumero(
            @Parameter(description = "Número de factura que se desea actualizar", required = true) @PathVariable int numero, @RequestBody Factura nuevaFactura) {
        return facturaService.actualizarFacturaPorNumero(numero, nuevaFactura);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar factura por ID", description = "Elimina una factura existente por su ID.")
    public String eliminarFactura(
            @Parameter(description = "ID de la factura que se desea eliminar", required = true) @PathVariable Long id) {
        return facturaService.eliminarFacturaById(id);
    }

    @DeleteMapping
    @Operation(summary = "Eliminar todas las facturas", description = "Elimina todas las facturas")
    public ResponseEntity<HttpStatus> deleteAllFacturas() {
        facturaService.deleteAllfacturas();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
















