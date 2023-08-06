<!DOCTYPE html>
<html>
<head>
     <title>Detalles de Factura</title>
    <head>
    <title>Listado de Facturas</title>  
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>

<body>
    <h1>Detalles de Factura</h1>
 
    <div class="container">
        <table class="table">
            <tr>
                <th>ID</th>
                <th>Número</th>
                <th>Concepto</th>
                <th>Importe</th>
                <th>Acciones</th>
            </tr>
           
                <tr>
                    <td>${factura.id}</td>
                    <td>${factura.numero}</td>
                    <td>${factura.concepto}</td>
                    <td>${factura.importe}</td>
                    <td>
                        <button class="btn btn-primary" onclick="updateFactura(${factura.id})">Actualizar</button>
                        <button class="btn btn-danger" onclick="deleteFactura(${factura.id})">Eliminar</button>
                    </td>
                </tr>
            
        </table>
    </div>
</body>
</html>

