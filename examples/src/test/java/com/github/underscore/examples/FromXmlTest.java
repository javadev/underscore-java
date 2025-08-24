/*
 * The MIT License (MIT)
 *
 * Copyright 2015 Valentyn Kolesnikov <0009-0003-9608-3364@orcid.org>
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.junit.jupiter.api.Test;

/**
 * Underscore examples unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class FromXmlTest {

    @Test
    public void fromXml() {
        String string =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "\n"
                        + "\n<root>"
                        + "\n<Details>"
                        + "\n    <detail-a>"
                        + "\n"
                        + "\n        <detail> attribute 1 of detail a </detail>"
                        + "\n        <detail> attribute 2 of detail a </detail>"
                        + "\n        <detail> attribute 3 of detail a </detail>"
                        + "\n"
                        + "\n    </detail-a>"
                        + "\n"
                        + "\n    <detail-b>"
                        + "\n        <detail> attribute 1 of detail b </detail>"
                        + "\n        <detail> attribute 2 of detail b </detail>"
                        + "\n"
                        + "\n    </detail-b>"
                        + "\n"
                        + "\n"
                        + "\n</Details>"
                        + "\n</root>";
        assertEquals(
                "{Details={detail-a={detail=[ attribute 1 of detail a ,  attribute 2 of detail a ,  attribute 3 of detail a ]}, "
                        + "detail-b={detail=[ attribute 1 of detail b ,  attribute 2 of detail b ]}}}",
                FromXml.fromXml(string).toString());
    }
}
