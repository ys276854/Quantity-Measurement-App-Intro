class Solution {

    // 🔹 LengthUnit (from UC8)
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

    // 🔹 Length class (unchanged)
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
            if (other == null) throw new IllegalArgumentException();
            return this.toFeet() == other.toFeet();
        }

        double convertTo(LengthUnit targetUnit) {
            return convert(this.value, this.unit, targetUnit);
        }

        static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
            if (!Double.isFinite(value) || sourceUnit == null || targetUnit == null)
                throw new IllegalArgumentException();

            double base = sourceUnit.convertToBaseUnit(value);
            return targetUnit.convertFromBaseUnit(base);
        }

        Length add(Length other) {
            double sum = this.toFeet() + other.toFeet();
            return new Length(this.unit.convertFromBaseUnit(sum), this.unit);
        }

        Length add(Length other, LengthUnit targetUnit) {
            double sum = this.toFeet() + other.toFeet();
            return new Length(targetUnit.convertFromBaseUnit(sum), targetUnit);
        }

        void validate(double value, LengthUnit unit) {
            if (!Double.isFinite(value) || unit == null)
                throw new IllegalArgumentException();
        }
    }

    // 🔹 NEW: WeightUnit
    enum WeightUnit {
        KG(1.0),
        G(1.0 / 1000.0),
        LB(0.453592);

        private final double toKgFactor;

        WeightUnit(double factor) {
            this.toKgFactor = factor;
        }

        double convertToBaseUnit(double value) {
            return value * toKgFactor;
        }

        double convertFromBaseUnit(double baseValue) {
            return baseValue / toKgFactor;
        }
    }

    // 🔹 NEW: Weight class
    static class Weight {
        double value;
        WeightUnit unit;

        Weight(double value, WeightUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        double toKg() {
            return unit.convertToBaseUnit(value);
        }

        boolean isEqual(Weight other) {
            if (other == null) throw new IllegalArgumentException();
            return this.toKg() == other.toKg();
        }

        Weight convertTo(WeightUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException();
            double base = this.toKg();
            return new Weight(targetUnit.convertFromBaseUnit(base), targetUnit);
        }

        static double convert(double value, WeightUnit sourceUnit, WeightUnit targetUnit) {
            if (!Double.isFinite(value) || sourceUnit == null || targetUnit == null)
                throw new IllegalArgumentException();

            double base = sourceUnit.convertToBaseUnit(value);
            return targetUnit.convertFromBaseUnit(base);
        }

        Weight add(Weight other) {
            double sum = this.toKg() + other.toKg();
            return new Weight(this.unit.convertFromBaseUnit(sum), this.unit);
        }

        Weight add(Weight other, WeightUnit targetUnit) {
            double sum = this.toKg() + other.toKg();
            return new Weight(targetUnit.convertFromBaseUnit(sum), targetUnit);
        }

        void validate(double value, WeightUnit unit) {
            if (!Double.isFinite(value) || unit == null)
                throw new IllegalArgumentException();
        }
    }

    static class QuantityMeasurementApp {
        boolean compareLength(Length l1, Length l2) {
            return l1.isEqual(l2);
        }

        boolean compareWeight(Weight w1, Weight w2) {
            return w1.isEqual(w2);
        }
    }

    public static void main(String[] args) {

        // Length still works
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCH);
        System.out.println(l1.isEqual(l2)); // true

        // Weight usage
        Weight w1 = new Weight(1.0, WeightUnit.KG);
        Weight w2 = new Weight(1000.0, WeightUnit.G);

        System.out.println(w1.isEqual(w2)); // true

        Weight sum = w1.add(new Weight(1.0, WeightUnit.LB), WeightUnit.KG);
        System.out.println(sum.value + " " + sum.unit);
    }
}