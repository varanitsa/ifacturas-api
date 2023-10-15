package com.arquitecturajava.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.arquitecturajava.rest.entity.Factura;
import com.arquitecturajava.rest.repository.FacturaRepository2;
import com.arquitecturajava.rest.service.FacturaService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FacturaServiceTest {

    @Mock
    private FacturaRepository2 facturaRepository;

    @InjectMocks
    private FacturaService facturaService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuscarTodas() {
        // Crea una lista ficticia de facturas
        List<Factura> facturas = new ArrayList<>();
        facturas.add(new Factura(1L,1, "Factura 1", 100));
        facturas.add(new Factura(2L,2, "Factura 2", 200));
        facturas.add(new Factura(3L,3, "Factura 3", 300));

        // Cuando se llame a facturaRepository.findAll(), retorna la lista ficticia de facturas
        when(facturaRepository.findAll()).thenReturn(facturas);

        // Llama al método buscarTodas() en el servicio
        List<Factura> resultado = facturaService.buscarTodas();

        // Verifica que el tamaño de la lista retornada sea el mismo que el tamaño de la lista ficticia de facturas
        assertEquals(facturas.size(), resultado.size());

        for (int i = 0; i < facturas.size(); i++) {
            assertEquals(facturas.get(i), resultado.get(i));
        }
    }
}
