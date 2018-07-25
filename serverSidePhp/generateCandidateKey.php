<?php

//Format of the token is ---- [sha1(username)][yday][mon][year][minutes][mday][seconds][month][hours][sha1(name)]

function generateCandiToken($name){	
	$mydate = getdate();
	$token = sha1($name."candidate");
	return $token;
}

//echo generateToken("himanshu");


?>