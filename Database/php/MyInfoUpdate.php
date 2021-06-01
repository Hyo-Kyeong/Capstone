<?php 
    include('DBconn.php');

    $stmt = mysqli_prepare($con, "UPDATE SPP_USER SET pw=?, birth=?, phone=? where id=?");
    mysqli_stmt_bind_param($stmt, "ssss", $PW, $BIRTH, $PHONE, $ID);

    $ID=$_POST['id'];
    $PW=$_POST['pw'];
    $BIRTH=$_POST['birth'];
    $PHONE=$_POST['phone'];

    mysqli_stmt_execute($stmt);
    
?>
