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
implementation 'com.smparkworld.parkwork:parkwork:1.1.0'
</pre>

## Example
- String request
<pre>                      
HashMap&lt;String, String&gt; strData = new HashMap<>();
strData.put("exampleKey", "exampleData");

ParkWork.create(this, "")   // Enter the Request-URI.
        .setOnResponseListener(new ParkWork.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                // Enter the code to process after receiving data from the server.
            }
            @Override
            public void onError(String errorMessage) {
                // Enter the code to process after receiving error message from the ParkWork library.
            }
        })
        .setStringData(strData)
        .start();

// PHP ///////////////////////////////////////
&lt;?php
    $data = $_POST[exampleKey];

    // Enter the code to process after receiving data from the device.
    $response = $data." from server.";
    
    echo $response;
?&gt;
</pre>
<br> 

- Image Uploading
<pre>
HashMap&lt;String, String&gt; imgData = new HashMap<>();
imgData.put("exampleKey", "");  // Enter the image URI of Content-path or Absolute-path.

ParkWork.create(this, "")   // Enter the Request-URI.
        .setOnResponseListener(new ParkWork.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                // Enter the code to process after receiving data from the server.
            }
            @Override
            public void onError(String errorMessage) {
                // Enter the code to process after receiving error message from the ParkWork library.
            }
        })
        .setImageData(imgData)
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

- String request and Image uploading
<pre>
HashMap&lt;String, String&gt; strData = new HashMap<>();
strData.put("exampleKey", "exampleData");

HashMap&lt;String, String&gt; imgData = new HashMap<>();
imgData.put("exampleKey", "");  // Enter the image URI of Content-path or Absolute-path.

ParkWork.create(this, "")   // Please enter the URI.
        .setOnResponseListener(new ParkWork.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                // Enter the code to process after receiving data from the server.
            }
            @Override
            public void onError(String errorMessage) {
                // Enter the code to process after receiving error message from the ParkWork library.
            }
        })
        .setStringData(strData)
        .setImageData(imgData)
        .start();
// PHP ///////////////////////////////////////
&lt;?php
    $textData = $_GET[exampleKey];
    
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
<br>

- Receive progress info
<pre>
HashMap&lt;String, String&gt; imgData = new HashMap<>();
imgData.put("exampleKey", "");  // Enter the image URI of Content-path or Absolute-path.

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
        .setOnProgressUpdateListener(new ParkWork.OnProgressUpdateListener() {
            @Override
            public void onProgressUpdate(int progress) {
                // The percentage of progress is returned as a parameter.
                // For example, if you upload 4 images, 25 is returned.
            }
        })
        .setImageData(imgData)
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
