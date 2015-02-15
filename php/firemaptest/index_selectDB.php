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
            ,"text"=>"<h5>".urlencode($row['name'])."</h5>".urlencode($row['address'])
            ,"name"=>urlencode($row['name'])); 
        }

        return $rawdata;

     }


     function select_LevelDifficultyOfFireRescue($conn,$level){
        $sql_select = "SELECT * FROM LevelDifficultyOfFireRescue where level=".$level;
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>floatval($row['lat'])
            ,"lng"=>floatval($row['lng'])
            ,"text"=>"<h5>".urlencode($row['name'])."</h5>".urlencode($row['address'])."</br>"
            ,"name"=>urlencode($row['name'])
            ,"section"=>urlencode($row['section'])
            ,"address"=>urlencode($row['address'])
            ,"item"=>intval($row['item'])
            ,"hasAisle"=>$row['hasAisle']
            ,"level"=>intval($row['level'])
            ); 
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
            ,"text"=>"<h5>".urlencode($row['name'])."</h5>".urlencode($row['address_for_display'])."</br>".$row['telephone']."</br>".urlencode($row['category'])
            ,"name"=>urlencode($row['name'])
            ,"category"=>urlencode($row['category']));
        }

        return $rawdata;


     }


     function select_NarrowRoadway($conn,$level){
        $sql_select = "SELECT * FROM NarrowRoadway where level='".$level."'";
        $stmt = sqlsrv_query($conn,$sql_select);
        if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
        }

        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

            $rawdata[]= array("lat"=>$row['lat']
            ,"lng"=>$row['lng']
            ,"level"=>$row['level']
            ,"polygon"=>$row['polygon']
            ,"name"=>$row['roadway']);
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
            ,"text"=>"<h5>".urlencode($row['name'])."</h5>".urlencode($row['address'])."</br>".urlencode($row['checkResult'])
            ,"name"=>urlencode($row['name']));
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
            ,"text"=>"<h5>".urlencode($row['address'])."</h5>"
            ,"area"=>floatval($row['area'])
            ,"name"=>urlencode($row['address']));

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
            $rawdata[]=$row['json_polygon'];    
         }
         
         return $rawdata;
         
     }


     function select_Result($conn){

         $sql_select = "SELECT * FROM Result";
         $stmt = sqlsrv_query($conn,$sql_select);
         if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
         }

         while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
            $rawdata[]= array("polygon"=>$row['json_polygon']
            ,"score"=>floatval($row['score'])
            );
         }
         
         return $rawdata;
         
     }


     function select_Alert($conn){

         $rawdata = NULL;

         $sql_select = "SELECT * FROM mobile_record";
         $stmt = sqlsrv_query($conn,$sql_select);
         if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
         }

         while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

             $rawdata[]= array("lat"=>floatval($row['y'])
            ,"lng"=>floatval($row['x'])
            ,"pics"=>$row['pics']
            ,"worker"=>$row['worker']
            );
            
         }
         
         return $rawdata;
         
     }

     


     switch($_GET["data"]){
         case "firedepartment":
            $rawdata=select_FireDepartment($conn);
            echo urldecode(json_encode($rawdata));
            break;
         case "hosiptal":
            $rawdata=select_Hosiptal($conn);
            echo urldecode(json_encode($rawdata));
            break;
        case "serious":
            $rawdata=select_SeriousFailureLocation($conn);
            echo urldecode(json_encode($rawdata));
            break;
        case "illegal":
            $rawdata=select_IllegalConstruction($conn);
            echo urldecode(json_encode($rawdata));
            break;
        case "village":
            $rawdata=select_Village($conn);
            echo urldecode(json_encode($rawdata));
            break;
        case "result":
            $rawdata=select_Result($conn);
            echo urldecode(json_encode($rawdata));
            break;
        case "narrow_red":
            $rawdata=select_NarrowRoadway($conn,"red");
            echo urldecode(json_encode($rawdata));
            break;
        case "narrow_yellow":
            $rawdata=select_NarrowRoadway($conn,"yellow");
            echo urldecode(json_encode($rawdata));
            break;
        case "narrow_blue":
            $rawdata=select_NarrowRoadway($conn,"blue");
            echo urldecode(json_encode($rawdata));
            break;
        case "difficulty_2":
            $rawdata=select_LevelDifficultyOfFireRescue($conn,2);
            echo urldecode(json_encode($rawdata));
            break;
        case "difficulty_1":
            $rawdata=select_LevelDifficultyOfFireRescue($conn,1);
            echo urldecode(json_encode($rawdata));
            break;
        case "alert":
            $rawdata=select_Alert($conn);
            echo urldecode(json_encode($rawdata));
            break;
         default:
            break;
     }

?>


