import java.lang.Math;

class TargetJava {

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("The file need 2 parameters: function value");
        }

        final String COSINUS = "cos";
        final String SINUS = "sin";
        final String TAN = "tan";
        final String SQRT = "sqrt";
        final String A_COS = "acos";
        final String A_SIN = "asin";
        final String A_TAN = "atan";

        String function = args[0];
        float value = Float.parseFloat(args[1]);

        double res;

        switch (function) {
        case COSINUS:
            res = Math.cos(value);
            break;

        case SINUS:
            res = Math.sin(value);
            break;

        case TAN:
            res = Math.tan(value);
            break;

        case SQRT:
            res = Math.sqrt(value);
            break;

        case A_COS:
            res = Math.acos(value);
            break;

        case A_SIN:
            res = Math.asin(value);
            break;

        case A_TAN:
            res = Math.atan(value);
            break;
        default:
            throw new IllegalArgumentException("Error in function selection");
        }

        // Display the result
        System.out.println(value + "; " + res);
    }
}