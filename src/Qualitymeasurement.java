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

        LengthUnit(double factor) { this.factor = factor; }

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

        WeightUnit(double factor) { this.factor = factor; }

        public double getConversionFactor() { return factor; }
        public double convertToBaseUnit(double value) { return value * factor; }
        public double convertFromBaseUnit(double baseValue) { return baseValue / factor; }
        public String getUnitName() { return name(); }
    }

    enum VolumeUnit implements IMeasurable {
        LITRE(1.0),
        MILLILITRE(0.001),
        GALLON(3.78541);

        private final double factor;

        VolumeUnit(double factor) { this.factor = factor; }

        public double getConversionFactor() { return factor; }
        public double convertToBaseUnit(double value) { return value * factor; }
        public double convertFromBaseUnit(double baseValue) { return baseValue / factor; }
        public String getUnitName() { return name(); }
    }

    static class Quantity<U extends IMeasurable> {
        private final double value;
        private final U unit;

        private enum Operation { ADD, SUBTRACT, DIVIDE }

        Quantity(double value, U unit) {
            if (!Double.isFinite(value) || unit == null) throw new IllegalArgumentException();
            this.value = value;
            this.unit = unit;
        }

        double toBase() {
            return unit.convertToBaseUnit(value);
        }

        private double operate(Quantity<U> other, Operation op) {
            if (other == null) throw new IllegalArgumentException();
            if (this.unit.getClass() != other.unit.getClass()) throw new IllegalArgumentException();
            if (!Double.isFinite(other.value)) throw new IllegalArgumentException();

            double a = this.toBase();
            double b = other.toBase();

            switch (op) {
                case ADD: return a + b;
                case SUBTRACT: return a - b;
                case DIVIDE:
                    if (b == 0.0) throw new IllegalArgumentException();
                    return a / b;
                default: throw new IllegalArgumentException();
            }
        }

        public Quantity<U> convertTo(U targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException();
            double base = toBase();
            return new Quantity<>(round(targetUnit.convertFromBaseUnit(base)), targetUnit);
        }

        public Quantity<U> add(Quantity<U> other) {
            return add(other, this.unit);
        }

        public Quantity<U> add(Quantity<U> other, U targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException();
            double result = operate(other, Operation.ADD);
            return new Quantity<>(round(targetUnit.convertFromBaseUnit(result)), targetUnit);
        }

        public Quantity<U> subtract(Quantity<U> other) {
            return subtract(other, this.unit);
        }

        public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException();
            double result = operate(other, Operation.SUBTRACT);
            return new Quantity<>(round(targetUnit.convertFromBaseUnit(result)), targetUnit);
        }

        public double divide(Quantity<U> other) {
            return operate(other, Operation.DIVIDE);
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

        <U extends IMeasurable> Quantity<U> subtract(Quantity<U> q1, Quantity<U> q2) {
            return q1.subtract(q2);
        }

        <U extends IMeasurable> Quantity<U> subtract(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
            return q1.subtract(q2, targetUnit);
        }

        <U extends IMeasurable> double divide(Quantity<U> q1, Quantity<U> q2) {
            return q1.divide(q2);
        }
    }

    public static void main(String[] args) {

        QuantityMeasurementApp app = new QuantityMeasurementApp();

        Quantity<LengthUnit> l1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(6.0, LengthUnit.INCH);

        System.out.println(app.add(l1, l2));
        System.out.println(app.subtract(l1, l2));
        System.out.println(app.divide(l1, l2));
    }
}