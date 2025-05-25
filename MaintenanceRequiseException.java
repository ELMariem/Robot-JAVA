// Si le robot a trop travaillé (>100h).
public class MaintenanceRequiseException extends RobotException {
    public MaintenanceRequiseException (String st) {
        super(st);
    }  
}
