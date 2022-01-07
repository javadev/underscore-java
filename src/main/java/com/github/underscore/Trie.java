/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2022 Valentyn Kolesnikov
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

public class Trie {
    public static class TrieNode {
        // Initialize your data structure here.
        TrieNode[] children;
        boolean isWord;

        public TrieNode() {
            children = new TrieNode[1071];
        }
    }

    private final TrieNode root;
    private boolean startWith;

    public Trie() {
        root = new TrieNode();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        insert(word, root, 0);
    }

    private void insert(String word, TrieNode root, int idx) {
        if (idx == word.length()) {
            root.isWord = true;
            return;
        }
        int index = word.charAt(idx) - ' ';
        if (root.children[index] == null) {
            root.children[index] = new TrieNode();
        }
        insert(word, root.children[index], idx + 1);
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        return search(word, root, 0);
    }

    public boolean search(String word, TrieNode root, int idx) {
        if (idx == word.length()) {
            startWith = true;
            return root.isWord;
        }
        int index = word.charAt(idx) - ' ';
        if (root.children[index] == null) {
            startWith = false;
            return false;
        }

        return search(word, root.children[index], idx + 1);
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        search(prefix);
        return startWith;
    }
}
