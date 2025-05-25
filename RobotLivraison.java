import java.util.Scanner;

public class RobotLivraison extends RobotConnecte {
    private String colisActuel; //. Contient le nom ou la description du colis actuellement
    private String destination; // Lieu où le colis doit être livré
    private boolean enlivraison; // si le robot est en cours de livraison.(false=robot est disponible)
    private float distance=0;
    private static final int ENERGIE_LIVRAISON=15;//l’énergie nécessaire pour effectuer une livraison complète sinon rise EnergieInsuffisanteException
    private static final int ENERGIE_CHARGEMENT=5;//L’énergie nécessaire pour charger un colis sinon (EnergieInsuffisanteException
    
    public String getColisActuel() { return colisActuel; }
    public void setColisActuel(String colisActuel) { this.colisActuel = colisActuel; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public boolean isEnlivraison() { return enlivraison; }
    public void setEnlivraison(boolean enlivraison) { this.enlivraison = enlivraison; }
    public float getDistance() { return distance; } 
    public void setDistance(float distance) { this.distance = distance; }
    public RobotLivraison(String id, int x, int y) {
        super(id, x, y);
        this.colisActuel = "0";
        this.destination = null ; 
        this.enlivraison= false;
    }

    @Override
    public void deplacer(int row, int col)  throws RobotException{
        int currentcol = this.getY(); 
        int currentrow = this.getX();
       
        float distance2=(float) Math.sqrt(Math.pow(row - currentrow, 2) + Math.pow(col - currentcol, 2));
        if (distance>100){
            throw new RobotException("distance supérieur à 100 unités");
        }
        this.distance=distance2;
        //this.verifierMaintenance();
        this.verifierEnergie((double)distance*0.3);
        this.consommerEnergie((double)distance*0.3);
        this.setHeuresUtilisation(this.getHeuresUtilisation() + Math.round(distance / 10));
       //this.verifierMaintenance();
        this.setX(row);
        this.setY(col);
        this.ajouterHistorique("le robot a été deplacer ");
    }

    public void FaireLivraison(int Destrow, int Destcol) throws RobotException {
        if (!this.enlivraison) {
        throw new RobotException("Aucun colis à livrer.");
    }
        this.deplacer(Destrow, Destcol); 
        this.consommerEnergie((double)ENERGIE_LIVRAISON);
        this.verifierMaintenance();
        this.colisActuel="0";
        this.enlivraison=false;
        this.ajouterHistorique("Livraison terminée à "+this.destination);
        this.destination = null; 
    }
    public void chargercolis(String destination, String colis) throws EnergieInsuffisanteException, RobotException {
        if(!this.enlivraison && this.colisActuel.equals("0") ){

            this.verifierEnergie((double)ENERGIE_CHARGEMENT);
            this.colisActuel="1 "+colis;
            this.destination=destination;
            this.enlivraison = true; 
            this.consommerEnergie((double)ENERGIE_CHARGEMENT);
            this.ajouterHistorique("le robot a chargé une colis à "+destination);
        
        }else{
            throw new RobotException(" le robot n'est pas libre il ne peut pas charger la colis ");
        }
    };

    @Override
    public String toString() {
        String bool="non";
        if (this.isConnecte()){bool="oui";}
        return "[ ID : "+this.getId()+" , Position : ("+this.getX()+","+this.getY()+")"+
        " , Energie :"+this.getEnergie()+"% , Heures : "+this.getHeuresUtilisation()+"h , Colis :"+this.colisActuel+", Destionation : "+this.destination+ ", Connecté : "+bool+ "]" ;
    }

    @Override
    public void effectuerTache() throws RobotException {
        if(!this.isEnMarche()){
            throw new RobotException("Le robot doit être démarré pour effectuer une tâche ");
        }
        if(this.enlivraison){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez la coordonnée x de la destination : ");
            int x = scanner.nextInt();

            System.out.print("Entrez la coordonnée y de la destination : ");
            int y = scanner.nextInt();

            this.FaireLivraison(x,y);
           // scanner.close();
            }else{
                Scanner scanner = new Scanner(System.in);
                System.out.print("voulez vous charger un nouveau colis ? (oui/non) ");
                String answer = scanner.nextLine();
                if(answer.equals("oui")){
                    this.verifierEnergie(ENERGIE_CHARGEMENT);
                    System.out.print("donner la destination ");
                    String dest = scanner.nextLine();
                    this.chargercolis(dest,dest);
                }else if(answer.equals("non")){
                    this.ajouterHistorique("En attente de colis ");
                }else{throw new RobotException("answer needs to be 'oui/non' ");}


            }
    }
    
    
}
