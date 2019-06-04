<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include('model.class.php');

$model = new Model();


$data = [];

if (!empty($_REQUEST['licenseplate'])) {
    $vehicles = $model->deleteVehicles($_REQUEST['licenseplate']);

    if (!empty($vehicles)) {
        print_r("1");
    } else {
    	print_r("0");
    }
}