package com.arquitecturajava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/listado-facturas")
public class FacturaController {
    private final FacturaService facturaService;

    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    
    @GetMapping
    public String mostrarListadoFacturas(Model model) {
        List<Factura> facturas = facturaService.buscarTodas();
        model.addAttribute("facturas", facturas);

        return "facturaTemplate"; 
    }
    
    

    @GetMapping("/{id}")
    public String buscarFacturaPorID(@PathVariable Long id, Model model) {
        Factura factura = facturaService.buscarFacturaPorID(id);
        model.addAttribute("factura", factura);

        return "factura_id"; 
    }
    
    
    
 


    }

