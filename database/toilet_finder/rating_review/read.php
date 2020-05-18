<?php
	require_once('connection.php');

	$id = json_decode(file_get_contents('php://input'), true)["id_toilet"];

	$read_sql = "SELECT username, id_rating, ratingNumber, nama, ratingText , date
				FROM `ratings`
				LEFT OUTER JOIN user USING (id_user)
				LEFT OUTER JOIN toilet USING (id_toilet)
				LEFT OUTER JOIN reviews USING (id_rating)
				WHERE id_toilet = $id";
	$query = mysqli_query($CON, $read_sql);
	$output_array = [];

	if($query){
		while($query_result = mysqli_fetch_assoc($query)){
			// printf("%s %s %s\n", $query_result["username"], $query_result["email"], $query_result["gender"]);
			

			$output_array[] = array(
					'username'=>$query_result["username"],
					'ratingNumber'=>$query_result["ratingNumber"],
					'ratingText'=>$query_result["ratingText"],
					'date'=>$query_result["date"],
					'id_rating'=>$query_result["id_rating"]
				);

		}


	}
			header('Content-Type: application/json');	
		echo json_encode(array('ratings' => $output_array));
		mysqli_free_result($query);
?>