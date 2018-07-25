<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "votetochange";
  // Create connection
$conn = new mysqli($servername, $username,$password, $dbname);
  // Check connection
if ($conn->connect_error) {
    
 die("Connectionsfailed: " . $conn->connect_error);
} 
echo "";
?>



