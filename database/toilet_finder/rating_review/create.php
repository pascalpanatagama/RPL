<?php
	require_once('connection.php');
	header('Content-Type: application/json');	

	$data = json_decode(file_get_contents('php://input'), true);
	
	
	if (isset($data['ratingNumber']) && isset($data['id_toilet']) && isset($data['id_user'])) {
	$id_user = $data['id_user'];
	$id_toilet = $data['id_toilet'];		

		$ratingNumber = $data['ratingNumber'];
		$response=[];
		
		$query = mysqli_query($CON, "INSERT INTO ratings(id_user, id_toilet, ratingNumber, date) VALUES ('$id_user','$id_toilet','$ratingNumber', NOW())");
		
	 	if($query){
		 	$lastID = mysqli_insert_id($CON);
	 		// echo $lastID;
	 		if(isset($data['ratingText']) && strlen($data['ratingText']) > 0){
				$rating_text = $data['ratingText'];
				$reviews = "INSERT INTO reviews(id_rating, ratingText) VALUES ('$lastID', '$rating_text')";
				$query_review = mysqli_query($CON, $reviews);			
			 	echo json_encode(array('message'=>'Rating n review successfully added.'));
	 		} else {
	 			echo json_encode(array('message'=>'Rating successfully added.'));
	 		}
		} else {
			http_response_code(400);
			echo json_encode(array("error"=>'Duplikat review'));
		}
	} else {
		http_response_code(400);
  		echo json_encode(array('message'=>'required field is empty.'));		
	}



	// if(!$username || !$pass || !$email || !$gender){
	// }else{
	// 	$query = mysqli_query($CON, "INSERT INTO user(username, password, email, gender) VALUES ('$username','$pass','$email','$gender')");
	// 	 	if($query){
 //     			echo json_encode(array('message'=>'student data successfully added.'));
 //   			}else{
 //     			echo json_encode(array(
 //     				'message'=>'student data failed to add.',
 //     				'error'=>mysqli_error($CON)
 //     			));
 //   			}
		// $result = $CON->query("SELECT * FROM user");
		// if($CON->query("INSERT INTO user(username, password, email, gender) VALUES ('$username','$pass','$email','$gender')")) {
		// 	echo json_encode(array('message'=>'student data successfully added.'));
		// } else {
		// 	printf("Error: %s\n", $CON->error);
		// }

	// }
?>