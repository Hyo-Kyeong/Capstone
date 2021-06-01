<?php 
    include('DBconn.php');

    $stmt = mysqli_prepare($con, "INSERT INTO SPP_USER (id, pw, birth, name, phone) VALUES (?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($stmt, "sssss", $ID, $PW, $NAME, $BIRTH, $PHONE);

    $ID=$_POST['id'];
    $PW=$_POST['pw'];
    $NAME=$_POST['name'];
    $BIRTH=$_POST['birth'];
    $PHONE=$_POST['phone'];

    mysqli_stmt_execute($stmt);
    
?>
