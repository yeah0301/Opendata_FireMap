<?php
    

    require_once("include/connectDB.php");
	header("Content-Type:text/html; charset=utf-8");

     $rawdata = NULL;

     
     function select_FireDepartment($conn){
        $sql_select = "SELECT * FROM FireDepartment";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>floatval($row['lat'])
            ,"lng"=>floatval($row['lng'])
            ,"name"=>urlencode($row['name'])
            ,"address"=>urlencode($row['address']));
        }

        return $rawdata;
     }


     function select_Hosiptal($conn){
        $sql_select = "SELECT * FROM EmergencyHospital";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>floatval($row['lat'])
            ,"lng"=>floatval($row['lng'])
            ,"name"=>urlencode($row['name'])
            ,"address"=>urlencode($row['address_for_display'])
            ,"telephone"=>$row['telephone']
            ,"category"=>urlencode($row['category']));
        }

        return $rawdata;
     }


     function select_NarrowRoadway($conn){
        $sql_select = "SELECT * FROM NarrowRoadway";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>floatval($row['lat'])
            ,"lng"=>floatval($row['lng'])
            ,"roadway"=>urlencode($row['roadway'])
            ,"section"=>urlencode($row['section'])
            ,"width"=>floatval($row['width'])
            ,"team"=>urlencode($row['team']));
        }

        return $rawdata;
     }


     function select_LevelDifficultyOfFireRescue($conn){
        $sql_select = "SELECT * FROM LevelDifficultyOfFireRescue";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>floatval($row['lat'])
            ,"lng"=>floatval($row['lng'])
            ,"section"=>urlencode($row['section'])
            ,"address"=>urlencode($row['address'])
            ,"level"=>floatval($row['level'])
            ,"item"=>floatval($row['item'])
            ,"name"=>urlencode($row['name'])
            ,"hasAisle"=>$row['hasAisle']);
        }

        return $rawdata;
     }

     function select_IllegalConstruction($conn){
        $sql_select = "SELECT * FROM IllegalConstruction";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>floatval($row['lat'])
            ,"lng"=>floatval($row['lng'])
            ,"section"=>urlencode($row['section'])
            ,"address"=>urlencode($row['address'])
            ,"area"=>floatval($row['area'])
            ,"date"=>$row['date']);
        }

        return $rawdata;
     }

     function select_SeriousFailureLocation($conn){
        $sql_select = "SELECT * FROM SeriousFailureLocation";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>floatval($row['lat'])
            ,"lng"=>floatval($row['lng'])
            ,"name"=>urlencode($row['name'])
            ,"address"=>urlencode($row['address'])
            ,"checkResult"=>urlencode($row['checkResult'])
            ,"date"=>$row['date']);
        }

        return $rawdata;
     }

     function select_FireCount($conn){
        $sql_select = "SELECT * FROM FireCount";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
            $obj=(array)$row['date'];
            $rawdata[]= array("section"=>urlencode($row['section'])
            ,"count"=>intval($row['count'])
            ,"date"=>$obj[date]);
        }

        return $rawdata;
     }


     function select_Village($conn){
        $sql_select = "SELECT * FROM Village";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
           
            $rawdata[]= array("name"=>urlencode($row['name'])
            ,"section"=>urlencode($row['section'])
            ,"area"=>floatval($row['area'])
            ,"polygon"=>$row['polygon']);
        }

        return $rawdata;
     }

     switch($_GET["data"]){
         case NULL:
            $rawdata=select_Hosiptal($conn);
            break;
         case "hosiptal":
            $rawdata=select_Hosiptal($conn);
            echo urldecode(json_encode($rawdata));
            break;
         case "firedepartment":
            $rawdata=select_FireDepartment($conn);
            echo urldecode(json_encode($rawdata));
            break;
         case "narrowroadway":
            $rawdata=select_NarrowRoadway($conn);
            echo urldecode(json_encode($rawdata));
            break;
         case "difficulty":
            $rawdata=select_LevelDifficultyOfFireRescue($conn);
            echo urldecode(json_encode($rawdata));
            break;
         case "illegal":
            $rawdata=select_IllegalConstruction($conn);
            echo urldecode(json_encode($rawdata));
            break;
         case "serious":
            $rawdata=select_SeriousFailureLocation($conn);
            echo urldecode(json_encode($rawdata));
            break;
        case "firecount":
            $rawdata=select_FireCount($conn);
            echo urldecode(json_encode($rawdata));
            break;
         case "village":
            $rawdata=select_Village($conn);
            echo urldecode(json_encode($rawdata));
            break;
         default:
            break;
     }

?>


