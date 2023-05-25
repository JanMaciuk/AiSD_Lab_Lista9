import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    @SuppressWarnings("ConstantConditions") // Sprawdzenie czy podałem wyrażenie, by design jest opcja pobrania z konsoli.
    public static void main(String[] args) {

        String input = "((4+3)-(2+1)*2+3)/2";
        // Jeżeli nie wpiszę wyrażenia to pobieram z konsoli
        if (input.equals("")) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Podaj wyrażenie do przekonwertowania na ONP:");
            input = scan.nextLine();
        }

        ArrayList<String> rpn = RPN.getRPN(input);
        Tree drzewko = new Tree(rpn);
        drzewko.printRPN();
        drzewko.printNormal();
        System.out.println("Wynik wyrażenia: "+drzewko.result());
        System.out.println("Rozmiar drzewa:  "+ drzewko.size()); // Liczba węzłów
        System.out.println("Wysokość drzewa: "+ drzewko.height());
        System.out.println("Liczba liści:    "+ drzewko.leafElements());
    }



}