class Solution {

    interface IMeasurable {
        double getConversionFactor();
        double convertToBaseUnit(double value);
        double convertFromBaseUnit(double baseValue);
        String getUnitName();
    }

    enum LengthUnit implements IMeasurable {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CM(0.393701 / 12.0);

        private final double factor;

        LengthUnit(double factor) {
            this.factor = factor;
        }

        public double getConversionFactor() { return factor; }
        public double convertToBaseUnit(double value) { return value * factor; }
        public double convertFromBaseUnit(double baseValue) { return baseValue / factor; }
        public String getUnitName() { return name(); }
    }

    enum WeightUnit implements IMeasurable {
        KG(1.0),
        G(1.0 / 1000.0),
        LB(0.453592);

        private final double factor;

        WeightUnit(double factor) {
            this.factor = factor;
        }

        public double getConversionFactor() { return factor; }
        public double convertToBaseUnit(double value) { return value * factor; }
        public double convertFromBaseUnit(double baseValue) { return baseValue / factor; }
        public String getUnitName() { return name(); }
    }

    // 🔹 NEW: VolumeUnit
    enum VolumeUnit implements IMeasurable {
        LITRE(1.0),
        MILLILITRE(0.001),
        GALLON(3.78541);

        private final double factor;

        VolumeUnit(double factor) {
            this.factor = factor;
        }

        public double getConversionFactor() { return factor; }
        public double convertToBaseUnit(double value) { return value * factor; }
        public double convertFromBaseUnit(double baseValue) { return baseValue / factor; }
        public String getUnitName() { return name(); }
    }

    static class Quantity<U extends IMeasurable> {
        private final double value;
        private final U unit;

        Quantity(double value, U unit) {
            if (!Double.isFinite(value) || unit == null) {
                throw new IllegalArgumentException();
            }
            this.value = value;
            this.unit = unit;
        }

        double toBase() {
            return unit.convertToBaseUnit(value);
        }

        public Quantity<U> convertTo(U targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException();

            double base = toBase();
            double converted = targetUnit.convertFromBaseUnit(base);

            return new Quantity<>(round(converted), targetUnit);
        }

        public Quantity<U> add(Quantity<U> other) {
            return add(other, this.unit);
        }

        public Quantity<U> add(Quantity<U> other, U targetUnit) {
            if (other == null || targetUnit == null) {
                throw new IllegalArgumentException();
            }

            double sum = this.toBase() + other.toBase();
            double result = targetUnit.convertFromBaseUnit(sum);

            return new Quantity<>(round(result), targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity<?> other = (Quantity<?>) obj;

            if (this.unit.getClass() != other.unit.getClass()) return false;

            return Double.compare(this.toBase(), other.toBase()) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBase());
        }

        @Override
        public String toString() {
            return value + " " + unit.getUnitName();
        }

        private double round(double val) {
            return Math.round(val * 100.0) / 100.0;
        }
    }

    static class QuantityMeasurementApp {

        <U extends IMeasurable> boolean compare(Quantity<U> q1, Quantity<U> q2) {
            return q1.equals(q2);
        }

        <U extends IMeasurable> Quantity<U> convert(Quantity<U> q, U targetUnit) {
            return q.convertTo(targetUnit);
        }

        <U extends IMeasurable> Quantity<U> add(Quantity<U> q1, Quantity<U> q2) {
            return q1.add(q2);
        }

        <U extends IMeasurable> Quantity<U> add(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
            return q1.add(q2, targetUnit);
        }
    }

    public static void main(String[] args) {

        QuantityMeasurementApp app = new QuantityMeasurementApp();

        // Volume usage
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        System.out.println(app.compare(v1, v2)); // true

        Quantity<VolumeUnit> v3 = v1.convertTo(VolumeUnit.GALLON);
        System.out.println(v3); // ~0.26 GALLON

        Quantity<VolumeUnit> sum = v1.add(v2, VolumeUnit.LITRE);
        System.out.println(sum); // 2.0 LITRE
    }
}