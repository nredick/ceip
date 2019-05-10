<html>
<head>
    <title>Analyze Image Sample</title>
</head>
	<body>
<?php
	$remote = "https://redbuddevelopment.com/wp-content/uploads/2016/03/DSC_0019.jpg";
	$local = base64_encode(file_get_contents('images-1.jpeg'));
	//azure computer vision subscription key
$ocpApimSubscriptionKey = '9f7acebb2f2548e280fce69f75cf8151';
	//initialize a curl command sequence
$ch = curl_init();
	//set curl POST options
	//the url to send the request to with parameters for the api response
curl_setopt($ch, CURLOPT_URL, 'https://eastus.api.cognitive.microsoft.com/vision/v2.0/analyze?visualFeatures=Categories,Description&details=Landmarks&language=en');
	//receive a response from the api = true
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	//pass an image url to the POST request 
//curl_setopt($ch, CURLOPT_POSTFIELDS, "{\"url\":\"".$remote."\"}");
curl_setopt($ch, CURLOPT_POSTFIELDS, $local);
	//request type is POST	
curl_setopt($ch, CURLOPT_POST, true);
	//create an array of request headers 
$headers = array();
$headers[] = 'Ocp-Apim-Subscription-Key:'.$ocpApimSubscriptionKey;
$headers[] = 'Content-Type: application/json';
	//pass headers to curl command 
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
	//execute the POST request
$result = curl_exec($ch);
if(curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}
	//close the curl command 
curl_close ($ch);
	

	//if the result isn't null, parse the response
if($result != null){
	$response = json_decode($result, true);
	$response = (array)$response;
	foreach($response as $key => $value){ 
		echo ucwords($key).": ";
			foreach((array)$value as $x => $z){
				if($key=="categories"){ //categories
					echo "<br>";
					foreach((array)$z as $a => $b){
						if(!is_array($b)){
							echo "&nbsp&nbsp&nbsp&nbsp".ucwords($a).": ".$b."<br>";
						}						
					}
				}if($key=="description"){//tags
					echo "<br>&nbsp&nbsp&nbsp&nbsp".ucwords($x).": ";
					foreach((array)$z as $a => $b){
						if($x=="tags"){
							if(!is_array($b)){
								echo $b;
								if($a!=count($z)-1){
									echo ", ";
								}
							}	
						}if($x=="captions"){//captions
							foreach((array)$z as $c => $d){
								foreach((array)$d as $s => $t){
									echo "<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp".ucwords($s).": ".$t;	
								}	
								echo "<br>";
							}			
						}
					}
				}if($key=="requestId"){
					foreach((array)$z as $c => $d){
						echo $d."<br>";
						}
					}
				}if($key=="metadata"){
					echo "<br>";
					foreach((array)$value as $key => $val){
						echo "&nbsp&nbsp&nbsp&nbsp".ucwords($key).": ".$val."<br>";
					}
				}
			}
		}	
	echo "<pre>".json_encode($response, JSON_PRETTY_PRINT)."</pre>";	
?>
</body>
</html>
