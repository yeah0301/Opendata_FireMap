# 守護台灣OpenData FireMap

##Demo
> Website: http://firemaptest.azurewebsites.net/index.php?#<br>

####系統預覽圖

<img src='https://ed0f44ae43a4d55f8a982614f767043d121b66e6.googledrive.com/host/0B_zzOKvrcEq_VHR4ZUQ3SEJEWWc/preview.PNG'  alt='#' width:'674' height='347'/>

####API介面
<img src='https://ed0f44ae43a4d55f8a982614f767043d121b66e6.googledrive.com/host/0B_zzOKvrcEq_VHR4ZUQ3SEJEWWc/apipreview.PNG'  alt='#' width:'674' height='347'/>

####APP #建議使用QR Droid掃描
<img src='https://ed0f44ae43a4d55f8a982614f767043d121b66e6.googledrive.com/host/0B_zzOKvrcEq_VHR4ZUQ3SEJEWWc/barcode_firemap.png' alt='#' width:'135' height='135'/>

============================================================================
##Synopsis
####風險揭露

火災的發生難以預防，其所造成的災害也常常令人扼腕。但我們卻可以透過更有效率的消防救援將傷害降至最低 ; 然而火災救援卻常因為道路狹小、夜晚路燈不足、消防栓位置過遠…等因素，導致救援上時間的延長及不能有效的把握 黃金救火時間，造成諸多憾事發生。更甚者，因違建等因素波及他人，使得更 大的生命、財務損失。因此提供火災應變指數評估資訊並整合出相關資訊是勢在必行。

####資訊傳遞

整合政府OpenData與地理資訊系統(GIS)繪製出危險火災地區(例如：違建），或 消防栓、路燈點位等資訊，讓使用者可以一目了然得知台北市火災應變能力的 資訊提供可能影響火災救援應變的因素。

####資料分析與地理資訊系統服務

有效的利用政府OpenData以圖層、熱力圖的方式呈現，並且分析與整合 OpenData數據提供里別的火災風險與應變指數，最後將這些資訊匯出 提供 API服務，其包含JSON、KML圖層檔，以提供相關應用的介接。 此作品主要提供可能影響火災救援應變的因素，並整合計算出該區的防火危險 指數給使用者查看，讓民眾也能了解相關防火資訊並監督政府作業，生活能更 加的安全。


####系統流程圖

<img src='https://ed0f44ae43a4d55f8a982614f767043d121b66e6.googledrive.com/host/0B_zzOKvrcEq_VHR4ZUQ3SEJEWWc/flow.jpg' alt='#' width:'469.5' height='350'/>


####Prototype連結
  https://drive.google.com/file/d/0B_zzOKvrcEq_RHM4Znlwd2tpQzg/view?usp=sharing

=============================================================================
##API Reference

使用者可藉由介接、下載JSON檔及KML三種方式取得OpenData

本系統提供資料如下：

  - 臺北市急救責任醫院
  - 臺北市搶救不易狹小巷道清冊
  - 一、二級火災搶救困難地區
  - 「屋頂違建隔出3個使用單元以上」清冊
  - 重大不合格場所
  - 火災發生次數
  - 臺北市里界圖
  - 臺北地區消防栓分布點位
  - 臺北市政府消防局各分隊座標位置


連結：http://firemaptest.azurewebsites.net/api_page.php

####METHOD: GET

  - 火災應變能力評估
  URL:
~~~~~~~
{
	"name":里名(string),
	"section":此里所在行政區(string),
	"score":經由多項因素整合的 火災應變能力評估 分數
		正號表示分數高於平均，負號反之
		分數越高表示該里 火災應變能力 越好(double)
}
~~~~~~~
  - 臺北市急救責任醫院
  URL:	http://firemaptest.azurewebsites.net/api_selectDB.php?data=hosiptal
~~~~~~~
{
	"lat":WGS84緯度(decimal),
	"lng":WGS84經度(decimal),
	"name"：醫院名稱(string),
	"address":醫院所在地址(string),
	"telephone":醫院電話(string),
	"category":醫院等級分類，有區域醫院、地區醫院、醫學中心(string)
}
~~~~~~~
  - 臺北市政府消防局各分隊座標位置
 URL:
~~~~~~~
{
	"lat":WGS84緯度(decimal),
	"lng":WGS84經度(decimal),
	"name"：消防隊分隊名稱(string),
	"address":消防隊所在地址(string)
}
~~~~~~~
  - 臺北市搶救不易狹小巷道清冊
  URL:
~~~~~~~
{
	"lat":WGS84緯度(decimal),
	"lng":WGS84經度(decimal),
	"roadway":搶救不易狹小巷道名稱(string),
	"section":此巷道所在行政區(string)
	"width":此巷道寬度(double),
	"team":隸屬那個消防分隊管轄(string)
}
~~~~~~~
  - 一、二級火災搶救困難地區
URL:
~~~~~~~
{
	"lat":WGS84緯度(decimal),
	"lng":WGS84經度(decimal),
	"section":搶救困難地區所在行政區(string),
	"address"::搶救困難地區所在地址(string),
	"level":火災搶救困難等級(int),
	"item":火災搶救困難評定項目(int),
	"name":搶救困難地區名稱(string),
	"hasAisle":是否有消防通道(bit)
}
~~~~~~~
  - 「屋頂違建隔出3個使用單元以上」清冊
  URL:
~~~~~~~
{
	"lat":WGS84緯度(decimal),
	"lng":WGS84經度(decimal),
	"section":屋頂違建所在行政區(string),
	"address"::屋頂違建所在地址(string),
	"area":屋頂違建面積(float),
	"date":標示為違建日期(date)
}
~~~~~~~
  - 重大不合格場所
URL:
~~~~~~~
{
	"lat":WGS84緯度(decimal),
	"lng":WGS84經度(decimal),
	"name":重大不合格場所名稱(string),
	"address":重大不合格場所地址(string),
	"checkResult":檢查結果(string),
	"date":標示為重大不合格場所日期(date)
}
~~~~~~~
  - 火災發生次數
URL:
~~~~~~~
{
	"section":火災發生地區(string),
	"count":該月份火災發生次數(int),
	"date":月份(date)
}
~~~~~~~
  - 臺北市里界圖
URL:
~~~~~~~
{
	"name":里名(string),
	"section":此里所在行政區(string),
	"area":此里面積(double),
	"polygon":WGS84緯度 WGS84經度 空白分格連續經緯度包圍此里(string)
}
~~~~~~~

=============================================================================
##License





