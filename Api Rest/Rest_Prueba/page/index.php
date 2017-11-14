<?php

/**
 * index
 * 
 * Rest_Prueba
 * 
 */
require_once '../include/ClsLogeo.php';
require_once '../include/ClsProducto.php';
require_once '../include/ClsCarrito.php';

require '../libs/Slim/Slim.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

$app->config(array(
    'templates.path' => 'vistas',
));

$app->contentType('text/html; charset=utf-8');

/* ----------------------------------------------------------------------------- */

/**
 * Logeo
 * url - /Rest_Prueba/page/logeo
 * method - POST
 */
$app->post('/logeo', function() use ($app) {
    $json = $app->request->getBody();
    $data = json_decode($json, true);

    $response = $app->response();
    $response['Content-Type'] = 'application/json';

    if (!empty($data)) {
        $NRO_HABITACION = $data['NRO_HABITACION'];
        $NRO_DOCUMENTO = $data['NRO_DOCUMENTO'];
        $USUARIO = $data['USUARIO'];
        $PASSWORD = $data['PASSWORD'];
        $dbL = new ClsLogeo();
        $res = $dbL->existLogeo($NRO_HABITACION, $NRO_DOCUMENTO, $USUARIO, $PASSWORD);

        if ($res == TRUE) {
            $array2["code"] = 200;
            $array2["desc"] = "Ingreso Correcto!!!";
            $array1["status"] = $array2;
        } else if ($res == FALSE) {
            $array2["code"] = 201;
            $array2["desc"] = "Falló en Logeo";
            $array1["status"] = $array2;
        } 
        $response->body(json_encode($array1));
    } else {
        $array2["code"] = 203;
        $array2["desc"] = "Campos necesarios";
        $array1["status"] = $array2;
        $response->body(json_encode($array1));
    }
});

/**
 * Logeo
 * url - /Rest_Prueba/page/servicio
 * method - POST
 */
$app->post('/servicio', function() use ($app) {
    $json = $app->request->getBody();
    $data = json_decode($json, true);

    $response = $app->response();
    $response['Content-Type'] = 'application/json';

    if (!empty($data)) {
        $ID_SERVICIO = $data['ID_SERVICIO'];
        
        $dbP = new ClsProducto();
        $res = $dbP->obtenerLista($ID_SERVICIO);

        $array1 = array();
        $array1["content"] = $res;
        $response->body(json_encode($array1));
    } else {
        $array2["code"] = 203;
        $array2["desc"] = "Campos necesarios";
        $array1["status"] = $array2;
        $response->body(json_encode($array1));
    }
});

/**
 * Carrito
 * url - /Rest_Prueba/page/carrito
 * method - POST
 */
$app->post('/carrito', function() use ($app) {
    $json = $app->request->getBody();
    $data = json_decode($json, true);

    $response = $app->response();
    $response['Content-Type'] = 'application/json';

    if (!empty($data)) {
        $NRO_HABITACION = $data['NRO_HABITACION'];
        $USUARIO = $data['USUARIO'];
        $ID_PRODUCTO = $data['ID_PRODUCTO'];
        
        $dbP = new ClsLCarrito();
        $res = $dbP->Carrito($NRO_HABITACION, $USUARIO, $ID_PRODUCTO);
        
        $response->body(json_encode($res));
    } else {
        $array2["code"] = 203;
        $array2["desc"] = "Campos necesarios";
        $array1["status"] = $array2;
        $response->body(json_encode($array1));
    }
});

/**
 * CarritoAbierto
 * url - /Rest_Prueba/page/carritoabierto
 * method - POST
 */
$app->post('/carritoabierto', function() use ($app) {
    $json = $app->request->getBody();
    $data = json_decode($json, true);

    $response = $app->response();
    $response['Content-Type'] = 'application/json';

    if (!empty($data)) {
        $NRO_HABITACION = $data['NRO_HABITACION'];
        $USUARIO = $data['USUARIO'];
        
        $dbP = new ClsLCarrito();
        $res = $dbP->obtenerCarritoAbierto($NRO_HABITACION, $USUARIO);
        
        $response->body(json_encode($res));
    } else {
        $array2["code"] = 203;
        $array2["desc"] = "Campos necesarios";
        $array1["status"] = $array2;
        $response->body(json_encode($array1));
    }
});

/**
 * deletedetalle
 * url - /Rest_Prueba/page/deletedetalle
 * method - POST
 */
$app->post('/deletedetalle', function() use ($app) {
    $json = $app->request->getBody();
    $data = json_decode($json, true);

    $response = $app->response();
    $response['Content-Type'] = 'application/json';

    if (!empty($data)) {
        $ID_CARRITO = $data['ID_CARRITO'];
        $SECUENCIA = $data['SECUENCIA'];
        $ID_PRODUCTO = $data['ID_PRODUCTO'];
        
        $dbP = new ClsLCarrito();
        $res = $dbP->deleteDetalle($ID_CARRITO, $SECUENCIA, $ID_PRODUCTO);
        
        $response->body(json_encode($res));
    } else {
        $array2["code"] = 203;
        $array2["desc"] = "Campos necesarios";
        $array1["status"] = $array2;
        $response->body(json_encode($array1));
    }
});

/**
 * obtenercarrito
 * url - /Rest_Prueba/page/obtenercarrito
 * method - POST
 */
$app->post('/obtenercarrito', function() use ($app) {
    $json = $app->request->getBody();
    $data = json_decode($json, true);

    $response = $app->response();
    $response['Content-Type'] = 'application/json';

    if (!empty($data)) {
        $ID_CARRITO = $data['ID_CARRITO'];
        
        $dbP = new ClsLCarrito();
        $res = $dbP->obtenerCarrito($ID_CARRITO);
        
        $response->body(json_encode($res));
    } else {
        $array2["code"] = 203;
        $array2["desc"] = "Campos necesarios";
        $array1["status"] = $array2;
        $response->body(json_encode($array1));
    }
});

/**
 * detallecarrito
 * url - /Rest_Prueba/page/detallecarrito
 * method - POST
 */
$app->post('/detallecarrito', function() use ($app) {
    $json = $app->request->getBody();
    $data = json_decode($json, true);

    $response = $app->response();
    $response['Content-Type'] = 'application/json';

    if (!empty($data)) {
        $ID_CARRITO = $data['ID_CARRITO'];
        
        $dbP = new ClsLCarrito();
        $res = $dbP->detalleCarrito($ID_CARRITO);
        
        $array1 = array();
        $array1["content"] = $res;
        $response->body(json_encode($array1));
    } else {
        $array2["code"] = 203;
        $array2["desc"] = "Campos necesarios";
        $array1["status"] = $array2;
        $response->body(json_encode($array1));
    }
});
$app->run();

