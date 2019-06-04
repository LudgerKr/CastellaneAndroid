<?php

include('model.class.php');


if (isset($_REQUEST['iduser']) && isset($_REQUEST['lastname']) && isset($_REQUEST['firstname'])
    && isset($_REQUEST['age']) && isset($_REQUEST['phone']) && isset($_REQUEST['email'])) {
    
        $model = new Model();
        $model->update_profile($_REQUEST['iduser'], $_REQUEST['lastname'], $_REQUEST['firstname'],
        $_REQUEST['age'], $_REQUEST['phone'], $_REQUEST['email'], $_REQUEST['password']);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }