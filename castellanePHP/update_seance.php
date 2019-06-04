<?php

include('model.class.php');

if (isset($_REQUEST['idseance']) && isset($_REQUEST['date'])
    && isset($_REQUEST['time'])) {
    
        $model = new Model();
        $model->update_seance($_REQUEST['idseance'], $_REQUEST['date'], $_REQUEST['time']);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }