<?php
include('model.class.php');

$data = [];
$data['nb'] = 0;

if (isset($_REQUEST['email']) && isset($_REQUEST['password'])) {
	$email = $_REQUEST['email'];
	$password = $_REQUEST['password'];
	$model = new Model();
	$user = $model->check_connexion($email, $password);
	if ($user) {
		$data['nb'] = 1;
		$data['iduser'] = $user['iduser'];
		$data['lastname'] = $user['lastname'];
		$data['firstname'] = $user['firstname'];
	}
}

echo json_encode([$data]);
?>