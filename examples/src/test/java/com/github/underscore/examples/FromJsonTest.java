/*
 * The MIT License (MIT)
 *
 * Copyright 2015 Valentyn Kolesnikov
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

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Underscore examples unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class FromJsonTest {

    @Test
    public void fromJson() {
        String string =
        "{\n  \"glossary\": {\n    \"title\": \"example glossary\",\n    \"GlossDiv\": {\n      \"title\":"
        + " \"S\",\n      \"GlossList\": {\n        \"GlossEntry\": {\n          \"ID\": \"SGML\",\n"
        + "          \"SortAs\": \"SGML\",\n          \"GlossTerm\": \"Standard Generalized Markup Language\",\n"
        + "          \"Acronym\": \"SGML\",\n          \"Abbrev\": \"ISO 8879:1986\",\n          \"GlossDef\": {\n"
        + "            \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n"
        + "            \"GlossSeeAlso\": [\n              \"GML\",\n              \"XML\"\n            ]\n"
        + "          },\n          \"GlossSee\": \"markup\"\n        }\n      }\n    }\n  }\n}";
        assertEquals("{glossary={title=example glossary, GlossDiv={title=S, GlossList={GlossEntry="
        + "{ID=SGML, SortAs=SGML, GlossTerm=Standard Generalized Markup Language, Acronym=SGML,"
        + " Abbrev=ISO 8879:1986, GlossDef={para=A meta-markup language, used to create markup"
        + " languages such as DocBook., GlossSeeAlso=[GML, XML]}, GlossSee=markup}}}}}",
        FromJson.fromJson(string).toString());
    }
}
