/*
 * The MIT License (MIT)
 *
 * Copyright 2023-2024 Valentyn Kolesnikov
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

/** The type Xml builder. */
public class XmlBuilder {
    private static final String SELF_CLOSING = "-self-closing";
    private static final String TRUE = "true";
    private final Map<String, Object> data;
    private String path;
    private String savedPath;

    /**
     * Instantiates a new Xml builder.
     *
     * @param rootName the root name
     */
    XmlBuilder(String rootName) {
        data = new LinkedHashMap<>();
        Map<String, Object> value = new LinkedHashMap<>();
        value.put(SELF_CLOSING, TRUE);
        data.put(rootName, value);
        path = rootName;
    }

    /**
     * Create xml builder.
     *
     * @param rootName the root name
     * @return the xml builder
     */
    public static XmlBuilder create(String rootName) {
        return new XmlBuilder(rootName);
    }

    /**
     * Parse xml builder.
     *
     * @param xml the xml
     * @return the xml builder
     */
    public static XmlBuilder parse(String xml) {
        Map<String, Object> xmlData = U.fromXmlMap(xml);
        XmlBuilder xmlBuilder = new XmlBuilder(Xml.XmlValue.getMapKey(xmlData));
        xmlBuilder.setData(xmlData);
        return xmlBuilder;
    }

    /**
     * E xml builder.
     *
     * @param elementName the element name
     * @return the xml builder
     */
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

    /**
     * A xml builder.
     *
     * @param attributeName the attribute name
     * @param value the value
     * @return the xml builder
     */
    public XmlBuilder a(String attributeName, String value) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.set(data, path + ".-" + attributeName, value);
        return this;
    }

    /**
     * C xml builder.
     *
     * @param comment the comment
     * @return the xml builder
     */
    public XmlBuilder c(String comment) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.update(data, path + ".#comment", comment);
        return this;
    }

    /**
     * xml builder.
     *
     * @param target the target
     * @param value the value
     * @return the xml builder
     */
    public XmlBuilder i(String target, String value) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.set(data, "?" + target, value);
        return this;
    }

    /**
     * D xml builder.
     *
     * @param cdata the cdata
     * @return the xml builder
     */
    public XmlBuilder d(String cdata) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.update(data, path + ".#cdata-section", cdata);
        return this;
    }

    /**
     * T xml builder.
     *
     * @param text the text
     * @return the xml builder
     */
    public XmlBuilder t(String text) {
        U.remove(data, path + "." + SELF_CLOSING);
        U.update(data, path + ".#text", text);
        return this;
    }

    /**
     * Import xml builder xml builder.
     *
     * @param xmlBuilder the xml builder
     * @return the xml builder
     */
    public XmlBuilder importXmlBuilder(XmlBuilder xmlBuilder) {
        data.putAll(xmlBuilder.data);
        return this;
    }

    /**
     * Up xml builder.
     *
     * @return the xml builder
     */
    public XmlBuilder up() {
        if (path.equals(savedPath)) {
            path = path.substring(0, path.lastIndexOf("."));
        }
        path = path.substring(0, path.lastIndexOf("."));
        return this;
    }

    /**
     * Root xml builder.
     *
     * @return the xml builder
     */
    public XmlBuilder root() {
        int index = path.indexOf(".");
        XmlBuilder xmlBuilder = new XmlBuilder(index == -1 ? path : path.substring(0, index));
        xmlBuilder.setData(data);
        return xmlBuilder;
    }

    /**
     * Gets document.
     *
     * @return the document
     */
    public org.w3c.dom.Document getDocument() {
        try {
            return Xml.Document.createDocument(asString());
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Set xml builder.
     *
     * @param path the path
     * @param value the value
     * @return the xml builder
     */
    public XmlBuilder set(final String path, final Object value) {
        U.set(data, path, value);
        return this;
    }

    /**
     * Remove xml builder.
     *
     * @param key the key
     * @return the xml builder
     */
    public XmlBuilder remove(final String key) {
        U.remove(data, key);
        return this;
    }

    /**
     * Build map.
     *
     * @return the map
     */
    public Map<String, Object> build() {
        return U.deepCopyMap(data);
    }

    /**
     * Clear xml builder.
     *
     * @return the xml builder
     */
    public XmlBuilder clear() {
        data.clear();
        return this;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return data.size();
    }

    /**
     * As string string.
     *
     * @return the string
     */
    public String asString() {
        return U.toXml(data);
    }

    /**
     * To xml string.
     *
     * @param identStep the ident step
     * @return the string
     */
    public String toXml(Xml.XmlStringBuilder.Step identStep) {
        return Xml.toXml(data, identStep);
    }

    /**
     * To xml string.
     *
     * @return the string
     */
    public String toXml() {
        return U.toXml(data);
    }

    /**
     * To json string.
     *
     * @param identStep the ident step
     * @return the string
     */
    public String toJson(Json.JsonStringBuilder.Step identStep) {
        return Json.toJson(data, identStep);
    }

    /**
     * To json string.
     *
     * @return the string
     */
    public String toJson() {
        return U.toJson(data);
    }

    private void setData(Map<String, Object> newData) {
        data.clear();
        data.putAll(newData);
    }
}
