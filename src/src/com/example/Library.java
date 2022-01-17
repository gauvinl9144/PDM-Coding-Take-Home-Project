package src.com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Library {
    //Separate libraries for Fiction and Non-Fiction to sort more efficiently
    List<Book> fictionLibrary = new ArrayList<Book>();
    List<Book> nonFictionLibrary = new ArrayList<Book>();
    List<Book> searchedList = new ArrayList<Book>();
    //Parse CSV file into books, adding those books into the Binary Search Tree containing all the books
    public void generateBookList(String filename)
    {
        //Temporary variables to hold data collected in the CSV
        String title = "";
        String author = "";
        String isFictionSample = "";
        int isFictionInt = 0;
        boolean isFirstBook = true;
        boolean isFiction = false;
        String category = "";
        String numberOfPagesString = "";
        String numberOfPagesSubString = "";
        int numberOfPages = 0;
        String storeNumberString = "";
        String storeNumberSubString = "";
        int storeNumber = 0;
        try{   
            //Creating a scanner for the file using commas as the seperator between values 
            File originalBookListing = new File(filename);
            Scanner originalBookListingReader = new Scanner(originalBookListing);
            originalBookListingReader.useDelimiter(",");

            //While loop to create all book objects for data inputted
            while(originalBookListingReader.hasNext())
            {
                title = originalBookListingReader.next();
                //Appending the title to not have a space at the beginning
                if(!isFirstBook)
                    title = title.substring(1, title.length());
                else
                    isFirstBook = false;
                author = originalBookListingReader.next();
                author = author.substring(1, author.length());

                //The entire isFiction block is meant to sort items into 
                //the fictionLibrary and nonFictionLibrary lists. This is done to increase
                //search and sorting efficiency
                isFictionSample = originalBookListingReader.next();
                isFictionSample = isFictionSample.substring(1);
                isFictionInt = Integer.parseInt(isFictionSample);
                if(isFictionInt == 1)
                    isFiction = true;
                else
                    isFiction = false;
                category = originalBookListingReader.next();
                category = category.substring(1, category.length());
                numberOfPagesString = originalBookListingReader.next();
                numberOfPagesSubString = numberOfPagesString.substring(1, numberOfPagesString.length());
                numberOfPages = Integer.parseInt(numberOfPagesSubString);
                storeNumberString = originalBookListingReader.next();
                storeNumberSubString = storeNumberString.substring(1, storeNumberString.length());
                storeNumber = Integer.parseInt(storeNumberSubString);
                Book newBook = new Book(title, author, isFiction, category, numberOfPages, storeNumber);
                if(isFiction)
                    fictionLibrary.add(newBook);
                else
                    nonFictionLibrary.add(newBook);
            }
            originalBookListingReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    public void sortBookLists()
    {
        //Utilizing an object designed to use quick sort to sort the library
        //ForkJoinPool allows for this sort to be done in a multithreaded fashion to increase efficiency
        QuickSortAlgo<Book> fictionQuickSort = new QuickSortAlgo<Book>(fictionLibrary);
        QuickSortAlgo<Book> nonFictionQuickSort = new QuickSortAlgo<Book>(nonFictionLibrary);

        ForkJoinPool fictionPool = new ForkJoinPool();
        ForkJoinPool nonFictionPool = new ForkJoinPool();
        fictionPool.invoke(fictionQuickSort);
        nonFictionPool.invoke(nonFictionQuickSort);
    }

    public void parseIntoCSV()
    {
        //This is to sort the file information when it must be stored off,
        //allowing for it to be accessed again without having to quicksort
        File newFile = new File("sortedBookListing.csv");
        try {
            newFile.createNewFile();
            FileWriter newBookListing = new FileWriter("sortedBookListing.csv", true);

            BufferedWriter newBookListingWriter = new BufferedWriter(newBookListing);
            
            //Two while loops to add the fictionLibrary and nonFictionLibrary to the new CSV
            while(fictionLibrary.size() != -1)
            {
                newBookListingWriter.write(fictionLibrary.get(0).getTitle() + ", ");
                newBookListingWriter.write(fictionLibrary.get(0).getAuthor() + ", ");
                newBookListingWriter.write(fictionLibrary.get(0).getIsFiction() + ", ");
                newBookListingWriter.write(fictionLibrary.get(0).getCategory() + ", ");
                newBookListingWriter.write(fictionLibrary.get(0).getNumberOfPages() + ", ");
                newBookListingWriter.write(fictionLibrary.get(0).getStoreNumber() + ", ");

                fictionLibrary.remove(0);
            }

            while(nonFictionLibrary.size() != -1)
            {
                newBookListingWriter.write(nonFictionLibrary.get(0).getTitle() + ", ");
                newBookListingWriter.write(nonFictionLibrary.get(0).getAuthor() + ", ");
                newBookListingWriter.write(nonFictionLibrary.get(0).getIsFiction() + ", ");
                newBookListingWriter.write(nonFictionLibrary.get(0).getCategory() + ", ");
                newBookListingWriter.write(nonFictionLibrary.get(0).getNumberOfPages() + ", ");
                if(fictionLibrary.size() != 0)
                    newBookListingWriter.write(nonFictionLibrary.get(0).getStoreNumber() + ", ");
                else   
                    newBookListingWriter.write(nonFictionLibrary.get(0).getStoreNumber());

                fictionLibrary.remove(0);
            }

            newBookListingWriter.close();
        } catch (IOException e) {
            System.out.println("An error has occurred with generating the file");
            e.printStackTrace();
        }
        
    }

    public void searchList(String titleSearchQuery, String authorSearchQuery, 
                            int isFiction, String categorySearchQuery, int pageNumberSearchQuery,
                            boolean searchByMinimumPages, int storeNumberSearchQuery)
    {

    }

    public static void main(String[] args) throws Exception {
            Library newLibrary = new Library();
            newLibrary.generateBookList("C:\\Users\\GauvinLuke\\.vscode\\Java\\PDM Take Home Coding Challenge\\lib\\originalBookList.csv");
            newLibrary.sortBookLists();
            String titleSearchQuery = "";
            String authorSearchQuery = "";
            int isFiction = 2;
            String categorySearchQuery = "";
            int pageNumberSearchQuery = -1;
            boolean searchByMinimumPages = false;
            boolean validInput = false;
            int storeNumberSearchQuery = -1;
            boolean stayInLoop = true;
            boolean searchOnCurrentField = false;
            Scanner inputScanner = new Scanner(System.in);
            String inputScannerInput = "";

            do{
                System.out.println("Would you like to search on the title? Please enter \"Y\" or \"N\" without the quotes.");
                inputScannerInput = inputScanner.nextLine();
                while(inputScannerInput != "Y" && inputScannerInput != "y" && inputScannerInput != "N" && inputScannerInput != "n")
                {
                    System.out.println("Please enter valid input:");
                    inputScannerInput = inputScanner.nextLine();
                }
                if(inputScannerInput == "Y" || inputScannerInput == "y")
                {
                    System.out.println("Please enter your title search query:");
                    titleSearchQuery = inputScanner.nextLine();
                    inputScannerInput = "";
                }

                System.out.println("Would you like to search on the author?");
                inputScannerInput = inputScanner.nextLine();
                while(inputScannerInput != "Y" && inputScannerInput != "y" && inputScannerInput != "N" && inputScannerInput != "n")
                {
                    System.out.println("Please enter valid input:");
                    inputScannerInput = inputScanner.nextLine();
                }
                if(inputScannerInput == "Y" || inputScannerInput == "y")
                {
                    System.out.println("Please enter your author search query:");
                    authorSearchQuery = inputScanner.nextLine();
                    inputScannerInput = "";
                }

                System.out.println("Would you like to search on whether or not the book is fiction?");
                inputScannerInput = inputScanner.nextLine();
                while(inputScannerInput != "Y" && inputScannerInput != "y" && inputScannerInput != "N" && inputScannerInput != "n")
                {
                    System.out.println("Please enter valid input:");
                    inputScannerInput = inputScanner.nextLine();
                }
                if(inputScannerInput == "Y" || inputScannerInput == "y")
                {
                    System.out.println("Please enter \"Y\" if your book is Fiction, otherwise enter \"N\":");
                    inputScannerInput = inputScanner.nextLine();

                    while(inputScannerInput != "Y" && inputScannerInput != "y" && inputScannerInput != "N" && inputScannerInput != "n")
                    {
                        System.out.println("Please enter valid input:");
                        inputScannerInput = inputScanner.nextLine();
                    }
                    if(inputScannerInput == "Y" || inputScannerInput == "y")
                    {
                        isFiction = 1;
                    }
                    else
                    {
                        isFiction = 0;
                    }
                    inputScannerInput = "";
                }

                System.out.println("Would you like to search on the category?");
                inputScannerInput = inputScanner.nextLine();
                while(inputScannerInput != "Y" && inputScannerInput != "y" && inputScannerInput != "N" && inputScannerInput != "n")
                {
                    System.out.println("Please enter valid input:");
                    inputScannerInput = inputScanner.nextLine();
                }
                if(inputScannerInput == "Y" || inputScannerInput == "y")
                {
                    System.out.println("Please enter your category search query:");
                    categorySearchQuery = inputScanner.nextLine();
                    inputScannerInput = "";
                }

                System.out.println("Would you like to search on the number of pages?");
                inputScannerInput = inputScanner.nextLine();
                while(inputScannerInput != "Y" && inputScannerInput != "y" && inputScannerInput != "N" && inputScannerInput != "n")
                {
                    System.out.println("Please enter valid input:");
                    inputScannerInput = inputScanner.nextLine();
                }
                if(inputScannerInput == "Y" || inputScannerInput == "y")
                {
                    System.out.println("Would you like to search by minimum number of pages? Please enter \"Y\" or \"N\":");
                    inputScannerInput = inputScanner.nextLine();
                    while(inputScannerInput != "Y" && inputScannerInput != "y" && inputScannerInput != "N" && inputScannerInput != "n")
                    {
                        System.out.println("Please enter valid input:");
                        inputScannerInput = inputScanner.nextLine();
                    }
                    if(inputScannerInput == "Y" || inputScannerInput == "y")
                    {
                        searchByMinimumPages = true;
                        System.out.println("Please enter the minimum number of pages you would like to see");
                        while(!validInput)
                        {
                            try{
                                inputScannerInput = inputScanner.nextLine();
                                pageNumberSearchQuery = Integer.parseInt(inputScannerInput);
                                validInput = true;
                            }
                            catch(NumberFormatException nfe){
                                System.out.println("Please input a valid minimum number of pages");
                            }
                        }
                        validInput = false;
                    }
                    else
                    {
                        searchByMinimumPages = false;
                        System.out.println("Please enter the maximum number of pages you would like to see");
                        while(!validInput)
                        {
                            try{
                                inputScannerInput = inputScanner.nextLine();
                                pageNumberSearchQuery = Integer.parseInt(inputScannerInput);
                                validInput = true;
                            }
                            catch(NumberFormatException nfe){
                                System.out.println("Please input a valid maximum number of pages");
                            }
                        }
                        validInput = false;
                    }
                    inputScannerInput = "";
                }

                System.out.println("Would you like to search on the exact store number?");
                inputScannerInput = inputScanner.nextLine();
                while(inputScannerInput != "Y" && inputScannerInput != "y" && inputScannerInput != "N" && inputScannerInput != "n")
                {
                    System.out.println("Please enter valid input:");
                    inputScannerInput = inputScanner.nextLine();
                }
                if(inputScannerInput == "Y" || inputScannerInput == "y")
                {
                    System.out.println("Please enter your exact store number:");
                    while(!validInput)
                        {
                            try{
                                inputScannerInput = inputScanner.nextLine();
                                storeNumberSearchQuery = Integer.parseInt(inputScannerInput);
                                if(storeNumberSearchQuery < 10 && storeNumberSearchQuery >= 0)
                                {
                                    validInput = true;
                                }
                            }
                            catch(NumberFormatException nfe){
                                System.out.println("Please input a valid store number");
                            }
                        }
                        validInput = false;
                }

                newLibrary.searchList(titleSearchQuery, authorSearchQuery, isFiction, categorySearchQuery, pageNumberSearchQuery, searchByMinimumPages, storeNumberSearchQuery);
                ListIterator<Book> libraryIterator = newLibrary.searchedList.listIterator();
                while(libraryIterator.hasNext())
                {
                    System.out.println(libraryIterator.next());
                }

            } while(stayInLoop);


            newLibrary.parseIntoCSV();
    }
}
