import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

abstract class Robot {
    private String id; 
    private int x, y, heuresUtilisation;
    private double energie;
    //x et y Position ; energie c'est niveau denergie en% ; HU nbr dheure avant maintenance
    private List<String> historiqueActions;
    private boolean enMarche; //si le robot active ou inactive
    // constructeur
    public Robot() {
        this.historiqueActions = new ArrayList<>();
        energie=100;
        heuresUtilisation=0;
        this.ajouterHistorique("Robot créé ");
    }
    //getters et setters :
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public double getEnergie() { return energie; }
    public void setEnergie(double energie) { this.energie = energie; }
    public int getHeuresUtilisation() { return heuresUtilisation; }
    public void setHeuresUtilisation(int heuresUtilisation) { this.heuresUtilisation = heuresUtilisation; }
    public List<String> getHistoriqueActions() { return historiqueActions; }
    public void setHistoriqueActions(List<String> historiqueActions) { this.historiqueActions = historiqueActions; }
    public boolean isEnMarche() { return enMarche; }
    public void setEnMarche(boolean enMarche) { this.enMarche = enMarche; }

    public void ajouterHistorique(String act){
        LocalDateTime maintenant = LocalDateTime.now();
        // Formater la date et l'heure en String
        DateTimeFormatter formatteur = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss", Locale.FRENCH);
        String dateHeureActuelle = maintenant.format(formatteur);

        historiqueActions.add(dateHeureActuelle+" "+act);
        System.err.println(historiqueActions);
    }

    public void verifierEnergie(double energieRequise) throws EnergieInsuffisanteException{
        if (this.getEnergie()< energieRequise){
            throw new EnergieInsuffisanteException("Energie est insuffisante");       
        }
    }
    public void verifierMaintenance() throws MaintenanceRequiseException {
        if (this.getHeuresUtilisation() >=5) {
            throw new MaintenanceRequiseException("Le robot a besoin de maintenance");
        }
    }
    public void demarrer() throws RobotException{
        if (this.getEnergie()>10){
            this.setEnMarche(true);
            this.ajouterHistorique("Robot en marche");
        }else{
            this.ajouterHistorique("Robot ne peut pas demarrer");
            throw new RobotException("Manque D'energie");

        }
    }
    public void arreter(){
        this.setEnMarche(false);
            this.ajouterHistorique("Robot est arreté");
    }
    public void consommerEnergie(double quantite)throws EnergieInsuffisanteException{
        if(this.getEnergie()-quantite>0){
            this.setEnergie(this.getEnergie()-quantite); 
        }else{
            throw new EnergieInsuffisanteException("Energie est insuffisante");       
        }
    }
    public void recharger(int quantite){
        if(this.getEnergie()+quantite<100){
            this.setEnergie(this.getEnergie()+quantite); 
        }else{
            this.setEnergie(100);
            System.out.println("la batterie est completement charger");
        }
    }
    public abstract  void deplacer(int x,int y) throws RobotException ;;
    public abstract  void effectuerTache() throws RobotException ;;
    String getHistorique(){
        return String.join("\n", this.historiqueActions); // Joins each action with a newline
    }
    @Override
    public String toString() {
        return "RobotIndustriel [ID : " + this.id + ", Position : (" + x + "," + y + ") , énergie : " + energie + 
               "% , Heure : " + heuresUtilisation + "]";
    }
}
