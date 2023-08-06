package com.arquitecturajava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.Id;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacturaService {
    private final FacturaRepository2 facturaRepository;

    @Autowired
    public FacturaService(FacturaRepository2 facturaRepository) {
        this.facturaRepository = facturaRepository;
    }


    public List<Factura> buscarFacturaPorNumero(int numero) {
        List<Factura> facturas = facturaRepository.findByNumero(numero);

        if (facturas.isEmpty()) {
            throw new RuntimeException("No se encontró ninguna factura con el número " + numero);
        }

        return facturas;
    }


    public List<Long> obtenerTodosLosIdsDeFacturas() {
        return facturaRepository.findAllIds();
    }
    public List<Factura> buscarTodas() {
        return facturaRepository.findAll();
    }
    
    public Factura buscarFacturaPorID(Long id) {
    	
    	Optional<Factura> facturaOptional = facturaRepository.findById(id);

        if (facturaOptional.isEmpty()) {
            throw new RuntimeException("No se encontró ninguna factura con el id " + id);
        }

        return facturaOptional.get();

    
    }




    public Factura agregarFactura(Factura factura) {
        int numeroFactura = factura.getNumero();
        List<Factura> facturasExistente = facturaRepository.findByNumero(numeroFactura);

        if (!facturasExistente.isEmpty()) {
            throw new RuntimeException("Ya existe una factura con el número " + numeroFactura);
        }

        return facturaRepository.save(factura);
    }

    // Método para guardar varias facturas

    /*
    public List<Factura> agregarFacturas(List<Factura> facturas) {
        return facturaRepository.saveAll(facturas);
    }
    */

    public List<Factura> agregarFacturas(List<Factura> facturas) throws FacturasExistenException {
        List<Factura> nuevasFacturas = new ArrayList<>();
        List<Integer> numerosFacturasExistentes = new ArrayList<>();

        for (Factura factura : facturas) {
            int numeroFactura = factura.getNumero();
            List<Factura> facturasExistentesPorNumero = facturaRepository.findByNumero(numeroFactura);

            if (facturasExistentesPorNumero.isEmpty()) {
                // La factura no existe, podemos agregarla
                facturaRepository.save(factura);
                nuevasFacturas.add(factura);
            } else {
                // La factura ya existe, agregamos el número a la lista de facturas existentes
                numerosFacturasExistentes.add(numeroFactura);
            }
        }

        if (!numerosFacturasExistentes.isEmpty()) {
            throw new FacturasExistenException("Ya existen facturas con los siguientes números: " + numerosFacturasExistentes);
        }

        return nuevasFacturas;
    }


    public Factura actualizarFactura(Long id, Factura nuevaFactura) {
        Factura facturaExistente = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna factura con el ID " + id));

        // Actualizar los campos de la factura existente con los valores de la nueva factura
        facturaExistente.setNumero(nuevaFactura.getNumero());
        facturaExistente.setConcepto(nuevaFactura.getConcepto());
        facturaExistente.setImporte(nuevaFactura.getImporte());

        // Guardar los cambios en la base de datos
        return facturaRepository.save(facturaExistente);
    }



    public Map<String, Object> actualizarFacturaByNumero(int numero, Factura nuevaFactura) {
        List<Factura> facturasExistente = facturaRepository.findByNumero(numero);

        if (facturasExistente.isEmpty()) {
            throw new RuntimeException("No se encontró ninguna factura con el número " + numero);
        }

        Factura facturaExistente = facturasExistente.get(0); // Obtenemos la primera factura de la lista

        // Actualizar los campos de la factura existente con los valores de la nueva factura
        facturaExistente.setNumero(nuevaFactura.getNumero());
        facturaExistente.setConcepto(nuevaFactura.getConcepto());
        facturaExistente.setImporte(nuevaFactura.getImporte());

        // Guardar los cambios en la base de datos
        facturaRepository.save(facturaExistente);

        String mensaje = "La factura con el número " + numero + " ha sido actualizada exitosamente.";

        // Crear un mapa para contener tanto la entidad Factura actualizada como el mensaje
        Map<String, Object> response = new HashMap<>();
        response.put("factura", facturaExistente);
        response.put("mensaje", mensaje);

        return response;
    }




    public String eliminarFacturaPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna factura con el ID " + id));

        facturaRepository.delete(factura);
        return "La factura con el ID " + id + " ha sido eliminada exitosamente.";
    }

}

