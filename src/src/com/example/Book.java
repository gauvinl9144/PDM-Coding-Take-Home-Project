package src.com.example;

public class Book implements Comparable<Book>{
    String title;
    String author;
    boolean isFiction;
    String category;
    int numberOfPages;
    int storeNumber;

    public Book() {
        this.title = "";
        this.author = "";
        this.isFiction = false;
        this.category = "";
        this.numberOfPages = 0;
        this.storeNumber = 0;
    }

    public Book(String title, String author, boolean isFiction, String category, int numberOfPages, int storeNumber)
    {
        this.title = title;
        this.author = author;
        this.isFiction = isFiction;
        this.category = category;
        this.numberOfPages = numberOfPages;
        this.storeNumber = storeNumber;
    }

    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }

    public boolean getIsFiction()
    {
        return isFiction;
    }

    public String getCategory()
    {
        return category;
    }

    public int getNumberOfPages()
    {
        return numberOfPages;
    }

    public int getStoreNumber()
    {
        return storeNumber;
    }

    public int compareTo(Book other)
    {
        if(!this.category.equalsIgnoreCase(other.category))
        {
            return this.category.compareTo(other.category);
        }
        else if(!this.author.equalsIgnoreCase(other.author))
        {
            return this.author.compareTo(other.author);
        }
        else if(!this.title.equalsIgnoreCase(other.title))
        {
            return this.title.compareTo(other.title);
        }
        else if(!(this.numberOfPages == other.numberOfPages))
        {
            return this.numberOfPages - other.numberOfPages;
        }
        return this.storeNumber - other.storeNumber;
    }

}
