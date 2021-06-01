<?php 
    include('DBconn.php');

    $stmt = mysqli_prepare($con, "SELECT name, birth, phone FROM SPP_USER WHERE id=? AND pw=?");

    mysqli_stmt_bind_param($stmt, "ss", $ID, $PW);

    $ID=$_POST['id'];
    $PW=$_POST['pw'];

    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $NAME, $BIRTH, $PHONE);
    mysqli_stmt_fetch($stmt);

    $respnse = array();
    $response["name"] = $NAME;
    $response["birth"] = $BIRTH;
    $response["phone"] = $PHONE;
    echo json_encode($response);
?>