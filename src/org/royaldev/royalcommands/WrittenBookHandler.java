package org.royaldev.royalcommands;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles written books. It allows one to set/get the author, name, and text of the book.
 * <p/>
 * It must be constructed with a CraftItemStack of a Written Book. It does not need to have content:
 * <pre>new WrittenBookHandler(new CraftItemStack(Material.WRITTEN_BOOK));</pre>
 *
 * @author jkcclemens
 * @since 0.2.5pre
 */
public class WrittenBookHandler {

    private List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<String>();
        int len = string.length();
        for (int i = 0; i < len; i += partitionSize)
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        return parts;
    }

    private NBTTagList pages;

    private String title;
    private String author;

    private int amount;
    private short damage;
    private byte data;

    /**
     * Pass a signed/written book to this and you will be able to extract needed data.
     * <p/>
     * Note also that a newly formed CraftItemStack containing a written book can be used to create a new book with contents.
     *
     * @param i CraftItemStack containing written book
     * @throws IllegalArgumentException If ItemStack wasn't a written book
     * @throws NullPointerException     If ItemStack was null
     */
    public WrittenBookHandler(CraftItemStack i) throws IllegalArgumentException, NullPointerException {
        if (i == null) throw new NullPointerException("ItemStack cannot be null!");
        if (!i.getType().equals(Material.WRITTEN_BOOK))
            throw new IllegalArgumentException("ItemStack was not a written book!");
        NBTTagCompound tag = i.getHandle().getTag();
        if (tag == null) tag = new NBTTagCompound();
        pages = tag.getList("pages");
        title = tag.getString("title");
        author = tag.getString("author");

        amount = i.getAmount();
        damage = i.getDurability();
        data = i.getData().getData();
    }

    /**
     * If you happen to catch the signing packet (how in blazes), you can use this.
     *
     * @param tag NBTTagCompound containing written book data
     * @throws NullPointerException If tag is null
     */
    public WrittenBookHandler(NBTTagCompound tag) throws NullPointerException {
        if (tag == null) throw new NullPointerException("Tag cannot be null!");
        pages = tag.getList("pages");
        title = tag.getString("title");
        author = tag.getString("author");
        amount = 1;
        damage = (short) 0;
        data = (byte) 0;
    }

    /**
     * Gets the author of the book.
     *
     * @return Author's name or null if no author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the title of the book.
     *
     * @return Book's title or null if no name
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets all the pages in a list of strings.
     *
     * @return List of pages or null
     */
    public List<String> getPages() {
        if (pages == null) return null;
        List<String> toRet = new ArrayList<String>();
        for (int i = 0; i < pages.size(); i++) {
            NBTBase b = pages.get(i);
            if (b == null || !(b instanceof NBTTagString)) continue;
            NBTTagString s = (NBTTagString) b;
            toRet.add(s.data);
        }
        return toRet;
    }

    /**
     * Gets the whole book in full text. This joins the output of {@link #getPages()}.
     *
     * @return String contents of book or null
     */
    public String getFullText() {
        List<String> ps = getPages();
        if (ps == null) return null;
        StringBuilder sb = new StringBuilder();
        for (String page : ps) {
            sb.append(page);
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * Gets the book with any changes made via this API.
     *
     * @return New book
     */
    public CraftItemStack getBook() {
        CraftItemStack book = new CraftItemStack(Material.WRITTEN_BOOK, amount, damage, data);
        NBTTagCompound bookTag = new NBTTagCompound(title);
        bookTag.setString("author", author);
        bookTag.setString("title", title);
        bookTag.set("pages", pages);
        book.getHandle().setTag(bookTag);
        return book;
    }

    /**
     * Sets the author of the book.
     * <p/>
     * Use {@link #getBook()} to get the book with changes.
     *
     * @param s New author
     * @throws NullPointerException If new author is null
     */
    public void setAuthor(String s) throws NullPointerException {
        if (s == null) throw new NullPointerException("New author cannot be null!");
        author = s;
    }

    /**
     * Sets the title of the book.
     * <p/>
     * Use {@link #getBook()} to get the book with changes.
     *
     * @param s New title
     * @throws NullPointerException If new title is null
     */
    public void setTitle(String s) throws NullPointerException {
        if (s == null) throw new NullPointerException("New title cannot be null!");
        title = s;
    }

    /**
     * Sets the text of the book.
     * <p/>
     * Use {@link #getBook()} to get the book with changes.
     *
     * @param s New text
     * @throws NullPointerException If new text is null
     */
    public void setText(String s) throws NullPointerException {
        if (s == null) throw new NullPointerException("New text cannot be null!");
        NBTTagList nbttl = new NBTTagList();
        List<String> ps = getParts(s, 256);
        for (int i = 0; i < ps.size(); i++) {
            NBTTagString nbtts = new NBTTagString(String.valueOf(i), ps.get(i));
            nbttl.add(nbtts);
        }
        pages = nbttl;
    }

}
