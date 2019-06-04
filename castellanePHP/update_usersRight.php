<?php

include('model.class.php');

if (isset($_REQUEST['email']) && isset($_REQUEST['right'])) {
        
        $model = new Model();
        $right = $_REQUEST['right'];
        $model->update_usersRight($_REQUEST['email'], $right);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }