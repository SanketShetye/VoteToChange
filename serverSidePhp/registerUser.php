<?php

require "config.php";

$sql = "INSERT INTO user_details (Name, Username, Password, email) VALUES ('".$_POST["name"]."','".$_POST["username"]."','".$_POST["password"]."','".$_POST["email"]."')";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
$conn->close();
?>