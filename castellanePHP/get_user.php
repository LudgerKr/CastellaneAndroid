<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('model.class.php');

$model = new Model();

$data = [];
if (isset($_REQUEST['email']) && isset($_REQUEST['right'])) {

$users = $model->select_user($_REQUEST['email'], $_REQUEST['right']);

		foreach ($users as $user) {
				$data[] = ["iduser" => $user['iduser'],
				"lastname" => utf8_encode($user['lastname']),
				"firstname" => utf8_encode($user['firstname']),
				"age" => utf8_encode($user['age']),
				"phone" => utf8_encode($user['phone']),
				"email" => utf8_encode($user['email']),
				"right" => utf8_encode($user['right'])];
				echo json_encode($data);
		}
	}