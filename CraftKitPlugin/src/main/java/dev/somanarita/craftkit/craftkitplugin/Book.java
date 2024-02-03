package dev.somanarita.craftkit.craftkitplugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class Book
{
    String title;
    String author;
    String currentPage = "";
    int numPages = 0;
    int numLines = 0;

    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) book.getItemMeta();

    public String getTitle()
    {
        return title;
    }
    public String getAuthor()
    {
        return author;
    }
    public int getNumPages()
    {
        return numPages;
    }
    public ItemStack getItemStack() {
    	return book;
    }

    public void setTitle(String title)
    {
        this.title = title;
        bookMeta.setTitle(title);
    }
    public void setAuthor(String author)
    {
        this.author = author;
        bookMeta.setAuthor(author);
    }
    public void setNumPages(int numPages)
    {
        this.numPages = numPages;
    }

    public void addPage ()
    {
        bookMeta.addPage(currentPage);
        ++numPages;
    }

    public void addToPage (String line)
    {
        if (numLines == 13)
        {
            addPage();
            this.currentPage = "";
            this.numLines = 0;
            this.currentPage = this.currentPage + title + "\n";
            this.currentPage = this.currentPage + line + "\n";
        }
        else if (numLines == 0)
        {
            this.currentPage = this.currentPage + title + "\n";
            this.currentPage = this.currentPage + line + "\n";
        }
        else
        {
            this.currentPage = this.currentPage + line + "\n";
        }
    
        ++numLines;
    }
    
    public void addToPageLink (String url, String name)
    {
    	bookMeta.spigot().addPage(ComponentSerializer.parse("[\"\",{\"text\":\"Arqade\",\"color\":\"gray\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://gaming.stackexchange.com/\"}}]"));
    }

    public void addInfo()
    {
        book.setItemMeta(bookMeta);
    }

    public void giveBook(Player p)
    {
        if (p.getInventory().firstEmpty() != -1)
        {
            p.getInventory().addItem(book);
        }
        else
        {
            p.sendMessage("cant give you " + title + " book, your inventory is full!");
        }
    }
}