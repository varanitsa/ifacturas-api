package com.arquitecturajava.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class FacturaControllerTests {

    @InjectMocks
    private FacturaController facturaController;

    @Mock
    private FacturaService facturaService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(facturaController).build();
    }


    @Test
    public void testObtenerTodasLasFacturas() throws Exception {
        // Lista ficticia de facturas
        List<Factura> facturas = new ArrayList<>();
        facturas.add(new Factura(1L, 1, "Factura 1", 100));
        facturas.add(new Factura(2L, 2, "Factura 2", 200));
        facturas.add(new Factura(3L, 3, "Factura 3", 300));
        // Simulamos el comportamiento del servicio para buscar todas las facturas ficticias

        when(facturaService.buscarTodas()).thenReturn(facturas);

        // Realizamos la solicitud GET al endpoint /facturas
        mockMvc.perform(get("/facturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].concepto").value("Factura 1"))
                .andExpect(jsonPath("$[0].importe").value(100))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].concepto").value("Factura 2"))
                .andExpect(jsonPath("$[1].importe").value(200))
                .andExpect(jsonPath("$[2].id").value(3L))
                .andExpect(jsonPath("$[2].concepto").value("Factura 3"))
                .andExpect(jsonPath("$[2].importe").value(300));


        verify(facturaService, times(1)).buscarTodas();
    }


    @Test
    public void obtenerTodosLosIdsDeFacturas() throws Exception {
        // Lista ficticia de facturas
        List<Long> facturas = Arrays.asList(1L, 2L, 3L);


        when(facturaService.obtenerTodosLosIdsDeFacturas()).thenReturn(facturas);


        mockMvc.perform(get("/facturas/ids"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facturas", hasSize(3)))
                .andExpect(jsonPath("$.facturas[0].id").value(1L))
                .andExpect(jsonPath("$.facturas[1].id").value(2L))
                .andExpect(jsonPath("$.facturas[2].id").value(3L));


        verify(facturaService, times(1)).obtenerTodosLosIdsDeFacturas();
    }

    @Test
    public void testBuscarFacturaPorNumero() {
        int numeroFactura = 123;

        List<Factura> facturas = new ArrayList<>();
        facturas.add(new Factura(1L, numeroFactura, "Factura 123", 100));


        when(facturaService.buscarFacturaPorNumero(numeroFactura)).thenReturn(facturas);

        // Llama al método del controlador que se está probando
        List<Factura> resultado = facturaController.buscarFacturaPorNumero(numeroFactura);

        // Verifica que el resultado coincida con las facturas ficticias
        assertEquals(facturas, resultado);

        // Verifica que el método del servicio se haya llamado con el número de factura correcto
        verify(facturaService).buscarFacturaPorNumero(numeroFactura);
    }
}



