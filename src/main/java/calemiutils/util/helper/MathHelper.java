package calemiutils.util.helper;

public class MathHelper {

    public static int[] getCountingArray (int offset, int max) {

        int length = max - offset;

        int[] array = new int[length + 1];

        for (int i = 0; i < array.length; i++) {
            array[i] = offset + i;
        }

        return array;
    }

    public static int getAmountToAdd (int startingValue, int amountToAdd, int maxAmount) {

        if (startingValue + amountToAdd > maxAmount) {
            return 0;
        }

        return amountToAdd;
    }

    public static int getAmountToFill (int startingValue, int amountToAdd, int maxAmount) {

        if (startingValue + amountToAdd > maxAmount) {
            return maxAmount - startingValue;
        }

        return 0;
    }

    public static int scaleInt (int value, int maxValue, int maxScale) {
        float f = value * (float) maxScale / maxValue;
        return (int) f;
    }
}
