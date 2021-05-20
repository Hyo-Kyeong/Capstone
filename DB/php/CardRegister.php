<?php 
    include('DBconn.php');

    $stmt = mysqli_prepare($con, "INSERT INTO CARD(card, expiration_month, expiration_year, cvc) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($stmt, "siii", $CARD, $EXPIRATION_MONTH, $EXPIRATION_YEAR, $CVC);

    $CARD=$_POST['card'];
    $EXPIRATION_MONTH=$_POST['expiration_month'];
    $EXPIRATION_YEAR=$_POST['expiration_year'];
    $CVC=$_POST['cvc'];

    mysqli_stmt_execute($stmt);


    $stmt = mysqli_prepare($con, "UPDATE SPP_USER SET card=? where id=?");
    mysqli_stmt_bind_param($stmt, "ss", $CARD, $ID);

    $ID=$_POST['id'];
    $CARD=$_POST['card'];

    mysqli_stmt_execute($stmt);


    
?>
