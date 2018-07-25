<?php
	require "config.php";
	
	$electionKey=$_GET["electionKey"]."_can";
	$username=$_GET["username"];
	$candidateName=$_GET["candidateName"];
	
	$sql = "SELECT * FROM `".$electionKey."` WHERE Username= '".$candidateName."'";
	$result = $conn->query($sql);
	if($result->num_rows >0)
	{
		$row = $result->fetch_assoc();
		$voteCount=$row["Vote_Count"]+1;
		$sql = "INSERT INTO `".$_GET["electionKey"]."_Vo` (`Username`) VALUES ('".$username."')";
		if($conn->query($sql)==TRUE)
		{
			$sql = "UPDATE `".$electionKey."` SET `Vote_Count`= ".$voteCount." WHERE Username = '".$candidateName."'";
			if($conn->query($sql)==TRUE)
			{
				echo "1";
			}
			else{
				echo "Error: " . $sql . "<br>" . $conn->error;
			}
		}
		else{
			echo "Error: " . $sql . "<br>" . $conn->error;
		}
	}
	else{
		echo "Error: " . $sql . "<br>" . $conn->error;
	}
	
	
?>