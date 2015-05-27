/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public final class BookMaker {

    private static Book convert(final String text) {
        final Book b = new Book();
        final List<String> ourPages = new ArrayList<>();
        boolean isBookText = false;
        String page = "";
        for (String line : text.split("\\r?\\n")) {
            if (line.contains("<book>")) {
                isBookText = true;
                line = line.replace("<book>", "");
            }
            if (isBookText) {
                if (line.contains("</book>")) {
                    break;
                }
                if (line.length() >= 2 && "//".equals(line.substring(0, 2))) {
                    continue;
                }
                if (line.contains("<author>")) {
                    b.setAuthor(line.replace("<author>", "").replace("</author>", ""));
                } else if (line.contains("<title>")) {
                    b.setTitle(line.replace("<title>", "").replace("</title>", ""));
                } else if (line.contains("<page>")) {
                    page = "";
                } else if (line.contains("</page>")) {
                    ourPages.add(page);
                } else {
                    page = page + line + "\n";
                }
            }
        }
        b.setPages(ourPages);
        return b;
    }

    public static ItemStack createBook(final String text) {
        final Book b = BookMaker.convert(text);
        String title = b.title;
        final String author = b.author;
        final List<String> pages = b.pages;
        if (title == null || author == null || pages == null) return null;
        final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        final BookMeta bm = (BookMeta) book.getItemMeta();
        bm.setTitle(b.title);
        bm.setAuthor(b.author);
        bm.setPages(b.pages);
        book.setItemMeta(bm);
        return book;
    }

    private static class Book {

        private String title;
        private String author;
        private List<String> pages;

        private Book() {
            this.title = null;
            this.author = null;
            this.pages = null;
        }

        public void setAuthor(final String author) {
            this.author = author;
        }

        public void setPages(final List<String> pages) {
            this.pages = pages;
        }

        public void setTitle(final String title) {
            if (title.length() > 16) this.title = title.substring(0, 16);
            else this.title = title;
        }
    }
}

