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

        // Validation
        void validate(double value, Unit unit) {
            if (Double.isNaN(value)) {
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

        // Cross-unit comparisons
        Length l1 = new Length(1.0, Unit.YARD);     // 3 feet
        Length l2 = new Length(3.0, Unit.FEET);

        Length l3 = new Length(2.54, Unit.CM);      // 1 inch
        Length l4 = new Length(1.0, Unit.INCH);

        System.out.println(app.compare(l1, l2)); // true
        System.out.println(app.compare(l3, l4)); // true
    }
}