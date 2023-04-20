package com.github.underscore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class XmlBuilderTest {
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
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<Projects>\n"
                        + "  <java-xmlbuilder language=\"Java\" scm=\"SVN\">\n"
                        + "    <Location type=\"URL\">http://code.google.com/p/java-xmlbuilder/</Location>\n"
                        + "  </java-xmlbuilder>\n"
                        + "  <JetS3t language=\"Java\" scm=\"CVS\">\n"
                        + "    <Location type=\"URL\">http://jets3t.s3.amazonaws.com/index.html</Location>\n"
                        + "  </JetS3t>\n"
                        + "</Projects>",
                xmlBuilder.xml());
    }
}
