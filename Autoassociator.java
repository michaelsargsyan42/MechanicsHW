public class Autoassociator {
    private int[][] weights;
    private int trainingCapacity;

    public Autoassociator(CourseArray courses) {
        int courseCount = courses.length();
        weights = new int[courseCount][courseCount];
        trainingCapacity = 0;
    }

    public int getTrainingCapacity() {
        return trainingCapacity;
    }

    public void training(int[] pattern) {
        int n = pattern.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    weights[i][j] += pattern[i] * pattern[j];
                }
            }
        }
        trainingCapacity++;
    }

    public int unitUpdate(int[] neurons) {
        int n = neurons.length;
        int index = (int) (Math.random() * n);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += weights[index][i] * neurons[i];
        }
        neurons[index] = sum > 0 ? 1 : -1;
        return index;
    }

    public void unitUpdate(int[] neurons, int index) {
        int n = neurons.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += weights[index][i] * neurons[i];
        }
        neurons[index] = sum > 0 ? 1 : -1;
    }

    public void chainUpdate(int[] neurons, int steps) {
        for (int step = 0; step < steps; step++) {
            unitUpdate(neurons);
        }
    }

    public void fullUpdate(int[] neurons) {
        boolean updated;
        do {
            updated = false;
            int[] oldState = neurons.clone();
            for (int i = 0; i < neurons.length; i++) {
                unitUpdate(neurons, i);
            }
            if (!java.util.Arrays.equals(oldState, neurons)) {
                updated = true;
            }
        } while (updated);
    }
}
