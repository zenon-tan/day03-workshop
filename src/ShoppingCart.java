import java.io.Console;

public class ShoppingCart {

    public static void main(String[] args) {

        // Create a class ShoppingCartDB 
        ShoppingCartDB cart = new ShoppingCartDB("cartdb"); //default folder: db
        cart.startShell(); // Calls class method startShell and start program
    }
    

}