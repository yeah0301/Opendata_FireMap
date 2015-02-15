<?php
require_once("DB_INFO.php");

class DBClass{
	private $con;
	public function __construct() {
		$this->con = sqlsrv_connect(SERVER_NAME, array("UID"=>USER, "PWD"=>PASSWORD, "Database"=>DATABASE, "CharacterSet" => "UTF-8"));
		if($conn === false){
			die(print_r(sqlsrv_errors()));
		}
    }
    public function dbQuery($sql){
    	return sqlsrv_query($this->con, $sql);
    }
    public function close_db(){
    	sqlsrv_close($this->con);
    }
}
?>