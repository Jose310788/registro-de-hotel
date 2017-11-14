<?php
class ClsProducto {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }
    
    public function obtenerLista($ID_SERVICIO){
        $stmt = $this->conn->prepare("SELECT ID_PRODUCTO, NOMBRE, DESCRIPCION, PRECIO, ESTADO, IMAGEN "
                                    ."FROM producto "
                                    ."WHERE ESTADO=1 AND ID_SERVICIO=:ID_SERVICIO");
        $stmt->bindParam(":ID_SERVICIO", $ID_SERVICIO);
        $stmt->execute();
        $resultados = $stmt->fetchAll(PDO::FETCH_ASSOC);
        return $resultados;
    }
}

