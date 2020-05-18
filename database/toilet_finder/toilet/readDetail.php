<?php
	require_once('connection.php');
	header('Content-Type: application/json');	
	$id = json_decode(file_get_contents('php://input'), true)["id_toilet"];


		$read_sql = "SELECT * FROM toilet WHERE id_toilet=$id";

		$query = mysqli_query($CON, $read_sql);

		if($query){
			if($query_result = mysqli_fetch_assoc($query)){
				// printf("%s %s %s\n", $query_result["username"], $query_result["email"], $query_result["gender"]);
				

				$output_array= array(
						'nama'=>$query_result["nama"],
						'gender'=>$query_result["gender"],
						'id_toilet'=>$query_result["id_toilet"],
						'jam_buka'=>$query_result["jam_buka"],
						'jam_tutup'=>$query_result["jam_tutup"],
						'tipe'=>$query_result["tipe"],
					);

			} else {
				$output_array = array(
					'message'=>'ID not found'
				);

			}
			header('Content-Type: application/json');	
			echo json_encode($output_array);
			mysqli_free_result($query);

		} 
?>