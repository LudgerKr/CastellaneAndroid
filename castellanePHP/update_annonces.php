<?php

include('model.class.php');

if (isset($_REQUEST['idnew']) && isset($_REQUEST['title'])
    && isset($_REQUEST['content']) && isset($_REQUEST['userid'])) {
    
        $model = new Model();
        $model->update_annonces($_REQUEST['idnew'], $_REQUEST['title'], $_REQUEST['content'],
        $_REQUEST['userid']);
        print('[{"confirm: "1"}]');
    } else {
        print('[{"confirm: "0"}]');
    }