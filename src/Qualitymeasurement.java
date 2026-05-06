class Solution {

    enum Unit {
        FEET, INCH
    }

    static class Quantity {
        double value;
        Unit unit;

        Quantity(double value, Unit unit) {
            this.value = value;
            this.unit = unit;
        }

        // Convert everything to inches (base unit)
        double toInches() {
            switch (unit) {
                case FEET:
                    return value * 12;
                case INCH:
                    return value;
                default:
                    throw new IllegalArgumentException("Unknown unit");
            }
        }
    }

    static void validate(double v1, double v2) {
        if (Double.isNaN(v1) || Double.isNaN(v2)) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    static boolean areEqual(Quantity q1, Quantity q2) {
        validate(q1.value, q2.value);

        double v1 = q1.toInches();
        double v2 = q2.toInches();

        return Math.abs(v1 - v2) < 0.0001;
    }

    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, Unit.FEET);
        Quantity q2 = new Quantity(12.0, Unit.INCH);

        System.out.println(areEqual(q1, q2)); // true ✅
    }
}