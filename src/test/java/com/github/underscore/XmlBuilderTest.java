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
    void parse() {
        XmlBuilder xmlBuilder = XmlBuilder.parse(XML);
        assertEquals(XML, xmlBuilder.asString());
    }

    @Test
    void root() {
        XmlBuilder xmlBuilder = new XmlBuilder(null);
        assertTrue(xmlBuilder.root() instanceof Document);
    }

    @Test
    void rootError() {
        class XmlBuilderCustom extends XmlBuilder {
            public XmlBuilderCustom(String rootName) {
                super(rootName);
            }

            @Override
            String createXml() {
                return "[\"abc\u0010\"]";
            }
        }
        XmlBuilder xmlBuilder = new XmlBuilderCustom(null);
        assertThrows(IllegalArgumentException.class, xmlBuilder::root);
    }

    @Test
    void getDocument() {
        XmlBuilder xmlBuilder = new XmlBuilder(null);
        assertTrue(xmlBuilder.getDocument() instanceof Document);
    }
}
