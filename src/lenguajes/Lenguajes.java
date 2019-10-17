package lenguajes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Lenguajes {
    public static Scanner leer = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        ArrayList<String> ar = new ArrayList<>();
        TAC u = new TAC();
        String equation;
        
        // entrada de ecuacion
        System.out.println("Reverse Polish Notation in Java");
        System.out.println("Ingresa tu ecuacion sin letras: ");
        equation = leer.next();
        
        char[] pieces = equation.toCharArray();
        
        String[] infixarray = new String[pieces.length];
        
        for (int i = 0; i < pieces.length; i++) {
            char temp = pieces[i];
            infixarray[i] = Character.toString(temp);
        }
        
        Queue<String> array = new Lenguajes().Process(infixarray);
        
        System.out.println(" ");
        
        // impresion del resultado
        System.out.println("Notación polaca: " + queueToString(array));
        System.out.println("infijo a postfijo: " + postfijo(equation));
        System.out.println("infijo a prefijo: " + Prefijo(equation));
        System.out.println("Código intermedio triplos: ");
        u.Mainprocess(equation);
    }
    
    // método para construir a un string mediante un queue 
    private static String queueToString(Queue<String> queue) {
        
        // forma 1 de hacerlo
        StringBuilder sb = new StringBuilder();
        
        queue.stream().forEach((item) -> {
            sb.append(item);
        });
        
        // forma 2 de hacerlo
        //queue.forEach((item) -> {
            //sb.append(item);
        //});
        
        return sb.toString();
    }
    
    // metodo de revision de operando
    public static boolean Check(String str){
        try {
            Double.valueOf(str);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    // proceso de prioridad 
    Queue<String>Process(String[] infix) {
        
        Map<String,Integer> priority = new HashMap<>();
        priority.put("/", 5);
        priority.put("*", 5);
        priority.put("+", 4);
        priority.put("-", 4);
        priority.put("(", 0);
        
        Queue<String> line =  new LinkedList<>();
        Stack<String> stack = new Stack<>();
        
        for(String digit: infix) {
            if ("(".equals(digit)) {
                stack.push(digit);
                continue;
            }
            if (")".equals(digit)) {
                while (!"(".equals(stack.peek())) {
                    line.add(stack.pop());
                }
                stack.pop();
                continue;
            }
            if (priority.containsKey(digit)) {
                while (!stack.empty() && priority.get(digit) <= priority.get(stack.peek())) {
                    line.add(stack.pop());
                }
                stack.push(digit);
                continue;
            }
            if (Check(digit)) {
                line.add(digit);
                continue;
            }
            throw new IllegalArgumentException("No es un dato correcto");
        }
        while (!stack.isEmpty()) {
            line.add(stack.pop());
        }
        return line;
    }

    private static int Importance(char d) {
        
        /*
        *
        * Orden de importancia
        * 1 - exponencial
        * 2 - division y multiplicacion
        * 3 - mas y menos
        *
        * */
        
        switch(d) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }
    
    private static String postfijo(String equation) {
        
        String result = "";
        Stack<Character> s = new Stack<>();
        for (int i = 0; i < equation.length(); i++) {
            char piece = equation.charAt(i);

            if (Importance(piece) > 0) {
                //revisar si es operador
                while (s.isEmpty() == false && Importance(s.peek()) >= Importance(piece)) {
                    result += s.pop();
                }
                s.push(piece);
            } else if (piece == ')') {
                // el valor no es operador
                // entonces debe ser parentesis o valor
                char flag = s.pop();
                while (flag != '(') {
                    result += flag;
                    flag = s.pop();
                }
            } else if (piece == '(') {
                s.push(piece);
            } else {
                // el dato no es operador ni parentesis
                result += piece;
            }
        }
        for (int i = 0; i <= s.size(); i++) {
            result += s.pop();
        }
        return result;
    }
    
    private static StringBuilder Prefijo(String equation) {
        
        StringBuilder result = new StringBuilder();
        StringBuilder input = new StringBuilder(equation);
        input.reverse();
        Stack<Character> s = new Stack<>();
        
        char[] equationchar = new String(input).toCharArray();
        
        for (int i = 0; i < equationchar.length; i++) {
            if (equationchar[i] == '(') {
                equationchar[i] = ')';
                i++;
            } else if (equationchar[i] == ')') {
                equationchar[i] = '(';
                i++;
            }
        }
        for (int i = 0; i < equationchar.length; i++) {
            char piece = equationchar[i];
            
            if (Importance(piece) > 0) {
                while (s.isEmpty() == false && Importance(s.peek()) >= Importance(piece)) {
                    result.append(s.pop());
                }
                s.push(piece);
            } else if (piece == ')') {
                char flag = s.pop();
                while (flag != '(') {
                    result.append(flag);
                    flag = s.pop();
                }
            } else if (piece == '('){
                s.push(piece);
            } else {
                result.append(piece);
            }
        }
        for (int i = 0; i < s.size(); i++) {
            result.append(s.pop());
        }
        return result.reverse();
    }
}