class Solution {

    // Unit enum with conversion factor to FEET
    enum Unit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CM(0.393701 / 12.0);

        double toFeetFactor;

        Unit(double factor) {
            this.toFeetFactor = factor;
        }
    }

    // Generic Length class
    static class Length {
        double value;
        Unit unit;

        Length(double value, Unit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        // Convert to base unit (feet)
        double toFeet() {
            return value * unit.toFeetFactor;
        }

        // Equality check
        boolean isEqual(Length other) {
            if (other == null) {
                throw new IllegalArgumentException("Invalid input: null");
            }
            return this.toFeet() == other.toFeet();
        }

        // 🔹 NEW: Instance conversion
        double convertTo(Unit targetUnit) {
            return convert(this.value, this.unit, targetUnit);
        }

        // 🔹 NEW: Static conversion API
        static double convert(double value, Unit sourceUnit, Unit targetUnit) {

            // Validation
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid number");
            }
            if (sourceUnit == null || targetUnit == null) {
                throw new IllegalArgumentException("Invalid unit");
            }

            // Step 1: convert to base (feet)
            double valueInFeet = value * sourceUnit.toFeetFactor;

            // Step 2: convert from base to target
            double result = valueInFeet / targetUnit.toFeetFactor;

            return result;
        }

        // Validation
        void validate(double value, Unit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid number");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Invalid unit");
            }
        }
    }

    // App class
    static class QuantityMeasurementApp {
        boolean compare(Length l1, Length l2) {
            return l1.isEqual(l2);
        }
    }

    // Main method
    public static void main(String[] args) {

        QuantityMeasurementApp app = new QuantityMeasurementApp();

        // Equality (existing behavior)
        Length l1 = new Length(1.0, Unit.YARD);
        Length l2 = new Length(3.0, Unit.FEET);
        System.out.println(app.compare(l1, l2)); // true

        // 🔹 Conversion examples
        double inches = Length.convert(1.0, Unit.FEET, Unit.INCH);
        double feet = Length.convert(30.48, Unit.CM, Unit.FEET);

        System.out.println(inches); // 12.0
        System.out.println(feet);   // ~1.0

        // Instance method usage
        Length l3 = new Length(2.0, Unit.YARD);
        System.out.println(l3.convertTo(Unit.FEET)); // 6.0
    }
}