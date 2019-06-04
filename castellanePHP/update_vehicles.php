<?php

include('model.class.php');

if (isset($_REQUEST['mileage']) && isset($_REQUEST['status']) && isset($_REQUEST['licenseplate'])) {
    
        $model = new Model();
        $model->update_vehicles($_REQUEST['mileage'], $_REQUEST['status'], $_REQUEST['licenseplate']);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }