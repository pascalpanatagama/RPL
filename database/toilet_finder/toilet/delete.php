<?php
	require_once('connection.php');

	$id = json_decode(file_get_contents('php://input'), true)["id_toilet"];

	$del_query = "DELETE FROM toilet WHERE id_toilet=$id";
	$query = mysqli_query($CON, $del_query);
	$banyak_data_terhapus = mysqli_affected_rows($CON);

	if($query){
		if($banyak_data_terhapus > 0){
			echo json_encode(
				array(
					"message"=> $banyak_data_terhapus . " data berhasil dihapus"
				)
			);
		} else {
			// error ketika query jalan tapi data tidak terhapus karena tak ada idnya
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