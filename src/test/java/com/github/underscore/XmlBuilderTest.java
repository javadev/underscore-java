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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

class XmlBuilderTest {
    private static final String XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<Projects>\n"
                    + "  <underscore-java language=\"Java\" scm=\"SVN\">\n"
                    + "    <Location type=\"URL\">https://github.com/javadev/underscore-java/</Location>\n"
                    + "  </underscore-java>\n"
                    + "  <JetS3t language=\"Java\" scm=\"CVS\">\n"
                    + "    <Location type=\"URL\">https://jets3t.s3.amazonaws.com/index.html</Location>\n"
                    + "  </JetS3t>\n"
                    + "</Projects>";

    @Test
    void createXml() {
        XmlBuilder xmlBuilder =
                XmlBuilder.create("Projects")
                        .e("underscore-java")
                        .a("language", "Java")
                        .a("scm", "SVN")
                        .e("Location")
                        .a("type", "URL")
                        .t("https://github.com/javadev/underscore-java/")
                        .up()
                        .up()
                        .e("JetS3t")
                        .a("language", "Java")
                        .a("scm", "CVS")
                        .e("Location")
                        .a("type", "URL")
                        .t("https://jets3t.s3.amazonaws.com/index.html");
        assertEquals(XML, xmlBuilder.asString());
    }

    @Test
    void comment() {
        XmlBuilder xmlBuilder = XmlBuilder.create("Projects").c("text");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<Projects>\n"
                        + "  <!--text-->\n"
                        + "</Projects>",
                xmlBuilder.asString());
    }

    @Test
    void inst() {
        XmlBuilder xmlBuilder = XmlBuilder.create("Projects").i("target", "text");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<Projects></Projects>\n"
                        + "<?target text?>",
                xmlBuilder.asString());
    }

    @Test
    void importXmlBuilder() {
        XmlBuilder xmlBuilder = XmlBuilder.create("a");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <b/>\n"
                        + "  <a/>\n"
                        + "</root>",
                XmlBuilder.create("b").importXmlBuilder(xmlBuilder).asString());
    }

    @Test
    void cdata() {
        XmlBuilder xmlBuilder = XmlBuilder.create("Projects").d("text");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<Projects><![CDATA[text]]></Projects>",
                xmlBuilder.asString());
    }

    @Test
    void parse() {
        XmlBuilder xmlBuilder = XmlBuilder.parse(XML);
        assertEquals(XML, xmlBuilder.asString());
    }

    @Test
    void getDocument() {
        XmlBuilder xmlBuilder = new XmlBuilder(null);
        assertTrue(xmlBuilder.getDocument() instanceof Document);
    }

    @Test
    void getDocumentError() {
        class XmlBuilderCustom extends XmlBuilder {
            public XmlBuilderCustom(String rootName) {
                super(rootName);
            }

            @Override
            public String asString() {
                return "[\"abc\u0010\"]";
            }
        }
        XmlBuilder xmlBuilder = new XmlBuilderCustom(null);
        assertThrows(IllegalArgumentException.class, xmlBuilder::getDocument);
    }

    @Test
    void set() {
        XmlBuilder xmlBuilder = new XmlBuilder("json");
        xmlBuilder.set("xml", "123");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <json/>\n"
                        + "  <xml>123</xml>\n"
                        + "</root>",
                xmlBuilder.asString());
    }

    @Test
    void remove() {
        XmlBuilder xmlBuilder = new XmlBuilder("root").e("123");
        xmlBuilder.remove("root.123");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>", xmlBuilder.asString());
    }

    @Test
    void build() {
        XmlBuilder xmlBuilder = new XmlBuilder("xml").e("123");
        assertEquals("{xml={123={-self-closing=true}}}", xmlBuilder.build().toString());
    }

    @Test
    void clear() {
        XmlBuilder xmlBuilder = new XmlBuilder("xml").e("123");
        xmlBuilder.clear();
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>", xmlBuilder.asString());
    }

    @Test
    void isEmpty() {
        XmlBuilder xmlBuilder = new XmlBuilder("xml").e("123");
        assertFalse(xmlBuilder.isEmpty());
        xmlBuilder.clear();
        assertTrue(xmlBuilder.isEmpty());
    }

    @Test
    void size() {
        XmlBuilder xmlBuilder = new XmlBuilder("xml").e("123");
        assertEquals(1, xmlBuilder.size());
    }

    @Test
    void root() {
        XmlBuilder xmlBuilder = new XmlBuilder("root");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>",
                xmlBuilder.root().asString());
        XmlBuilder xmlBuilder2 = new XmlBuilder("root").e("e");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <e/>\n"
                        + "</root>",
                xmlBuilder2.root().asString());
    }

    @Test
    void xmlBuilderArray() {
        String lines = "1,name1,department1\n2,name2,department2\n3,name3,department3";
        XmlBuilder xmlBuilder = XmlBuilder.create("Employees");
        for (String line : lines.split("\n")) {
            String[] columns = line.split(",");
            xmlBuilder
                    .e("Employee")
                    .e("Code")
                    .t(columns[0])
                    .up()
                    .e("Name")
                    .t(columns[1])
                    .up()
                    .e("Department")
                    .t(columns[2])
                    .up()
                    .up();
        }
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<Employees>\n"
                        + "  <Employee>\n"
                        + "    <Code>1</Code>\n"
                        + "    <Name>name1</Name>\n"
                        + "    <Department>department1</Department>\n"
                        + "  </Employee>  <Employee>\n"
                        + "    <Code>2</Code>\n"
                        + "    <Name>name2</Name>\n"
                        + "    <Department>department2</Department>\n"
                        + "  </Employee>  <Employee>\n"
                        + "    <Code>3</Code>\n"
                        + "    <Name>name3</Name>\n"
                        + "    <Department>department3</Department>\n"
                        + "  </Employee>\n"
                        + "</Employees>",
                xmlBuilder.asString());
    }

    @Test
    void toXmlFormat() {
        XmlBuilder xmlBuilder = new XmlBuilder("xml").e("a").e("b");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><a><b/></a></xml>",
                xmlBuilder.toXml(Xml.XmlStringBuilder.Step.COMPACT));
    }

    @Test
    void toXml() {
        XmlBuilder xmlBuilder = XmlBuilder.parse(XML);
        assertEquals(XML, xmlBuilder.toXml());
    }

    @Test
    void toJsonFormat() {
        XmlBuilder xmlBuilder = new XmlBuilder("json").e("a").e("b");
        assertEquals(
                "{\"json\":{\"a\":{\"b\":{\"-self-closing\":\"true\"}}}}",
                xmlBuilder.toJson(Json.JsonStringBuilder.Step.COMPACT));
    }

    @Test
    void toJson() {
        XmlBuilder xmlBuilder = XmlBuilder.parse(XML);
        assertEquals(U.xmlToJson(XML), xmlBuilder.toJson());
    }

    @Test
    void bug21() {
        XmlBuilder xmlBuilder = XmlBuilder.create("root");
        xmlBuilder.e("g1").t("Styles like ").e("g2").t("bold").up().t("is supported");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<root>\n"
                        + "  <g1>Styles like <g2>bold</g2>is supported</g1>\n"
                        + "</root>",
                xmlBuilder.asString());
    }
}
