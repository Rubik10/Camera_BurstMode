
<?php 

	echo 'dentro de upload';

	if($_SERVER['REQUEST_METHOD']=='POST') {
	 
	 	$image = $_POST['image'];
	    $name = $_POST['name'];
	 
	 
	 $path = "img/".$name.".png";
	file_put_contents($path,base64_decode($image));
	
	echo "Successfully Uploaded";

	}

 ?>