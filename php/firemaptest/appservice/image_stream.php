<?php
// error_reporting(E_ERROR | E_WARNING | E_PARSE);
error_reporting(E_ALL);

require_once '../include/DBClass.php';

$db = new DBClass();

$target_path  = "./upload/";
$base=$_REQUEST['image'];

$lon = $_POST['x'];
$lat = $_POST['y'];
$worker = $_POST['worker'];
$eventid = $_POST['eventid'];
$desc = $_POST['desc'];

list($usec, $sec) = explode(" ", microtime());
$rnum = $sec.substr($usec,2);
$filenam = $eventid."_".$rnum.".jpg";
$fp = fopen("tmp1.txt", 'w+');
$sql = "INSERT INTO mobile_record(pics, x, y, worker, eventid) VALUES('$filenam','$lon','$lat','$worker','$eventid')";
fwrite($fp, $sql);
fclose($fp);
$db->dbQuery($sql);
$db->close_db();



//reverse
// $pic = stripslashes($base);
// $file = fopen($target_path.$filenam, 'wb');
// fwrite($file, base64_decode($base));
// file_put_contents($target_path.$filenam, $im);
// fclose($file);

// Load
// $source = imagecreatefromjpeg($target_path.$filenam);
$source = imagecreatefromstring(base64_decode($base));
// Rotate
$rotate = imagerotate($source, 270, 0);

// Output
imagejpeg($rotate, $target_path.$filenam);
// Free the memory
imagedestroy($source);
imagedestroy($rotate);

?>