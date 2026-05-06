class QualityMeasurement {

    enum Unit {
        FEET
    }

    static class Quantity {
        double value;
        Unit unit;

        Quantity(double value, Unit unit) {
            this.value = value;
            this.unit = unit;
        }
    }

    static class QuantityMeasurementApp {

        boolean areEqual(Quantity q1, Quantity q2) {
            if (q1 == null || q2 == null) {
                throw new IllegalArgumentException("Invalid input: null values not allowed");
            }

            if (q1.unit != Unit.FEET || q2.unit != Unit.FEET) {
                throw new IllegalArgumentException("Both quantities must be in feet");
            }

            return Math.abs(q1.value - q2.value) < 0.0001;
        }
    }

    public static void main(String[] args) {
        System.out.println("Quantity Measurement App Initialized");

        QuantityMeasurementApp app = new QuantityMeasurementApp();

        Quantity q1 = new Quantity(5.0, Unit.FEET);
        Quantity q2 = new Quantity(5.0, Unit.FEET);

        boolean result = app.areEqual(q1, q2);
        System.out.println(result);
    }
}