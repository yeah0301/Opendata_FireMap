

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Fire map</title>


    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-theme.min.css" rel="stylesheet">
    
  
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?signed_in=true&key=AIzaSyA5Y5YzTgPbmdsw0Rbwe_k7Wn-TGOnDW_s&sensor=false&language=zh-TW&libraries=visualization"></script>
    
    <script type="text/javascript" src="js/googleMap_config.js"></script>
    <script type="text/javascript" src="js/googleMap_object.js"></script>
    <script type="text/javascript" src="js/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    
    
    <script type="text/javascript">

        function remove_FireDepartment() {
            if (state == 'layer') {
                $("#store_firedepartment").remove();
                deleteMarkers('FireDepartment');
                deleteCircles('FireDepartment');
                FireDepartmentJson = null;
            }

            else if (state == 'heatmap') {
                $("#store_firedepartment").remove();
                deleteHeatmap('FireDepartment');
                FireDepartmentJson = null;
            }

        }

        function remove_Hospital() {
            if (state == 'layer') {
                $("#store_hospital").remove();
                deleteMarkers('Hospital');
                deleteCircles('Hospital');
                HospitalJson = null;
            }

            else if (state == 'heatmap') {
                $("#store_hospital").remove();
                deleteHeatmap('Hospital');
                HospitalJson = null;
            }
        }

        function remove_SeriousFailureLocation() {
            if (state == 'layer') {
                $("#store_seriousfailurelocation").remove();
                deleteMarkers('SeriousFailureLocation');
                deleteCircles('SeriousFailureLocation');
                SeriousFailureLocationJson = null;
            }

            else if (state == 'heatmap') {
                $("#store_seriousfailurelocation").remove();
                deleteHeatmap('SeriousFailureLocation');
                SeriousFailureLocationJson = null;
            }

        }

        function remove_IllegalConstruction() {
            if (state == 'layer') {
                $("#store_illegalconstruction").remove();
                deleteMarkers('IllegalConstruction');
                deleteCircles('IllegalConstruction');
                IllegalConstructionJson = null;
            }

            else if (state == 'heatmap') {
                $("#store_illegalconstruction").remove();
                deleteHeatmap('IllegalConstruction');
                IllegalConstructionJson = null;
            }

        }

        function remove_LevelDifficultyOfFireRescue(level) {

            switch (level) {
                case 2:
                    if (state == 'layer') {
                        $("#store_leveldifficulty2").remove();
                        deleteMarkers('difficulty_2');
                        deleteCircles('difficulty_2');
                        LevelDifficultyJson_2 = null;
                    }
                    else if (state == 'heatmap') {
                        $("#store_leveldifficulty2").remove();
                        deleteHeatmap('difficulty_2');
                        LevelDifficultyJson_2 = null;
                    }
                    break;
                case 1:
                    if (state == 'layer') {
                        $("#store_leveldifficulty1").remove();
                        deleteMarkers('difficulty_1');
                        deleteCircles('difficulty_1');
                        LevelDifficultyJson_1 = null;
                    }

                    else if (state == 'heatmap') {
                        $("#store_leveldifficulty1").remove();
                        deleteHeatmap('difficulty_1');
                        LevelDifficultyJson_1 = null;
                    }
                    break;
            }

        }

        function remove_NarrowRoadway(level) {

            switch (level) {
                case 'red':
                    $("#store_narrow_red").remove();
                    deletePolygons('narrow_red');
                    break;
                case 'yellow':
                    $("#store_narrow_yellow").remove();
                    deletePolygons('narrow_yellow');
                    break;
                case 'blue':
                    $("#store_narrow_blue").remove();
                    deletePolygons('narrow_blue');
                    break;

            }

        }

        function remove_Village() {
            $("#store_village").remove();
            deletePolygons("Village");
        }


        function remove_Hydrant(address) {
            $("#store_hydrant" + address).remove();
            deleteMarkers('hydrant', address);
        }


        function remove_Result() {
            $("#store_result").remove();
            deletePolygons("Result");

            $("#layer").attr('class', 'active');
            $("#heat").attr('class', '');
            $("#result").attr('class', '');
            $("#resultcontrol").css("border", "2px solid #fff");
        }

        function load_FireDepartment() {

            currentData = 'FireDepartment';
            var deferred = $.Deferred();
            var botton_color;

            if (!document.getElementById("store_firedepartment")) {

                $.get("index_selectDB.php", { data: "firedepartment" }, function (json) {
                    FireDepartmentJson = JSON.parse(json);
                    deferred.resolve("finish");
                });

                deferred.done(function (msg) {

                    load_table("臺北市政府消防局各分隊座標位置", FireDepartmentJson);

                    if (state == 'layer') {
                        initial_makers(FireDepartmentJson, 'FireDepartment');
                        botton_color = 'btn-success';
                    }

                    else if (state == 'heatmap') {
                        initial_heatmap(FireDepartmentJson, 'FireDepartment');
                        botton_color = 'btn-warning';
                    }

                    $("#store_bar").append("<button type='button' class='btn " + botton_color + "' id='store_firedepartment'"
                                             + " onclick='remove_FireDepartment()'>消防局</button>");
                });

            }
        }

        function load_Hospital() {

            currentData = 'Hospital';
            var deferred = $.Deferred();
            var botton_color;

            if (!document.getElementById("store_hospital")) {

                $.get("index_selectDB.php", { data: "hosiptal" }, function (json) {
                    HospitalJson = JSON.parse(json);
                    deferred.resolve("finish");
                });

                deferred.done(function (msg) {

                    load_table("臺北市急救責任醫院", HospitalJson);

                    if (state == 'layer') {
                        initial_makers(HospitalJson, 'Hospital');
                        botton_color = 'btn-success';
                    }

                    else if (state == 'heatmap') {
                        initial_heatmap(HospitalJson, 'Hospital');
                        botton_color = 'btn-warning';
                    }

                    $("#store_bar").append("<button type='button' class='btn " + botton_color + "' id='store_hospital'"
                                                + " onclick='remove_Hospital()'>臺北市急救責任醫院</button>");
                });
            }
        }

        function load_SeriousFailureLocation() {

            currentData = 'SeriousFailureLocation';
            var deferred = $.Deferred();
            var botton_color;

            if (!document.getElementById("store_seriousfailurelocation")) {

                $.get("index_selectDB.php", { data: "serious" }, function (json) {
                    SeriousFailureLocationJson = JSON.parse(json);
                    deferred.resolve("finish");
                });

                deferred.done(function (msg) {

                    load_table("重大檢查不合格地區", SeriousFailureLocationJson);

                    if (state == 'layer') {
                        initial_makers(SeriousFailureLocationJson, 'SeriousFailureLocation');
                        botton_color = 'btn-success';
                    }

                    else if (state == 'heatmap') {
                        initial_heatmap(SeriousFailureLocationJson, 'SeriousFailureLocation');
                        botton_color = 'btn-warning';
                    }

                    $("#store_bar").append("<button type='button' class='btn " + botton_color + "' id='store_seriousfailurelocation'"
                                                + " onclick='remove_SeriousFailureLocation()'>重大檢查不合格地區</button>");
                });

            }
        }


        function load_LevelDifficultyOfFireRescue(level) {

            var deferred = $.Deferred();
            var botton_color;
            var isExecute = false;

            if (level == 2 && !document.getElementById("store_leveldifficulty2")) {
                currentData = 'difficulty_2';
                isExecute = true;
            }
            else if (level == 1 && !document.getElementById("store_leveldifficulty1")) {
                currentData = 'difficulty_1';
                isExecute = true;
            }


            if (isExecute) {
                $.get("index_selectDB.php", { data: currentData }, function (json) {
                    if (level == 2)
                        LevelDifficultyJson_2 = JSON.parse(json);
                    else if (level == 1)
                        LevelDifficultyJson_1 = JSON.parse(json);

                    deferred.resolve("finish");
                });


                deferred.done(function (msg) {

                    if (level == 2) {
                        load_table("二級搶救困難地區", LevelDifficultyJson_2);
                        if (state == 'layer') {
                            initial_makers(LevelDifficultyJson_2, 'difficulty_2');
                            botton_color = 'btn-success';
                        }

                        else if (state == 'heatmap') {
                            initial_heatmap(LevelDifficultyJson_2, 'difficulty_2');
                            botton_color = 'btn-warning';
                        }

                    }

                    else if (level == 1) {
                        load_table("二級搶救困難地區", LevelDifficultyJson_1);
                        if (state == 'layer') {
                            initial_makers(LevelDifficultyJson_1, 'difficulty_1');
                            botton_color = 'btn-success';
                        }

                        else if (state == 'heatmap') {
                            initial_heatmap(LevelDifficultyJson_1, 'difficulty_1');
                            botton_color = 'btn-warning';
                        }


                    }

                    if (state == 'layer')
                        $("#store_bar").append("<button type='button' class='btn " + botton_color + "' id='store_leveldifficulty" + level + "'"
                                                + " onclick='remove_LevelDifficultyOfFireRescue(" + level + ")'>" + level + "級搶救困難地區</button>");

                    else if (state == 'heatmap')
                        $("#store_bar").append("<button type='button' class='btn " + botton_color + "' id='store_leveldifficulty" + level + "'"
                                                + " onclick='remove_LevelDifficultyOfFireRescue(" + level + ")'>" + level + "級搶救困難地區</button>");

                });
            }

        }

        function load_IllegalConstruction() {

            currentData = 'IllegalConstruction';
            var deferred = $.Deferred();
            var botton_color;

            if (!document.getElementById("store_illegalconstruction")) {

                $.get("index_selectDB.php", { data: "illegal" }, function (json) {
                    IllegalConstructionJson = JSON.parse(json);
                    deferred.resolve("finish");
                });

                deferred.done(function (msg) {

                    load_table("「屋頂違建隔出3個使用單元以上」清冊", IllegalConstructionJson);

                    if (state == 'layer') {
                        initial_makers(IllegalConstructionJson, 'IllegalConstruction');
                        botton_color = 'btn-success';
                    }

                    else if (state == 'heatmap') {
                        initial_heatmap(IllegalConstructionJson, 'IllegalConstruction');
                        botton_color = 'btn-warning';
                    }

                    $("#store_bar").append("<button type='button' class='btn " + botton_color + "' id='store_illegalconstruction'"
                                                + " onclick='remove_IllegalConstruction()'>違建</button>");
                });

            }
        }


        function load_Village() {

            currentData = 'Village';
            var deferred = $.Deferred();

            if (!document.getElementById("store_village")) {

                $('#loading').modal('toggle');
                $('#loading').show();

                $("#store_bar").append("<button type='button' class='btn btn-success' id='store_village'"
                                                + " onclick='remove_Village()'>里別</button>");

                $.get("index_selectDB.php", { data: "village" }, function (json) {
                    var obj = JSON.parse(json);
                    var i = 0;

                    while (i < obj.length) {
                        initialize_polygon(obj[i], '#FFFF33');
                        i++;
                    }
                    deferred.resolve("finish");
                });

                deferred.done(function (msg) {
                    $('#loading').modal('hide');
                });
            }
        }

        function load_NarrowRoadway(level) {

            var isExecute = false;

            if (level == 'red' && !document.getElementById("store_narrow_red")) {
                currentData = 'narrow_red';
                $("#store_bar").append("<button type='button' class='btn btn-success' id='store_narrow_red'"
                                                + " onclick='remove_NarrowRoadway(\"red\")'>狹小巷道 紅區</button>");
                isExecute = true;
            }
            else if (level == 'yellow' && !document.getElementById("store_narrow_yellow")) {
                currentData = 'narrow_yellow';
                $("#store_bar").append("<button type='button' class='btn btn-success' id='store_narrow_yellow'"
                                                + " onclick='remove_NarrowRoadway(\"yellow\")'>狹小巷道 黃區</button>");
                isExecute = true;
            }
            else if (level == 'blue' && !document.getElementById("store_narrow_blue")) {
                currentData = 'narrow_blue';
                $("#store_bar").append("<button type='button' class='btn btn-success' id='store_narrow_blue'"
                                        + " onclick='remove_NarrowRoadway(\"blue\")'>狹小巷道 藍區</button>");
                isExecute = true;
            }


            if (isExecute) {
                $.get("index_selectDB.php", { data: currentData }, function (json) {
                    var obj = JSON.parse(json);

                    load_table("臺北市搶救不易狹小巷道清冊", obj);

                    var i = 0;

                    while (i < obj.length) {
                        if (obj[i].level == "red")
                            NarrowRoadwayColor = '#FF0000';
                        else if (obj[i].level == "yellow")
                            NarrowRoadwayColor = '#FF8800';
                        else if (obj[i].level == "blue")
                            NarrowRoadwayColor = '#0000FF';

                        initialize_polygon(obj[i].polygon, NarrowRoadwayColor);
                        i++;

                    }
                });
            }
        }

        function load_result() {



            $("#layer").attr('class', '');
            $("#heat").attr('class', '');
            $("#result").attr('class', 'active');
            $("#resultcontrol").css("border", "5px solid #FF0000");

            var deferred = $.Deferred();
            currentData = 'Result';

            if (!document.getElementById("store_result")) {

                $("#store_bar").append("<button type='button' class='btn btn-success' id='store_result'"
                                        + " onclick='remove_Result()'>火災應變能力 評估</button>");

                $('#loading').modal('toggle');
                $('#loading').show();

                $.get("index_selectDB.php", { data: "result" }, function (json) {
                    var obj = JSON.parse(json);
                    var i = 0;

                    while (i < obj.length) {

                        if (obj[i].score < -0.6)
                            initialize_polygon(obj[i].polygon, '#FF0000');
                        else if (obj[i].score >= -0.6 && obj[i].score < -0.2)
                            initialize_polygon(obj[i].polygon, '#FF8800');
                        else if (obj[i].score >= -0.2 && obj[i].score < 0.2)
                            initialize_polygon(obj[i].polygon, '#FFBB00');
                        else if (obj[i].score >= 0.2 && obj[i].score < 0.6)
                            initialize_polygon(obj[i].polygon, '#FFFF00');
                        else if (obj[i].score >= 0.6)
                            initialize_polygon(obj[i].polygon, '#77FF00');

                        i++;
                    }
                    deferred.resolve("finish");
                });


                deferred.done(function (msg) {
                    $('#loading').modal('hide');
                });

            }
        }


        function alertEvent() {

            $.get("index_selectDB.php", { data: "alert" }, function (json) {
                var obj = JSON.parse(json);
                var i = obj.length - 1;
                if (obj != null) {

                    while (i < obj.length) {
                        var content = "<h4>火災現場</h4>"
                                    + "<img src='appservice/upload/"
                                    + obj[i].pics
                                    + "'width='50%' alt='#'/>"

                        var marker = new google.maps.Marker({ map: map
                                , position: new google.maps.LatLng(obj[i].lat, obj[i].lng)
                                , zIndex: 80 - 1
                                , title: "123"
                        });

                        var infowindow = new google.maps.InfoWindow({
                            content: content,
                            maxWidth: 200
                        });

                        google.maps.event.addListener(marker, 'click', function () {
                            infowindow.open(marker.get('map'), marker);
                        });
                        i++;
                    }

                }
            });

            setTimeout(alertEvent, 5000);
        }

        alertEvent();

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
      <a class="navbar-brand" href="#">FireMap</a>
    </div>

    <?php require_once("about.php");?>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
       
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">function<span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <!--
            <li><a href="#" onclick="load_heatmap()">Heat map</a></li>
            -->

            <li><a href="#" onclick="clearMarkers();">Hide All Makers</a></li>
            <li><a href="#" onclick="showMarkers();">Show All Markers</a></li>
            <li><a href="#" onclick="deleteMarkers(); deleteCircles();">Delete All Markers</a></li>
            <li><a href="#" onclick="showCircles();">Show All Circles</a></li>
            <li><a href="#" onclick="clearCircles();">Hide All Circles</a></li>

            <li class="divider"></li>
            <li><a href="#" data-toggle="modal" data-target=".bs-example-modal-sm">關於</a></li>
          </ul>
        </li>

        <li id="result">
          <a href="#" onclick="load_result();">
                <img src="img/analytics-32.png" alt="#"/>
                火災應變能力 評估
          </a>
        </li>

        <li id="layer" class="active">
          <a href="#" onclick="load_layer();">
                <img src="img/layers-square-32.png" alt="#"/>
                圖層
          </a>
        </li>

        <li id="heat">
            <a href="#" onclick="load_heatmap();">
                <img src="img/table_heatmap.png" alt="#"/>
                Heat Map
            </a>
        </li>

        <li><a href="api_page.php">
            <img src="img/api-o.png" alt="#"/>
            API
            </a>
        </li>

      </ul>
      <form class="navbar-form navbar-left" role="search">
        <div class="form-group">
          <input id="search_text" type="text" class="form-control" placeholder="Search">
        </div>
        <button type="submit" class="btn btn-default" onclick="search();">搜尋</button>
      </form>
      
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
 
    
    <div class="container">
    <div class="row">
        <div class="col-md-9 col-md-push-3" id="content">

            <div class="well well-sm" id="store_bar"></div>

            
            <?php require_once("loading.php");?>

            <div id="map-canvas" style="width: 840px; height: 480px; margin: 0; padding: 0;"></div>
           
            <?php require_once("table.php");?>


        </div>
       
        <div class="col-md-3 col-md-pull-9">
            <div class="list-group" > 
                <a href="#" class="list-group-item active" id="list_title">Layer</a>


                <a href="#" class="list-group-item" id="debug">
                    <img src="img/Fire_Hydrant-32.png" alt="#"/>
                    搜尋附近消防栓
                    <span class="glyphicon glyphicon-search" aria-hidden="true" style="display:block;float:right"></span>

                    <form class="navbar-form" role="search">
                        <div class="form-group">
                            <input id="search_hydrant_text" type="text" class="form-control" placeholder="Search" style="width:70%">
                            <button type="submit" class="btn btn-default" onclick="search_hydrant();">搜尋</button>
                        </div>
                    </form>
                </a>

                <a href="#" class="list-group-item" onclick="load_FireDepartment();">
                    <img src="img/Fire_station-32.png" alt="#"/>
                    消防局
                </a>

                <a href="#" class="list-group-item" onclick="load_Hospital();">
                    <img src="img/hospital-building.png" alt="#"/>
                    臺北市急救責任醫院
                </a>

                
                <a href="#" class="list-group-item" onclick="load_SeriousFailureLocation();">
                    <img src="img/wrong-01-32.png" alt="#"/>
                    重大不合格地區
                </a>

                <a href="#" class="list-group-item" onclick="load_IllegalConstruction();">
                    <img src="img/House.png" alt="#"/>
                    違建
                </a>

                <a href="#collapse0" class="list-group-item" data-toggle="collapse" >
                    <img src="img/Alerts-10-32.png" alt="#"/>
                    火災搶救困難地區
                    <span class="glyphicon glyphicon-chevron-down" aria-hidden="true" style="display:block;float:right"></span> 
                </a>

                <div class="collapse" id="collapse0">
                    <div class="list-group">

                        <a href="#" class="list-group-item" onclick="load_LevelDifficultyOfFireRescue(1);">
                            <img src="img/badge-first-32.png" alt="#"/>
                            一級搶救困難地區
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true" style="display:block;float:right"></span> 
                        </a>

                        <a href="#" class="list-group-item" onclick="load_LevelDifficultyOfFireRescue(2);">
                            <img src="img/campfire-32.png" alt="#"/>
                            二級搶救困難地區
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true" style="display:block;float:right"></span> 
                        </a>

                    </div>
                </div>

                <a href="#collapse1" class="list-group-item" data-toggle="collapse" >
                    <img src="img/Road_sign_travel_transportation.png" alt="#"/>
                    狹小巷道
                    <span class="glyphicon glyphicon-chevron-down" aria-hidden="true" style="display:block;float:right"></span> 
                </a>

                <div class="collapse" id="collapse1">
                    <div class="list-group">

                        <a href="#" class="list-group-item" onclick="load_NarrowRoadway('red');">
                            <img src="img/030-32.png" alt="#"/>
                            寬度2.5~4m
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true" style="display:block;float:right"></span> 
                        </a>

                        <a href="#" class="list-group-item" onclick="load_NarrowRoadway('yellow');">
                            <img src="img/alley-32.png" alt="#"/>
                            寬度4~5m
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true" style="display:block;float:right"></span> 
                        </a>

                        <a href="#" class="list-group-item" onclick="load_NarrowRoadway('blue');">
                            <img src="img/tower_skyscraper-32.png" alt="#"/>
                            6層↑ 長15m↓寬6m↓
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true" style="display:block;float:right"></span> 
                        </a>
                    
                    </div>
                </div>

                 <!--
                <a href="#" class="list-group-item" onclick="load_ParkWaterStation();">緊急維生取水站</a>
                 -->

                <a href="#collapse2" class="list-group-item" data-toggle="collapse">
                    <img src="img/659976-map-32.png" alt="#"/>
                    區里別
                    <span class="glyphicon glyphicon-chevron-down" aria-hidden="true" style="display:block;float:right"></span> 
                </a>

                 <div class="collapse" id="collapse2">
                    <div class="list-group">

                        <a href="#" class="list-group-item" onclick="load_section();">
                            <img src="img/layout_golden_section-32.png" alt="#"/>
                            行政區
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true" style="display:block;float:right"></span> 
                        </a>

                        <a href="#" class="list-group-item" onclick="load_Village();">
                            <img src="img/forest-32.png" alt="#"/>
                            里別
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true" style="display:block;float:right"></span> 
                        </a>


                    </div>
                 <div>
            </div>
        </div>
    </div>
    </div>
        </div>
         </div>


    <?php require_once("footer.php");?>


</body>

</html>​