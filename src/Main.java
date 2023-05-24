import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj wyrażenie do przekonwertowania na ONP:");
        String input = scan.nextLine();
        System.out.println(getRPN(input));
    }

    // Algorytm: https://pl.wikipedia.org/wiki/Odwrotna_notacja_polska
    public static ArrayList<String> getRPN(String input) {
        // Zakładam że nawiasy w wyrażeniu są zbalansowane, inne znaki są pomijane.
        ArrayList<String> StringList = splitString(input);
        Stack<String> stos = new Stack<>();
        ArrayList<String> wyjscie = new ArrayList<>();

        for (String i : StringList) {
            // Jeżeli jest liczbą to po prostu dodaje ją do wyjścia.
            if (isNumber(i)) { wyjscie.add(i); }

            // Nawiasy otwierające dodaje do stosu.
            else if (Objects.equals(i, "(")) { stos.push(i); }

            // Dla nawiasów zamykających dodaje zdejmuje operatory ze stosu dopóki nie napotkam odpowiadającego mu nawiasu otwierającego.
            else if (Objects.equals(i, ")")) {
                while (!Objects.equals(stos.peek(), "(")) {
                    wyjscie.add(stos.pop());
                }
                stos.pop();
            }

            // Obsłużyłem już liczby i nawiasy, powinny zostać tylko operatory.
            // Jeżeli nie jest to operator to informuje użytkownika że nie umie pisać i po prostu pomijam ten znak.
            else if (!isOperator(i)) { System.out.println("Napotkano niepoprawny symbol, pomijam"); }
            else {
                // Jeżeli kolejność wykonywania działań tego elementu jest mniejsza/równa od elementu ze szczytu stosu, dodaje element szczytowy do wyjścia.
                // (Upewniam się że operatory wykonywane pierwsze są zapisywane pierwsze).
                // (Jeżeli następny element nie byłby operatorem to zwracam priorytet 0, więc obecny nie będzie miał mniejszego priorytetu.)
                while (!stos.empty() && kolejnosc(i) <= kolejnosc(stos.peek())) {
                    wyjscie.add(stos.pop());
                }
                // Kiedy dodałem już elementy o wyższym priorytecie dodaje obecny operator do stosu.
                stos.push(i);
            }
        }
        // Dodaje operatory pozostałe na stosie.
        while (!stos.empty()) {
            wyjscie.add(stos.pop());
        }

        return wyjscie;
    }

    private static boolean isOperator(String c) {
        return Objects.equals(c, "+") || Objects.equals(c, "-") || Objects.equals(c, "*") || Objects.equals(c, "/");
    }

    private static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private static ArrayList<String> splitString(String input) {
        ArrayList<String> output = new ArrayList<>();
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char iZnak = input.charAt(i);

            if (Character.isDigit(iZnak)) {
                //Tworzę liczbę z kolejnych cyfr, aby zapisywać liczby wielocyfrowe razem, nie jako osobne cyfry.
                string.append(iZnak);

                // Jeżeli dotarłem do końca stringa lub następny znak nie jest cyfrą, to dodaje zbudowaną liczbę do wyjścia.
                if (i == input.length() - 1 || !Character.isDigit(input.charAt(i + 1))) {
                    output.add(string.toString());
                    string.setLength(0); // Zeruję zbudowaną liczbę.
                }
            }
            // Jeżeli następny znak nie jest cyfrą to po prostu dodaje go do wyjścia.
            else { output.add(String.valueOf(iZnak)); }
        }

        return output;
    }

    private static int kolejnosc(String operator) {
        // Zwracam priorytet operatora w wykonywaniu działań.
        switch (operator) {
            case "+", "-" -> { return 1; }
            case "*", "/" -> { return 2; }
            default -> { return 0; } // Jeżeli podamy coś co nie jest operatorem
        }
    }



}