package com.arquitecturajava.rest;

import com.arquitecturajava.rest.controller.FacturaController;
import com.arquitecturajava.rest.entity.Factura;
import com.arquitecturajava.rest.service.FacturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
        facturas.add(new Factura(1, "Factura 1", 100));
        facturas.add(new Factura(2, "Factura 2", 200));
        facturas.add(new Factura(3, "Factura 3", 300));
        // Simulamos el comportamiento del servicio para buscar todas las facturas ficticias

        when(facturaService.buscarTodas()).thenReturn(facturas);

        // Realizamos la solicitud GET al endpoint /facturas
        mockMvc.perform(get("/facturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].concepto").value("Factura 1"))
                .andExpect(jsonPath("$[0].importe").value(100))
                .andExpect(jsonPath("$[1].concepto").value("Factura 2"))
                .andExpect(jsonPath("$[1].importe").value(200))
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
        facturas.add(new Factura(numeroFactura, "Factura 123", 100));


        when(facturaService.buscarFacturaPorNumero(numeroFactura)).thenReturn(facturas);

        // Llamamos al método del controlador que se está probando
        List<Factura> resultado = facturaController.buscarFacturaPorNumero(numeroFactura);

        // Verificamos que el resultado coincida con las facturas ficticias
        assertEquals(facturas, resultado);

        // Verificamos que el método del servicio se haya llamado con el número de factura correcto
        verify(facturaService).buscarFacturaPorNumero(numeroFactura);
    }


    @Test
    public void testCrearFactura() throws Exception {
        // Creamos una instancia de Factura para enviar en la solicitud
        Factura factura = new Factura();
        factura.setId(1L);
        factura.setNumero(1);
        factura.setConcepto("Concepto de factura");
        factura.setImporte(100.0);

        // Mockear el servicio para que devuelva una factura cuando se llama a agregarFactura
        Mockito.when(facturaService.agregarFactura(Mockito.any(Factura.class))).thenReturn(factura);

        // Realizar la solicitud POST
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\":1,\"concepto\":\"Concepto de factura\",\"importe\":100.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numero").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.concepto").value("Concepto de factura"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.importe").value(100.0))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/facturas/1"));
    }


    @Test
    public void testCrearFacturas() throws Exception {
        List<Factura> facturas = new ArrayList<>();
        facturas.add(new Factura(1, "Factura 1", 100));
        facturas.add(new Factura(2, "Factura 2", 200));
        facturas.add(new Factura(3, "Factura 3", 300));

        Mockito.when(facturaService.agregarFacturas(Mockito.anyList())).thenReturn(facturas);


        // Creamos una lista de objetos JSON que representen las facturas
        List<ObjectNode> facturaJsonList = facturas.stream().map(factura -> {
            ObjectNode facturaJson = new ObjectMapper().createObjectNode();
            facturaJson.put("id", factura.getId());
            facturaJson.put("numero", factura.getNumero());
            facturaJson.put("concepto", factura.getConcepto());
            facturaJson.put("importe", factura.getImporte());
            return facturaJson;
        }).collect(Collectors.toList());

        // Convertimos la lista de objetos JSON a una cadena JSON
        String facturaJsonString = new ObjectMapper().writeValueAsString(facturaJsonList);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/facturas/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facturaJsonString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numero").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].concepto").value("Factura 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].importe").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].numero").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].concepto").value("Factura 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].importe").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].numero").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].concepto").value("Factura 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].importe").value(300));



            /*.andExpect(result -> {
                        String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
                        String baseUrl = "http://localhost/facturas/bulk/";
                        List<Long> idsExpected = Arrays.asList(1L, 2L, 3L);
                        for (Long idExpected : idsExpected) {
                            String expectedLocation = baseUrl + idExpected;
                            assertTrue(locationHeader.contains(expectedLocation));
                        }
                    });*/
    }


    @Test
    public void testActualizarFacturaById() throws Exception {

        Long facturaId = 1L;
        Factura nuevaFactura = new Factura(1, "Nueva Factura", 500.0);

        mockMvc.perform(put("/facturas/" + facturaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(nuevaFactura)))
                .andExpect(status().isOk());

        verify(facturaService).actualizarFacturaById(eq(facturaId), any());
    }


    @Test
    public void testActualizarFacturaPorNumero() throws Exception {
        // Datos de ejemplo
        int numeroFactura = 1;
        Factura nuevaFactura = new Factura(numeroFactura, "Nueva Factura", 500.0);

        mockMvc.perform(put("/facturas/numero/" + numeroFactura)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(nuevaFactura)))
                .andExpect(status().isOk());

        verify(facturaService).actualizarFacturaPorNumero(eq(numeroFactura), any());
    }

    @Test
    public void testEliminarFactura() throws Exception {

        Long facturaId = 1L;

        mockMvc.perform(delete("/facturas/" + facturaId))
                .andExpect(status().isOk());

        verify(facturaService).eliminarFacturaById(eq(facturaId));
    }

}