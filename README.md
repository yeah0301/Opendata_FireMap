# 守護台灣OpenData FireMap

##Demo
> Website: http://firemaptest.azurewebsites.net/index.php?#<br>

####系統預覽圖

<img src='https://ed0f44ae43a4d55f8a982614f767043d121b66e6.googledrive.com/host/0B_zzOKvrcEq_VHR4ZUQ3SEJEWWc/preview.PNG'  alt='#' width:'674' height='347'/>

####API介面
<img src='https://ed0f44ae43a4d55f8a982614f767043d121b66e6.googledrive.com/host/0B_zzOKvrcEq_VHR4ZUQ3SEJEWWc/apipreview.PNG'  alt='#' width:'674' height='347'/>

####APP #建議使用QR Droid掃描
<img src='https://ed0f44ae43a4d55f8a982614f767043d121b66e6.googledrive.com/host/0B_zzOKvrcEq_VHR4ZUQ3SEJEWWc/qrcode_new.png' alt='#' width:'135' height='135'/>

============================================================================
##Synopsis
####風險揭露

火災的發生難以預防，其所造成的災害也常常令人扼腕。但我們卻可以透過更 有效率的消防救援將傷害降至最低 ; 然而火災救援卻常因為道路狹小、夜晚路 燈不足、消防栓位置過遠…等因素，導致救援上時間的延長及不能有效的把握 黃金救火時間，造成諸多憾事發生。更甚者，因違建等因素波及他人，使得更 大的生命、財務損失。因此提供火災應變指數評估資訊並整合出相關資訊是勢 在必行。

####資訊傳遞

整合政府OpenData與地理資訊系統(GIS)繪製出危險火災地區(例如：違建），或 消防栓、路燈點位等資訊，讓使用者可以一目了然得知台北市火災應變能力的 資訊提供可能影響火災救援應變的因素。

####資料分析與地理資訊系統服務

有效的利用政府OpenData以圖層、熱力圖的方式呈現，並且分析與整合 OpenData數據提供行政區與里別的火災風險與應變指數，最後將這些資訊匯出 提供 API服務，其包含JSON、KML圖層檔，以提供相關應用的介接。 此作品主要提供可能影響火災救援應變的因素，並整合計算出該區的防火危險 指數給使用者查看，讓民眾也能了解相關防火資訊並監督政府作業，生活能更 加的安全。


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

=============================================================================
##License





