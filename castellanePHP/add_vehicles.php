<?php

include('model.class.php');


if (isset($_REQUEST['licenseplate']) && isset($_REQUEST['mileage']) && isset($_REQUEST['brand']) && isset($_REQUEST['status'])) {
    
        $model = new Model();
        $model->add_vehicles($_REQUEST['licenseplate'], $_REQUEST['mileage'], $_REQUEST['brand'], $_REQUEST['status']);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }

