
var state='layer';//layer or heatmap
var currentData; //point to current data

var lat = [];
var lng = [];
var text = [];

var geocoder = new google.maps.Geocoder();
var g = google.maps;

var map, heatmap;
var heatmapData = [];

var center = new g.LatLng(25.042424, 121.532985); //地圖中心座標
var z = 14; //地圖縮放比例（數值越大，細節越清楚）

var l = 1000; //半徑，單位為公尺
var bc = '#000000'; //圓周顏色
var fc = '#0000FF'; //圓內顏色
var ic = 'http://google-maps-icons.googlecode.com/files/radiation.png'; //標誌圖案；若刪除，則恢復預設標誌


var hospital_icon = 'img/hospital-building.png';
var angrybird_icon = 'http://addon.discuz.com/resource/plugin/yuzhu_ezhichina.png?7BL8b';


var wo = true; //是否顯示地圖說明視窗。true＝是；false＝否
var wc = true; //是否自動關閉地圖說明視窗。true＝是；false＝否
var wt = 5000; //自動關閉地圖說明視窗預設時間，計時單位為千分之一秒

var markers = [];//所有標記
var circles = [];//所有圓圈
var polygons = [];//所有polygon


var NarrowRoadwayColor = '#FF0000';
var NarrowRoadwayRadius = [];

var FireDepartmentColor = '#0000FF';
var FireDepartmentRadius = 1000;

var HospitalColor = []; 
var HospitalRadius = [];

var SeriousFailureLocationColor = '#FF0000';
var SeriousFailureLocationRadius = 50;

var IllegalConstructionColor = '#FF8800';
var IllegalConstructionRadius = [];

var ParkWaterStationColor = '#00FFCC';
var ParkWaterStationRadius = 1000;