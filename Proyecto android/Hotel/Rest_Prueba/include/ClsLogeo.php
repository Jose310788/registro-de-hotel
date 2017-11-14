<?php
class ClsLogeo {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }
    
    public function existLogeo($NRO_HABITACION, $NRO_DOCUMENTO, $USUARIO, $PASSWORD) {
        $stmt = $this->conn->prepare("select USUARIO "
                                    ."from logeo "
                                    ."where NRO_HABITACION=:NRO_HABITACION and "
                                    ."NRO_DOCUMENTO=:NRO_DOCUMENTO and "
                                    ."USUARIO=:USUARIO and "
                                    ."PASSWORD=:PASSWORD and "
                                    ."ESTADO=1");
        $stmt->bindParam(":NRO_HABITACION", $NRO_HABITACION);
        $stmt->bindParam(":NRO_DOCUMENTO", $NRO_DOCUMENTO);
        $stmt->bindParam(":USUARIO", $USUARIO);
        $stmt->bindParam(":PASSWORD", $PASSWORD);
        $stmt->execute();
        $rowCount = count($stmt->fetchAll(PDO::FETCH_ASSOC));//contar las filas
        
        return $rowCount;
        /*if($rowCount > 0){
            return true;
        }else{
            return false;
        }*/
    }
}

