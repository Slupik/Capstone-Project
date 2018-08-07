<?php
$file = 'saveddata.json';
$current = json_encode(file_get_contents('php://input'));
file_put_contents($file, $current);
?>