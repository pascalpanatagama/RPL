<?php
	require_once('connection.php');

	$id = $_POST['id_user'];
	$username = $_POST['username'];
	$pass = $_POST['password'];
	$email = $_POST['email'];
	$gender = $_POST['gender'];

	$del_query = "UPDATE user SET username='$username', password='$pass', email='$email', gender='$gender' WHERE id_user=$id";
	$query = mysqli_query($CON, $del_query);
	$banyak_data_terupdate = mysqli_affected_rows($CON);

	if($query){
		if($banyak_data_terupdate > 0){
			echo json_encode(
				array(
					"message"=> $banyak_data_terupdate . " data berhasil diupdate"
				)
			);
		} else {
			// error ketika query jalan tapi data tidak terupdate karena tak ada idnya
			echo json_encode(
				array(
					"message"=>"data tidak ditemukan", 
				)
			);
		}
	} else {
			// error database tidak terquery
			echo json_encode(
				array(
					"message"=>"query tidak jalan", 
					"error"=>mysqli_error($CON)
				)
			);
	}

?>