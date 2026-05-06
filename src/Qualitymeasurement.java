class Solution {

    enum Unit {
        FEET(1.0),
        INCH(1.0 / 12.0);

        final double toFeetFactor;

        Unit(double factor) {
            this.toFeetFactor = factor;
        }
    }

    static class Length {
        final double value;
        final Unit unit;

        Length(double value, Unit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        double toFeet() {
            return value * unit.toFeetFactor;
        }

        boolean isEqual(Length other) {
            if (other == null) {
                throw new IllegalArgumentException("Invalid input: null");
            }
            return Math.abs(this.toFeet() - other.toFeet()) < 0.0001;
        }

        private static void validate(double value, Unit unit) {
            if (Double.isNaN(value)) {
                throw new IllegalArgumentException("Invalid number");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Invalid unit");
            }
        }
    }

    static class QuantityMeasurementApp {
        boolean compare(Length l1, Length l2) {
            if (l1 == null || l2 == null) {
                throw new IllegalArgumentException("Invalid input: null");
            }
            return l1.isEqual(l2);
        }
    }

    public static void main(String[] args) {
        QuantityMeasurementApp app = new QuantityMeasurementApp();

        Length l1 = new Length(5.0, Unit.FEET);
        Length l2 = new Length(5.0, Unit.FEET);

        Length l3 = new Length(12.0, Unit.INCH);
        Length l4 = new Length(1.0, Unit.FEET);

        System.out.println(app.compare(l1, l2)); // true
        System.out.println(app.compare(l3, l4)); // true
    }
}