package SimulatorCore;

import java.util.ArrayList;

public class Path {
    private final ArrayList<Pair<Integer, Location>> trajectory;
    private final int startTime;
    private int pathIndex;
    private Location rackLocation;
    private Location pickerLocation;

    public Path(ArrayList<Pair<Integer, Location>> trajectory, int startTime) {
        this.trajectory = trajectory;
        this.startTime = startTime;
        this.pathIndex = 0;
    }

    public int getStartTIme() {
        return this.startTime;
    }

    public ArrayList<Pair<Integer, Location>> getTrajectory() {
        return this.trajectory;
    }

    public void setRackLocation(Location rackLocation) {
        this.rackLocation = rackLocation;
    }

    public void setPickerLocation(Location pickerLocation) {
        this.pickerLocation = pickerLocation;
    }

    public Location nextLocation(int currentTime) {
        if (pathIndex >= trajectory.size()) return null;
        int duration = currentTime - this.startTime;
        if (duration >= this.trajectory.get(this.pathIndex).getKey()) {
            ++pathIndex;
            return this.trajectory.get(this.pathIndex - 1).getValue();
        }
        return null;
    }

    public boolean isLocatedRack() {
        if (this.pathIndex == 0) return false;
        return this.trajectory.get(this.pathIndex - 1).getValue().equals(this.rackLocation);
    }

    public boolean isLocatedPicker() {
        return this.trajectory.get(this.pathIndex - 1).getValue().equals(this.pickerLocation);
    }

    public void extend(Path p) {
        int timeOffset = this.trajectory.get(this.trajectory.size() - 1).getKey();
        this.trajectory.remove(this.trajectory.size() - 1);
        for (Pair<Integer, Location> stp: p.getTrajectory()) {
            this.trajectory.add(new Pair<>(timeOffset + stp.getKey(), stp.getValue()));
        }
    }

    public int getFinishTime() {
        return this.startTime + this.trajectory.get(this.trajectory.size() - 1).getKey();
    }
}
