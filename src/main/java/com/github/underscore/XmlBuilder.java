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

    public XmlBuilder e(String elementName) {
        Object object = U.get(data, path);
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

    public String xml() {
        return U.toXml(data);
    }
}
