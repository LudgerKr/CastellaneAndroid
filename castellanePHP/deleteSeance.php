<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include('model.class.php');

$model = new Model();

$data = [];

if (!empty($_REQUEST['idseance'])) {
    $seance = $model->deleteSeance($_REQUEST['idseance']);

    if (!empty($seance)) {
        print_r("1");
    } else {
    	print_r("0");
    }
}