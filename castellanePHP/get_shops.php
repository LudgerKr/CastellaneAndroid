<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('model.class.php');

$model = new Model();

$data = [];
//echo json_encode($data);
//exit();
if (isset($_REQUEST['idshop'])) {
	$id = $_REQUEST['idshop'];
$shops = $model->get_shops($id);

		foreach ($shops as $shop) {
				/*$data[] = ["idshop" => $shop['idshop'],
						    "title" => $shop['title'],
							"price" => $shop['price'],
							"content" => $shop['content'],
							"content2" => $shop['content2'],
							"content3" => $shop['content3'],
							/*"content4" => $shop['content4']
				//error_log($shop['content4']);
				echo json_encode($data);*/
				/*echo json_encode([["idshop" => $shop['idshop'],
								   "title" => $shop['title'],
								   "price" => $shop['price']]]);
				exit();
				error_log("title => ".$shop['title']);*/
				$data[] = ["idshop" => $shop['idshop'],
				"title" => utf8_encode($shop['title']),
				"price" => $shop['price'],
				"content" => utf8_encode($shop['content']),
				"content2" => utf8_encode($shop['content2']),
				"content3" => utf8_encode($shop['content3']),
				"content4" => utf8_encode($shop['content4']),
				"content5" => utf8_encode($shop['content5'])];
				echo json_encode($data);
		}
	}

//error_log("data[0] => ".json_encode($data[0]['idshop']));
//echo json_encode(["yolo" => "wesh"]);