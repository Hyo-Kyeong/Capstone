<?php 
    include('DBconn.php');

    $stmt = mysqli_prepare($con, "SELECT EXISTS ( SELECT * FROM SPP_USER WHERE id=? AND pw=?) as login");

    mysqli_stmt_bind_param($stmt, "ss", $ID, $PW);

    $ID=$_POST['id'];
    $PW=$_POST['pw'];

    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $LOGIN);
    mysqli_stmt_fetch($stmt);

    $respnse = array();
    $response["login"] = $LOGIN;
    echo json_encode($response);
?>
