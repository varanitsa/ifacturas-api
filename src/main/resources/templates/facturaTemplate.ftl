<!DOCTYPE html>
<html>
<head>
    <title>Listado de Facturas</title>
    <head>
    <title>Listado de Facturas</title>  <!-- Agrega las siguientes líneas para cargar Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>

<body>
    <h1>Listado de Facturas</h1>
    <table class="table">
        <tr>
            <th>ID</th>
            <th>Número</th>
            <th>Concepto</th>
            <th>Importe</th>
           
        </tr>
        <#list facturas as factura>
            <tr>
                <td>${factura.id}</td>
                <td>${factura.numero}</td>
                <td>${factura.concepto}</td>
                <td>${factura.importe}</td>
            </tr>
        </#list>
    </table>
</body>
</html>
