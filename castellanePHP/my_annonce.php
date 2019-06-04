<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include('model.class.php');

$model = new Model();

$data = [];

if (!empty($_REQUEST['title'])) {
    $annonce = $model->my_annonce($_REQUEST['title']);

    if (!empty($annonce)) {
        $data[] = $annonce;
    }
}

echo json_encode($data);