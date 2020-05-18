<?php
	require_once('connection.php');

	$read_sql = "SELECT * FROM toilet WHERE isApproved = 1";
	$query = mysqli_query($CON, $read_sql);
	$output_array = [];

	if($query){
		while($query_result = mysqli_fetch_assoc($query)){
			// printf("%s %s %s\n", $query_result["username"], $query_result["email"], $query_result["gender"]);
			

			$output_array[] = array(
					'nama'=>$query_result["nama"],
					'latitude'=>$query_result["latitude"],
					'longitude'=>$query_result["longitude"],
					'gender'=>$query_result["gender"],
					'id_toilet'=>$query_result["id_toilet"]
				);

		}
		header('Content-Type: application/json');	
		echo json_encode($output_array);
		mysqli_free_result($query);

	}
?>