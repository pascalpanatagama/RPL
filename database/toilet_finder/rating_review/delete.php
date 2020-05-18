<?php
	require_once('connection.php');
	header('Content-Type: application/json');	

	$id = json_decode(file_get_contents('php://input'), true)["id_rating"];

	$del_query = "DELETE FROM ratings WHERE id_rating=$id";
	$delreview_query = "DELETE FROM reviews WHERE id_rating=$id";

	$query1 = mysqli_query($CON, $del_query);
	$banyak_data_rating_terhapus = mysqli_affected_rows($CON);
	$query2 = mysqli_query($CON, $delreview_query);
	$banyak_data_review_terhapus = mysqli_affected_rows($CON);
	if($query1 ||  $query2){
		if($banyak_data_rating_terhapus > 0){
			if($banyak_data_review_terhapus > 0){
				echo json_encode(
					array(
						"message"=> "Data rating dan review berhasil dihapus"
					)
				);
			} else {
				echo json_encode(
					array(
						"message"=> "Data rating berhasil dihapus"
					)
				);

			}

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