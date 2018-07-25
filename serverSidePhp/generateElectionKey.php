<?php

//Format of the token is ---- [sha1(username)][yday][mon][year][minutes][mday][seconds][month][hours][sha1(name)]

function generateToken($username, $name){	
	$mydate = getdate();
	$token = sha1($name.$username);
	return $token;
}

//echo generateToken("himanshu");


?>