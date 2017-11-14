<?php

class ClsPersona {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }
    
    /**
     * Añadir Persona
     * @param String $ci -> ci de la persona
     * @param String $nombre -> nombre de la persona
     * @param String $paterno -> paterno de la persona
     * @param String &materno -> materno de la persona
     */
    public function AdicionarPersona($ci, $nombre, $paterno, $materno)
    {
        if(!$this->existPersona($ci)){
            $stmt = $this->conn->prepare("INSERT INTO persona(ci, nombre, paterno, materno) "
                                       . "VALUES (:ci,:nombre,:paterno,:materno)");
            $stmt->bindParam(":ci", $ci);
            $stmt->bindParam(":nombre", $nombre);
            $stmt->bindParam(":paterno", $paterno);
            $stmt->bindParam(":materno", $materno);
            
            $result = $stmt->execute();

            if ($result) {
                // User successfully inserted
                $response = "HECHO";
            } else {
                // Failed to create user
                $response = "FALLO";
            }
            
        } else {
            $response = "EXISTE";
        }
        return $response;
    }
    
    /**
     * Verifica por duplicidad usando el ci de la persona
     * @param String $ci -> ci para verificar en db
     * @return boolean
     */
    private function existPersona($ci) {
        $stmt = $this->conn->prepare("SELECT * FROM persona WHERE ci=?");
        $stmt->bindParam(1, $ci, PDO::PARAM_STMT);
        $stmt->execute();
        $rowCount = count($stmt->fetchAll(PDO::FETCH_ASSOC));//contar las filas
        
        if($rowCount > 0){
            return true;
        }else{
            return false;
        }
    }
    
    public function getPersonas(){
        $stmt = $this->conn->prepare("SELECT ci, nombre, paterno, materno FROM persona");
        $stmt->execute();
        $resultados = $stmt->fetchAll(PDO::FETCH_ASSOC);
        return $resultados;
    }
    public function obtener($ci){
         $stmt = $this->conn->prepare("select ci, paterno ,materno from persona where ci=:ci");
        $stmt->bindParam(":ci", $ci);
         $stmt->execute();
        
        $resultados = $stmt->fetchAll(PDO::FETCH_ASSOC);
        return $resultados;
    }
}