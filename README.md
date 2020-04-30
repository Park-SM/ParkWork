# ParkWork
Android simple networking library.

## How to use
1. Add maven repository.
<pre>
maven {
    url 'https://dl.bintray.com/parksm/maven/'
}
</pre>
2. Add to build.gradle
<pre>
implementation 'com.smparkworld.parkwork:parkwork:1.0.0'
</pre>

## Example
- String request
<pre>
HashMap&lt;String, String&gt; outputData = new HashMap<>();
outputData.put("exampleKey", "exampleData");

ParkWork.create(this, "")   // Enter the Request-URI.
        .setListener(new ParkWork.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                // Enter the code to process after receiving data from the server.
            }
            @Override
            public void onError(String errorMessage) {
                // Enter the code to process after receiving error message from the ParkWork library.
            }
        })
        .setData(outputData)
        .start();

// PHP ///////////////////////////////////////
&lt;?php
    $data = $_REQUEST[exampleKey];

    // Enter the code to process after receiving data from the device.
    $response = $data." from server.";
    
    echo $response;
?&gt;
</pre>
<br> 

- Image Uploading
<pre>
HashMap&lt;String, String&gt; outputData = new HashMap<>();
outputData.put("exampleKey", "");  // Enter the image URI of Content-path or Absolute-path.

ParkWork.create(this, "")   // Enter the Request-URI.
        .setListener(new ParkWork.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                // Enter the code to process after receiving data from the server.
            }
            @Override
            public void onError(String errorMessage) {
                // Enter the code to process after receiving error message from the ParkWork library.
            }
        })
        .setData(outputData)
        .setType(ParkWork.WORK_IMAGE) // Default value is ParkWork.WORK_STRING
        .start();
        
// PHP ///////////////////////////////////////
&lt;?php
    $storage = "";  // Enter the directory path where the image will be saved
    if($_FILES["exampleKey"]["error"] == 0) {

        $fname = $_FILES["exampleKey"]["name"];
        if(!move_uploaded_file($_FILES["exampleKey"]["tmp_name"], $storage.$fname))
            exit("Error: Failed to upload image");

    } else {
        exit("Error: Not found image.");
    }

    echo("Success");
?&gt;
</pre>
<br>

- Get only response
<pre>
ParkWork.create(this, "")   // Enter the Request-URI.
        .setListener(new ParkWork.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                // Enter the code to process after receiving data from the server.
            }
            @Override
            public void onError(String errorMessage) {
                // Enter the code to process after receiving error message from the ParkWork library.
            }
        })
        .start();
</pre>

## License
<pre>
Copyright 2020 ParkSM

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.```
</pre>
