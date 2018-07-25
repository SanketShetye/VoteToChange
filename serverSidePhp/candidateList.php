<?php
	//http://localhost/VoteToChange/candidateList.php?electionKey=3b2684586c1fbb1b482648c6da2db17c613b27b1
	require "config.php";
	
	//Receiving election key and selecting candidate
	$sql = "SELECT `Username` FROM `".$_POST["electionKey"]."_Can`";
	
	$finalResult="";
	
	$result = $conn->query($sql);
	
	if($result->num_rows > 0)
	{
		//selecting candidate and seperating using #1234#
		while($row=$result->fetch_assoc())
		{
			$finalResult= $finalResult.$row["Username"]."#1234#";
		}
		echo $finalResult;
	}
	else
	{
		echo "Error: " . $sql . "<br>" . $conn->error;
	}
?>