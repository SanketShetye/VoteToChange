<?php
	//http://localhost/VoteToChange/registerCandidate.php?username=SAn&&candidateKey=a6b04b1fa140229c70098a699b7a65b11d5052ee
	require "config.php";
    
	$username=$_POST["username"];
	$output="";
	
	//validating candidate key
	$sql = "SELECT * FROM `election_details` WHERE Candidate_key='".$_POST["candidateKey"]."'";
	
	$result = $conn->query($sql);

	//only one entry present
	if ($result->num_rows == 1) {
		$row = $result->fetch_assoc();
		$electionName = $row["Election_name"];
		$electionKey = $row["Election_key"];
		$electionStatus = $row["Election_status"];
		$output = "".$electionName." ".$electionKey." ".$electionStatus."";
		//------accept elecionKey and name too-------
		//-----check election Status too----
		if($electionStatus==0)
		{
			$sql = "INSERT INTO `".$row["Election_key"]."_Can`(`Username`, `Vote_Count`) VALUES ('".$username."',0)";
			if($conn->query($sql)==TRUE)
			{
				echo "1 ".$output;
			}
			else
			{
				//-----already exist------
				echo "Error: ".$sql . "<br>" . $conn->error;
			}
		}
		else{
			//electionStatus not 0
			echo "0 ".$output;
		}		
	}
	else {
		echo "Error: ".$sql . "<br>" . $conn->error;
	}
?>