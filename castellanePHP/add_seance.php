<?php

include('model.class.php');


if (isset($_REQUEST['date']) && isset($_REQUEST['time'])) {
    
        $model = new Model();
        $model->add_seance($_REQUEST['date'], $_REQUEST['time']);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }

