public abstract class RobotConnecte extends Robot implements Connectable {
    private boolean connecte; // Indique si le robot est actuellement connecté à un réseau.
    private String reseauConnecte; // Nom du réseau auquel le robot est connecté

    public boolean isConnecte() { return connecte; }
    public void setConnecte(boolean connecte) { this.connecte = connecte; }
    public String getReseauConnecte() { return reseauConnecte; }
    public void setReseauConnecte(String reseauConnecte) { this.reseauConnecte = reseauConnecte; }
    
    public RobotConnecte(String id,int x,int y){
        super();
        this.setId(id);
        this.setX(x);
        this.setY(y);
        connecte=false;
        reseauConnecte=null;
    }

	@Override
	public void connecter(String reseau) throws EnergieInsuffisanteException, MaintenanceRequiseException {
		this.verifierMaintenance();
        this.verifierEnergie(5);
        this.reseauConnecte= reseau;
        this.connecte=true;
        this.consommerEnergie(5);
        this.ajouterHistorique("Robot est connecté à"+ reseau);
	}

	@Override
	public void deconnecter() {
        this.ajouterHistorique("Robot est deconnecté à"+ this.reseauConnecte);
        this.connecte=false;
        this.reseauConnecte=null;
    }

	@Override
	public void envoyerDonnees(String donnees) throws EnergieInsuffisanteException, MaintenanceRequiseException,RobotException {
		this.verifierMaintenance();
        if(!this.connecte){
            throw new RobotException("le robot n'est pas connecté à un reseau!!");
        }
        this.verifierEnergie(3);
        this.consommerEnergie(3);
        this.ajouterHistorique("Le robot a envoyé les données suivantes "+ donnees);
    }
}