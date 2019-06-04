<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include('model.class.php');

$model = new Model();

$data = [];

if (!empty($_REQUEST['userid'])) {
    $seance = $model->get_MySeance($_REQUEST['userid']);
}