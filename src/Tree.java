import java.util.ArrayList;
import java.util.Stack;

public class Tree {

    class Node {
        protected String value; // Wartość drzewa.
        protected Node left;  // Wskaźnik na lewy element.
        protected Node right; // Wskaźnik na prawy element.

        public Node(String value) {
            this.value = value;
            left = null;
            right = null;
        }
    }

    Node root = null; // Korzeń drzewa, początkow drzewo jest puste.

    public Tree(ArrayList<String> input) {
        root = buildTree(input);
    }

    private Node buildTree(ArrayList<String> input) {

        Stack<Node> stos = new Stack<>();

        for (String str : input) {
            Node node = new Node(str);

            if (RPN.isOperator(str)) {
                // Kiedy napotkam operator, elementy na których operuje (ze stosu) są zapisywane jako jego dzieci.
                // (Na pewno pierwsze dwa elementy nie będą operatorami, nie byłoby to sensowne wyrażenie.)
                node.right = stos.pop();
                node.left = stos.pop();
            }
            // Liczby są po prostu dodawane na stos, sam operator też, będzie poddrzewem.
            stos.push(node);
        }

        return stos.pop(); // Korzeń
    }

    private static void postOrderWalkPrint(Node node) {
        // zwykły przegląd PostOrder zwróci RPN.
        if (node != null) {
            postOrderWalkPrint(node.left);
            postOrderWalkPrint(node.right);
            System.out.print(node.value + " ");
        }
    }
    public void printRPN() {
        System.out.print("Wyrażenie w Odwrotnej Notacji Polskiej: ");
        postOrderWalkPrint(root);
        System.out.println();

    }

    public void printNormal() {
        System.out.print("Wyrażenie w normalnej notacji: ");
        inOrderWalkPrint(root);
        System.out.println();
    }

    private void inOrderWalkPrint(Node node) {
        // Koniec drzewa.
        if (node == null) { return; }

        // Jeżeli mamy operator dotarliśmy do operacji, wypisuje nawias początkowy.
        if (RPN.isOperator(node.value)) {
            System.out.print("(");
        }

        inOrderWalkPrint(node.left);
        System.out.print(node.value);
        inOrderWalkPrint(node.right);

        // Koniec wyrażenia
        if (RPN.isOperator(node.value)) {
            System.out.print(")");
        }
    }

    public double result() { return calculate(root); }

    private double calculate(Node node) {

        // Kiedy dojdę do końca zwracam cyfry obydwóch odnóg,
        // następnie wykonuję operację określoną w rodzicu i zwracam wynik jako liczbę do góry.
        if (!RPN.isOperator(node.value)) {
            return Double.parseDouble(node.value);
        }

        double leftResult = calculate(node.left);
        double rightResult = calculate(node.right);

        return switch (node.value) {
            case "+" -> leftResult + rightResult;
            case "-" -> leftResult - rightResult;
            case "*" -> leftResult * rightResult;
            case "/" -> leftResult / rightResult;
            default -> 0;
        };
    }
    public int size() { return InOrderWalkCount(root); }

    private int InOrderWalkCount(Node root) {
        // Przegląd drzewa zawsze odwiedza każdy węzeł tylko raz, więc wystarczy zwiększać licznik o 1 z każdym odwiedzonym węzłem.
        if (root != null) {
            return InOrderWalkCount(root.left) + 1 + InOrderWalkCount(root.right);
        }
        return 0;
    }

    public int leafElements() {
        return inOrderWalkLeafCount(root);
    }

    private int inOrderWalkLeafCount(Node root) {
        // Jeżeli obydwóch potomków jest null, to jestem na końcu drzewa. Wystarczy zliczać ile razy to się zdarza.
        if (root != null) {
            int count = 0;
            if ((root.left == null && root.right == null)) { count++; }
            return inOrderWalkLeafCount(root.left) + count + inOrderWalkLeafCount(root.right);
        }
        return 0;
    }

    public int height() {
        return Height(root);
    }

    private int Height(Node root) {
        // Zwracam największą długość ze wszystkich ścieżek od korzenia w dół.
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(Height(root.left), Height(root.right));
    }



}