/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2025 Valentyn Kolesnikov
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"java:S3740", "java:S3776"})
public final class Json {

    private static final int PARSE_MAX_DEPTH = 10_000;

    private Json() {}

    private static final String NULL = "null";
    private static final String DIGIT = "digit";

    public static class JsonStringBuilder {
        public enum Step {
            TWO_SPACES(2),
            THREE_SPACES(3),
            FOUR_SPACES(4),
            COMPACT(0),
            TABS(1);
            private final int indent;

            Step(int indent) {
                this.indent = indent;
            }

            public int getIndent() {
                return indent;
            }
        }

        private final StringBuilder builder;
        private final Step identStep;
        private int indent;

        public JsonStringBuilder(Step identStep) {
            builder = new StringBuilder();
            this.identStep = identStep;
        }

        public JsonStringBuilder() {
            builder = new StringBuilder();
            this.identStep = Step.TWO_SPACES;
        }

        public JsonStringBuilder append(final char character) {
            builder.append(character);
            return this;
        }

        public JsonStringBuilder append(final String string) {
            builder.append(string);
            return this;
        }

        public JsonStringBuilder fillSpaces() {
            builder.append(
                    String.valueOf(identStep == Step.TABS ? '\t' : ' ')
                            .repeat(Math.max(0, indent)));
            return this;
        }

        public JsonStringBuilder incIndent() {
            indent += identStep.getIndent();
            return this;
        }

        public JsonStringBuilder decIndent() {
            indent -= identStep.getIndent();
            return this;
        }

        public JsonStringBuilder newLine() {
            if (identStep != Step.COMPACT) {
                builder.append('\n');
            }
            return this;
        }

        public Step getIdentStep() {
            return identStep;
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static class JsonArray {
        private JsonArray() {}

        public static void writeJson(Collection collection, JsonStringBuilder builder) {
            if (collection == null) {
                builder.append(NULL);
                return;
            }
            Iterator iter = collection.iterator();
            builder.append('[').incIndent();
            if (!collection.isEmpty()) {
                builder.newLine();
            }
            while (iter.hasNext()) {
                Object value = iter.next();
                builder.fillSpaces();
                JsonValue.writeJson(value, builder);
                if (iter.hasNext()) {
                    builder.append(',').newLine();
                }
            }
            builder.newLine().decIndent().fillSpaces().append(']');
        }

        public static void writeJson(byte[] byteArray, JsonStringBuilder builder) {
            if (byteArray == null) {
                builder.append(NULL);
            } else if (byteArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIndent().newLine();
                builder.fillSpaces().append(String.valueOf(byteArray[0]));
                for (int i = 1; i < byteArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(byteArray[i]));
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }

        public static void writeJson(short[] shortArray, JsonStringBuilder builder) {
            if (shortArray == null) {
                builder.append(NULL);
            } else if (shortArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIndent().newLine();
                builder.fillSpaces().append(String.valueOf(shortArray[0]));
                for (int i = 1; i < shortArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(shortArray[i]));
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }

        public static void writeJson(int[] intArray, JsonStringBuilder builder) {
            if (intArray == null) {
                builder.append(NULL);
            } else if (intArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIndent().newLine();
                builder.fillSpaces().append(String.valueOf(intArray[0]));
                for (int i = 1; i < intArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(intArray[i]));
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }

        public static void writeJson(long[] longArray, JsonStringBuilder builder) {
            if (longArray == null) {
                builder.append(NULL);
            } else if (longArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIndent().newLine();
                builder.fillSpaces().append(String.valueOf(longArray[0]));
                for (int i = 1; i < longArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(longArray[i]));
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }

        public static void writeJson(float[] floatArray, JsonStringBuilder builder) {
            if (floatArray == null) {
                builder.append(NULL);
            } else if (floatArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIndent().newLine();
                builder.fillSpaces().append(String.valueOf(floatArray[0]));
                for (int i = 1; i < floatArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(floatArray[i]));
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }

        public static void writeJson(double[] doubleArray, JsonStringBuilder builder) {
            if (doubleArray == null) {
                builder.append(NULL);
            } else if (doubleArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIndent().newLine();
                builder.fillSpaces().append(String.valueOf(doubleArray[0]));
                for (int i = 1; i < doubleArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(doubleArray[i]));
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }

        public static void writeJson(boolean[] booleanArray, JsonStringBuilder builder) {
            if (booleanArray == null) {
                builder.append(NULL);
            } else if (booleanArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIndent().newLine();
                builder.fillSpaces().append(String.valueOf(booleanArray[0]));
                for (int i = 1; i < booleanArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append(String.valueOf(booleanArray[i]));
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }

        public static void writeJson(char[] charArray, JsonStringBuilder builder) {
            if (charArray == null) {
                builder.append(NULL);
            } else if (charArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').incIndent().newLine();
                builder.fillSpaces().append('\"').append(String.valueOf(charArray[0])).append('\"');
                for (int i = 1; i < charArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    builder.append('"').append(String.valueOf(charArray[i])).append('"');
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }

        public static void writeJson(Object[] objectArray, JsonStringBuilder builder) {
            if (objectArray == null) {
                builder.append(NULL);
            } else if (objectArray.length == 0) {
                builder.append("[]");
            } else {
                builder.append('[').newLine().incIndent().fillSpaces();
                JsonValue.writeJson(objectArray[0], builder);
                for (int i = 1; i < objectArray.length; i++) {
                    builder.append(',').newLine().fillSpaces();
                    JsonValue.writeJson(objectArray[i], builder);
                }
                builder.newLine().decIndent().fillSpaces().append(']');
            }
        }
    }

    public static class JsonObject {
        private JsonObject() {}

        public static void writeJson(Map map, JsonStringBuilder builder) {
            if (map == null) {
                builder.append(NULL);
                return;
            }
            Iterator iter = map.entrySet().iterator();
            builder.append('{').incIndent();
            if (!map.isEmpty()) {
                builder.newLine();
            }
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                builder.fillSpaces().append('"');
                builder.append(JsonValue.escape(String.valueOf(entry.getKey())));
                builder.append('"');
                builder.append(':');
                if (builder.getIdentStep() != JsonStringBuilder.Step.COMPACT) {
                    builder.append(' ');
                }
                JsonValue.writeJson(entry.getValue(), builder);
                if (iter.hasNext()) {
                    builder.append(',').newLine();
                }
            }
            builder.newLine().decIndent().fillSpaces().append('}');
        }
    }

    public static class JsonValue {
        private JsonValue() {}

        public static void writeJson(Object value, JsonStringBuilder builder) {
            if (value == null) {
                builder.append(NULL);
            } else if (value instanceof String) {
                builder.append('"').append(escape((String) value)).append('"');
            } else if (value instanceof Double) {
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
            } else if (value instanceof Map) {
                JsonObject.writeJson((Map) value, builder);
            } else if (value instanceof Collection) {
                JsonArray.writeJson((Collection) value, builder);
            } else {
                doWriteJson(value, builder);
            }
        }

        private static void doWriteJson(Object value, JsonStringBuilder builder) {
            if (value instanceof byte[]) {
                JsonArray.writeJson((byte[]) value, builder);
            } else if (value instanceof short[]) {
                JsonArray.writeJson((short[]) value, builder);
            } else if (value instanceof int[]) {
                JsonArray.writeJson((int[]) value, builder);
            } else if (value instanceof long[]) {
                JsonArray.writeJson((long[]) value, builder);
            } else if (value instanceof float[]) {
                JsonArray.writeJson((float[]) value, builder);
            } else if (value instanceof double[]) {
                JsonArray.writeJson((double[]) value, builder);
            } else if (value instanceof boolean[]) {
                JsonArray.writeJson((boolean[]) value, builder);
            } else if (value instanceof char[]) {
                JsonArray.writeJson((char[]) value, builder);
            } else if (value instanceof Object[]) {
                JsonArray.writeJson((Object[]) value, builder);
            } else {
                builder.append('"').append(escape(value.toString())).append('"');
            }
        }

        public static String escape(String inputString) {
            if (inputString == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            escape(inputString, sb);
            return sb.toString();
        }

        private static void escape(String inputString, StringBuilder sb) {
            final int len = inputString.length();
            for (int i = 0; i < len; i++) {
                char ch = inputString.charAt(i);
                switch (ch) {
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '€':
                        sb.append('€');
                        break;
                    default:
                        if (ch <= '\u001F'
                                || ch >= '\u007F' && ch <= '\u009F'
                                || ch >= '\u2000' && ch <= '\u20FF') {
                            String ss = Integer.toHexString(ch);
                            sb.append("\\u");
                            sb.append("0".repeat(4 - ss.length()));
                            sb.append(ss.toUpperCase());
                        } else {
                            sb.append(ch);
                        }
                        break;
                }
            }
        }
    }

    public static class ParseException extends RuntimeException {
        private final int offset;
        private final int line;
        private final int column;

        public ParseException(String message, int offset, int line, int column) {
            super(String.format("%s at %d:%d", message, line, column));
            this.offset = offset;
            this.line = line;
            this.column = column;
        }

        public int getOffset() {
            return offset;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }
    }

    public static class JsonParser {
        private final String json;
        private int index;
        private int line;
        private int lineOffset;
        private int current;
        private final StringBuilder captureBuffer = new StringBuilder();
        private int captureStart;
        private final int maxDepth;

        public JsonParser(String string, int maxDepth) {
            this.json = string;
            this.maxDepth = maxDepth;
            line = 1;
            captureStart = -1;
        }

        public Object parse() {
            read();
            skipWhiteSpace();
            final Object result = readValue(0);
            skipWhiteSpace();
            if (!isEndOfText()) {
                throw error("Unexpected character");
            }
            return result;
        }

        private Object readValue(int depth) {
            if (depth > maxDepth) {
                throw error("Maximum depth exceeded");
            }
            switch (current) {
                case 'n':
                    return readNull();
                case 't':
                    return readTrue();
                case 'f':
                    return readFalse();
                case '"':
                    return readString();
                case '[':
                    return readArray(depth + 1);
                case '{':
                    return readObject(depth + 1);
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return readNumber();
                default:
                    throw expected("value");
            }
        }

        private List<Object> readArray(int depth) {
            read();
            List<Object> array = new ArrayList<>();
            skipWhiteSpace();
            if (readChar(']')) {
                return array;
            }
            do {
                skipWhiteSpace();
                array.add(readValue(depth));
                skipWhiteSpace();
            } while (readChar(','));
            if (!readChar(']')) {
                throw expected("',' or ']'");
            }
            return array;
        }

        private Map<String, Object> readObject(int depth) {
            read();
            Map<String, Object> object = new LinkedHashMap<>();
            skipWhiteSpace();
            if (readChar('}')) {
                return object;
            }
            do {
                skipWhiteSpace();
                String name = readName();
                skipWhiteSpace();
                if (!readChar(':')) {
                    throw expected("':'");
                }
                skipWhiteSpace();
                object.put(name, readValue(depth));
                skipWhiteSpace();
            } while (readChar(','));
            if (!readChar('}')) {
                throw expected("',' or '}'");
            }
            return object;
        }

        private String readName() {
            if (current != '"') {
                throw expected("name");
            }
            return readString();
        }

        private String readNull() {
            read();
            readRequiredChar('u');
            readRequiredChar('l');
            readRequiredChar('l');
            return null;
        }

        private Boolean readTrue() {
            read();
            readRequiredChar('r');
            readRequiredChar('u');
            readRequiredChar('e');
            return Boolean.TRUE;
        }

        private Boolean readFalse() {
            read();
            readRequiredChar('a');
            readRequiredChar('l');
            readRequiredChar('s');
            readRequiredChar('e');
            return Boolean.FALSE;
        }

        private void readRequiredChar(char ch) {
            if (!readChar(ch)) {
                throw expected("'" + ch + "'");
            }
        }

        private String readString() {
            read();
            startCapture();
            while (current != '"') {
                if (current == '\\') {
                    pauseCapture();
                    readEscape();
                    startCapture();
                } else if (current < 0x20) {
                    throw expected("valid string character");
                } else {
                    read();
                }
            }
            String string = endCapture();
            read();
            return string;
        }

        private void readEscape() {
            read();
            switch (current) {
                case '"':
                case '/':
                case '\\':
                    captureBuffer.append((char) current);
                    break;
                case 'b':
                    captureBuffer.append('\b');
                    break;
                case 'f':
                    captureBuffer.append('\f');
                    break;
                case 'n':
                    captureBuffer.append('\n');
                    break;
                case 'r':
                    captureBuffer.append('\r');
                    break;
                case 't':
                    captureBuffer.append('\t');
                    break;
                case 'u':
                    char[] hexChars = new char[4];
                    boolean isHexCharsDigits = true;
                    for (int i = 0; i < 4; i++) {
                        read();
                        if (!isHexDigit()) {
                            isHexCharsDigits = false;
                        }
                        hexChars[i] = (char) current;
                    }
                    if (isHexCharsDigits) {
                        captureBuffer.append((char) Integer.parseInt(new String(hexChars), 16));
                    } else {
                        captureBuffer
                                .append("\\u")
                                .append(hexChars[0])
                                .append(hexChars[1])
                                .append(hexChars[2])
                                .append(hexChars[3]);
                    }
                    break;
                default:
                    throw expected("valid escape sequence");
            }
            read();
        }

        private Number readNumber() {
            startCapture();
            readChar('-');
            int firstDigit = current;
            if (!readDigit()) {
                throw expected(DIGIT);
            }
            if (firstDigit != '0') {
                while (readDigit()) {
                    // ignored
                }
            }
            readFraction();
            readExponent();
            final String number = endCapture();
            final Number result;
            if (number.contains(".") || number.contains("e") || number.contains("E")) {
                if (number.length() > 9
                        || (number.contains(".") && number.length() - number.lastIndexOf('.') > 2)
                                && number.charAt(number.length() - 1) == '0') {
                    result = new java.math.BigDecimal(number);
                } else {
                    result = Double.valueOf(number);
                }
            } else {
                if (number.length() > 19) {
                    result = new java.math.BigInteger(number);
                } else {
                    result = Long.valueOf(number);
                }
            }
            return result;
        }

        private void readFraction() {
            if (!readChar('.')) {
                return;
            }
            if (!readDigit()) {
                throw expected(DIGIT);
            }
            while (readDigit()) {
                // ignored
            }
        }

        private void readExponent() {
            if (!readChar('e') && !readChar('E')) {
                return;
            }
            if (!readChar('+')) {
                readChar('-');
            }
            if (!readDigit()) {
                throw expected(DIGIT);
            }
            while (readDigit()) {
                // ignored
            }
        }

        private boolean readChar(char ch) {
            if (current != ch) {
                return false;
            }
            read();
            return true;
        }

        private boolean readDigit() {
            if (!isDigit()) {
                return false;
            }
            read();
            return true;
        }

        private void skipWhiteSpace() {
            while (isWhiteSpace()) {
                read();
            }
        }

        private void read() {
            if (index == json.length()) {
                current = -1;
                return;
            }
            if (current == '\n') {
                line++;
                lineOffset = index;
            }
            current = json.charAt(index++);
        }

        private void startCapture() {
            captureStart = index - 1;
        }

        private void pauseCapture() {
            captureBuffer.append(json, captureStart, index - 1);
            captureStart = -1;
        }

        private String endCapture() {
            int end = current == -1 ? index : index - 1;
            String captured;
            if (captureBuffer.length() > 0) {
                captureBuffer.append(json, captureStart, end);
                captured = captureBuffer.toString();
                captureBuffer.setLength(0);
            } else {
                captured = json.substring(captureStart, end);
            }
            captureStart = -1;
            return captured;
        }

        private ParseException expected(String expected) {
            if (isEndOfText()) {
                return error("Unexpected end of input");
            }
            return error("Expected " + expected);
        }

        private ParseException error(String message) {
            int absIndex = index;
            int column = absIndex - lineOffset;
            int offset = isEndOfText() ? absIndex : absIndex - 1;
            return new ParseException(message, offset, line, column - 1);
        }

        private boolean isWhiteSpace() {
            return current == ' ' || current == '\t' || current == '\n' || current == '\r';
        }

        private boolean isDigit() {
            return current >= '0' && current <= '9';
        }

        private boolean isHexDigit() {
            return isDigit()
                    || current >= 'a' && current <= 'f'
                    || current >= 'A' && current <= 'F';
        }

        private boolean isEndOfText() {
            return current == -1;
        }
    }

    public static String toJson(Collection collection, JsonStringBuilder.Step identStep) {
        final JsonStringBuilder builder = new JsonStringBuilder(identStep);
        JsonArray.writeJson(collection, builder);
        return builder.toString();
    }

    public static String toJson(Collection collection) {
        return toJson(collection, JsonStringBuilder.Step.TWO_SPACES);
    }

    public static String toJson(Map map, JsonStringBuilder.Step identStep) {
        final JsonStringBuilder builder = new JsonStringBuilder(identStep);
        JsonObject.writeJson(map, builder);
        return builder.toString();
    }

    public static String toJson(Map map) {
        return toJson(map, JsonStringBuilder.Step.TWO_SPACES);
    }

    public static Object fromJson(String string) {
        return fromJson(string, PARSE_MAX_DEPTH);
    }

    public static Object fromJson(String string, int maxDepth) {
        return new JsonParser(string, maxDepth).parse();
    }

    public static String formatJson(String json, JsonStringBuilder.Step identStep) {
        Object result = fromJson(json);
        if (result instanceof Map) {
            return toJson((Map) result, identStep);
        }
        return toJson((List) result, identStep);
    }

    public static String formatJson(String json) {
        return formatJson(json, JsonStringBuilder.Step.TWO_SPACES);
    }
}
