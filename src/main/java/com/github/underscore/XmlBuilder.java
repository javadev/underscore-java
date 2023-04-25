package com.github.underscore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XmlBuilder {
    private static final String SELF_CLOSING = "-self-closing";
    private static final String TRUE = "true";
    private final Map<String, Object> data;
    private String path;
    private String savedPath;

    XmlBuilder(String rootName) {
        data = new LinkedHashMap<>();
        Map<String, Object> value = new LinkedHashMap<>();
        value.put(SELF_CLOSING, TRUE);
        data.put(rootName, value);
        path = rootName;
    }

    public static XmlBuilder create(String rootName) {
        return new XmlBuilder(rootName);
    }

    public static XmlBuilder parse(String xml) {
        Map<String, Object> xmlData = U.fromXmlMap(xml);
        XmlBuilder xmlBuilder = new XmlBuilder(Xml.XmlValue.getMapKey(xmlData));
        xmlBuilder.setData(xmlData);
        return xmlBuilder;
    }

    @SuppressWarnings("unchecked")
    public XmlBuilder e(String elementName) {
        U.remove(data, path + "." + SELF_CLOSING);
        Map<String, Object> value = new LinkedHashMap<>();
        value.put(SELF_CLOSING, TRUE);
        Object object = U.get(data, path + "." + elementName);
        if (object instanceof Map) {
            List<Object> list = new ArrayList<>(Arrays.asList(object, value));
            U.set(data, path + "." + elementName, list);
            path += "." + elementName + ".1";
            savedPath = path;
        } else if (object instanceof List) {
            path += "." + elementName + "." + ((List<Object>) object).size();
            savedPath = path;
            ((List<Object>) object).add(value);
        } else {
            U.set(data, path + "." + elementName, value);
            path += "." + elementName;
        }
        return this;
    }

    public XmlBuilder a(String attributeName, String value) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.set(data, path + ".-" + attributeName, value);
        return this;
    }

    public XmlBuilder c(String comment) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.update(data, path + ".#comment", comment);
        return this;
    }

    public XmlBuilder d(String cdata) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.update(data, path + ".#cdata-section", cdata);
        return this;
    }

    public XmlBuilder t(String text) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.update(data, path + ".#text", text);
        return this;
    }

    public XmlBuilder up() {
        if (path.equals(savedPath)) {
            path = path.substring(0, path.lastIndexOf("."));
        }
        path = path.substring(0, path.lastIndexOf("."));
        return this;
    }

    public XmlBuilder root() {
        int index = path.indexOf(".");
        XmlBuilder xmlBuilder = new XmlBuilder(index == -1 ? path : path.substring(0, index));
        xmlBuilder.setData(data);
        return xmlBuilder;
    }

    public org.w3c.dom.Document getDocument() {
        try {
            return Xml.Document.createDocument(asString());
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public XmlBuilder set(final String path, final Object value) {
        U.set(data, path, value);
        return this;
    }

    public XmlBuilder remove(final String key) {
        U.remove(data, key);
        return this;
    }

    public XmlBuilder clear() {
        data.clear();
        return this;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public String asString() {
        return U.toXml(data);
    }

    public String toXml() {
        return U.toXml(data);
    }

    public String toJson() {
        return U.toJson(data);
    }

    private void setData(Map<String, Object> newData) {
        data.clear();
        data.putAll(newData);
    }
}
