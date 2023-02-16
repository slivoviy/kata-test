import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Main {
    private static final String mathSymbols = "+-*/";
    private static final String romanSymbols = "VIX";

    public static BiFunction<Integer, Integer, Integer> plus = Integer::sum;
    public static BiFunction<Integer, Integer, Integer> minus = (a, b) -> a - b;
    public static BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
    // в задаче и примерах не было указано, можно ли выводить double при делении, поэтому допустим, что оно целочисленное
    public static BiFunction<Integer, Integer, Integer> divide = (a, b) -> a / b;

    public static String calc(String input) throws Exception {
        Map<Character, BiFunction<Integer, Integer, Integer>> operations = Map.of('+', plus, '-', minus, '*', multiply, '/', divide);

        int output;
        int left, right;

        char operation = getOperation(input);
        int opIndex = input.indexOf(operation);

        boolean hasRoman = false;

        if (romanSymbols.contains(input.substring(0, 1))) {
            left = romanToArabic(input.substring(0, opIndex));
            right = romanToArabic(input.substring(opIndex + 1));
            hasRoman = true;
        } else {
            left = Integer.parseInt(input.substring(0, opIndex));
            right = Integer.parseInt(input.substring(opIndex + 1));
        }

        output = operations.get(operation).apply(left, right);

        if(hasRoman) {
            if(output <= 0) throw new Exception("Result roman number less or equal 0");
            return arabicToRoman(output);
        }
        else return Integer.toString(output);
    }

    public static boolean isValidString(String input) {
        boolean hasArabic = false, hasRoman = false, hasOperation = false;

        if (input.length() > 9) return false;

        for (char c : input.toCharArray()) {
            if (!(Character.isDigit(c) || mathSymbols.contains(Character.toString(c)) || romanSymbols.contains(Character.toString(c))))
                return false;

            if(mathSymbols.contains(Character.toString(c))) {
                if(!hasOperation) hasOperation = true;
                else return false;
            }
            if (Character.isDigit(c)) hasArabic = true;
            if (romanSymbols.contains(Character.toString(c))) hasRoman = true;
        }

        return !(hasArabic && hasRoman) && hasOperation;
    }

    public static char getOperation(String input) {
        if (input.contains("+")) return '+';
        if (input.contains("-")) return '-';
        if (input.contains("*")) return '*';
        if (input.contains("/")) return '/';

        return '0';
    }

    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int output = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                output += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        return output;
    }
    public static String arabicToRoman(int number) {
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String input = reader.readLine();
        input = input.replaceAll("\\s", "");

        if (!isValidString(input)) throw new Exception("Invalid string");

        String output = calc(input);
        System.out.println(output);
    }
}
