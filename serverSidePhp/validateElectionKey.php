<?php
	//http://localhost/VoteToChange/validateElectionKey.php?electionKey=3b2684586c1fbb1b482648c6da2db17c613b27b1&username=sank
	require "config.php";

	
	//Table exists or not
	$sql = "SELECT * FROM `".$_POST["electionKey"]."_Can`";

	if ($conn->query($sql) == TRUE) {
		
		$sql = "SELECT * FROM `election_details` WHERE Election_key = '".$_POST["electionKey"]."'";
		$result = $conn->query($sql);
		if ($result->num_rows==1) {
			//1- Exists
			$row=$result->fetch_assoc();
			echo "1 ".$row["Election_status"];
		}
		else {
			//Election_key error
			echo "Error: " . $sql . "<br>" . $conn->error;
		}
	}
	else {
		//No election_can
		echo "Error: " . $sql . "<br>" . $conn->error;
	}
?>