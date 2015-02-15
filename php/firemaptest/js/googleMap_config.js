
var state='layer';//layer or heatmap
var currentData; //point to current data

var lat = [];
var lng = [];
var text = [];


var geocoder = new google.maps.Geocoder();
var g = google.maps;

var map, heatmap = new google.maps.visualization.HeatmapLayer();
var heatmapData = [];

var center = new g.LatLng(25.042424, 121.532985); //地圖中心座標
var z = 14; //地圖縮放比例（數值越大，細節越清楚）

var l = 1000; //半徑，單位為公尺
var bc = '#000000'; //圓周顏色
var fc = '#0000FF'; //圓內顏色
var ic = 'http://google-maps-icons.googlecode.com/files/radiation.png'; //標誌圖案；若刪除，則恢復預設標誌


var angrybird_icon = 'http://addon.discuz.com/resource/plugin/yuzhu_ezhichina.png?7BL8b';
var hospital_icon = 'img/hospital-building.png';
var hydrant_icon = 'img/fire-hydrant-2.png';


var wo = true; //是否顯示地圖說明視窗。true＝是；false＝否
var wc = true; //是否自動關閉地圖說明視窗。true＝是；false＝否
var wt = 5000; //自動關閉地圖說明視窗預設時間，計時單位為千分之一秒

var markers = [];//所有標記
var circles = [];//所有圓圈
var polygons = [];//所有polygon





var HydrantMarkers = [];
var HydrantMarkers_HUMAN = [];



var LevelDifficultyJson_1;
var LevelDifficultyJson_2;
var LevelDifficultyHeatmap_1;
var LevelDifficultyHeatmap_2;
var LevelDifficultyColor_1 = '#FF8800';
var LevelDifficultyColor_2 = '#FF0000';
var LevelDifficultyRadius_1 = 50;
var LevelDifficultyRadius_2 = 70;
var LevelDifficultyMarkers_2 = [];
var LevelDifficultyCircles_2 = [];
var LevelDifficultyMarkers_1 = [];
var LevelDifficultyCircles_1 = [];

var ResultPolygons  = [];
var VillagePolygons = [];


var NarrowRoadwayPolygons_RED = [];
var NarrowRoadwayPolygons_YELLOW = [];
var NarrowRoadwayPolygons_BLUE = [];
var NarrowRoadwayColor ;
var NarrowRoadwayRadius ;


var FireDepartmentJson;
var FireDepartmentHeatmap ;
var FireDepartmentMarkers = [];
var FireDepartmentCircles = [];
var FireDepartmentColor = '#0000FF';
var FireDepartmentRadius = 1000;
var FireDepartmentGradient = ['rgba(0, 255, 255, 0)','rgba(0, 255, 255, 1)','rgba(0, 191, 255, 1)','rgba(0, 127, 255, 1)',
                              'rgba(0, 63, 255, 1)','rgba(0, 0, 255, 1)','rgba(0, 0, 223, 1)','rgba(0, 0, 191, 1)'];

var HospitalJson;
var HospitalHeatmap ;
var HospitalMarkers = [];
var HospitalCircles = [];           
var HospitalColor ; 
var HospitalRadius ;
var HospitalGradient = ['rgba(0, 255, 255, 0)','rgba(0, 255, 255, 1)','rgba(0, 191, 255, 1)','rgba(0, 127, 255, 1)',
                              'rgba(0, 63, 255, 1)','rgba(0, 0, 255, 1)'];


var SeriousFailureLocationJson;
var SeriousFailureLocationHeatmap;
var SeriousFailureLocationMarkers = [];
var SeriousFailureLocationCircles = [];  
var SeriousFailureLocationColor = '#FF0000';
var SeriousFailureLocationRadius = 50;
var SeriousFailureLocationGradient;



var IllegalConstructionJson;
var IllegalConstructionHeatmap;
var IllegalConstructionMarkers = [];
var IllegalConstructionCircles = []; 
var IllegalConstructionColor = '#FF8800';
var IllegalConstructionRadius ;


var ParkWaterStationColor = '#00FFCC';
var ParkWaterStationRadius = 1000;