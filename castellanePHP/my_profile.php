<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include('model.class.php');

$model = new Model();

$data = [];

if (!empty($_REQUEST['email'])) {
    $profile = $model->my_profile($_REQUEST['email']);

    if (!empty($profile)) {
        $data[] = $profile;
    }
}

echo json_encode($data);