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
package com.github.underscore.lodash;

import com.github.underscore.Function;
import java.util.*;

public final class Xml {
    private static final String NULL = "null";
    private static final String ELEMENT_TEXT = "element";
    private static final String CDATA = "#cdata-section";
    private static final String COMMENT = "#comment";
    private static final String ENCODING = "#encoding";
    private static final String TEXT = "#text";
    private static final String ELEMENT = "<" + ELEMENT_TEXT + ">";
    private static final String CLOSED_ELEMENT = "</" + ELEMENT_TEXT + ">";
    private static final String EMPTY_ELEMENT = ELEMENT + CLOSED_ELEMENT;
    private static final String NULL_ELEMENT = ELEMENT + NULL + CLOSED_ELEMENT;
    private static final java.nio.charset.Charset UTF_8 = java.nio.charset.Charset.forName("UTF-8");
    private static final java.util.regex.Pattern ATTRS = java.util.regex.Pattern.compile(
        "((?:(?!\\s|=).)*)\\s*?=\\s*?[\"']?((?:(?<=\")(?:(?<=\\\\)\"|[^\"])*|(?<=')"
        + "(?:(?<=\\\\)'|[^'])*)|(?:(?!\"|')(?:(?!\\/>|>|\\s).)+))");

    public static class XmlStringBuilder {
        public enum Step {
            TWO_SPACES(2), THREE_SPACES(3), FOUR_SPACES(4), COMPACT(0), TABS(1);
            private int ident;
            Step(int ident) {
                this.ident = ident;
            }
            public int getIdent() {
                return ident;
            }
        }

        protected final StringBuilder builder;
        private final Step identStep;
        private int ident;

        public XmlStringBuilder() {
            builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n");
            identStep = Step.TWO_SPACES;
            ident = 2;
        }

        public XmlStringBuilder(StringBuilder builder, Step identStep, int ident) {
            this.builder = builder;
            this.identStep = identStep;
            this.ident = ident;
        }

        public XmlStringBuilder append(final String string) {
            builder.append(string);
            return this;
        }

        public XmlStringBuilder fillSpaces() {
            for (int index = 0; index < ident; index += 1) {
                builder.append(identStep == Step.TABS ? '\t' : ' ');
            }
            return this;
        }

        public XmlStringBuilder incIdent() {
            ident += identStep.getIdent();
            return this;
        }

        public XmlStringBuilder decIdent() {
            ident -= identStep.getIdent();
            return this;
        }

        public XmlStringBuilder newLine() {
            if (identStep != Step.COMPACT) {
                builder.append("\n");
            }
            return this;
        }

        public int getIdent() {
            return ident;
        }

        public Step getIdentStep() {
            return identStep;
        }

        public String toString() {
            return builder.toString() + "\n</root>";
        }
    }

    public static class XmlStringBuilderWithoutRoot extends XmlStringBuilder {
        public XmlStringBuilderWithoutRoot(XmlStringBuilder.Step identStep, String encoding) {
            super(new StringBuilder("<?xml version=\"1.0\" encoding=\""
                + U.escape(encoding) + "\"?>" + (identStep == Step.COMPACT ? "" : "\n")), identStep, 0);
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class XmlStringBuilderWithoutHeader extends XmlStringBuilder {
        public XmlStringBuilderWithoutHeader(XmlStringBuilder.Step identStep, int ident) {
            super(new StringBuilder(), identStep, ident);
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class XmlStringBuilderText extends XmlStringBuilderWithoutHeader {
        public XmlStringBuilderText(XmlStringBuilder.Step identStep, int ident) {
            super(identStep, ident);
        }
    }

    public static class XmlArray {
        public static void writeXml(Collection collection, String name, XmlStringBuilder builder,
            boolean parentTextFound, Set<String> namespaces) {
            if (collection == null) {
                builder.append(NULL);
                return;
            }

            if (name != null) {
                builder.fillSpaces().append("<").append(XmlValue.escapeName(name, namespaces)).append(">").incIdent();
                if (!collection.isEmpty()) {
                    builder.newLine();
                }
            }
            writeXml(collection, builder, name, parentTextFound, namespaces);
            if (name != null) {
                builder.decIdent().newLine().fillSpaces().append("</")
                        .append(XmlValue.escapeName(name, namespaces)).append(">");
            }
        }

        @SuppressWarnings("unchecked")
        private static void writeXml(Collection collection, XmlStringBuilder builder, String name,
            final boolean parentTextFound, Set<String> namespaces) {
            boolean localParentTextFound = parentTextFound;
            final List entries = U.newArrayList(collection);
            for (int index = 0; index < entries.size(); index += 1) {
                final Object value = entries.get(index);
                final boolean addNewLine = index < entries.size() - 1
                    && !String.valueOf(entries.get(index + 1)).startsWith("{" + TEXT);
                if (value == null) {
                    builder.fillSpaces()
                        .append("<" + (name == null ? ELEMENT_TEXT : XmlValue.escapeName(name, namespaces)) + ">"
                        + NULL + "</" + (name == null ? ELEMENT_TEXT : XmlValue.escapeName(name, namespaces)) + ">");
                } else {
                    if (value instanceof Map && ((Map) value).size() == 1) {
                        XmlObject.writeXml((Map) value, null, builder, localParentTextFound, namespaces);
                        if (String.valueOf(((Map.Entry) ((Map) value).entrySet().iterator()
                            .next()).getKey()).startsWith(TEXT)) {
                            localParentTextFound = true;
                            continue;
                        }
                    } else {
                        XmlValue.writeXml(value, name == null ? ELEMENT_TEXT : name, builder, localParentTextFound,
                            namespaces);
                    }
                    localParentTextFound = false;
                }
                if (addNewLine) {
                    builder.newLine();
                }
            }
        }

        public static void writeXml(byte[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(short[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(int[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(long[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(float[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(double[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(boolean[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(char[] array, XmlStringBuilder builder) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    builder.fillSpaces().append(ELEMENT);
                    builder.append(String.valueOf(array[i]));
                    builder.append(CLOSED_ELEMENT);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }

        public static void writeXml(Object[] array, String name, XmlStringBuilder builder, boolean parentTextFound,
                Set<String> namespaces) {
            if (array == null) {
                builder.fillSpaces().append(NULL_ELEMENT);
            } else if (array.length == 0) {
                builder.fillSpaces().append(EMPTY_ELEMENT);
            } else {
                for (int i = 0; i < array.length; i++) {
                    XmlValue.writeXml(array[i], name == null ? ELEMENT_TEXT : name, builder,
                        parentTextFound, namespaces);
                    if (i != array.length - 1) {
                        builder.newLine();
                    }
                }
            }
        }
    }

    public static class XmlObject {
        @SuppressWarnings("unchecked")
        public static void writeXml(Map map, String name, final XmlStringBuilder builder,
            final boolean parentTextFound, final Set<String> namespaces) {
            if (map == null) {
                XmlValue.writeXml(NULL, name, builder, false, namespaces);
                return;
            }

            final List<XmlStringBuilder> elems = U.newArrayList();
            final List<String> attrs = U.newArrayList();
            final XmlStringBuilder.Step identStep = builder.getIdentStep();
            final int ident = builder.getIdent() + (name == null ? 0 : builder.getIdentStep().getIdent());
            final List<Map.Entry> entries = U.newArrayList(map.entrySet());
            for (int index = 0; index < entries.size(); index += 1) {
                final Map.Entry entry = entries.get(index);
                final boolean addNewLine = index < entries.size() - 1
                    && !String.valueOf(entries.get(index + 1).getKey()).startsWith(TEXT);
                if (String.valueOf(entry.getKey()).startsWith("-") && !(entry.getValue() instanceof Map)
                    && !(entry.getValue() instanceof List)) {
                    attrs.add(" " + XmlValue.escapeName(String.valueOf(entry.getKey()).substring(1), namespaces)
                        + "=\"" + U.escape(String.valueOf(entry.getValue())) + "\"");
                    if (String.valueOf(entry.getKey()).startsWith("-xmlns:")) {
                        namespaces.add(String.valueOf(entry.getKey()).substring(7));
                    }
                } else if (U.escape(String.valueOf(entry.getKey())).startsWith(TEXT)) {
                    addText(entry, elems, identStep, ident);
                } else {
                    processElements(entry, identStep, ident, addNewLine, elems, namespaces);
                }
            }
            if (name != null) {
                if (!parentTextFound) {
                    builder.fillSpaces();
                }
                builder.append("<").append(XmlValue.escapeName(name, namespaces)).append(U.join(attrs, ""))
                        .append(">").incIdent();
                if (!elems.isEmpty() && !(elems.get(0) instanceof XmlStringBuilderText)) {
                    builder.newLine();
                }
            }
            for (XmlStringBuilder localBuilder1 : elems) {
                builder.append(localBuilder1.toString());
            }
            if (name != null) {
                builder.decIdent();
                if (!elems.isEmpty() && !(elems.get(elems.size() - 1) instanceof XmlStringBuilderText)) {
                    builder.newLine().fillSpaces();
                }
                builder.append("</").append(XmlValue.escapeName(name, namespaces)).append(">");
            }
        }

        private static void processElements(final Map.Entry entry, final XmlStringBuilder.Step identStep,
                final int ident, final boolean addNewLine, final List<XmlStringBuilder> elems,
                final Set<String> namespaces) {
            if (U.escape(String.valueOf(entry.getKey())).startsWith(COMMENT)) {
                addComment(entry, identStep, ident, addNewLine, elems, "<!--", "-->");
            } else if (U.escape(String.valueOf(entry.getKey())).startsWith(CDATA)) {
                addComment(entry, identStep, ident, addNewLine, elems, "<![CDATA[", "]]>");
            } else if (entry.getValue() instanceof List && !((List) entry.getValue()).isEmpty()) {
                addElements(identStep, ident, entry, namespaces, elems, addNewLine);
            } else {
                addElement(identStep, ident, entry, namespaces, elems, addNewLine);
            }
        }

        private static void addText(final Map.Entry entry, final List<XmlStringBuilder> elems,
                final XmlStringBuilder.Step identStep, final int ident) {
            if (entry.getValue() instanceof List) {
                for (Object value : (List) entry.getValue()) {
                    elems.add(new XmlStringBuilderText(identStep, ident).append(U.escape(String.valueOf(value))));
                }
            } else {
                elems.add(new XmlStringBuilderText(identStep, ident).append(
                        U.escape(String.valueOf(entry.getValue()))));
            }
        }

        private static void addElements(final XmlStringBuilder.Step identStep, final int ident, Map.Entry entry,
                Set<String> namespaces, final List<XmlStringBuilder> elems, final boolean addNewLine) {
            boolean parentTextFound = !elems.isEmpty() && elems.get(elems.size() - 1) instanceof XmlStringBuilderText;
            final XmlStringBuilder localBuilder;
            if (String.valueOf(((List) entry.getValue()).get(0)).startsWith("{" + TEXT)
                || String.valueOf(((List) entry.getValue()).get(((List) entry.getValue()).size() - 1))
                    .startsWith("{" + TEXT)) {
                    localBuilder = new XmlStringBuilderText(identStep, ident);
            } else {
                localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            }
            XmlArray.writeXml((List) entry.getValue(), localBuilder,
                    String.valueOf(entry.getKey()), parentTextFound, namespaces);
            if (addNewLine) {
                localBuilder.newLine();
            }
            elems.add(localBuilder);
        }

        private static void addElement(final XmlStringBuilder.Step identStep, final int ident, Map.Entry entry,
                Set<String> namespaces, final List<XmlStringBuilder> elems, final boolean addNewLine) {
            boolean parentTextFound = !elems.isEmpty() && elems.get(elems.size() - 1) instanceof XmlStringBuilderText;
            XmlStringBuilder localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            XmlValue.writeXml(entry.getValue(), String.valueOf(entry.getKey()),
                    localBuilder, parentTextFound, namespaces);
            if (addNewLine) {
                localBuilder.newLine();
            }
            elems.add(localBuilder);
        }

        private static void addComment(Map.Entry entry, XmlStringBuilder.Step identStep, int ident,
                boolean addNewLine, List<XmlStringBuilder> elems, String openElement, String closeElement) {
            if (entry.getValue() instanceof List) {
                for (Iterator iterator = ((List) entry.getValue()).iterator(); iterator.hasNext(); ) {
                    addCommentValue(identStep, ident, String.valueOf(iterator.next()),
                            iterator.hasNext() || addNewLine, elems, openElement, closeElement);
                }
            } else {
                addCommentValue(identStep, ident, String.valueOf(entry.getValue()), addNewLine, elems,
                        openElement, closeElement);
            }
        }

        private static void addCommentValue(XmlStringBuilder.Step identStep, int ident, String value,
                boolean addNewLine, List<XmlStringBuilder> elems, String openElement, String closeElement) {
            boolean parentTextFound = !elems.isEmpty() && elems.get(elems.size() - 1) instanceof XmlStringBuilderText;
            XmlStringBuilder localBuilder = new XmlStringBuilderWithoutHeader(identStep, ident);
            if (!parentTextFound) {
                localBuilder.fillSpaces();
            }
            localBuilder.append(openElement).append(value).append(closeElement);
            if (addNewLine) {
                localBuilder.newLine();
            }
            elems.add(localBuilder);
        }
    }

    public static class XmlValue {
        public static void writeXml(Object value, String name, XmlStringBuilder builder, boolean parentTextFound,
            Set<String> namespaces) {
            if (value instanceof Map) {
                XmlObject.writeXml((Map) value,  name, builder, parentTextFound, namespaces);
                return;
            }
            if (value instanceof Collection) {
                XmlArray.writeXml((Collection) value, name, builder, parentTextFound, namespaces);
                return;
            }
            if (!parentTextFound) {
                builder.fillSpaces();
            }
            builder.append("<" + XmlValue.escapeName(name, namespaces) + ">");
            if (value == null) {
                builder.append(NULL);
            } else if (value instanceof String) {
                builder.append(escape((String) value));
            } else {
                processArrays(value, builder, name, parentTextFound, namespaces);
            }
            builder.append("</" + XmlValue.escapeName(name, namespaces) + ">");
        }

        private static void processArrays(Object value, XmlStringBuilder builder, String name,
                boolean parentTextFound, Set<String> namespaces) {
            if (value instanceof Double) {
                if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Float) {
                if (((Float) value).isInfinite() || ((Float) value).isNaN()) {
                    builder.append(NULL);
                } else {
                    builder.append(value.toString());
                }
            } else if (value instanceof Number) {
                builder.append(value.toString());
            } else if (value instanceof Boolean) {
                builder.append(value.toString());
            } else if (value instanceof byte[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((byte[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof short[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((short[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else {
                processArrays2(value, builder, name, parentTextFound, namespaces);
            }
        }

        private static void processArrays2(Object value, XmlStringBuilder builder, String name,
                boolean parentTextFound, Set<String> namespaces) {
            if (value instanceof int[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((int[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof long[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((long[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof float[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((float[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof double[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((double[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof boolean[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((boolean[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof char[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((char[]) value, builder);
                builder.decIdent().newLine().fillSpaces();
            } else if (value instanceof Object[]) {
                builder.newLine().incIdent();
                XmlArray.writeXml((Object[]) value, name, builder, parentTextFound, namespaces);
                builder.decIdent().newLine().fillSpaces();
            } else {
                builder.append(value.toString());
            }
        }

        public static String escapeName(String name, Set<String> namespaces) {
            final int length = name.length();
            if (length == 0) {
                return "__EE__EMPTY__EE__";
            }
            final StringBuilder result = new StringBuilder();
            char ch = name.charAt(0);
            if (com.sun.org.apache.xerces.internal.util.XMLChar.isNameStart(ch) && ch != ':') {
                result.append(ch);
            } else {
                result.append("__").append(Base32.encode(Character.toString(ch))).append("__");
            }
            for (int i = 1; i < length; ++i) {
                ch = name.charAt(i);
                if (ch == ':' && ("xmlns".equals(name.substring(0, i))
                        || namespaces.contains(name.substring(0, i)))) {
                    result.append(ch);
                } else if (com.sun.org.apache.xerces.internal.util.XMLChar.isName(ch) && ch != ':') {
                    result.append(ch);
                } else {
                    result.append("__").append(Base32.encode(Character.toString(ch))).append("__");
                }
            }
            return result.toString();
        }

        public static String escape(String s) {
            if (s == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            escape(s, sb);
            return sb.toString();
        }

        private static void escape(String s, StringBuilder sb) {
            final int len = s.length();
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                switch (ch) {
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("'");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\t");
                    break;
                default:
                    if (ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F'
                        || ch >= '\u2000' && ch <= '\u20FF') {
                        String ss = Integer.toHexString(ch);
                        sb.append("&#x");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase()).append(";");
                    } else {
                        sb.append(ch);
                    }
                    break;
                }
            }
        }
    }

    public static String toXml(Collection collection, XmlStringBuilder.Step identStep) {
        final XmlStringBuilder builder = new XmlStringBuilderWithoutRoot(identStep, UTF_8.name());
        builder.append("<root>").incIdent();
        if (collection == null || !collection.isEmpty()) {
            builder.newLine();
        }
        XmlArray.writeXml(collection, null, builder, false, U.<String>newLinkedHashSet());
        return builder.newLine().append("</root>").toString();
    }

    public static String toXml(Collection collection) {
        return toXml(collection, XmlStringBuilder.Step.TWO_SPACES);
    }

    public static String toXml(Map map, XmlStringBuilder.Step identStep) {
        final XmlStringBuilder builder;
        final Map localMap;
        if (map != null && map.containsKey(ENCODING)) {
            builder = new XmlStringBuilderWithoutRoot(identStep, String.valueOf(map.get(ENCODING)));
            localMap = (Map) U.clone(map);
            localMap.remove(ENCODING);
        } else {
            builder = new XmlStringBuilderWithoutRoot(identStep, UTF_8.name());
            localMap = map;
        }
        if (localMap == null || localMap.size() != 1
            || (String.valueOf(((Map.Entry) localMap.entrySet().iterator().next()).getKey())).startsWith("-")
            || ((Map.Entry) localMap.entrySet().iterator().next()).getValue() instanceof List) {
            final String name;
            if (localMap != null && localMap.size() == 1
                && ((Map.Entry) localMap.entrySet().iterator().next()).getValue() instanceof List
                && !((List) ((Map.Entry) localMap.entrySet().iterator().next()).getValue()).isEmpty()) {
                name = String.valueOf(((Map.Entry) localMap.entrySet().iterator().next()).getKey());
            } else {
                name = "root";
            }
            XmlObject.writeXml(localMap, name, builder, false, U.<String>newLinkedHashSet());
        } else {
            XmlObject.writeXml(localMap, null, builder, false, U.<String>newLinkedHashSet());
        }
        return builder.toString();
    }

    public static String toXml(Map map) {
        return toXml(map, XmlStringBuilder.Step.TWO_SPACES);
    }

    @SuppressWarnings("unchecked")
    private static Object getValue(final Object value) {
        if (value instanceof Map && ((Map<String, Object>) value).entrySet().size() == 1) {
            final Map.Entry<String, Object> entry = ((Map<String, Object>) value).entrySet().iterator().next();
            if (TEXT.equals(entry.getKey()) || entry.getKey().equals(ELEMENT_TEXT)) {
                return entry.getValue();
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> createMap(final org.w3c.dom.Node node,
        final Function<Object, Object> nodeMapper, Map<String, Object> attrMap, int[] uniqueIds,
        final Function<Object, Object> valueMapper, String source, int[] sourceIndex) {
        final Map<String, Object> map = U.newLinkedHashMap();
        map.putAll(attrMap);
        final org.w3c.dom.NodeList nodeList = node.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            final org.w3c.dom.Node currentNode = nodeList.item(index);
            final String name = currentNode.getNodeName();
            final Object value;
            final int attributesLength = currentNode.getAttributes() == null
                    ? 0 : currentNode.getAttributes().getLength();
            if (currentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                sourceIndex[0] = source.indexOf("<" + name, sourceIndex[0]) + name.length() + 2;
                final Map<String, Object> attrMapLocal = U.newLinkedHashMap();
                if (attributesLength > 0) {
                    final java.util.regex.Matcher matcher = ATTRS.matcher(source.substring(
                        sourceIndex[0], source.indexOf(">", sourceIndex[0])));
                    while (matcher.find()) {
                        addNodeValue(attrMapLocal, '-' + matcher.group(1), matcher.group(2), nodeMapper, uniqueIds);
                    }
                }
                value = createMap(currentNode, nodeMapper, attrMapLocal, uniqueIds, valueMapper, source, sourceIndex);
            } else {
                if (COMMENT.equals(name)) {
                    sourceIndex[0] = source.indexOf("-->", sourceIndex[0]) + 3;
                } else if (CDATA.equals(name)) {
                    sourceIndex[0] = source.indexOf("]]>", sourceIndex[0]) + 3;
                }
                value = currentNode.getTextContent();
            }
            if (TEXT.equals(name) && valueMapper.apply(value).toString().isEmpty()) {
                continue;
            }
            addNodeValue(map, name, value, nodeMapper, uniqueIds);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static void addNodeValue(final Map<String, Object> map, final String name, final Object value,
            final Function<Object, Object> nodeMapper, int[] uniqueIds) {
        if (map.containsKey(name)) {
            if (TEXT.equals(name)) {
                map.put(name + uniqueIds[0], nodeMapper.apply(getValue(value)));
                uniqueIds[0] += 1;
            } else if (COMMENT.equals(name)) {
                map.put(name + uniqueIds[1], nodeMapper.apply(getValue(value)));
                uniqueIds[1] += 1;
            } else if (CDATA.equals(name)) {
                map.put(name + uniqueIds[2], nodeMapper.apply(getValue(value)));
                uniqueIds[2] += 1;
            } else {
                final Object object = map.get(name);
                if (object instanceof List) {
                    addText(map, name, (List<Object>) object, value);
                } else {
                    final List<Object> objects = U.newArrayList();
                    objects.add(object);
                    addText(map, name, objects, value);
                    map.put(name, objects);
                }
            }
        } else {
            map.put(name, nodeMapper.apply(getValue(value)));
        }
    }

    @SuppressWarnings("unchecked")
    private static void addText(final Map<String, Object> map, final String name, final List<Object> objects,
        final Object value) {
        int lastIndex = map.size() - 1;
        final int index = objects.size();
        while (true) {
            final Map.Entry lastElement = (Map.Entry) map.entrySet().toArray()[lastIndex];
            if (name.equals(String.valueOf(lastElement.getKey()))) {
                break;
            }
            final Map<String, Object> text = U.newLinkedHashMap();
            text.put(String.valueOf(lastElement.getKey()), map.remove(lastElement.getKey()));
            objects.add(index, text);
            lastIndex -= 1;
        }
        objects.add(getValue(value));
    }

    @SuppressWarnings("unchecked")
    public static Object fromXml(final String xml, final Function<Object, Object> valueMapper) {
        if (xml == null) {
            return null;
        }
        try {
            org.w3c.dom.Document document = createDocument(xml);
            final Object result = createMap(document, new Function<Object, Object>() {
                public Object apply(Object object) {
                    return object;
                }
            }, Collections.<String, Object>emptyMap(), new int[] {1, 1, 1}, valueMapper, xml, new int[] {0});
            if (document.getXmlEncoding() != null && !"UTF-8".equalsIgnoreCase(document.getXmlEncoding())) {
                ((Map) result).put(ENCODING, document.getXmlEncoding());
            } else if (((Map.Entry) ((Map) result).entrySet().iterator().next()).getKey().equals("root")
                && (((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue() instanceof List
                || ((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue() instanceof Map)) {
                return ((Map.Entry) ((Map) result).entrySet().iterator().next()).getValue();
            }
            return result;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static Object fromXml(final String xml) {
        return fromXml(xml, new Function<Object, Object>() {
                public Object apply(Object object) {
                    return String.valueOf(object).trim();
                }
        });
    }

    private static org.w3c.dom.Document createDocument(final String xml)
            throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException {
        final javax.xml.parsers.DocumentBuilderFactory factory =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
        final javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
        return builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
    }

    public static Object fromXmlMakeArrays(final String xml) {
        try {
            org.w3c.dom.Document document = createDocument(xml);
            return createMap(document, new Function<Object, Object>() {
                public Object apply(Object object) {
                    return object instanceof List ? object : U.newArrayList(Arrays.asList(object));
                }
            }, Collections.<String, Object>emptyMap(), new int[] {1, 1, 1},
            new Function<Object, Object>() {
                public Object apply(Object object) {
                    return String.valueOf(object).trim();
                }
        }, xml, new int[] {0});
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static String formatXml(String xml, XmlStringBuilder.Step identStep) {
        Object result = fromXml(xml);
        if (result instanceof Map) {
            return toXml((Map) result, identStep);
        }
        return toXml((List) result, identStep);
    }

    public static String formatXml(String xml) {
        return formatXml(xml, XmlStringBuilder.Step.THREE_SPACES);
    }
}
