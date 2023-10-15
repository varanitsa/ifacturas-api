package com.arquitecturajava.rest.service;

import com.arquitecturajava.rest.entity.Factura;
import com.arquitecturajava.rest.repository.FacturaRepository2;
import com.arquitecturajava.rest.exception.FacturasExistenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FacturaService {
    private final FacturaRepository2 facturaRepository;

    @Autowired
    public FacturaService(FacturaRepository2 facturaRepository) {
        this.facturaRepository = facturaRepository;
    }


    public List<Factura> buscarFacturaPorNumero(int numero) {
        List<Factura> factura = facturaRepository.findByNumero(numero);

        if (factura.isEmpty()) {
            throw new RuntimeException("No se encontró ninguna factura con el número " + numero);
        }

        return factura;
    }

    public Optional<Factura> buscarFacturaById(Long id) {
        Optional<Factura> factura = facturaRepository.findById(id);

        if (factura.isEmpty()) {
            throw new RuntimeException("No se encontró ninguna factura con el id " + id);
        }

        return factura;
    }


    public List<Long> obtenerTodosLosIdsDeFacturas() {
        return facturaRepository.findAllIds();
    }
    public List<Factura> buscarTodas() {
        return facturaRepository.findAll();
    }


    public Factura agregarFactura(Factura factura) {
        int numeroFactura = factura.getNumero();
        List<Factura> facturasExistente = facturaRepository.findByNumero(numeroFactura);

        if (!facturasExistente.isEmpty()) {
            throw new RuntimeException("Ya existe una factura con el número " + numeroFactura);
        }

        Factura nuevaFactura = facturaRepository.save(factura);

        System.out.println("ID de la nueva factura: " + nuevaFactura.getId());

        return nuevaFactura;
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


    public Factura actualizarFacturaById(Long id, Factura nuevaFactura) {
        Factura facturaExistente = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna factura con el ID " + id));

        // Actualizar los campos de la factura existente con los valores de la nueva factura
        facturaExistente.setNumero(nuevaFactura.getNumero());
        facturaExistente.setConcepto(nuevaFactura.getConcepto());
        facturaExistente.setImporte(nuevaFactura.getImporte());

        // Guardar los cambios en la base de datos
        return facturaRepository.save(facturaExistente);
    }



    public Map<String, Object> actualizarFacturaPorNumero(int numero, Factura nuevaFactura) {
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




    public String eliminarFacturaById(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna factura con el ID " + id));

        facturaRepository.delete(factura);
        return "La factura con el ID " + id + " ha sido eliminada exitosamente.";
    }

}

