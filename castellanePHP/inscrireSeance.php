<?php

include('model.class.php');


if (isset($_REQUEST['iduser']) && isset($_REQUEST['idseance'])) {
    
        $model = new Model();
        $model->inscrireSeance($_REQUEST['iduser'], $_REQUEST['idseance']);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }

