/*
 * The MIT License (MIT)
 *
 * Copyright 2023-2025 Valentyn Kolesnikov <0009-0003-9608-3364@orcid.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.underscore;

import java.util.ArrayList;
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
            List<Object> list = new ArrayList<>();
            list.add(object);
            list.add(value);
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

    public XmlBuilder i(String target, String value) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.set(data, "?" + target, value);
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

    public XmlBuilder importXmlBuilder(XmlBuilder xmlBuilder) {
        data.putAll(xmlBuilder.data);
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

    public Map<String, Object> build() {
        return U.deepCopyMap(data);
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

    public String toXml(Xml.XmlStringBuilder.Step identStep) {
        return Xml.toXml(data, identStep);
    }

    public String toXml() {
        return U.toXml(data);
    }

    public String toJson(Json.JsonStringBuilder.Step identStep) {
        return Json.toJson(data, identStep);
    }

    public String toJson() {
        return U.toJson(data);
    }

    private void setData(Map<String, Object> newData) {
        data.clear();
        data.putAll(newData);
    }
}
