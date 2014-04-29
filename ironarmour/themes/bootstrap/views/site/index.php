<?php

/* @var $this SiteController */

$this->pageTitle = Yii::app()->name;

//echo phpinfo();
/**/
$m = new MongoClient();
$db = $m->gpsData;

$gps = $db->gpsDataCollection;

/*
$gps->remove();
$coords = array(
        'coordinates'     => array("4043.5717N","07400.2783W"),
    );
$gps->insert($coords);

$coords = array(
        'coordinates'     => array("4043.6717N","07400.2783W"),
    );
$gps->insert($coords);

$coords = array(
        'coordinates'     => array("4043.6717N","07400.2783W"),
    );
$gps->insert($coords);
*/
$cursor = $gps->find();

$one = $gps->findOne();
$count = 0;
$points = '';
foreach ($cursor as $doc) 
{
    foreach ($doc['data']['coordinates'] as $location) 
    {
        if ($count % 2 == 0) $points.= "|" . DMStoDEC($location, 'lattitude');
        else $points.= "," . DMStoDEC($location, 'longitude');

        $count ++;
    }
}

//var_dump($cursor);

//http://maps.google.com/maps/api/staticmap?zoom=14&size=512x512&maptype=roadmap&path=color:0x0000ff|weight:5|40.737102,-73.990318|40.749825,-73.987963|40.752946,-73.987384|40.755823,-73.986397&&sensor=false

$url = "http://maps.google.com/maps/api/staticmap" . "?zoom=16" . "&size=312x312" . "&maptype=roadmap&path=color:0x0000ff|weight:5" . $points . "&sensor=false";

function DMStoDEC($dms, $longlat) 
{
    
    $direction = substr($dms, -1);
    
    if ($longlat == 'lattitude') 
    {
        $deg = substr($dms, 0, 2);
        $min = substr($dms, 2, 8);
    }
    if ($longlat == 'longitude') 
    {
        $deg = substr($dms, 0, 3);
        $min = substr($dms, 3, 8);
    }
    
    //echo ConvertDMSToDD($deg, $min, $direction)."</br>";
    return ConvertDMSToDD($deg, $min, $direction);
}

function ConvertDMSToDD($days, $minutes, $direction) 
{
    $modifier = 1;
    switch ($direction) 
    {
    case 'N':
        $modifier = 1;
        break;

    case 'N':
        $modifier = - 1;
        break;

    case 'E':
        $modifier = 1;
        break;

    case 'W':
        $modifier = - 1;
        break;
    }
    $dd = $modifier * ($days + $minutes / 60);
    return $dd;
}
?>


<div class="col-sm-5 col-sm-offset-3">
<legend>My Run</legend>
<image src="<?php
echo $url
?>" alt="map" />
</div>

<?php
echo "<pre>";
print_r($one);
echo "</pre>";
?>