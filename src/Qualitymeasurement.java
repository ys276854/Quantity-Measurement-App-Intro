class Solution {

    // 🔹 Standalone LengthUnit (extracted)
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CM(0.393701 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double factor) {
            this.toFeetFactor = factor;
        }

        double convertToBaseUnit(double value) {
            return value * toFeetFactor;
        }

        double convertFromBaseUnit(double baseValue) {
            return baseValue / toFeetFactor;
        }
    }

    static class Length {
        double value;
        LengthUnit unit;

        Length(double value, LengthUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        double toFeet() {
            return unit.convertToBaseUnit(value);
        }

        boolean isEqual(Length other) {
            if (other == null) {
                throw new IllegalArgumentException("Invalid input: null");
            }
            return this.toFeet() == other.toFeet();
        }

        double convertTo(LengthUnit targetUnit) {
            return convert(this.value, this.unit, targetUnit);
        }

        static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid number");
            }
            if (sourceUnit == null || targetUnit == null) {
                throw new IllegalArgumentException("Invalid unit");
            }

            double base = sourceUnit.convertToBaseUnit(value);
            return targetUnit.convertFromBaseUnit(base);
        }

        Length add(Length other) {
            if (other == null) {
                throw new IllegalArgumentException("Invalid input: null");
            }

            double sumBase = this.toFeet() + other.toFeet();
            double result = this.unit.convertFromBaseUnit(sumBase);

            return new Length(result, this.unit);
        }

        Length add(Length other, LengthUnit targetUnit) {
            if (other == null) {
                throw new IllegalArgumentException("Invalid input: null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Invalid target unit");
            }

            double sumBase = this.toFeet() + other.toFeet();
            double result = targetUnit.convertFromBaseUnit(sumBase);

            return new Length(result, targetUnit);
        }

        static Length add(Length l1, Length l2) {
            return l1.add(l2);
        }

        static Length add(Length l1, Length l2, LengthUnit targetUnit) {
            return l1.add(l2, targetUnit);
        }

        void validate(double value, LengthUnit unit) {
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

        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCH);

        Length result1 = l1.add(l2);
        System.out.println(result1.value + " " + result1.unit); // 2.0 FEET

        Length result2 = l1.add(l2, LengthUnit.YARD);
        System.out.println(result2.value + " " + result2.unit); // ~0.667 YARD

        double inches = Length.convert(1.0, LengthUnit.FEET, LengthUnit.INCH);
        System.out.println(inches); // 12.0

        QuantityMeasurementApp app = new QuantityMeasurementApp();
        System.out.println(app.compare(l1, l2)); // true
    }
}