<?php
	require "config.php";
	
	$sql = "SELECT `Election_status` FROM `election_details` WHERE Election_key='".$_POST["electionKey"]."'";
	
	$result = $conn->query($sql);
	if ($conn->query($sql) == TRUE) {
		$result = $conn->query($sql);
		if ($result->num_rows==1) {
			//1- Exists
			$row=$result->fetch_assoc();
			echo $row["Election_status"];
		}
		else {
			//Election_key error
			echo "-1";
		}
	}
	else {
		//No election_can
		echo "-1";
	}
	
?>