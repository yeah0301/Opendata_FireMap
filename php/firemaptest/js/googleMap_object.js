function clear(){

    lat = [];
    lng = [];
    text = [];
    heatmapData = [];

}

function load_traffic(){
  var trafficLayer = new google.maps.TrafficLayer();
  trafficLayer.setMap(map);
}


function load_heatmap(){

    deleteMarkers();
    deleteCircles();
    deletePolygons();

    state = 'heatmap';

    initialize_heatmap(heatmapData);

    $("#list_title").text("Heat Map");
}


function load_layer(){

    state = 'layer';

    //clear heat map
    heatmap.setMap(null);

    switch(currentData){
        case 'FireDepartment':
            initialize_makers(lat, lng, text, FireDepartmentRadius, FireDepartmentColor);
            break;
        case 'Hospital':
            initialize_makers(lat,lng,text,HospitalRadius,HospitalColor);
            break;
        case 'NarrowRoadway':
            initialize_makers(lat,lng,text,NarrowRoadwayRadius,NarrowRoadwayColor);
            break;
        case 'SeriousFailureLocation':
            initialize_makers(lat, lng, text, SeriousFailureLocationRadius, SeriousFailureLocationColor);
            break;
        case 'ParkWaterStation':
            initialize_makers(lat, lng, text, ParkWaterStationRadius, ParkWaterStationColor);
            break;
        default:
            break;
    }

    $("#list_title").text("Layer");

}


function initialize_makers(lat,lng,text,radius,color){
        
    var i=0;
    if(radius instanceof Array && color instanceof Array){
        while(i<lat.length){
            initMarker(lat[i], lng[i], text[i], radius[i], color[i]);
            i++;
        }
    }else if(radius instanceof Array && !color instanceof Array){
        while(i<lat.length){
            initMarker(lat[i], lng[i], text[i], radius[i], color);
            i++;
        }
    }else{
        while(i<lat.length){
            initMarker(lat[i], lng[i], text[i], radius, color);
            i++;
        }
    }
}

function initialize_heatmap(MVCdata){

     var pointArray = new google.maps.MVCArray(MVCdata);

     heatmap = new google.maps.visualization.HeatmapLayer({
        data: pointArray,
        radius:100,
        opacity:0.7
        });

     heatmap.setMap(map);
}

function initialize() {
  
  var mapOptions = {
    zoom: 14,
    center: center,

    //mapTypeId: google.maps.MapTypeId.ROADMAP,
    //minZoom : 2,

    overviewMapControl :true,
    overviewMapControlOptions:{
        position:google.maps.ControlPosition.BOTTOM_LEFT
    }
    
  };

  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);

}


function setMessageBlock(marker, text) {

   var infowindow = new google.maps.InfoWindow({
        content: text,
   });

   google.maps.event.addListener(marker, 'click', function () {
       infowindow.open(marker.get('map'), marker);
   });

}

function setPolygonMessageBlock(polygon,text,position){
    var infowindow = new google.maps.InfoWindow({
        content: text
        //position:position
   });

   infowindow.setPosition(position);

   google.maps.event.addListener(marker, 'click', function () {
       infowindow.open(marker.get('map'), marker);
   });

}

function initialize_polygon(json,color){
    
    var obj=JSON.parse(json);
       var paths = [];
       var i = 0;
    
       while(i<obj.length){
            paths[i] = new g.LatLng(obj[i].lat, obj[i].lng);
            i++;
       }

        var polygon = new g.Polygon({
           path:paths,
           map:map,
           strokeColor: '#FF0000',
           strokeOpacity: 0.8,
           strokeWeight: 2, 
           fillColor:color, 
           fillOpacity: 0.35});

    polygons.push(polygon);
}


function initMarker(lat, lng, text,r,color) {

    
   if (currentData=='NarrowRoadway') {

       initialize_polygon(r,color);

   }else{

         var coordinate = new g.LatLng(lat, lng);
         var marker = new g.Marker({ map: map, position: coordinate, /*icon: hospital_icon,*/ zIndex: 80 - 1, title: text });
        
        var circle = new g.Circle({ map: map, radius: r, fillColor: color, strokeWeight: 1, strokeColor: bc });
        circle.bindTo('center', marker, 'position');
        circles.push(circle);

        setMessageBlock(marker, text);
        markers.push(marker);
   
   }
  
}



 function load_section(){

     var ctaLayer = new google.maps.KmlLayer({
        url: 'https://mapsengine.google.com/map/kml?mid=zv8SczbAcpuw.kfNCLayILdn8&lid=zv8SczbAcpuw.kGGySsbX9BdA'
     });
        
     ctaLayer.setMap(map);
     
}


 function load_section2(){

     var ctaLayer = new google.maps.KmlLayer({
        url: 'http://ogc.tpgos.taipei.gov.tw/tp98_6_demo/kml/%E8%87%BA%E5%8C%97%E5%B8%82%E9%87%8C%E7%95%8C%E5%9C%96.kml'
     });
        
     ctaLayer.setMap(map);
     
}



function load_Hydrant(){

    var ctaLayer = new google.maps.KmlLayer({
        url: 'http://ogc.tpgos.taipei.gov.tw/tp98_6_demo/kml/%E5%A4%A7%E8%87%BA%E5%8C%97%E5%9C%B0%E5%8D%80%E6%B6%88%E9%98%B2%E6%A0%93%E5%88%86%E5%B8%83%E9%BB%9E%E4%BD%8D%E5%9C%96.kml'
     });
        
     ctaLayer.setMap(map);
  
}



function search(){

    var address = document.getElementById("search_text").value;

    geocoder.geocode( { 'address': address}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        map.setCenter(results[0].geometry.location);
        var marker = new google.maps.Marker({
            map: map,
            position: results[0].geometry.location,
            icon:angrybird_icon
        });
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
    });

}

function clearPolygons(){
    setAllMap(polygons,null);
}

function deletePolygons(){
    clearPolygons();
    polygons = [];
}

// Removes the markers from the map, but keeps them in the array.
 function clearMarkers() {
    setAllMap(markers,null);
 }

 function setAllMap(objects,map) {
    for (var i = 0; i < objects.length; i++) {
        objects[i].setMap(map);
    }
}

// Shows any markers currently in the array.
function showMarkers() {
  setAllMap(markers,map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
  clearMarkers();
  markers = [];
}

// Shows any circles currently in the array.
function showCircles() {
  setAllMap(circles,map);
}

// Removes the circles from the map, but keeps them in the array.
function clearCircles(){
    setAllMap(circles,null);
}

// Deletes all circles in the array by removing references to them.
function deleteCircles(){
    clearCircles();
    circles = [];
}



function back(){
    
     if (wo == true) {
                w.open(map);
                if (wc == true) {
                    var o = setTimeout("w.close()", wt);
                }
            }
}

function load_table(table_name,name,lat,lng){

   $("#table_name").text(table_name);
   $("#test").children().remove();

   for(var i=0;i < name.length;i++){
       
        $("#test").append("<tr><th scope='row'>" + i + "</th><td>" + name[i] + "</td><td>" + lat[i] + "</td><td>" + lng[i] + "</td></tr>");
   }
   

}



