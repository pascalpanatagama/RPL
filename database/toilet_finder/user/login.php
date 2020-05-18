<?php
	require_once('connection.php');
	header('Content-Type: application/json');	
	
	$data = json_decode(file_get_contents('php://input'), true);

	if(isset($data["username"]) && isset($data["password"])){
		$username = $data["username"];
		$password = $data["password"];

		$read_username_db = "SELECT * FROM user WHERE username = '$username' AND password = '$password'";
		$query = mysqli_query($CON, $read_username_db);
		$output_array = [];

		if($query){
			while($query_result = mysqli_fetch_assoc($query)){
				$output_array = array(
									'username'=>$query_result["username"],
									'gender'=>$query_result["gender"],
									'foto'=>$query_result["foto"],
									'isAdmin'=>$query_result["isAdmin"],
									'id_user'=>$query_result["id_user"]
								);
			} 

			if(count($output_array) > 0){
					header('Content-Type: application/json');	
					echo json_encode($output_array);
					mysqli_free_result($query);

			} else {
					http_response_code(400);
					echo json_encode(array('message'=>'Username or password is incorrect'));
			}


		}


	}

?>
