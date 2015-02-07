<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Heatmaps</title>
    <style>
      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
      }
      #panel {
        position: absolute;
        top: 5px;
        left: 50%;
        margin-left: -180px;
        z-index: 5;
        background-color: #fff;
        padding: 5px;
        border: 1px solid #999;
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true&libraries=visualization"></script>
    <script>
// Adding 500 Data Points
var map, heatmap;


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
                }
                
            ?>

         var lat = <? echo json_encode($lat_arr)?>;
         var lng = <? echo json_encode($lng_arr)?>;

        var taxiData = [];
        var i=0;

        while (i < lat.length) {
               taxiData[i]=new google.maps.LatLng(lat[i],lng[i]);
                i++;
            }

function initialize() {
  var mapOptions = {
    zoom: 14,
    center: new google.maps.LatLng(25.042424, 121.532985),
    mapTypeId: google.maps.MapTypeId.SATELLITE
  };

  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);

}

function toggleHeatmap() {
  heatmap.setMap(heatmap.getMap() ? null : map);
}

function changeGradient() {
  var gradient = [
    'rgba(0, 255, 255, 0)',
    'rgba(0, 255, 255, 1)',
    'rgba(0, 191, 255, 1)',
    'rgba(0, 127, 255, 1)',
    'rgba(0, 63, 255, 1)',
    'rgba(0, 0, 255, 1)',
    'rgba(0, 0, 223, 1)',
    'rgba(0, 0, 191, 1)',
    'rgba(0, 0, 159, 1)',
    'rgba(0, 0, 127, 1)',
    'rgba(63, 0, 91, 1)',
    'rgba(127, 0, 63, 1)',
    'rgba(191, 0, 31, 1)',
    'rgba(255, 0, 0, 1)'
  ]
  heatmap.set('gradient', heatmap.get('gradient') ? null : gradient);
}

function changeRadius() {
  heatmap.set('radius', heatmap.get('radius') ? null : 200);
}

function changeOpacity() {
  heatmap.set('opacity', heatmap.get('opacity') ? null : 0.2);
}

function load_heatmap(){


        var pointArray = new google.maps.MVCArray(taxiData);

  heatmap = new google.maps.visualization.HeatmapLayer({
    data: pointArray,
    radius:100,
    opacity:0.7
  });

        heatmap.setMap(map);

        }

google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>

  <body>
    <div id="panel">
      <button onclick="toggleHeatmap()">Toggle Heatmap</button>
      <button onclick="changeGradient()">Change gradient</button>
      <button onclick="changeRadius()">Change radius</button>
      <button onclick="changeOpacity()">Change opacity</button>
        <button onclick="load_heatmap()">load</button>
    </div>
    <div id="map-canvas"></div>
  </body>
</html>