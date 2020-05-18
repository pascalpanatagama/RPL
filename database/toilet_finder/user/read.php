<?php
	require_once('connection.php');

	$read_sql = "SELECT * FROM user";
	$query = mysqli_query($CON, $read_sql);
	$output_array = [];

	if($query){
		while($query_result = mysqli_fetch_assoc($query)){
			// printf("%s %s %s\n", $query_result["username"], $query_result["email"], $query_result["gender"]);
			

			$output_array[] = array(
					'username'=>$query_result["username"],
					'email'=>$query_result["email"],
					'gender'=>$query_result["gender"]
				);

		}
		header('Content-Type: application/json');	
		echo json_encode($output_array);
		mysqli_free_result($query);

	}
?>