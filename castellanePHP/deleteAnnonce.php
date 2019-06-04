<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include('model.class.php');

$model = new Model();

$data = [];

if (!empty($_REQUEST['title'])) {
    $annonce = $model->deleteAnnonce($_REQUEST['title']);

    if (!empty($annonce)) {
        print_r("1");
    } else {
    	print_r("0");
    }
}