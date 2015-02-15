<?php
    
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
    
    .btn-group{
        float: right;
        margin-right: 23%;
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
       
            <ul class="nav nav-tabs " role="tablist" id="myTab">
                <li role="presentation"><a id="firedepartment" href="#" role="tab" data-toggle="tab">臺北市政府消防局各分隊</a></li>
                <li role="presentation"><a id="narrowroadway"href="" role="tab" data-toggle="tab">臺北市搶救不易狹小巷道清冊</a></li>
                <li role="presentation"><a id="difficulty" href="#" role="tab" data-toggle="tab">一、二級火災搶救困難地區</a></li>
                <li role="presentation"><a id="illegal" href="#" role="tab" data-toggle="tab">屋頂違建隔出3個使用單元以上</a></li>
                <li role="presentation"><a id="serious" href="#" role="tab" data-toggle="tab">重大不合格場所</a></li>
                <li role="presentation"><a id="firecount" href="#" role="tab" data-toggle="tab">火災發生次數</a></li>
                <li role="presentation"><a id="village" href="#" role="tab" data-toggle="tab">臺北市里界</a></li>
                <li role="presentation"><a id="hydrant" href="#" role="tab" data-toggle="tab">臺北地區消防栓分布點位</a></li>
                <li role="presentation" class="active"><a id="hosiptal" href="#" role="tab" data-toggle="tab">臺北市急救責任醫院</a></li>
            </ul>

            <script>
                $(function () {
                    $('#myTab a:last').tab('show')
                })

                function initialize() {
                    $(".box-header h1").append("<h1>臺北市急救責任醫院<small>src:臺北市政府衛生局</small></h1>");
                    $.get("api_selectDB.php", { data: "hosiptal" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    
                  
                }

                $("#hosiptal").click(function () {
                    var data = "hosiptal";

                    //location.href = "api_page.php?data=hosiptal";
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>臺北市急救責任醫院<small>src:臺北市政府衛生局</small></h1>");
                    $.get("api_selectDB.php", { data: "hosiptal" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    $("#download_json").attr("href", "https://firemap.blob.core.windows.net/firemapkml/hospital.json");

                });

                $("#firedepartment").click(function () {
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>臺北市政府消防局各分隊座標位置<small>src:臺北市政府消防局</small></h1>");
                    $.get("api_selectDB.php", { data: "firedepartment" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    $("#download_json").attr("href", "https://firemap.blob.core.windows.net/firemapkml/FireStation.json");


                });

                $("#narrowroadway").click(function () {
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>臺北市搶救不易狹小巷道清冊<small>src:臺北市政府消防局</small></h1>");
                    $.get("api_selectDB.php", { data: "narrowroadway" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    $("#download_json").attr("href", "https://firemap.blob.core.windows.net/firemapkml/NarrowStreet.json");

                });

                $("#difficulty").click(function () {
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>一、二級火災搶救困難地區<small>src:臺北市政府消防局</small></h1>");
                    $.get("api_selectDB.php", { data: "difficulty" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    $("#download_json").attr("href", "https://firemap.blob.core.windows.net/firemapkml/RescueDifficult.json");

                });

                $("#illegal").click(function () {
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>「屋頂違建隔出3個使用單元以上」清冊<small>src:</small></h1>");
                    $.get("api_selectDB.php", { data: "illegal" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    $("#download_json").attr("href", "https://firemap.blob.core.windows.net/firemapkml/IllegalConstruction.json");

                });

                $("#serious").click(function () {
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>重大不合格場所<small>src:</small></h1>");
                    $.get("api_selectDB.php", { data: "serious" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    $("#download_json").attr("href", "https://firemap.blob.core.windows.net/firemapkml/SubstandardPlace.json");

                });

                $("#firecount").click(function () {
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>火災發生次數<small>src:臺北市政府消防局</small></h1>");
                    $.get("api_selectDB.php", { data: "firecount" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    $("#download_json").attr("href", "");
                });


                $("#village").click(function () {
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>臺北市里界圖<small>src:圖資中心共通平台</small></h1>");
                    $.get("api_selectDB.php", { data: "village" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    $("#download_json").attr("href", "");
                    $("#download_kml").attr("href", "http://ogc.tpgos.taipei.gov.tw/tp98_6_demo/kml/%E8%87%BA%E5%8C%97%E5%B8%82%E9%87%8C%E7%95%8C%E5%9C%96.kml");
                });


                $("#hydrant").click(function () {
                    $(".box-header h1").contents().remove();
                    $(".box-header h1").append("<h1>臺北地區消防栓分布點位<small>src:圖資中心共通平台</small></h1>");
                    /*
                    $.get("api_selectDB.php", { data: "" }, function (msg) {
                        $(".box-content pre").text(msg);
                    });
                    */
                    $(".box-content pre").text("資料龐大 請改用下載．．．");
                    $("#download_json").attr("href", "");
                    $("#download_kml").attr("href", "http://ogc.tpgos.taipei.gov.tw/tp98_6_demo/kml/%E5%A4%A7%E8%87%BA%E5%8C%97%E5%9C%B0%E5%8D%80%E6%B6%88%E9%98%B2%E6%A0%93%E5%88%86%E5%B8%83%E9%BB%9E%E4%BD%8D%E5%9C%96.kml");
                });


                initialize();

           </script>


            <div class="box-header">
                
            <div class="btn-group">
                <button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    下載 <span class="caret"></span>
                </button>

                <ul class="dropdown-menu" role="menu">
                    <li><a href="https://firemap.blob.core.windows.net/firemapkml/hospital.json" id="download_json">JSON檔</a></li>
                    <li><a href="#" id="download_kml">KML檔</a></li>
                </ul>
            </div>

                <h1>臺北市急救責任醫院<small>src:臺北市政府衛生局</small></h1>
            </div>


            <hr class="line">

            <div class="box-content">
            <pre></pre>
            </div>
        
        </div>
    </body>
</html>

<?php 
    $rawdata = NULL;
?>
