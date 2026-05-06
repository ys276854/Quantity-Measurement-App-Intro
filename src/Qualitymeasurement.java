class Solution {

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

    static class Length {
        double value;
        Unit unit;

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
            return this.toFeet() == other.toFeet();
        }

        double convertTo(Unit targetUnit) {
            return convert(this.value, this.unit, targetUnit);
        }

        static double convert(double value, Unit sourceUnit, Unit targetUnit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid number");
            }
            if (sourceUnit == null || targetUnit == null) {
                throw new IllegalArgumentException("Invalid unit");
            }

            double valueInFeet = value * sourceUnit.toFeetFactor;
            return valueInFeet / targetUnit.toFeetFactor;
        }

        // UC6
        Length add(Length other) {
            if (other == null) {
                throw new IllegalArgumentException("Invalid input: null");
            }
            if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
                throw new IllegalArgumentException("Invalid number");
            }

            double sumInFeet = this.toFeet() + other.toFeet();
            double result = sumInFeet / this.unit.toFeetFactor;

            return new Length(result, this.unit);
        }

        // UC7
        Length add(Length other, Unit targetUnit) {
            if (other == null) {
                throw new IllegalArgumentException("Invalid input: null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Invalid target unit");
            }
            if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
                throw new IllegalArgumentException("Invalid number");
            }

            double sumInFeet = this.toFeet() + other.toFeet();
            double result = sumInFeet / targetUnit.toFeetFactor;

            return new Length(result, targetUnit);
        }

        static Length add(Length l1, Length l2) {
            return l1.add(l2);
        }

        static Length add(Length l1, Length l2, Unit targetUnit) {
            return l1.add(l2, targetUnit);
        }

        void validate(double value, Unit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid number");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Invalid unit");
            }
        }
    }

    static class QuantityMeasurementApp {
        boolean compare(Length l1, Length l2) {
            return l1.isEqual(l2);
        }
    }

    public static void main(String[] args) {

        Length l1 = new Length(1.0, Unit.FEET);
        Length l2 = new Length(12.0, Unit.INCH);

        Length result1 = l1.add(l2);
        System.out.println(result1.value + " " + result1.unit); // 2.0 FEET

        Length result2 = l1.add(l2, Unit.YARD);
        System.out.println(result2.value + " " + result2.unit); // ~0.667 YARD
    }
}