<html>
<head>
    <title>Analyze Image Sample</title>
</head>
<body>
<?php

$ocpApimSubscriptionKey = '9f7acebb2f2548e280fce69f75cf8151';
$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, 'https://eastus.api.cognitive.microsoft.com/vision/v2.0/analyze?visualFeatures=Categories,Description&details=Landmarks&language=en');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, "{\"url\":\"http://upload.wikimedia.org/wikipedia/commons/3/3c/Shaki_waterfall.jpg\"}");
curl_setopt($ch, CURLOPT_POST, 1);

$headers = array();
$headers[] = 'Ocp-Apim-Subscription-Key:'.$ocpApimSubscriptionKey;
$headers[] = 'Content-Type: application/json';
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

$result = curl_exec($ch);
if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}
curl_close ($ch);
//echo $result;
	
$response = json_decode($result, true);
	echo "<pre>" .
    json_encode($response, JSON_PRETTY_PRINT) . "</pre>";	
?>
</body>
</html>