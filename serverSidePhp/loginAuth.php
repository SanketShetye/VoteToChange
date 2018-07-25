<?php

require "config.php";

$sql = "Select * from user_details where Username='{$_POST["username"]}' and Password='{$_POST["password"]}'";

$result = $conn->query($sql);


if($result->num_rows >= 1){
	//this means that the username and password are correct.
	echo "1";
}
else{
	//Username and password are not correct.
	echo "0-".$_POST["username"]." -- ".$_POST["password"]."*";
}

?>