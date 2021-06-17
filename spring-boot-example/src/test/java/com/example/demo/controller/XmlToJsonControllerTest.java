package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertSame;

import com.github.underscore.lodash.U;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class XmlToJsonControllerTest {
    private XmlToJsonController testObj = new XmlToJsonController();

    @Test
    void xmltojson() {
        ResponseEntity<Map<String, Object>> result =
                testObj.xmltojson(U.objectBuilder().add("xml", "<a/>").build());
        assertSame(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void xmltojsonError() {
        ResponseEntity<Map<String, Object>> result =
                testObj.xmltojson(U.objectBuilder().add("xml", "<a/>1").build());
        assertSame(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void formatxml() {
        ResponseEntity<Map<String, Object>> result =
                testObj.formatxml(U.objectBuilder().add("xml", "<a/>").build());
        assertSame(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void formatxmlError() {
        ResponseEntity<Map<String, Object>> result =
                testObj.formatxml(U.objectBuilder().add("xml", "<a/>1").build());
        assertSame(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void jsontoxml() {
        ResponseEntity<Map<String, Object>> result =
                testObj.jsontoxml(U.objectBuilder().add("json", "{\"a\":1}").build());
        assertSame(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void jsontoxmlError() {
        ResponseEntity<Map<String, Object>> result =
                testObj.jsontoxml(U.objectBuilder().add("json", "{\"a\":1}1").build());
        assertSame(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void formatjson() {
        ResponseEntity<Map<String, Object>> result =
                testObj.formatjson(U.objectBuilder().add("json", "{\"a\":1}").build());
        assertSame(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void formatjsonError() {
        ResponseEntity<Map<String, Object>> result =
                testObj.formatjson(U.objectBuilder().add("json", "{\"a\":1}1").build());
        assertSame(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
