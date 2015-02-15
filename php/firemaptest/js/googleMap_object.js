
function load_heatmap(){
    if (state != 'heatmap') {

        state = 'heatmap';
        $("#list_title").text("Heat Map");
        $("#result").attr('class', '');
        $("#layer").attr('class', '');
        $("#heat").attr('class', 'active');

        if (FireDepartmentJson != null) {
            deleteMarkers('FireDepartment');
            deleteCircles('FireDepartment');
            $("#store_firedepartment").attr('class', 'btn btn-warning');
            initial_heatmap(FireDepartmentJson,'FireDepartment');
        }

        if (HospitalJson != null) {
            deleteMarkers('Hospital');
            deleteCircles('Hospital');
            $("#store_hospital").attr('class', 'btn btn-warning');
            initial_heatmap(HospitalJson,'Hospital');
        }

        if (SeriousFailureLocationJson != null) {
            deleteMarkers('SeriousFailureLocation');
            deleteCircles('SeriousFailureLocation');
            $("#store_seriousfailurelocation").attr('class', 'btn btn-warning');
            initial_heatmap(SeriousFailureLocationJson,'SeriousFailureLocation');
        }

        if (LevelDifficultyJson_2 != null) {
            deleteMarkers('difficulty_2');
            deleteCircles('difficulty_2');
            $("#store_leveldifficulty2").attr('class', 'btn btn-warning');
            initial_heatmap(LevelDifficultyJson_2,'difficulty_2');
        }

        if (LevelDifficultyJson_1 != null) {
            deleteMarkers('difficulty_1');
            deleteCircles('difficulty_1');
            $("#store_leveldifficulty1").attr('class', 'btn btn-warning');
            initial_heatmap(LevelDifficultyJson_1,'difficulty_1');
        }

        if (IllegalConstructionJson != null) {
            deleteMarkers('IllegalConstruction');
            deleteCircles('IllegalConstruction');
            $("#store_illegalconstruction").attr('class', 'btn btn-warning');
            initial_heatmap(IllegalConstructionJson,'IllegalConstruction');
        }

    }
    
}


function load_layer(){

    if (state != 'layer') {

        state = 'layer';
        $("#list_title").text("Layer");
        $("#result").attr('class', '');
        $("#heat").attr('class', '');
        $("#layer").attr('class', 'active');
        $("#layercontrol").css("border", "5px solid #FF0000");


        if (FireDepartmentJson != null) {
            deleteHeatmap('FireDepartment');
            $("#store_firedepartment").attr('class', 'btn btn-success');
            initial_makers(FireDepartmentJson,'FireDepartment');
        }

        if (HospitalJson != null) {
            deleteHeatmap('Hospital');
            $("#store_hospital").attr('class', 'btn btn-success');
            initial_makers(HospitalJson,'Hospital');
        }

        if (SeriousFailureLocationJson != null) {
            deleteHeatmap('SeriousFailureLocation');
            $("#store_seriousfailurelocation").attr('class', 'btn btn-success');
            initial_makers(SeriousFailureLocationJson,'SeriousFailureLocation');
        }

        if (LevelDifficultyJson_2 != null) {
            deleteHeatmap('difficulty_2');
            $("#store_leveldifficulty2").attr('class', 'btn btn-success');
            initial_makers(LevelDifficultyJson_2,'difficulty_2');
        }

        if (LevelDifficultyJson_1 != null) {
            deleteHeatmap('difficulty_1');
            $("#store_leveldifficulty1").attr('class', 'btn btn-success');
            initial_makers(LevelDifficultyJson_1,'difficulty_1');
        }

        if (IllegalConstructionJson != null) {
            deleteHeatmap('IllegalConstruction');
            $("#store_illegalconstruction").attr('class', 'btn btn-success');
            initial_makers(IllegalConstructionJson,'IllegalConstruction');
        }


    }

    

}

function initial_makers(json,data){
        
    var i=0;

    switch(data){

        case "FireDepartment":
            while(i<json.length){
                initMarker(json[i].lat, json[i].lng, 'img/Fire-Truck-2-48.png' ,json[i].text, FireDepartmentRadius, FireDepartmentColor);
                i++;
            }
            break;

        case "Hospital":
            while(i<json.length){

                if(json[i].category=="醫學中心"){
                    HospitalRadius = 2000;
                    HospitalColor = '#0000FF';
                }else if(json[i].category=="區域醫院"){
                    HospitalRadius = 1000;
                    HospitalColor = '#00BBFF';
                }else {
                    HospitalRadius = 666;
                    HospitalColor = '#00FFCC';
                }

                initMarker(json[i].lat, json[i].lng, 'img/hospital-building.png',json[i].text, HospitalRadius,HospitalColor);
                i++;
            }
            break;

        case "SeriousFailureLocation":
            while(i<json.length){
                initMarker(json[i].lat, json[i].lng, 'img/wrong-01-32.png',json[i].text, SeriousFailureLocationRadius, SeriousFailureLocationColor);
                i++;
            }
            break;

        case "IllegalConstruction":
            while(i<json.length){
                IllegalConstructionRadius = json[i].area/10;
                initMarker(json[i].lat, json[i].lng, 'img/House.png',json[i].text, IllegalConstructionRadius, IllegalConstructionColor);
                i++;
            }
            break;
        case "difficulty_2":
            while (i < json.length) {
                initMarker(json[i].lat, json[i].lng, 'img/campfire-32.png',json[i].text, LevelDifficultyRadius_2, LevelDifficultyColor_2);
                i++;
            }
        case "difficulty_1":
            while (i < json.length) {
                initMarker(json[i].lat, json[i].lng, 'img/badge-first-32.png',json[i].text, LevelDifficultyRadius_1, LevelDifficultyColor_1);
                i++;
            }
            break;
    }
}

function initial_heatmap(json,data){

    var MVCArray = [];
    var i=0;
    
    while(i<json.length){
        MVCArray[i] = new google.maps.LatLng(json[i].lat, json[i].lng);
        i++;
    }
    
    var pointArray = new google.maps.MVCArray(MVCArray);


    switch(data){
        case 'FireDepartment':
            heatmap = new google.maps.visualization.HeatmapLayer({
                data: pointArray,
                radius: 50,
                opacity: 0.6,
                gradient: FireDepartmentGradient
            });
            FireDepartmentHeatmap = heatmap;
            break;
        case 'Hospital':
            heatmap = new google.maps.visualization.HeatmapLayer({
                data: pointArray,
                radius:50,
                opacity:0.6,
                gradient:HospitalGradient
                });
                HospitalHeatmap = heatmap;
            break;
        case 'SeriousFailureLocation':
            heatmap = new google.maps.visualization.HeatmapLayer({
                data: pointArray,
                radius:50,
                opacity:0.6
                });
                SeriousFailureLocationHeatmap = heatmap;
            break;
        case 'difficulty_2':
            heatmap = new google.maps.visualization.HeatmapLayer({
                data: pointArray,
                radius:50,
                opacity:0.6
                });
                LevelDifficultyHeatmap_2 = heatmap;
            break;
        case 'difficulty_1':
            heatmap = new google.maps.visualization.HeatmapLayer({
                data: pointArray,
                radius:50,
                opacity:0.6
                });
                LevelDifficultyHeatmap_1 = heatmap;
            break;
        case 'IllegalConstruction':
            heatmap = new google.maps.visualization.HeatmapLayer({
                data: pointArray,
                radius:50,
                opacity:0.6
                });
                IllegalConstructionHeatmap = heatmap;
            break;
        
    }

     
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


  var resultControlDiv = document.createElement('div');
  var resultControl = new ResultControl(resultControlDiv, map);
  var layerControlDiv = document.createElement('div');
  var layerControl = new LayerControl(layerControlDiv, map);

  resultControlDiv.index = 1;
  layerControlDiv.index = 1;
  
  map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(resultControlDiv);
  map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(layerControlDiv);
  
  

}


function setMessageBlock(marker, text) {

   var infowindow = new google.maps.InfoWindow({
        content: text
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

    var polygon;

    switch(currentData){
        case 'Village':
            polygon = new g.Polygon({
                path:paths,
                map:map,
                strokeColor: '#00BBFF',
                strokeOpacity: 0.8,
                strokeWeight: 2, 
                fillColor:color, 
                fillOpacity: 0.5});

            VillagePolygons.push(polygon);
            break;

        case 'narrow_red':
            polygon = new g.Polygon({
                path:paths,
                map:map,
                strokeColor: color,
                strokeOpacity: 0.8,
                strokeWeight: 2, 
                fillColor:color, 
                fillOpacity: 0.5});

            NarrowRoadwayPolygons_RED.push(polygon);
            break;

        case 'narrow_yellow':
             polygon = new g.Polygon({
                path:paths,
                map:map,
                strokeColor: color,
                strokeOpacity: 0.8,
                strokeWeight: 2, 
                fillColor:color, 
                fillOpacity: 0.5});
            
            NarrowRoadwayPolygons_YELLOW.push(polygon);
            break;

        case 'narrow_blue':
             polygon = new g.Polygon({
                path:paths,
                map:map,
                strokeColor: color,
                strokeOpacity: 0.8,
                strokeWeight: 2, 
                fillColor:color, 
                fillOpacity: 0.5});

            NarrowRoadwayPolygons_BLUE.push(polygon);
            break;

        case 'Result':
            polygon = new g.Polygon({
                path: paths,
                map: map,
                strokeColor: '#666666',
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: color,
                fillOpacity: 0.6
            });

            ResultPolygons.push(polygon);
            break;
    }

    //polygons.push(polygon);
}


function initMarker(lat, lng, img, text,r,color) {


   var coordinate = new g.LatLng(lat, lng);
   var marker = new g.Marker({ map: map, position: coordinate, icon: img, zIndex: 80 - 1, title: text });
        
   var circle = new g.Circle({ map: map, radius: r, fillColor: color, strokeWeight: 1, strokeColor: bc });
   circle.bindTo('center', marker, 'position');

   setMessageBlock(marker, text);

   
   switch(currentData){
       case 'FireDepartment':
           FireDepartmentMarkers.push(marker);
           FireDepartmentCircles.push(circle);
           break;
       case 'Hospital':
           HospitalMarkers.push(marker);
           HospitalCircles.push(circle);
           break;
       case 'SeriousFailureLocation':
           SeriousFailureLocationMarkers.push(marker);
           SeriousFailureLocationCircles.push(circle);
           break;
       case 'IllegalConstruction':
           IllegalConstructionMarkers.push(marker);
           IllegalConstructionCircles.push(circle);
           break;
        case 'difficulty_2':
          LevelDifficultyMarkers_2.push(marker);
          LevelDifficultyCircles_2.push(circle);
          break;
        case 'difficulty_1':
          LevelDifficultyMarkers_1.push(marker);
          LevelDifficultyCircles_1.push(circle);
          break;
   }
   
   //markers.push(marker);
   //circles.push(circle);
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

    geocoder.geocode({ 'address': address }, function (results, status) {
        if (status == google.maps.GeocoderStatus.OK) {

            var location = results[0].geometry.location;
            map.setCenter(location);

            var marker = new google.maps.Marker({
                map: map,
                position: location,
                icon: angrybird_icon
            });


            $.get("searchDB.php", { lat: location.lat(), lng: location.lng() }, function (json) {
                var obj = JSON.parse(json);
                var i = 0;        
                var marker = [];

                while (i < obj.length) {

                    marker[i] = new google.maps.Marker({ map: map
                        , position: new google.maps.LatLng(obj[i].lat, obj[i].lng)
                        /*icon: hospital_icon*/
                        , zIndex: 80 - 1
                        , title: obj[i].text
                    });

                    i++;
                }

            });

        } else {
            alert("Geocode was not successful for the following reason: " + status);
        }
    });

}



function search_hydrant(){

    var address = document.getElementById("search_hydrant_text").value;

    geocoder.geocode({ 'address': address }, function (results, status) {
        if (status == google.maps.GeocoderStatus.OK) {

            var location = results[0].geometry.location;
            var formatted_address = results[0].formatted_address;
            map.setCenter(location);

            if (!document.getElementById("store_hydrant" + formatted_address)) {

                $("#store_bar").append("<button type='button' class='btn btn-success' id='store_hydrant" + formatted_address + "'"
                   + " onclick='remove_Hydrant(\"" + formatted_address + "\")'>搜尋" + address + "附近消防栓</button>");

                $.get("searchDB.php", { data: "hydrant", lat: location.lat(), lng: location.lng() }, function (json) {
                    var obj = JSON.parse(json);
                    var i = 0;
                    var marker = [];

                    HydrantMarkers_HUMAN[formatted_address] = new google.maps.Marker({
                        map: map,
                        position: location,
                        icon: 'img/Edit-Male-User.png'
                    });

                    while (i < obj.length) {

                        marker[i] = new google.maps.Marker({ map: map
                        , position: new google.maps.LatLng(obj[i].lat, obj[i].lng)
                        , icon: hydrant_icon
                        , zIndex: 80 - 1
                        , title: obj[i].text
                        });

                        i++;
                    }

                    HydrantMarkers[formatted_address] = marker;


                });
            }

        } else {
            alert("Geocode was not successful for the following reason: " + status);
        }
    });

}

function clearPolygons(){
    setAllMap(polygons,null);
}

function deletePolygons(data){

    switch(data){
        case 'Village':
            setAllMap(VillagePolygons,null);
            VillagePolygons = [];
            break;
        case 'narrow_red':
            setAllMap(NarrowRoadwayPolygons_RED,null);
            NarrowRoadwayPolygons_RED = [];
            break;
        case 'narrow_yellow':
            setAllMap(NarrowRoadwayPolygons_YELLOW,null);
            NarrowRoadwayPolygons_YELLOW = [];
            break;
        case 'narrow_blue':
            setAllMap(NarrowRoadwayPolygons_BLUE,null);
            NarrowRoadwayPolygons_BLUE = [];
            break;
        case 'Result':
            setAllMap(ResultPolygons,null);
            ResultPolygons = [];
            break;
    }

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
function deleteMarkers(data,hydrant_index) {

    switch(data){
        case 'FireDepartment':
            setAllMap(FireDepartmentMarkers,null);
            FireDepartmentMarkers = [];
            break;
        case 'Hospital':
            setAllMap(HospitalMarkers, null);
            HospitalMarkers = [];
            break;
        case 'SeriousFailureLocation':
            setAllMap(SeriousFailureLocationMarkers, null);
            SeriousFailureLocationMarkers = [];
            break;
        case 'IllegalConstruction':
            setAllMap(IllegalConstructionMarkers, null);
            IllegalConstructionMarkers = [];
            break;
        case 'difficulty_2':
            setAllMap(LevelDifficultyMarkers_2, null);
            LevelDifficultyMarkers_2 = [];
            break;
        case 'difficulty_1':
            setAllMap(LevelDifficultyMarkers_1, null);
            LevelDifficultyMarkers_1 = [];
            break;
         case 'hydrant':
            setAllMap(HydrantMarkers[hydrant_index], null);
            HydrantMarkers_HUMAN[hydrant_index].setMap(null);
            HydrantMarkers[hydrant_index] = [];
            HydrantMarkers_HUMAN[hydrant_index] = [];
            break;
   }
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
function deleteCircles(data){

    switch(data){
        case 'FireDepartment':
            setAllMap(FireDepartmentCircles,null);
            FireDepartmentCircles = [];
            break;
         case 'Hospital':
            setAllMap(HospitalCircles,null);
            HospitalCircles = [];
            break;
         case 'SeriousFailureLocation':
            setAllMap(SeriousFailureLocationCircles,null);
            SeriousFailureLocationCircles = [];
            break;
         case 'IllegalConstruction':
            setAllMap(IllegalConstructionCircles,null);
            IllegalConstructionCircles = [];
            break;
         case 'difficulty_2':
            setAllMap(LevelDifficultyCircles_2, null);
            LevelDifficultyCircles_2 = [];
            break;
         case 'difficulty_1':
            setAllMap(LevelDifficultyCircles_1, null);
            LevelDifficultyCircles_1 = [];
            break;
   }
}


function deleteHeatmap(data){

    switch(data){
        case 'FireDepartment':
            FireDepartmentHeatmap.setMap(null);
            FireDepartmentHeatmap = [];
            break;
        case 'Hospital':
            HospitalHeatmap.setMap(null);
            HospitalHeatmap = [];
            break;
         case 'SeriousFailureLocation':
            SeriousFailureLocationHeatmap.setMap(null);
            SeriousFailureLocationHeatmap = [];
            break;
         case 'IllegalConstruction':
            IllegalConstructionHeatmap.setMap(null);
            IllegalConstructionHeatmap = [];
            break;
         case 'difficulty_2':
            LevelDifficultyHeatmap_2.setMap(null);
            LevelDifficultyHeatmap_2 = [];
            break;
         case 'difficulty_1':
            LevelDifficultyHeatmap_1.setMap(null);
            LevelDifficultyHeatmap_1 = [];
            break;
   }
}



function back(){
    
     if (wo == true) {
                w.open(map);
                if (wc == true) {
                    var o = setTimeout("w.close()", wt);
                }
            }
}

function load_table(table_name,json){

   $("#table_name").text(table_name);
   $("#test").children().remove();
   var i=0;
  
   while(i<json.length){
       
       $("#test").append("<tr><th scope='row'>" + i + "</th><td>" + json[i].name + "</td><td>" + json[i].lat + "</td><td>" + json[i].lng + "</td></tr>");
       i++;
   }
}


function LayerControl(controlDiv, map) {

  // Set CSS for the control border
  var controlUI = document.createElement('div');
  controlUI.id ='layercontrol';
  controlUI.style.backgroundColor = '#fff';
  controlUI.style.border = '5px solid #FF0000';
  controlUI.style.borderRadius = '3px';
  controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
  controlUI.style.cursor = 'pointer';
  controlUI.style.marginBottom = '22px';
  controlUI.style.marginLeft = '10px';
  controlUI.style.textAlign = 'center';
  controlUI.title = '點選以顯示圖層';
  controlDiv.appendChild(controlUI);

  // Set CSS for the control interior
  var controlText = document.createElement('div');
  controlText.style.color = 'rgb(25,25,25)';
  controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
  controlText.style.fontSize = '16px';
  controlText.style.lineHeight = '38px';
  controlText.style.paddingLeft = '5px';
  controlText.style.paddingRight = '5px';
  controlText.innerHTML = "<img src='img/layers-square-32.png' alt='#'/>";
  controlUI.appendChild(controlText);

  google.maps.event.addDomListener(controlUI, 'click', function () {
      load_layer();
  });

}



function ResultControl(controlDiv, map) {

  // Set CSS for the control border
  var controlUI = document.createElement('div');
  controlUI.id ='resultcontrol';
  controlUI.style.backgroundColor = '#fff';
  controlUI.style.border = '2px solid #fff';
  controlUI.style.borderRadius = '3px';
  controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
  controlUI.style.cursor = 'pointer';
  controlUI.style.marginBottom = '22px';
  controlUI.style.textAlign = 'center';
  controlUI.title = '點選以顯示 火災應變能力評估';
  controlDiv.appendChild(controlUI);

  // Set CSS for the control interior
  var controlText = document.createElement('div');
  controlText.style.color = 'rgb(25,25,25)';
  controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
  controlText.style.fontSize = '16px';
  controlText.style.lineHeight = '38px';
  controlText.style.paddingLeft = '5px';
  controlText.style.paddingRight = '5px';
  controlText.innerHTML = "<img src='img/analytics-32.png' alt='#'/>";
  controlUI.appendChild(controlText);

  google.maps.event.addDomListener(controlUI, 'click', function () {
      if (!document.getElementById("store_result")) 
        load_result();
      else 
        remove_Result();
  });

}









