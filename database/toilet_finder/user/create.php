<?php
	require_once('connection.php');
	header('Content-Type: application/json');	

	$data = json_decode(file_get_contents('php://input'), true);

	if (isset($data['username']) && 
		isset($data['password']) && 
		isset($data['email']) && 
		isset($data['gender']) &&
		strlen($data['username']) > 0 &&
		strlen($data['password']) > 0 &&
		strlen($data['email']) > 0 &&
		strlen($data['gender']) > 0 ) {
		$username = $data['username'];
		$pass = $data['password'];
		$email = $data['email'];
		$gender = $data['gender'];

		$query = mysqli_query($CON, "INSERT INTO user(username, password, email, gender) VALUES ('$username','$pass','$email','$gender')");
	 	if($query){
			http_response_code(201);
 			echo json_encode(array('message'=>'student data successfully added.'));
			}else{
			http_response_code(500);
 			echo json_encode(array(
 				'message'=>'student data failed to add.',
 				'error'=>mysqli_error($CON)
 			));
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