public class RobotException extends Exception {
    public RobotException(){
        System.out.println("Manque d'energie");
    }
    public RobotException(String st) {
        super(st);
    }  
}
