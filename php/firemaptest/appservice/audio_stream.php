<?php
error_reporting(E_ERROR | E_WARNING | E_PARSE);

require_once '../include/DBClass.php';

$lon = $_POST['x'];
$lat = $_POST['y'];
$worker = $_POST['worker'];
$eventid = $_POST['eventid'];
$desc = $_POST['desc'];
$file = fopen("temp.txt", "w+");
$str = "Lon:".$lon.",Lat:".$lat.",Worker:".$worker.",EventID:".$eventid.".\n";
fwrite($file, $str);


$file_name = $worker."_".date("ymdhis").".mp3";
$file_name = str_replace(":","_",$file_name);
$file_path = "./audio_box/".$file_name;
if( !empty($_FILES['uploadedfile']) ){
    if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $file_path)) {
    	echo "success";
    	fwrite($file, "file uploadfile success ".$file_path."\n");
    } else{
    	echo "fail";
    	fwrite($file, "file uploadfile fail ".$file_path."\n");
    }
}
if( !empty($_POST['uploadedfile']) ){
//     fwrite($file, "uploadfile post write\n");
    if(move_uploaded_file($_POST['uploadedfile']['tmp_name'], $file_path)) {
    	echo "success";
    	fwrite($file, "post uploadfile success\n");
    } else{
    	echo "fail";
    	fwrite($file, "post uploadfile fail\n");
    }
}
$db = new DBClass();
$sql = "INSERT INTO audio_record(worker, eventid, mp3_path) VALUES('$worker','$eventid','$file_name');";
fwrite($file, $sql."\n");
$db->dbQuery($sql);
$db->close_db();

fclose($file);

?>