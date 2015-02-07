<?php

    require_once("include/DB_INFO.php");

    $conn = sqlsrv_connect(SERVER_NAME, array("UID"=>USER, "PWD"=>PASSWORD, "Database"=>DATABASE, "CharacterSet" => "UTF-8"));
	if($conn === false){
        die(print_r(sqlsrv_errors()));
	}


?>

