<?php 
    include('DBconn.php');

    $stmt = mysqli_prepare($con, "UPDATE SPP_USER SET car=? where id=?");
    mysqli_stmt_bind_param($stmt, "ss", $CAR, $ID);

    $ID=$_POST['id'];
    $CAR=$_POST['car'];

    mysqli_stmt_execute($stmt);
    
?>
