

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Fire map</title>


    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/index.css" rel="stylesheet">
    <script src="js/jquery-1.11.2.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

   
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?signed_in=true&key=AIzaSyA5Y5YzTgPbmdsw0Rbwe_k7Wn-TGOnDW_s&sensor=false&language=zh-TW&libraries=visualization"></script>
    
     <script type="text/javascript" src="js/googleMap_config.js"></script>
     <script type="text/javascript" src="js/googleMap_object.js"></script>
    
     <script type="text/javascript">
        

        function load_FireDepartment() {

            deleteMarkers();
            deleteCircles();
            deletePolygons();
            

            <?php
                
                require_once("include/connectDB.php");
	            header("Content-Type:text/html; charset=utf-8");

                $sql_select = "SELECT * FROM FireDepartment";
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

            clear();
            
            var i = 0;
            lat = <? echo json_encode($lat_arr)?>;
            lng = <? echo json_encode($lng_arr)?>;
            var name = <? echo json_encode($name_arr)?>;
            var addr=<?echo json_encode($addr_arr)?>;
            currentData='FireDepartment';

            load_table("臺北市政府消防局各分隊座標位置",name,lat,lng);

            while (i < lat.length) {
               heatmapData[i]=new google.maps.LatLng(lat[i],lng[i]);
               text[i]="<h5>"+name[i]+"</h5>"+addr[i];
               i++;
            }


            if(state=='layer')
                initialize_makers(lat,lng,text,FireDepartmentRadius,FireDepartmentColor);
            else if(state=='heatmap'){
                heatmap.setMap(null);
                initialize_heatmap(heatmapData);
            }
         

         <?php 
                $lat_arr = NULL;
                $lng_arr = NULL;
                $name_arr = NULL;
                $addr_arr = NULL;
         ?>


        }

        function load_Hospital(){

            deleteMarkers();
            deleteCircles();
         deletePolygons();
         

            <?php
                
                require_once("include/connectDB.php");
	            header("Content-Type:text/html; charset=utf-8");

                $sql_select = "SELECT * FROM EmergencyHospital";
                $stmt = sqlsrv_query($conn,$sql_select);
                if( $stmt === false ) {
                    die( print_r( sqlsrv_errors(), true));
                }

                while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
                    $name_arr[]=$row['name'];
                    $lat_arr[]=$row['lat'];
                    $lng_arr[]=$row['lng'];
                    $category_arr[]=$row['category'];
                    $addr_arr[]=$row['address_for_system'];
                    $telephone_arr[]=$row['telephone'];
                }

            ?>

            clear();
            
            var i = 0;
            lat = <? echo json_encode($lat_arr)?>;
            lng = <? echo json_encode($lng_arr)?>;
            var name = <? echo json_encode($name_arr)?>;
            var addr=<?echo json_encode($addr_arr)?>;
            var telephone=<?echo json_encode($telephone_arr)?>;
            var category=<?echo json_encode($category_arr)?>;

            currentData='Hospital';

            load_table("臺北市急救責任醫院",name,lat,lng);


            while (i < lat.length) {
                heatmapData[i]=new google.maps.LatLng(lat[i],lng[i]);
                text[i]="<h5>"+name[i]+"</h5>"+addr[i]+"</br>"+telephone[i]+"</br>"+category[i];

                if(category[i]=='醫學中心'){
                    HospitalRadius[i] = 2000;
                    HospitalColor[i] = '#0000FF';
                }
                else if(category[i]=='區域醫院'){
                     HospitalRadius[i] = 1000;
                     HospitalColor[i] = '#00BBFF';
                }
                else {
                     HospitalRadius[i] = 666;
                     HospitalColor[i] = '#00FFCC';
                }

                i++;
            }

            
            if(state=='layer')
                initialize_makers(lat,lng,text,HospitalRadius,HospitalColor);
             else if(state=='heatmap'){
                heatmap.setMap(null);
                initialize_heatmap(heatmapData);
            }


         <?php
             $name_arr = NULL;
             $lat_arr = NULL;
             $lng_arr = NULL;
             $category_arr = NULL;
             $addr_arr = NULL;
             $telephone_arr = NULL;
         ?>

        }

        function load_NarrowRoadway(){

            deleteMarkers();
            deleteCircles();
         deletePolygons();
          

            <?php
                
                require_once("include/connectDB.php");
	            header("Content-Type:text/html; charset=utf-8");

                $sql_select = "SELECT * FROM NarrowRoadway";
                $stmt = sqlsrv_query($conn,$sql_select);
                if( $stmt === false ) {
                    die( print_r( sqlsrv_errors(), true));
                }

                while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
                    $lat_arr[]=$row['lat'];
                    $lng_arr[]=$row['lng'];
                    $addr_arr[]=$row['roadway'];
                    $width_arr[]=$row['width'];
                    $polygon_arr[]=$row['polygon'];
                }

            ?>
            
            clear();

            var i = 0;
            lat = <? echo json_encode($lat_arr)?>;
            lng = <? echo json_encode($lng_arr)?>;
            var addr=<?echo json_encode($addr_arr)?>;
            var width=<?echo json_encode($width_arr)?>;
            var polygon=<?echo json_encode($polygon_arr)?>

            currentData='NarrowRoadway';

            while (i < lat.length) {
                heatmapData[i]=new google.maps.LatLng(lat[i],lng[i]);
                NarrowRoadwayRadius[i] = width[i]*10;
                text[i]="<h5>"+addr[i]+"</h5>";
                i++;
            }
         
            load_table("臺北市搶救不易狹小巷道清冊",addr,lat,lng);

            if(state=='layer')
                initialize_makers(lat,lng,text,polygon,NarrowRoadwayColor);
             else if(state=='heatmap'){
                heatmap.setMap(null);
                initialize_heatmap(heatmapData);
            }
            
         <?php
             $lat_arr = NULL;
             $lng_arr = NULL;
             $addr_arr = NULL;
             $width_arr = NULL;
             $polygon_arr = NULL;
         ?>

        }

        function load_SeriousFailureLocation(){
            
            deleteMarkers();
            deleteCircles();
         deletePolygons();
            

            <?php
                
                require_once("include/connectDB.php");
	            header("Content-Type:text/html; charset=utf-8");

                $sql_select = "SELECT * FROM SeriousFailureLocation";
                $stmt = sqlsrv_query($conn,$sql_select);
                if( $stmt === false ) {
                    die( print_r( sqlsrv_errors(), true));
                }

                while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
                    $name_arr[]=$row['name'];
                    $lat_arr[]=$row['lat'];
                    $lng_arr[]=$row['lng'];
                    $addr_arr[]=$row['address'];
                    $check_arr[]=$row['checkResult'];
                }

            ?>
            
            clear();

            var i = 0;

            lat = <? echo json_encode($lat_arr)?>;
            lng = <? echo json_encode($lng_arr)?>;
            var addr=<?echo json_encode($addr_arr)?>;
            var name=<?echo json_encode($name_arr)?>;
            var check=<?echo json_encode($check_arr)?>;

            currentData='SeriousFailureLocation';

            while (i < lat.length) {
                text[i]="<h5>"+name[i]+"</h5>"+addr[i]+"</br>"+check[i];
                heatmapData[i]=new google.maps.LatLng(lat[i],lng[i]);
                i++;
            }

         load_table("重大檢查不合格地區",name,lat,lng);


         if(state=='layer')
            initialize_makers(lat,lng,text,SeriousFailureLocationRadius,SeriousFailureLocationColor);
          else if(state=='heatmap'){
                heatmap.setMap(null);
                initialize_heatmap(heatmapData);
            }

         <?php
             $name_arr = NULL;
             $lat_arr = NULL;
             $lng_arr = NULL;
             $addr_arr = NULL;
             $check_arr = NULL;
         ?>

        }

        function load_IllegalConstruction(){

            deleteMarkers();
            deleteCircles();
         deletePolygons();

            <?php
                
                require_once("include/connectDB.php");
	            header("Content-Type:text/html; charset=utf-8");

                $sql_select = "SELECT * FROM IllegalConstruction";
                $stmt = sqlsrv_query($conn,$sql_select);
                if( $stmt === false ) {
                    die( print_r( sqlsrv_errors(), true));
                }

                while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
                    $lat_arr[]=$row['lat'];
                    $lng_arr[]=$row['lng'];
                    $addr_arr[]=$row['address'];
                    $area_arr[]=$row['area'];
                }

            ?>

            clear();
            
            var i = 0;

            lat = <? echo json_encode($lat_arr)?>;
            lng = <? echo json_encode($lng_arr)?>;
            var addr=<?echo json_encode($addr_arr)?>;
            var area=<?echo json_encode($area_arr)?>;

            currentData='IllegalConstruction';

            while (i < lat.length) {
                IllegalConstructionRadius[i] = area[i]/10;
                text[i]="<h5>"+addr[i]+"</h5>";
                heatmapData[i]=new google.maps.LatLng(lat[i],lng[i]);
                i++;
            }

         load_table("「屋頂違建隔出3個使用單元以上」清冊",addr,lat,lng);

         if(state=='layer')
            initialize_makers(lat,lng,text,IllegalConstructionRadius,IllegalConstructionColor);
          else if(state=='heatmap'){
                heatmap.setMap(null);
                initialize_heatmap(heatmapData);
            }

         <?php
             $lat_arr = NULL;
             $lng_arr = NULL;
             $addr_arr = NULL;
             $area_arr = NULL;
         ?>

        }


        function load_ParkWaterStation(){

            deleteMarkers();
            deleteCircles();
         deletePolygons();
            

            <?php
                
                require_once("include/connectDB.php");
	            header("Content-Type:text/html; charset=utf-8");

                $sql_select = "SELECT * FROM ParkWaterStation";
                $stmt = sqlsrv_query($conn,$sql_select);
                if( $stmt === false ) {
                    die( print_r( sqlsrv_errors(), true));
                }

                while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
                    $lat_arr[]=$row['lat'];
                    $lng_arr[]=$row['lng'];
                    $name_arr[]=$row['name'];
                    $addr_arr[]=$row['address'];
                    $note_arr[]=$row['note'];
                }

            ?>

            clear();
            
            var i = 0;

            lat = <? echo json_encode($lat_arr)?>;
            lng = <? echo json_encode($lng_arr)?>;
            var addr=<?echo json_encode($addr_arr)?>;
            var name=<?echo json_encode($name_arr)?>;
            var note=<?echo json_encode($note_arr)?>;

            currentData='ParkWaterStation';

            while (i < lat.length) {
                text[i]="<h5>"+name[i]+"</h5>"+addr[i]+"</br>"+note[i];
                heatmapData[i]=new google.maps.LatLng(lat[i],lng[i]);
                i++;
            }

            load_table("緊急維生取水站",name,lat,lng);


         if(state=='layer')
            initialize_makers(lat,lng,text,ParkWaterStationRadius,ParkWaterStationColor);
          else if(state=='heatmap'){
                heatmap.setMap(null);
                initialize_heatmap(heatmapData);
            }
             
         <?php
              $lat_arr = NULL;
              $lng_arr = NULL;
              $name_arr = NULL;
              $addr_arr = NULL;
              $note_arr = NULL; 
         ?>

        }

        function load_Village(){

            <?php
                
                require_once("include/connectDB.php");
	            header("Content-Type:text/html; charset=utf-8");

                $sql_select = "SELECT * FROM Village";
                $stmt = sqlsrv_query($conn,$sql_select);
                if( $stmt === false ) {
                    die( print_r( sqlsrv_errors(), true));
                }

                while( $row = sqlsrv_fetch_array( $stmt, SQLSRV_FETCH_ASSOC) ) {
                    $poly_arr[]=$row['json_polygon'];
                  
                }
                
            ?>

         var poly=<?echo json_encode($poly_arr)?>;
         var i=0;

         while(i<poly.length){
            initialize_polygon(poly[i],'#0000FF');
            i++;

         }

       }

       google.maps.event.addDomListener(window, 'load', initialize);
     </script>

</head>
<body style="border: 0 none; margin:0; padding:0;">

    <nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Fire Map</a>
    </div>

    <?php require_once("about.php");?>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="active"><a href="#">Link<span class="sr-only">(current)</span></a></li>
        
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">function<span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="#" onclick="load_layer();">Layer map</a></li>
            <li><a href="#" onclick="load_heatmap()">Heat map</a></li>
            <li><a href="api_page.php">API</a></li>
            <li class="divider"></li>
            <li><a href="#" data-toggle="modal" data-target=".bs-example-modal-sm">關於</a></li>
          </ul>
        </li>
        <li><a href="#" onclick="clearMarkers();">Hide All Makers</a></li>
        <li><a href="#" onclick="showMarkers();">Show All Markers</a></li>
        <li><a href="#" onclick="deleteMarkers(); deleteCircles();">Delete All Markers</a></li>
        <li><a href="#" onclick="showCircles();">Show All Circles</a></li>
        <li><a href="#" onclick="clearCircles();">Hide All Circles</a></li>

      </ul>
      <form class="navbar-form navbar-left" role="search">
        <div class="form-group">
          <input id="search_text" type="text" class="form-control" placeholder="Search">
        </div>
        <button type="submit" class="btn btn-default" onclick="search();">Submit</button>
      </form>
      
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

    
    <div class="container">
    <div class="row">
        <div class="col-md-9 col-md-push-3" id="content">
            <div id="map-canvas" style="width: 840px; height: 480px; margin: 0; padding: 0;"></div>
            <?php require_once("table.php");?>
        </div>

        <div class="col-md-3 col-md-pull-9">

            <div class="list-group" > 
                <a href="#" class="list-group-item active" id="list_title">Layer</a>
                <a href="#" class="list-group-item" onclick="load_FireDepartment();">消防局</a>
                <a href="#" class="list-group-item" onclick="load_Hospital();">臺北市急救責任醫院</a>
                <a href="#" class="list-group-item" onclick="load_NarrowRoadway();">狹小巷道</a>
                <a href="#" class="list-group-item" onclick="load_SeriousFailureLocation();">重大不合格地區</a>
                <a href="#" class="list-group-item" onclick="load_IllegalConstruction();">違建</a>
                <a href="#" class="list-group-item" onclick="load_ParkWaterStation();">緊急維生取水站</a>
                <a href="#" class="list-group-item" onclick="load_section();">行政區</a>
                <a href="#" class="list-group-item" onclick="load_traffic();">Traffic layer</a>
                <!--
                <a href="#" class="list-group-item" onclick="load_Village();">里</a>
                <a href="#" class="list-group-item" onclick="load_Hydrant();">消防栓</a>
                -->
                <a href="#" class="list-group-item" onclick="load_section2();">test</a>
                <a href="#" class="list-group-item" id="debug"></a>
               
            </div>
        </div>
    </div>
    </div>


    <?php require_once("footer.php");?>


</body>

</html>​