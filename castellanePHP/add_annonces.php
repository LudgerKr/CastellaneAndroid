<?php

include('model.class.php');


if (isset($_REQUEST['title']) && isset($_REQUEST['content']) && isset($_REQUEST['userid'])) {
    
        $model = new Model();
        $model->add_annonces($_REQUEST['title'], $_REQUEST['content'], $_REQUEST['userid']);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }

