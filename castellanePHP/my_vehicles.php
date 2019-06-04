<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('model.class.php');

$model = new Model();

$data = [];

if (!empty($_REQUEST['licenseplate'])) {
	$vehicles = $model->my_vehicles($_REQUEST['licenseplate']);

	if (!empty($vehicles)) {
	$data[] = ($vehicles);
		}
	}

					echo json_encode($data);
