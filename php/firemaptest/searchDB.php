<?php

    require_once("include/connectDB.php");
	header("Content-Type:text/html; charset=utf-8");

    $rawdata = NULL;

    /*
        $sql_select = "SELECT * FROM IllegalConstruction where SQRT( POWER(lat-?,2)+ POWER(lng-?,2) ) < 0.009";
        $params = array($_GET["lat"], $_GET["lng"]);
        $stmt = sqlsrv_query($conn,$sql_select,$params);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>floatval($row['lat'])
            ,"lng"=>floatval($row['lng'])
            ,"text"=>"<h5>".urlencode($row['address'])."</h5>"
            ,"area"=>floatval($row['area'])
            ,"name"=>urlencode($row['address']));

        }*/


        function search_Hydrant($conn){

             $sql_select = "SELECT * FROM Hydrant where SQRT( POWER(lng-?,2)+ POWER(lat-?,2) ) < 0.0054";
             
             $params = array($_GET["lat"], $_GET["lng"]);

             $stmt = sqlsrv_query($conn,$sql_select,$params);
             if( $stmt === false ) {
                die( print_r( sqlsrv_errors(), true));
             }

            while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
                $rawdata[]= array("lat"=>floatval($row['lng']),"lng"=>floatval($row['lat']));
            }
           
        return $rawdata;

      }

        
        switch($_GET["data"]){
            case "hydrant":
                $rawdata = search_Hydrant($conn);
                echo urldecode(json_encode($rawdata));
                break;
            
        }

        

?>

