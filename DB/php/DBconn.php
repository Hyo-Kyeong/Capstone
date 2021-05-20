<?php
	$con = mysqli_connect("spp.cvpwanalnvoc.ap-northeast-2.rds.amazonaws.com", "admin", "capstone", "SPP");

	mysqli_query($con, "set session character_set_connection=utf8;");
    mysqli_query($con, "set session character_set_results=utf8;");
    mysqli_query($con, "set session character_set_client=utf8;");

?>