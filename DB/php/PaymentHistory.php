<?php 
    
    header("Content-Type:text/html;charset=utf-8");

    include('DBconn.php');

    $stmt = mysqli_prepare($con, "SELECT PH.pay_date, PH.payment, PH.car, PH.place FROM PAYMENT_HISTORY PH INNER JOIN SPP_USER U on PH.user_id = U.user_id where U.id = ? and PH.pay_date BETWEEN ? and ?");

    mysqli_stmt_bind_param($stmt, "sss", $ID, $START_DATE, $END_DATE);

    $ID=$_POST['id'];
    $START_DATE=$_POST['start_date'];
    $END_DATE=$_POST['end_date'];


    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $PAY_DATE, $PAYMENT, $CAR, $PLACE);
    
    while(mysqli_stmt_fetch($stmt)) {

        $array[] = $PAY_DATE;
        $array[] = $PAYMENT;
        $array[] = $CAR;
        $array[] = $PLACE;

        $response[] = $array;
    }

    // while($row=mysqli_fetch_array($stmt)) {
    //     array_push($response, array('pay_date'=>$row[0], 'payment'=>$row[1], 'car'=>$row[2], 'place'=>$row[3]));
    // }


    echo json_encode($response, JSON_UNESCAPED_UNICODE);
?>
