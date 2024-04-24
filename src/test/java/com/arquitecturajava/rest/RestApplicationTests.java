package com.arquitecturajava.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

/*
@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
class RestApplicationTests {
	@Autowired
	FacturaRepository repositorio;

	@Test
	void buscarTodosTest() {
		List<Factura> lista = repositorio.buscarTodas();
		assertThat(lista,hasItem(new Factura(1)));
	}

}
*/
