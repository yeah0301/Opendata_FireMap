<?php
    
    require_once("include/connectDB.php");
    
     
	
         $sql_select = "SELECT lat,lng,name,address FROM FireDepartment";
         $stmt = sqlsrv_query($conn,$sql_select);
         if( $stmt === false ) {
            die( print_r( sqlsrv_errors(), true));
         }


        while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
            $lat_arr[]=$row['lat'];
            $lng_arr[]=$row['lng'];
            $name_arr[]=$row['name'];
            $addr_arr[]=$row['address'];
        }

    

?>


