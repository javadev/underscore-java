/*
 * The MIT License (MIT)
 *
 * Copyright 2017-2018 Valentyn Kolesnikov
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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Directory unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class DirectoryTest {

    private Directory directory = new Directory("name", null);

    @Test
    public void size() {
        directory.addEntry(new File("name", null, 10L));
        assertEquals(10L, directory.size());
    }

    @Test
    public void numberOfFiles() {
        directory.addEntry(new Directory("name", null));
        directory.addEntry(new File("name", null, 10L));
        directory.getContents();
        assertEquals(1L, directory.numberOfFiles() - 1);
    }
}
