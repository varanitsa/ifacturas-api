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
public class FacturaController {
    private final FacturaService facturaService;

    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    
    @GetMapping("vista_facturas")
    public String mostrarListadoFacturas(Model model) {
        List<Factura> facturas = facturaService.buscarTodas();
        model.addAttribute("facturas", facturas);

        return "facturaTemplate"; 
    }
    
    


    }

