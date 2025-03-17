import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.File;

class InventoryItem {
    int ID;
    String name;
    float price;
    int quantity;
    float totalprice;
    // I used floats here to handle decimals.

    public InventoryItem(int ID, String name, float price, int quantity, float totalprice) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = price * quantity;
        // Created class for easier creation of ArrayLists and data manipulation.
    }
    public String toString() {
        return ID + " " + name + " " + price + " " + quantity + " " + totalprice;
    }
    // created framework for printing data to command line.

    public String toFileString() {
        return ID + "," + name + "," + price + "," + quantity + "," + totalprice;
    }
    // created framework for printing to file, using commas instead of spaces as these will be used by my .split() function.
}

public class store
{
    public static void main(String args[])
    {
        ArrayList<InventoryItem> Items  = new ArrayList();
        // created Arraylist to split up the lines into separate arrays so I could make java understand that each line was a unique item with different properties.
        Scanner input = new Scanner(System.in);
        String choice;
        boolean ContinueProgram = true;
        // boolean so I could have a loop that wraps around the entire code to check if the user wishes to perform another action.

        try {
            File Inventory = new File("src/items.txt");
            Scanner Reader = new Scanner(Inventory);


            while (Reader.hasNextLine()) {
                String line = Reader.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }
                // made the code skip gaps in the .txt file.

                String[] DataUnit = line.split(",");
                // set the delimiter to be a comma to make each unit of data a subarray inside the wider ArrayList.

                int ID = Integer.parseInt(DataUnit[0]);
                String name = DataUnit[1];
                float price = Float.parseFloat(DataUnit[2]);
                int quantity = Integer.parseInt(DataUnit[3]);
                float totalprice;
                if (DataUnit.length >= 5) {
                    totalprice = Float.parseFloat(DataUnit[4]);
                } else {
                    totalprice = price * quantity;
                }
                // Gave each subarray a variable to work with.

                Items.add(new InventoryItem(ID, name, price, quantity, totalprice));
            }
            Reader.close();
        } catch(Exception e) {
            System.out.println("There was an error in reading the file.");
            e.printStackTrace();
        }
        // attempted to catch exception errors/

    while (ContinueProgram) {
        // beginning of the while loop that utilises my boolean variable to check if the user wants to continue.

        System.out.println("I N V E N T O R Y    M A N A G E M E N T    S Y S T E M");
        System.out.println("-----------------------------------------------");
        System.out.println("1. ADD NEW ITEM");
        System.out.println("2. UPDATE QUANTITY OF EXISTING ITEM");
        System.out.println("3. REMOVE ITEM");
        System.out.println("4. VIEW DAILY TRANSACTION REPORT");
        System.out.println("---------------------------------");
        System.out.println("5. Exit");


        System.out.print("\n Enter a choice and Press ENTER to continue[1-5]:");
        String userinput = input.next();


        while(!userinput.equals("5"))
        {
            if (userinput.equals("1"))	{
                try {
                    int maxID = 0;
                    for (InventoryItem item : Items) {
                        if (item.ID > maxID) {
                            maxID = item.ID;
                        }
                    }
                    int nextID = maxID + 1;
                    String FormattedID = String.format("%05d", nextID);
                    // formatted the ID so that it wasn't written into the file as a simple integer e.g instead of "00001", "1"

                    System.out.print("Please enter the name of your item: ");
                    String name = input.next();

                    System.out.print("Please enter the price of the item: ");
                    float price = input.nextFloat();

                    System.out.print("Please enter the quantity of the item: ");
                    int quantity = input.nextInt();

                    float totalprice = price * quantity;
                    // asked the user to input the values of the item they wish to add and stored them in a variable.

                    InventoryItem newItem = new InventoryItem(nextID, name, price, quantity, totalprice);
                    Items.add(newItem);
                    // created new item to store those variables.

                    PrintWriter writer = new PrintWriter(new java.io.FileOutputStream(new java.io.File("src/items.txt"), true));
                    writer.println(FormattedID + "," + name + "," + price + "," + quantity + "," + totalprice);
                    writer.close();
                    // wrote new item to the file.

                    PrintWriter transactionWriter = new PrintWriter(new java.io.FileOutputStream(new java.io.File("src/transactions.txt"), true));
                    transactionWriter.println("Added new item: " + FormattedID + "," + name + "," + price + "," + quantity + "," + totalprice);
                    transactionWriter.close();
                    // wrote log of new item to transactions.txt.

                } catch (Exception e) {
                    System.out.println("There was an error in reading the file.");
                    e.printStackTrace();
                }
                // attempted to catch exception errors.

                System.out.print("\n New Item Added");
                break;
                // broke the method here in order to avoid pre-emptive breaking and data corruption.
            }
            else if (userinput.equals("2")) {

                System.out.print("Please enter the name/ID of your item: ");
                String SearchQuery = input.next();
                // created a search query variable.

                ArrayList<InventoryItem> MatchedItems = new ArrayList();

                for (InventoryItem item : Items) {
                    if (String.valueOf(item.ID).equals(SearchQuery) || item.name.equalsIgnoreCase(SearchQuery)) {
                        MatchedItems.add(item);
                    }
                }
                // attempted to match files in the file to the search query.

                if (MatchedItems.isEmpty()) {
                    System.out.println("No matching items found.");
                } else if (MatchedItems.size() > 0) {
                System.out.println("Found " + MatchedItems.size() + " matching items.");
                for (int i = 0; i < MatchedItems.size(); i++) {
                    InventoryItem matchedItem = MatchedItems.get(i);
                    System.out.println((i + 1) + " ID: " + matchedItem.ID + " Name: " + matchedItem.name + " Price: " + matchedItem.price + " Quantity: " + matchedItem.quantity + " Total Price: " + matchedItem.totalprice);
                }
                // relayed that I found files that matched the query, and listed their information in the command line.

                System.out.print("Select the number of the item you wish to update: ");
                int Selection = input.nextInt() - 1;

                if (Selection >= 0 && Selection < MatchedItems.size()) {
                    InventoryItem SelectedItem = MatchedItems.get(Selection);
                    System.out.println("Selected ID: " + SelectedItem.ID + " | Name: " + SelectedItem.name + " | Price: " + SelectedItem.price + "| Quantity: " + SelectedItem.quantity + " | Total Price: " + SelectedItem.totalprice);
                    // asked the user to input the number of the item they wish to modify. The number is a counter I set up in the method above, located on the left side of the output.

                    System.out.print("Please enter quantity you wish to add/remove from selected item: ");
                    int QtyChange = input.nextInt();
                    SelectedItem.quantity += QtyChange;
                    SelectedItem.totalprice = SelectedItem.price * SelectedItem.quantity;
                    // asked user for the quantity they wish to add/subtract.

                    System.out.println("Updated quantity: " + SelectedItem.quantity);
                    // gave confirmation that the process of modification was complete.

                    try {
                        PrintWriter writer = new PrintWriter(new java.io.FileOutputStream(new java.io.File("src/items.txt")));
                        for (InventoryItem item : Items) {
                            String FormattedID = String.format("%05d", item.ID);
                            writer.println(FormattedID + "," + item.name + "," + item.price + "," + item.quantity + "," + (item.price * item.quantity));
                        }
                        // wrote new item to the file. This was basically done by rewriting each line of the file, with only the chosen item having its values maintained.

                        writer.close();
                        PrintWriter transactionWriter = new PrintWriter(new java.io.FileOutputStream(new java.io.File("src/transactions.txt"), true));
                        transactionWriter.println("Updated quantity of " + SelectedItem.name + " by " + QtyChange + ". Result: " + SelectedItem.quantity);
                        transactionWriter.close();
                        // wrote log pertaining to item modification.

                    } catch (Exception e) {
                        System.out.println("There was an error in reading the file.");
                        e.printStackTrace();
                    }
                    System.out.print("\n Item quantity updated");
                    break;
                } else {
                    System.out.println("Invalid selection.");
                }
                }
            }

            else if (userinput.equals("3")) {
                System.out.print("Please enter the name/ID of the item you wish to remove: ");
                String SearchQuery = input.next();
                // repurposed a lot of code from my update quantity method, as I wanted them to do basically the same thing just with different outcomes.

                ArrayList<InventoryItem> MatchedItems = new ArrayList();

                for (InventoryItem item : Items) {
                    if (String.valueOf(item.ID).equals(SearchQuery) || item.name.equalsIgnoreCase(SearchQuery)) {
                        MatchedItems.add(item);
                    }
                }

                if (MatchedItems.isEmpty()) {
                    System.out.println("No matching items found.");
                } else if (MatchedItems.size() > 0) {
                    System.out.println("Found " + MatchedItems.size() + " matching items.");
                    for (int i = 0; i < MatchedItems.size(); i++) {
                        InventoryItem matchedItem = MatchedItems.get(i);
                        System.out.println((i + 1) + " ID: " + matchedItem.ID + " Name: " + matchedItem.name + " Price: " + matchedItem.price + " Quantity: " + matchedItem.quantity + " Total Price: " + matchedItem.totalprice);
                    }

                    System.out.print("Select the number of the item you wish to remove: ");
                    int Selection = input.nextInt() - 1;

                    if (Selection >= 0 && Selection < MatchedItems.size()) {
                        InventoryItem SelectedItem = MatchedItems.get(Selection);
                        System.out.println("Selected ID: " + SelectedItem.ID + " | Name: " + SelectedItem.name + " | Price: " + SelectedItem.price + "| Quantity: " + SelectedItem.quantity + " | Total Price: " + SelectedItem.totalprice);

                        Items.remove(SelectedItem);
                        // made use of the remove function, passing SelectedItem as an argument.

                        try {
                        PrintWriter writer = new PrintWriter(new java.io.FileOutputStream(new java.io.File("src/items.txt")));
                        for (InventoryItem item : Items) {
                            String FormattedID = String.format("%05d", item.ID);
                            writer.println(FormattedID + "," + item.name + "," + item.price + "," + item.quantity + "," + (item.price * item.quantity));
                        }
                        writer.close();

                        PrintWriter transactionwriter = new PrintWriter(new java.io.FileOutputStream(new java.io.File("src/transactions.txt"), true));
                        transactionwriter.println("Removed item: " + SelectedItem.name + ".");
                        transactionwriter.close();
                        System.out.print("\n Item Removed");
                        break;
                        // logged item removal.

                        } catch (Exception e) {
                            System.out.println("There was an error in reading the file.");
                            e.printStackTrace();
                        }
                    }
                    }

                break;
            }
            else if (userinput.equals("4")) {
                try {
                    File TransactionsFileData = new File("src/transactions.txt");
                    Scanner LogReader = new Scanner(TransactionsFileData);
                    while (LogReader.hasNextLine()) {
                        String LogData = LogReader.nextLine();
                        System.out.println(LogData);
                    }
                    LogReader.close();
                } catch (Exception e) {
                    System.out.println("There was an error in reading the file.");
                    e.printStackTrace();
                }
                System.out.print("\n Report printed");
                break;
            }
            else {
                System.out.println("Input cannot be empty.");
            }
            // for this I just made a scanner read the entire document and then print what it found.

        }
        if (ContinueProgram) {
        System.out.print("\n Do you wish to continue? (y/n): ");
        String ContinueInput = input.next();
        if (ContinueInput.equalsIgnoreCase("n")) {
            ContinueProgram = false;
        }
        // endpoint of my boolean while loop that results in the user being asked if they wish to continue.
        }
    }
        System.out.println("\n\n Thanks for using this program...!");
    }
}