package com.github.underscore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

class XmlBuilderTest {
    private static final String XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<Projects>\n"
                    + "  <java-xmlbuilder language=\"Java\" scm=\"SVN\">\n"
                    + "    <Location type=\"URL\">http://code.google.com/p/java-xmlbuilder/</Location>\n"
                    + "  </java-xmlbuilder>\n"
                    + "  <JetS3t language=\"Java\" scm=\"CVS\">\n"
                    + "    <Location type=\"URL\">http://jets3t.s3.amazonaws.com/index.html</Location>\n"
                    + "  </JetS3t>\n"
                    + "</Projects>";

    @Test
    void createXml() {
        XmlBuilder xmlBuilder =
                XmlBuilder.create("Projects")
                        .e("java-xmlbuilder")
                        .a("language", "Java")
                        .a("scm", "SVN")
                        .e("Location")
                        .a("type", "URL")
                        .t("http://code.google.com/p/java-xmlbuilder/")
                        .up()
                        .up()
                        .e("JetS3t")
                        .a("language", "Java")
                        .a("scm", "CVS")
                        .e("Location")
                        .a("type", "URL")
                        .t("http://jets3t.s3.amazonaws.com/index.html");
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
    void xml() {
        XmlBuilder xmlBuilder = XmlBuilder.parse(XML);
        assertEquals(XML, xmlBuilder.xml());
    }

    @Test
    void json() {
        XmlBuilder xmlBuilder = XmlBuilder.parse(XML);
        assertEquals(U.xmlToJson(XML), xmlBuilder.json());
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
