package com.example.acpcw1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestController
public class Controller {

    private final Map<String, String> store = Collections.synchronizedMap(new HashMap<>());

    @GetMapping("/uuid")
    public String uuid() {
        return "<!DOCTYPE html>" + "<html>" + "<head>"+
                "<meta charset='utf-8'>"+
                "<title>Page title</title>"+
                "</head> "+ "<body>" + "<h1>s2693586</h1>" + "</body>" + "</html>";
    }

    //valuemanager?key=keyToUse&value=valueToWrite
    @PostMapping("/valuemanager")
    public ResponseEntity<Void> postValueManager1(@RequestParam String key, @RequestParam(required = false) String value) {
        if (key == null || key.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if key is null or empty
        }
        if (value == null || value.trim().isEmpty()) {
            store.put(key, null);
        } else {
            store.put(key, value);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    //valuemanager/key/valueToWrite
//    @PostMapping("/valuemanager/{key}/{value}")
//    public ResponseEntity<Void> postValueManager2(@PathVariable(required = false) String key, @PathVariable(required = false) String value) {
//        if (key == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        store.put(key, value);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @PostMapping("/valuemanager/{key}/{value}")
    public ResponseEntity<Void> postValueManager(@PathVariable String key, @PathVariable(required = false) String value) {
        if (key == null || key.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if key is null or empty
        }

        // Store the key-value pair, allowing a null value
        if (value == null || value.trim().isEmpty()) {
            store.put(key, null);
        }else{
            store.put(key, value);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
        // Return 200 OK
    }

    @PostMapping(path = {"/valuemanager/{key}", "/valuemanager/{key}/"})
    public ResponseEntity<Void> postValueManagerWithNullValue(@PathVariable String key) {
        if (key == null || key.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if key is null or empty
        }

        // Store the key with a null value
        store.put(key, null);
        return ResponseEntity.status(HttpStatus.OK).build(); // Return 200 OK
    }

    @DeleteMapping("/valuemanager/{key}")
    public ResponseEntity<Void> deleteValueManager(@PathVariable String key) {
        if (store.containsKey(key)) {
            store.remove(key);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping({ "/valuemanager", "/valuemanager/","/valuemanager/{key}"})
    public ResponseEntity<?> getValue(@PathVariable(value = "key", required = false) String key) {
//        System.out.println(key);
        if (key == null || key.trim().isEmpty()) {
            return ResponseEntity.ok(store);
        }

        if (store.containsKey(key)) {
            return ResponseEntity.status(HttpStatus.OK).body(store.get(key));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/callservice")
    public ResponseEntity<?> postCallService(@RequestBody Map<String, String> body) {
        String externalBaseUrl = body.get("externalBaseUrl");
        String parameters = body.get("parameters");

        if (externalBaseUrl == null) {
            return ResponseEntity.badRequest().body("Missing externalBaseUrl");
        }

//        String regex = "[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\\-._~:/?#\\[\\]@!$&'()*+,;=]+";

        String url = externalBaseUrl + "/" + parameters;

//        if (url.matches(regex)) {

            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                MediaType mediaType = response.getHeaders().getContentType();
                HttpStatus status = (HttpStatus) response.getStatusCode();
                String responseBody = response.getBody();
                return ResponseEntity.status(status).contentType(mediaType != null ? mediaType : MediaType.TEXT_PLAIN).body(responseBody);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
    }

}
