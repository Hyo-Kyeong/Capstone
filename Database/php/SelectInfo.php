<?php 
    include('DBconn.php');

    $stmt = mysqli_prepare($con, "SELECT S.name, S.birth, S.phone, S.car, S.card, C.cvc, C.expiration_year, C.expiration_month FROM SPP_USER AS S LEFT OUTER JOIN CARD AS C ON S.card = C.card where S.id=?");

    mysqli_stmt_bind_param($stmt, "s", $ID);

    $ID=$_POST['id'];    

    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $NAME, $BIRTH, $PHONE, $CAR, $CARD, $CVC, $EXP_YEAR, $EXP_MONTH);
    mysqli_stmt_fetch($stmt);

    $respnse = array();
    $response["name"] = $NAME;
    $response["birth"] = $BIRTH;
    $response["phone"] = $PHONE;
    $response["car"] = $CAR;
    $response["card"] = $CARD;
    $response["cvc"] = $CVC;
    $response["exp_year"] = $EXP_YEAR;
    $response["exp_month"] = $EXP_MONTH;
    echo json_encode($response);
?>