package SimulatorCore;

import java.util.HashMap;

public enum PickerGroup {
    INSTANCE;
    PickerGroup() {}
    private static final HashMap<Integer, Picker> idToPicker = Ground.INSTANCE.getIdToPicker();

    public boolean update(int t) {
        return idToPicker.values().stream().map(p -> p.picking(t)).reduce(Boolean::logicalOr).orElse(Boolean.FALSE);
    }

    public void outputStatistics(String prefix) {
        idToPicker.values().forEach(p -> System.out.println(p.statistics(prefix)));
    }

    public Picker getPickerById(int pickerId) {
        return idToPicker.get(pickerId);
    }

    public int getPickerNum() {
        return idToPicker.size();
    }

    public double getWorkingRate() {
        return idToPicker.values().stream().mapToDouble(Picker::getSingleWorkingRate).average().orElse(0.);
    }
}
