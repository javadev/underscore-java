/*
 * The MIT License (MIT)
 *
 * Copyright 2017 Valentyn Kolesnikov
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
package com.github.underscore.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Entry unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class EntryTest {

    private Entry entry = new Entry("name", null) {
        public long size() {
            return 0L;
        }
    };

    private Entry entryWithParent = new Entry("name", new Directory("name", null)) {
        public long size() {
            return 0L;
        }
    };

    @Test
    public void entry() {
        assertEquals("name", entry.getName());
    }

    @Test
    public void getFullPath() {
        assertEquals("name", entry.getFullPath());
        assertEquals("name/name", entryWithParent.getFullPath());
    }

    @Test
    public void delete() {
        assertFalse(entry.delete());
        assertFalse(entryWithParent.delete());
    }

    @Test
    public void getCreationTime() {
        assertTrue(entry.getCreationTime() > 0);
    }

    @Test
    public void changeNameAndGetName() {
        entry.changeName("New");
        assertEquals("New", entry.getName());
    }

    @Test
    public void setLastUpdatedAndGetLastUpdated() {
        entry.setLastUpdated(1L);
        assertEquals(1L, entry.getLastUpdated());
    }

    @Test
    public void setLastAccessedAndGetLastAccessed() {
        entry.setLastAccessed(1L);
        assertEquals(1L, entry.getLastAccessed());
    }
}
