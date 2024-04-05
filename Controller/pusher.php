<?php
require_once __DIR__ . '/../vendor/autoload.php';

$pusher = new Pusher\Pusher(
    'b26a50e9e9255fc95c8f',
    'a35da9008676aa90a2f5',
    '1783297',
    [
        'cluster' => 'ap1',
        'useTLS' => true
    ]
);
?>
