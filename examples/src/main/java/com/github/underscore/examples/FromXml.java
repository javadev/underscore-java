/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2018 Valentyn Kolesnikov
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
package com.github.underscore.examples;

import java.util.*;

/**
 * Examples for underscore-java.
 *
 * @author Valentyn Kolesnikov
 */
public class FromXml {
    @SuppressWarnings("unchecked")
    private static Object getValue(final Object value) {
        if (value instanceof Map && ((Map<String, Object>) value).entrySet().size() == 1) {
            final Map.Entry<String, Object> entry = ((Map<String, Object>) value).entrySet().iterator().next();
            if (entry.getKey().equals("#text") || entry.getKey().equals("element")) {
                return entry.getValue();
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> createMap(final org.w3c.dom.Node node) {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        final org.w3c.dom.NodeList nodeList = node.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            final org.w3c.dom.Node currentNode = nodeList.item(index);
            final String name = currentNode.getNodeName();
            final Object value;
            if (currentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                value = createMap(currentNode);
            } else {
                value = currentNode.getTextContent();
            }
            if (name.equals("#text") && value.toString().startsWith("\n")) {
                continue;
            }
            if (map.containsKey(name)) {
                final Object object = map.get(name);
                if (object instanceof List) {
                    ((List<Object>) object).add(getValue(value));
                } else {
                    final List<Object> objects = new ArrayList<Object>();
                    objects.add(object);
                    objects.add(getValue(value));
                    map.put(name, objects);
                }
            } else {
                map.put(name, getValue(value));
            }
        }
        return map;
    }

    public static Object fromXml(final String xml) {
        try {
            final java.io.InputStream stream = new java.io.ByteArrayInputStream(xml.getBytes("UTF-8"));
            final javax.xml.parsers.DocumentBuilderFactory factory =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            final org.w3c.dom.Document document = factory.newDocumentBuilder().parse(stream);
            return createMap(document.getDocumentElement());
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
