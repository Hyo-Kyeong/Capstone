<?php 
    include('DBconn.php');

    $stmt = mysqli_prepare($con, "INSERT INTO SPP_USER (id, pw, name, phone) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($stmt, "ssss", $ID, $PW, $NAME, $PHONE);

    $ID=$_POST['id'];
    $PW=$_POST['pw'];
    $NAME=$_POST['name'];
    $PHONE=$_POST['phone'];

    mysqli_stmt_execute($stmt);
    
?>
