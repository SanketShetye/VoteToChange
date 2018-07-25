<?php
	require "config.php";
	
	$username=$_POST["username"];
	$electionKey=$_POST["electionKey"];
	
	$sql = "SELECT `Election_status` FROM `election_details` WHERE Username='".$username."' AND Election_key='".$electionKey."'";
	$result=$conn->query($sql);
	
	if($result->num_rows()>0)
	{
		$row=$result->fetch_assoc();
		$electionStatus=$row["Election_status"];
		$sql = "UPDATE `election_details` SET `Election_status`= 2 WHERE Election_key='".$electionKey."'";
		if($conn->query($sql)==TRUE)
		{
			echo "1";
		}
		else
		{
			echo "Error: " . $sql . "<br>" . $conn->error;
		}
	}
	else
	{
		echo "Error: " . $sql . "<br>" . $conn->error;
	}
?>