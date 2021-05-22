package com.example.demo.controller;

import com.github.underscore.lodash.U;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/")
@RestController
class XmlToJsonController {
    @PostMapping("xmltojson")
    ResponseEntity<Map<String, Object>> xmltojson(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("json", U.xmlToJson((String) request.get("xml")));
        } catch (Exception ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("formatxml")
    ResponseEntity<Map<String, Object>> formatxml(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("xml", U.formatXml((String) request.get("xml")));
        } catch (Exception ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("jsontoxml")
    ResponseEntity<Map<String, Object>> jsontoxml(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("xml", U.jsonToXml((String) request.get("json")));
        } catch (Exception ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("formatjson")
    ResponseEntity<Map<String, Object>> formatjson(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("json", U.formatJson((String) request.get("json")));
        } catch (Exception ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
}
