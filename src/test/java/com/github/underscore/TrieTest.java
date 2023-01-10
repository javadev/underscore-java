/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2023 Valentyn Kolesnikov
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
class TrieTest {

    @Test
    void startsWith() {
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("apple");
        assertTrue(trie.startsWith("app"));
        assertFalse(trie.startsWith("b"));
    }

    @Test
    void startsWith2() {
        Trie trie = new Trie();
        trie.insert("тест");
        trie.insert("тест");
        assertTrue(trie.startsWith("те"));
        assertFalse(trie.startsWith("с"));
    }

    @Test
    void startsWith3() {
        Trie trie = new Trie();
        trie.insert("0123");
        trie.insert("0123");
        assertTrue(trie.startsWith("012"));
        assertFalse(trie.startsWith("3"));
    }
}
