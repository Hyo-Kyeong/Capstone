<?php    
    include('DBconn.php');
    

    $stmt = mysqli_prepare($con, "SELECT EXISTS ( SELECT * FROM SPP_USER WHERE ID=?) as id_check");

    mysqli_stmt_bind_param($stmt, "s", $ID);
    $ID=$_POST['id'];

    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $ID_CHECK);
    mysqli_stmt_fetch($stmt);

    $respnse = array();
    $response["id_exist"] = $ID_CHECK;
    echo json_encode($response);
?>
