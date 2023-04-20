package com.github.underscore;

import java.util.LinkedHashMap;
import java.util.Map;

public class XmlBuilder {
    private static final String SELF_CLOSING = "-self-closing";
    private static final String TRUE = "true";
    private final Map<String, Object> data;
    private String path;

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

    public XmlBuilder e(String elementName) {
        U.remove(data, path + "." + SELF_CLOSING);
        Map<String, Object> value = new LinkedHashMap<>();
        value.put(SELF_CLOSING, TRUE);
        U.set(data, path + "." + elementName, value);
        path += "." + elementName;
        return this;
    }

    public XmlBuilder a(String attributeName, String value) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.set(data, path + ".-" + attributeName, value);
        return this;
    }

    public XmlBuilder t(String text) {
        U.set(data, path + ".#text", text);
        return this;
    }

    public XmlBuilder up() {
        path = path.substring(0, path.lastIndexOf("."));
        return this;
    }

    public org.w3c.dom.Document root() {
        try {
            return Xml.Document.createDocument(createXml());
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public org.w3c.dom.Document getDocument() {
        return root();
    }

    String createXml() {
        return U.toXml(data);
    }

    public String xml() {
        return U.toXml(data);
    }

    private void setData(Map<String, Object> newData) {
        data.clear();
        data.putAll(newData);
    }
}
