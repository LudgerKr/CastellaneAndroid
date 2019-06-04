<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include('model.class.php');

$model = new Model();

$data = [];

if (!empty($_REQUEST['idseance'])) {
    $seance = $model->my_seance($_REQUEST['idseance']);

    if (!empty($seance)) {
        $data[] = $seance;
    }
}

echo json_encode($data);