<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('model.class.php');

$model = new Model();

$data = [];

if (isset($_REQUEST['right'])) {
	$right = $_REQUEST['right'];
$usersList = $model->get_usersList($right);

		foreach ($usersList as $userList) {
				$data[] = ["iduser" => $userList['iduser'],
				"lastname" => utf8_encode($userList['lastname']),
				"firstname" => utf8_encode($userList['firstname']),
				"age" => utf8_encode($userList['age']),
				"phone" => utf8_encode($userList['phone']),
				"email" => utf8_encode($userList['email']),
				"right" => utf8_encode($userList['right'])];
				echo json_encode($data);
		}
	}