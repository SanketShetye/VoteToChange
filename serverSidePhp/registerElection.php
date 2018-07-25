<?php

//on sucess the script returns-->  electionkey candidatekey 1 1

require "generateElectionKey.php";
require "config.php";
require "generateCandidateKey.php";

$token = generateToken($_POST["username"],$_POST["election_name"]);
$candiToken = generateCandiToken($_POST["election_name"]);

$initialStatus = "0";

$sql = "INSERT INTO election_details (Username,Election_name, Election_key, Candidate_key, Election_status, description) VALUES ('".$_POST["username"]."','".$_POST["election_name"]."','".$token."','".$candiToken."','".$initialStatus."','".$_POST["description"]."')";

$returnRes = "";
if ($conn->query($sql) === TRUE) {
    $returnRes = $token." ".$candiToken;
	
	//create all temp tables.
	$sql = "create table ".$token."_Can\n"
    . "(\n"
    . " Username varchar(100),\n"
    . " Vote_Count int,\n"
    . " primary key(Username)\n"
    . ")";

	if ($conn->query($sql) === TRUE) {
		$returnRes = $returnRes." 1";
		
		$sql = "create table ".$token."_Vo\n"
		. "(\n"
		. " Username varchar(100),\n"
		. " id int,\n"
		. " primary key(Username)\n"
		. ")";
		
		if ($conn->query($sql) === TRUE) {
			echo $returnRes." 1";
		}
		else{
			echo "3-Error: " . $sql . "<br>" . $conn->error;
		}
	}
	else{
		echo "2-Error: " . $sql . "<br>" . $conn->error;
	}
	
} else {
    echo "1-Error: " . $sql . "<br>" . $conn->error;
}
$conn->close();
?>