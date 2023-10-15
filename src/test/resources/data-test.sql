CREATE TABLE IF NOT EXISTS factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero INT,
    concepto VARCHAR(255),
    importe DECIMAL(10, 2)
);


INSERT INTO factura (id, numero, concepto, importe) VALUES (1L, 1, 'Factura 1', 100.00);
INSERT INTO factura (id, numero, concepto, importe) VALUES (2L, 2, 'Factura 2', 200.00);
INSERT INTO factura (id, numero, concepto, importe) VALUES (3L, 3, 'Factura 3', 300.00);
