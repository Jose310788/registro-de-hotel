<?php

class ClsLCarrito {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    //PARA CARRITO Y DETALLE CARRITO
    public function Carrito($NRO_HABITACION, $USUARIO, $ID_PRODUCTO) {
        //INSERT INTO carrito (NRO_HABITACION) VALUES(501);
        if ($this->existeCarrito($NRO_HABITACION, $USUARIO)) {
            //existe
            if ($this->addDetalleCarrito($NRO_HABITACION, $ID_PRODUCTO, $USUARIO)) {
                if ($this->updateMontoCarrito($NRO_HABITACION, $USUARIO)) {
                    //correcto
                    return $this->correcto("Se agregó correctamente");
                } else {
                    //incorrecto
                    return $this->incorrecto("No se agregó");
                }
            } else {
                //incorrecto
                return $this->incorrecto("No se agregó");
            }
        } else {
            //no existe
            if ($this->addCarrito($NRO_HABITACION, $USUARIO)) {
                //añadir detalle carrito con secuencia 1
                if ($this->addDetalleCarrito_1($ID_PRODUCTO, $USUARIO, $NRO_HABITACION)) {
                    //Actualizar Monto en Carrito
                    if ($this->updateMontoCarrito($NRO_HABITACION, $USUARIO)) {
                        //correcto
                        return $this->correcto("Se agregó correctamente");
                    } else {
                        //incorrecto
                        return $this->incorrecto("No se agregó");
                    }
                } else {
                    //incorrecto
                    
                    return $this->incorrecto("No se agregó");
                }
            } else {
                //incorrecto
                return $this->incorrecto("No se agregó");
            }
        }
    }

    //AÑADIR CARRITO
    public function addCarrito($NRO_HABITACION, $USUARIO) {
        $stmt = $this->conn->prepare("INSERT INTO carrito (NRO_HABITACION, USUARIO) VALUES(:NRO_HABITACION, :USUARIO)");
        $stmt->bindparam(":NRO_HABITACION", $NRO_HABITACION);
        $stmt->bindparam(":USUARIO", $USUARIO);
        $result = $stmt->execute();

        return $result;
    }

    //AÑADIR DETALLE CARRITO SECUENCIA = 1
    public function addDetalleCarrito_1($ID_PRODUCTO, $USUARIO, $NRO_HABITACION) {
        $stmt = $this->conn->prepare("INSERT INTO detalle_carrito (ID_CARRITO, SECUENCIA, ID_PRODUCTO) "
                . "VALUES ((SELECT * FROM(SELECT ID_CARRITO "
                . "FROM carrito "
                . "WHERE NRO_HABITACION=:NRO_HABITACION AND ESTADO=0 AND USUARIO=:USUARIO)AS X), "
                . "1, :ID_PRODUCTO)");
        $stmt->bindparam(":ID_PRODUCTO", $ID_PRODUCTO);
        $stmt->bindparam(":USUARIO", $USUARIO);
        $stmt->bindparam(":NRO_HABITACION", $NRO_HABITACION);
        $result = $stmt->execute();

        return $result;
    }

    //EXISTE CARRITO
    public function existeCarrito($NRO_HABITACION, $USUARIO) {
        $stmt = $this->conn->prepare("SELECT * "
                . "FROM carrito "
                . "WHERE NRO_HABITACION=:NRO_HABITACION AND "
                . "ESTADO=0 AND "
                . "USUARIO=:USUARIO");
        $stmt->bindparam(":NRO_HABITACION", $NRO_HABITACION);
        $stmt->bindparam(":USUARIO", $USUARIO);
        $stmt->execute();
        $rowCount = count($stmt->fetchAll(PDO::FETCH_ASSOC)); //contar las filas

        if ($rowCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    //AÑADIR DETALLE A DETALLE_CARRITO
    public function addDetalleCarrito($NRO_HABITACION, $ID_PRODUCTO, $USUARIO) {
        $stmt = $this->conn->prepare("INSERT INTO detalle_carrito (ID_CARRITO, SECUENCIA, ID_PRODUCTO) \n"
                . "VALUES ((SELECT ID_CARRITO FROM carrito WHERE NRO_HABITACION=:NRO_HABITACION and ESTADO=0 AND USUARIO=:USUARIO), \n"
                . "	(SELECT * FROM(SELECT MAX(SECUENCIA)+1 \n"
                . "	FROM detalle_carrito \n"
                . " WHERE ID_CARRITO=(SELECT * FROM (SELECT ID_CARRITO \n"
                . "	FROM carrito \n"
                . "	WHERE NRO_HABITACION=:NRO_HABITACION and ESTADO=0 AND USUARIO=:USUARIO LIMIT 1)AS X)\n"
                . "	ORDER BY SECUENCIA DESC LIMIT 1)AS Y), \n"
                . "	:ID_PRODUCTO)");
        $stmt->bindparam(":NRO_HABITACION", $NRO_HABITACION);
        $stmt->bindparam(":ID_PRODUCTO", $ID_PRODUCTO);
        $stmt->bindparam(":USUARIO", $USUARIO);
        $result = $stmt->execute();

        return $result;
    }

    //ACTUALIZAR EL MONTO DE CARRITO
    public function updateMontoCarrito($NRO_HABITACION, $USUARIO) {
        $stmt = $this->conn->prepare("UPDATE carrito SET MONTO=(SELECT * FROM(SELECT SUM(P.PRECIO) "
                . "FROM detalle_carrito DC "
                . "JOIN producto P ON DC.ID_PRODUCTO=P.ID_PRODUCTO "
                . "WHERE DC.ID_CARRITO=(SELECT * FROM(SELECT ID_CARRITO "
                . "FROM carrito "
                . "WHERE NRO_HABITACION=:NRO_HABITACION AND ESTADO=0 AND USUARIO=:USUARIO)AS Z))AS X) "
                . "WHERE ID_CARRITO=(SELECT * FROM(SELECT ID_CARRITO "
                . "FROM carrito "
                . "WHERE NRO_HABITACION=:NRO_HABITACION AND ESTADO=0 AND USUARIO=:USUARIO)AS Y)");
        $stmt->bindparam(":NRO_HABITACION", $NRO_HABITACION);
        $stmt->bindparam(":USUARIO", $USUARIO);
        $result = $stmt->execute();

        return $result;
    }
    //UPDATE MONTO CARRITO ON DELETE
    public function updateMontoCarritoDel($ID_CARRITO) {
        $stmt = $this->conn->prepare("UPDATE carrito SET MONTO=(SELECT * FROM(SELECT SUM(P.PRECIO) \n"
                                   . "	FROM detalle_carrito DC \n"
                                   . "	JOIN producto P ON DC.ID_PRODUCTO=P.ID_PRODUCTO \n"
                                   . " WHERE DC.ID_CARRITO=:ID_CARRITO )AS X) \n"
                                   . "	WHERE ID_CARRITO=:ID_CARRITO");
        $stmt->bindparam(":ID_CARRITO", $ID_CARRITO);
        $result = $stmt->execute();

        return $result;
    }    

    public function correcto($texto) {
        $array2["code"] = 200;
        $array2["desc"] = $texto;
        $array1["status"] = $array2;
        $response = $array1;

        return $response;
    }

    public function incorrecto($texto) {
        $array2["code"] = 201;
        $array2["desc"] = $texto;
        $array1["status"] = $array2;
        $response = $array1;

        return $response;
    }
    
    public function obtenerCarritoAbierto($NRO_HABITACION, $USUARIO) {
        $stmt = $this->conn->prepare("SELECT ID_CARRITO, ESTADO\n"
                                   . "FROM carrito \n"
                                   . "WHERE NRO_HABITACION=:NRO_HABITACION and ESTADO=0 AND USUARIO=:USUARIO");
        $stmt->bindparam(":NRO_HABITACION", $NRO_HABITACION);
        $stmt->bindparam(":USUARIO", $USUARIO);
        $stmt->execute();
        $rowCount = $stmt->rowCount();

        if ($rowCount > 0) {
            $id_carrito = $stmt->fetchColumn(0);
            $stmt->execute();
            $estado = $stmt->fetchColumn(1);
            
            $array2["code"] = 200;
            $array2["id_carrito"] = $id_carrito;
            $array2["estado"] = $estado;
            $array1["status"] = $array2;
            $response = $array1;
        } else {
            $array2["code"] = 201;
            $array2["desc"] = "No hay Carritos Abiertos";
            $array1["status"] = $array2;
            $response = $array1;
        }

        return $response;
    }
    //DELETE UN DETALLE DE DETALLE_CARRITO
    public function deleteDetalle($ID_CARRITO, $SECUENCIA, $ID_PRODUCTO){
        $stmt = $this->conn->prepare("DELETE FROM detalle_carrito WHERE ID_CARRITO=:ID_CARRITO AND "
                                   . "SECUENCIA=:SECUENCIA AND "
                                   . "ID_PRODUCTO=:ID_PRODUCTO");
        $stmt->bindparam(":ID_CARRITO", $ID_CARRITO);
        $stmt->bindparam(":SECUENCIA", $SECUENCIA);
        $stmt->bindparam(":ID_PRODUCTO", $ID_PRODUCTO);
        $result = $stmt->execute();

        if($result){
            //UPDATE monto on delete
            if($this->updateMontoCarritoDel($ID_CARRITO)){
                //correcto
                return $this->correcto("Se eliminó Correctamente");
            } else{
                //error
                return $this->incorrecto("No se eliminó");
            }
        } else {
            //error
            return $this->incorrecto("No se eliminó");
        }
    }
    //OBTENER CARRITO
    public function obtenerCarrito($ID_CARRITO){
        $stmt = $this->conn->prepare("SELECT ID_CARRITO,MONTO, ESTADO\n"
                                   . "FROM carrito\n"
                                   . "WHERE ID_CARRITO=:ID_CARRITO");
        $stmt->bindParam(":ID_CARRITO", $ID_CARRITO);
        $stmt->execute();

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        return $result;
    }
    //OBTENER DETALLE DEL CARRITO
    public function detalleCarrito($ID_CARRITO){
        $stmt = $this->conn->prepare("SELECT DC.SECUENCIA, P.ID_PRODUCTO, P.NOMBRE, P.DESCRIPCION, P.PRECIO, P.IMAGEN \n"
                                   . "FROM carrito C JOIN detalle_carrito DC ON C.ID_CARRITO=DC.ID_CARRITO\n"
                                   . " JOIN producto P ON DC.ID_PRODUCTO=P.ID_PRODUCTO\n"
                                   . "WHERE C.ID_CARRITO=:ID_CARRITO");
        $stmt->bindParam(":ID_CARRITO", $ID_CARRITO);
        $stmt->execute();

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        return $result;
    }
}
