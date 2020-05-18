<?php
	require_once('connection.php');
	header('Content-Type: application/json');	

	$data = json_decode(file_get_contents('php://input'), true);

	if (isset($data['nama']) && 
		isset($data['jam_buka']) && 
		isset($data['jam_tutup']) && 
		// isset($data['gender']) &&
		// isset($data['tipe']) &&
		isset($data['latitude']) &&
		isset($data['longitude']) ) {

		$nama = $data['nama'];
		$jamb = $data['jam_buka'];
		$jamt = $data['jam_tutup'];
		$gender = $data['gender'];
		$tipe = $data['tipe'];
		$lat = $data['latitude'];
		$lon = $data['longitude'];

		$query = mysqli_query($CON, "INSERT INTO toilet(nama, jam_buka, jam_tutup, gender, tipe, latitude, longitude) VALUES ('$nama','$jamb','$jamt','$gender','$tipe', '$lat', '$lon')");
	 	if($query){
 			echo json_encode(array('message'=>'toilet data successfully added.'));
			}else{
 			echo json_encode(array(
 				'message'=>'toilet data failed to add.',
 				'error'=>mysqli_error($CON)
 			));
		}
	} else {
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