<?php
	require_once('connection.php');
	header('Content-Type: application/json');	

	$data = json_decode(file_get_contents('php://input'), true);

	if (isset($data['username']) && 
		isset($data['usernameBaru'])) {

		$username = $data['username'];
		$usernameBaru = $data['usernameBaru'];

		$update_query = "UPDATE user SET username='$usernameBaru' WHERE username = '$username'" ;

		$query = mysqli_query($CON, $update_query);
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
				http_response_code(400);
				echo json_encode(
					array(
						"message"=>"data tidak ditemukan", 
					)
				);
			}
		} else {
				// error database tidak terquery
				http_response_code(400);
				echo json_encode(
					array(
						"message"=>"query tidak jalan", 
						"error"=>mysqli_error($CON)
					)
				);
		}
}
?>