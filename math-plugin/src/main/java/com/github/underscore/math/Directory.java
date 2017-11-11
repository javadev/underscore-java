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

import java.util.ArrayList;
import java.util.List;

public class Directory extends Entry {
    private final List<Entry> contents;

    public Directory(String name, Directory directory) {
        super(name, directory);
        contents = new ArrayList<Entry>();
    }

    protected List<Entry> getContents() {
        return contents;
    }

    public long size() {
        long size = 0;
        for (final Entry entry : contents) {
            size += entry.size();
        }
        return size;
    }

    public long numberOfFiles() {
        long count = 0;
        for (final Entry entry : contents) {
            if (entry instanceof Directory) {
                count += 1;
                Directory directory = (Directory) entry;
                count += directory.numberOfFiles();
            } else {
                count += 1;
            }
        }
        return count;
    }

    public boolean deleteEntry(Entry entry) {
        return contents.remove(entry);
    }

    public void addEntry(Entry entry) {
        contents.add(entry);
    }
}
