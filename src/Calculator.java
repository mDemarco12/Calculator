import java.util.*;

public class Calculator {
    //Scan input
    private static Scanner s = new Scanner(System.in);

    //Parse the ints
    private static int getOp(String exp, int cur) {
        Scanner s = new Scanner(exp.substring(cur));
        s.useDelimiter("[^0-9]");
        return s.nextInt();
    }

    //do the operation; Does the math
    private static int doOp(int op1, int op2, char operator) throws Exception {
        switch (operator) {
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case '*':
                return op1 * op2;
            case '/':
                return op1 / op2;
            case '^':
                return (int)Math.pow(op1,op2);
            default:
                throw new Exception("Unbalanced parentheses: An open parenthesis has not get closed!");
        }
    }

    private static int comparePrecedence(char op1, char op2) {
        if (op1 == '+' || op1 == '-')
            if (op2 == '+' || op2 == '-')
                return 0;
            else
                return -1;
        else if (op2 == '+' || op2 == '-')
            return 1;
        else
            return 0;
    }

    public static void main(String args[]) throws Exception {
        Stack<Character> operators = new Stack<Character>();
        Stack<Integer> operands = new Stack<Integer>();
        while (true) {
            System.out.println("Please enter a new expression: ");
            String exp = s.nextLine();
            if (exp.equals("DONE"))
                break;
            char prev = 0;
            for (int cur = 0; cur < exp.length(); cur++) {
                switch (exp.charAt(cur)) {
                    case '(':
                        operators.push(exp.charAt(cur));
                        break;
                    case '+':
                    case '-':
                        if(prev == 0 || prev == '(')//Treat + and - as the explicit sign of an integer!
                            operands.push(0);
                    case '*':
                    case '/':
                            if( prev == 0 || prev == ')')
                                operands.push(0);
                                //Don't push +,- over +,-,*,/
                                //Don't push *,/ over *,/
                    case '^':
                        while (!operators.isEmpty() && operators.peek() != '(')
                            if (comparePrecedence(operators.peek(), exp.charAt(cur)) >= 0) {
                                int op2 = operands.pop();//pop the second operand first
                                int result = doOp(operands.pop(), op2, operators.pop());
                                operands.push(result);
                            } else
                                break;
                        operators.push(exp.charAt(cur));
                        break;
                    case ')':
                        while (!operators.isEmpty() && operators.peek() != '(') {
                            int op2 = operands.pop();
                            operands.push(doOp(operands.pop(), op2, operators.pop()));
                        }
                        if(operators.empty())
                            throw new Exception("Unbalanced parentheses: An open parenthesis has been closed multiple times!");
                        operators.pop();//pop the matching open parenthesis
                        break;
                    case ' ':
                    case '\t'://skipping the white space characters
                        break;
                    default:
                        if (Character.isDigit(exp.charAt(cur))) {//integer found!
                            int temp = getOp(exp, cur);
                            while (cur < exp.length() && Character.isDigit(exp.charAt(cur)))//skip the whole number
                                cur++;
                            cur--;
                            operands.push(temp);//push it to operand's stack
                        } else
                            throw new Exception("Error: bad input");
                }
                if(!Character.isWhitespace(exp.charAt(cur)))
                    prev = exp.charAt(cur);
            }
            int result;
            while (!operators.isEmpty()) {
                int op2 = operands.pop();
                operands.push(doOp(operands.pop(), op2, operators.pop()));
            }
            System.out.println("The result is " + operands.pop());
        }
    }
}