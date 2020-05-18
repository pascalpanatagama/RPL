<?php
$HOST = 'localhost';
$USER = 'root';
$PASS = '';
$DB = 'toilet_finder';

//$CON = new mysqli($HOST,$USER,$PASS,$DB);

	$CON = mysqli_connect($HOST,$USER,$PASS,$DB);
// if($CON->connect_errno){
// 	printf("Connect failed: %s\n", $CON->connect_errno);
// 	exit();
// } else {
// 	printf("Connection to %s success\n", $DB);
// }

?>