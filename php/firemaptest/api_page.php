<?php
    
     require_once("include/connectDB.php");
	 header("Content-Type:text/html; charset=utf-8");

     $sql_select = "SELECT * FROM FireDepartment";
     $stmt = sqlsrv_query($conn,$sql_select);
     if( $stmt === false ) {
       die( print_r( sqlsrv_errors(), true));
      }

     while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {

        $rawdata[]= array("lat"=>floatval($row['lat']),"lng"=>floatval($row['lng']),"name"=>$row['name'],"address"=>$row['address']);
       
     }

?>


<style>
    div{
        display: block;

    }    
    
    .box-header{
        padding-left: 23px;
        
    }
    .box-content{
        padding-right: 23px;
        padding-left: 23px;
    }
    
    .box{
        margin-top: 23px;
        margin-right: 43px;
        margin-left: 43px;
        border: 1px solid #eeeaea;
        border-radius: 7px;
    }
</style>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title></title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery-1.11.2.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?key=AIzaSyA5Y5YzTgPbmdsw0Rbwe_k7Wn-TGOnDW_s&sensor=false&language=zh-TW"></script>
        
 
    </head>

    <body>

        <div class="box">
       
            <ul class="nav nav-tabs nav-justified" role="tablist" id="myTab">
                <li role="presentation" class="active"><a href="#home" role="tab" data-toggle="tab">臺北市急救責任醫院</a></li>
                <li role="presentation"><a href="#profile" role="tab" data-toggle="tab">Profile</a></li>
                <li role="presentation"><a href="#messages" role="tab" data-toggle="tab">Messages</a></li>
                <li role="presentation"><a href="#settings" role="tab" data-toggle="tab">Settings</a></li>
            </ul>

            <script>
                $(function () {
                    $('#myTab a:last').tab('show')
                })
            </script>


            <div class="box-header">
                <h1>臺北市急救責任醫院<small>    src:臺北市政府衛生局</small></h1>
            </div>

             <hr class="line">

            <div class="box-content">
            <pre><script>
 
                document.write(JSON.stringify(<? echo json_encode($rawdata)?>));

            </script></pre>
            </div>
        
        </div>
    </body>
</html>

<?php 
    $rawdata = NULL;
?>
