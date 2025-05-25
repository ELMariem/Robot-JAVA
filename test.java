import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class test {
 
    private static RobotLivraison robot;
    private static MAP map;
    private static JProgressBar energyBar; // energie de robot en %
    private static JProgressBar pollutionBar; // pollution engendré par de robot en %

   private static void increasePollution(int pol) {
    int currentPollution = pollutionBar.getValue();
    int newPollution = Math.min(currentPollution + pol, 100); // max de pollution 100%
    pollutionBar.setValue(newPollution);

    if (newPollution >= 90) {
        showPollutionDialog(); // bloquer la fenetre jusqu'a pollution<=90
    }
}
private static void showPollutionDialog() {
   
    JDialog dialog = new JDialog();
    dialog.setTitle("Pollution maximale !");
    dialog.setSize(1000, 500);
    dialog.setLayout(new BorderLayout());
    dialog.setLocationRelativeTo(null);
    dialog.getContentPane().setBackground(new Color(255, 204, 204)); 

    JLabel Label = new JLabel("Le robot est bloqué (pollution is >90 !!)", SwingConstants.CENTER);
    Label.setFont(new Font("Arial", Font.BOLD, 14));
    dialog.add(Label, BorderLayout.CENTER);

    dialog.setVisible(true);

    Timer countdownTimer = new Timer(500, new ActionListener() { //chek evey 500ms if the pollution is under 90% 
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if ( pollutionBar.getValue()<90 ) {
                dialog.dispose(); 
            }
        }
    });

    countdownTimer.start();
}

private static Timer pollutionDecreaseTimer;

private static void startPollutionDecreaseTimer() {
    pollutionDecreaseTimer = new Timer(3000, e -> { 
        int currentPollution = pollutionBar.getValue();
        if (currentPollution > 0) {
            pollutionBar.setValue(Math.max(currentPollution - 2, 0)); // Decrease pollution by 10% every 3 second
        }
    });
    pollutionDecreaseTimer.start();
}
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Gestion Robot Livraison");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());
        
        map = new MAP();

        JPanel statusPanel = new JPanel(new BorderLayout());
        JLabel statusMessage = new JLabel("Créez un robot pour commencer.", SwingConstants.CENTER);
        statusMessage.setFont(new Font("Arial", Font.BOLD,18)); 
        statusMessage.setBackground(new Color(127, 179, 213 )); 
        statusMessage.setOpaque(true);
        statusMessage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        statusPanel.add(statusMessage, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new GridLayout(2, 4, 0, 0)); //les buttons de controle
        JPanel energyPanel = new JPanel(new BorderLayout()); 
        JPanel pollutionPanel = new JPanel(new BorderLayout());

        energyBar = new JProgressBar(0, 100); 
        //energyBar.setValue(100); // Start with full energy
        energyBar.setStringPainted(true); 
        energyPanel.add(new JLabel("Énergie actuelle 🔋 "), BorderLayout.NORTH);
        energyBar.setForeground(new Color(34, 153, 84));
        energyPanel.add(energyBar, BorderLayout.CENTER);
        energyPanel.setVisible(false);

        pollutionBar = new JProgressBar(0, 100);
        pollutionBar.setValue(0); // begin with 0% pollution
        pollutionBar.setForeground(new Color(205, 97, 85)); // couleur rouge
        pollutionBar.setStringPainted(true);
        pollutionPanel.add(new JLabel("Niveau de pollution :"), BorderLayout.NORTH);
        pollutionPanel.add(pollutionBar, BorderLayout.CENTER);
        pollutionPanel.setVisible(false); 


        JButton btnCreer = new JButton("Créer Robot");
        JButton btnDemarrer = new JButton("Démarrer Robot");
        JButton btnCharger = new JButton("Charger Colis");
        JButton btnLivrer = new JButton("Livrer");
        JButton btnRecharger = new JButton("Recharger 10% Énergie"); 
        JButton btnRechargeEcologique = new JButton("Recharge Écologique");
        JButton btnHistorique = new JButton("Afficher Historique");
        JButton btnRegleLivraison = new JButton("Règle de la Livraison");
        JButton btnConnect = new JButton("Connecter");
        JButton btnDeconnect = new JButton("Déconnecter");
        JButton btnMaintenance = new JButton("Maintenance");
        JButton btnLibererColis = new JButton("Libérer Colis");

        // creation du robot
btnCreer.addActionListener(e -> {
            JTextField idField = new JTextField();
            JTextField energyField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            panel.add(new JLabel("ID du robot :"));
            panel.add(idField);
            panel.add(new JLabel("Énergie initiale (max 100) :"));
            panel.add(energyField);
            
            int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Créer un robot",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {

                String id = idField.getText().trim();
                int initialEnergy = Integer.parseInt(energyField.getText().trim());

                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "L'ID du robot ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (initialEnergy < 0 || initialEnergy > 100) {
                    JOptionPane.showMessageDialog(frame, "L'énergie initiale doit être entre 0 et 100.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                robot = new RobotLivraison(id, 0, 0);
                robot.setEnergie(initialEnergy); 
                map.setRobotPosition(0, 0);//position initiale de robot
                
                startPollutionDecreaseTimer(); // polution decrease by 2% every 3 seconde
                statusPanel.remove(statusMessage);
                statusPanel.setLayout(new GridLayout(2, 1)); 
                statusPanel.add(energyPanel); // Add energyPanel
                statusPanel.add(pollutionPanel); // Add pollutionPanel
                energyPanel.setVisible(true);
                pollutionPanel.setVisible(true);
                energyBar.setValue((int) robot.getEnergie());// update the robot energy 

                statusMessage.setText("Démarrez le robot pour commencer .");
                
               } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre valide pour l'énergie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
});

        // demarrage du robot
btnDemarrer.addActionListener(e -> {
            if (robot == null) {
                JOptionPane.showMessageDialog(frame, "Créez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                robot.demarrer();
                statusPanel.revalidate();
                statusPanel.repaint();
            } catch (RobotException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });


                // Charger l'energie de robot (methode non ecologique)
btnRecharger.addActionListener(e -> {
            if (robot == null) {
                JOptionPane.showMessageDialog(frame, "Créez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double currentEnergy = robot.getEnergie();
            if (currentEnergy >= 100) {
                JOptionPane.showMessageDialog(frame, "Le robot est chargé ", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            robot.recharger(10); // Recharge energy by 10%
            increasePollution(20);
            energyBar.setValue((int) robot.getEnergie()); //   Update the energy 
            JOptionPane.showMessageDialog(frame, "+10 % d’énergie, mais +20 % de pollution ! ");
        });

        //Charger l'energie de robot (methode ecologique) que pour energy >10%
btnRechargeEcologique.addActionListener(e -> {
            if (robot == null) {
                JOptionPane.showMessageDialog(frame, "Créez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!robot.isEnMarche()) {
                JOptionPane.showMessageDialog(frame, "Démarrez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(robot.getEnergie()<=10){
                JOptionPane.showMessageDialog(frame, " Pas assez d’énergie pour atteindre le Soleil !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (robot.getX() == map.ChamsRow && robot.getY() == map.ChamsCol) {
                JOptionPane.showMessageDialog(frame, "Le robot est déjà sur Chams. :)", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            double currentEnergy = robot.getEnergie();
            if (currentEnergy >= 100) {
                JOptionPane.showMessageDialog(frame, "Le robot est chargé ", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            try {

                robot.deplacer(map.ChamsRow, map.ChamsCol);
                map.setRobotPosition(map.ChamsRow, map.ChamsCol); // le robot doit deplacer vers le soleil
                robot.setDestination("Chams ☀️");
                robot.recharger(30);
                increasePollution(-30);
                energyBar.setValue((int) robot.getEnergie());
                JOptionPane.showMessageDialog(frame, "Recharge écologique effectuée +30 % d’énergie, mais -30 % de pollution !");
            } catch (RobotException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
});


    // Liste des destinations possibles
    String[] destinations = {"Tunis", "Algérie", "Turquie", "Croatie","Palestine","Italy","Russie"};
    // button chager colis ( le nom de colie + destination)
btnCharger.addActionListener(e -> {
        if (robot == null) {
            JOptionPane.showMessageDialog(frame, "Veuillez d'abord créer le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField colisField = new JTextField(); 
        JComboBox<String> comboBox = new JComboBox<>(destinations);

        panel.add(new JLabel("Nom du colis :"));
        panel.add(colisField);
        panel.add(new JLabel("Destination :"));
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(
        frame,
        panel,
        "Charger un colis",
        JOptionPane.OK_CANCEL_OPTION
    );

    if (result == JOptionPane.OK_OPTION) {
        String colisName = colisField.getText().trim(); 
        String destination = (String) comboBox.getSelectedItem(); 

        if (colisName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Veuillez entrer un nom pour le colis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            robot.chargercolis(destination,colisName);
            increasePollution(10);
            energyBar.setValue((int) robot.getEnergie()); // Update the progress bar with the robot's energy
            JOptionPane.showMessageDialog(frame, "Colis '" + colisName + "' chargé pour : " + destination);
        } catch (RobotException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
});

btnLivrer.addActionListener(e -> {
    if (robot == null) {
        JOptionPane.showMessageDialog(frame, "Créez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (robot.isConnecte() == false) {
        JOptionPane.showMessageDialog(frame, "Le robot doit etre connecter !", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!robot.isEnMarche()) {
        JOptionPane.showMessageDialog(frame, "Démarrez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!robot.isEnlivraison()) {
        JOptionPane.showMessageDialog(frame, "Aucun colis à livrer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String dest = robot.getDestination();
    increasePollution((int)robot.getDistance()*5);
    System.out.println(robot.getDistance());
    int destX = 0, destY = 0;

    switch (dest) {
        case "Tunis" -> { destX = 6; destY = 3; }
        case "Algérie" -> { destX = 6; destY = 1; }
        case "Croatie" -> { destX = 3; destY = 4; }
        case "Turquie" -> { destX = 3; destY = 6; }
        case "Palestine" -> { destX = 6; destY = 6; }
        case "Italy" -> { destX = 4; destY = 3; }
        case "Russie" -> { destX = 2; destY = 7; }
        default -> {
            JOptionPane.showMessageDialog(frame, "Destination inconnue.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; 
        }
    }

    // Move the robot to the destination
    try {
        robot.FaireLivraison(destX, destY);
        map.setRobotPosition(destX, destY); // Update the robot's position on the map
        energyBar.setValue((int) robot.getEnergie()); // Update the robot's energy

        JOptionPane.showMessageDialog(frame, "Livraison effectuée à : " + dest +" 👏");
    } catch (RobotException ex) {
        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
});
btnHistorique.addActionListener(e -> {
    if (robot == null) {
        JOptionPane.showMessageDialog(frame, "Aucun robot n'a été créé.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
btnLibererColis.addActionListener(event -> {
    if (robot == null) {
        JOptionPane.showMessageDialog(frame, "Créez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!robot.isEnlivraison()) {
        JOptionPane.showMessageDialog(frame, "Aucun colis à libérer.", "Information", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    robot.setColisActuel("0");
    robot.setDestination(null);
    robot.setEnlivraison(false);
    robot.ajouterHistorique("Colis libéré, robot disponible.");
    JOptionPane.showMessageDialog(frame, "Le robot est maintenant libre de tout colis.", "Libération", JOptionPane.INFORMATION_MESSAGE);
});
    
    JDialog dialog = new JDialog(frame, "Historique et Informations du Robot", true);
    dialog.setSize(400, 400);
    dialog.setLayout(new BorderLayout());
    dialog.setLocationRelativeTo(frame);

    JTextArea infoArea = new JTextArea();
    infoArea.setEditable(false);

    // robot information
    StringBuilder info = new StringBuilder();
     info.append("État actuel du robot :\n");
    info.append(robot.toString()).append("\n\n");
    info.append("--- Historique ---\n");
    info.append(robot.getHistorique());
    infoArea.setText(info.toString());

    dialog.add(new JScrollPane(infoArea), BorderLayout.CENTER);

    
    JButton btnClose = new JButton("Fermer");
    btnClose.addActionListener(closeEvent -> dialog.dispose());
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(btnClose);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    // Show the dialog
    dialog.setVisible(true);
});
btnRegleLivraison.addActionListener(e -> {
    
    String regles = """
        Règles de la Livraison:
        - Le robot doit être démarré et connecté avant toute livraison.
        - Le colis doit être chargé avant la livraison.
        - La pollution augmente en fonction de la distance parcourue.
        - L'énergie doit être suffisante pour effectuer la livraison.
        - Énergie requise pour chaque action.
        - Pollution augmente selon la distance.
        - Recharge classique ➕ pollution.
        - Recharge écologique ➖ pollution.
        """;
    JOptionPane.showMessageDialog(frame, regles, "Règle de la Livraison", JOptionPane.INFORMATION_MESSAGE);
});


btnConnect.addActionListener(e -> {
    if (robot == null) {
        JOptionPane.showMessageDialog(frame, "Créez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (robot.isConnecte() == true) {
        JOptionPane.showMessageDialog(frame, "le robot est deja connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    String connexion = JOptionPane.showInputDialog( 
        frame,
        "Entrez le nom du réseau ou la chaîne de connexion :",
        "Connexion du Robot",
        JOptionPane.QUESTION_MESSAGE
    );
    if (connexion != null && !connexion.trim().isEmpty()) {
        try {
            
            robot.deplacer(8, 0);
            map.setRobotPosition(8, 0);
            robot.connecter(connexion.trim());
            JOptionPane.showMessageDialog(frame, "Robot connecté à : " + connexion, "Connexion réussie", JOptionPane.INFORMATION_MESSAGE);
            energyBar.setValue((int) robot.getEnergie());
        } catch (RobotException ex1) {
            JOptionPane.showMessageDialog(frame, ex1.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    } else if (connexion != null) {
        JOptionPane.showMessageDialog(frame, "Aucune connexion saisie.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
});
btnDeconnect.addActionListener(e -> {
    if (robot == null) {
        JOptionPane.showMessageDialog(frame, "Aucun robot n'est connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (robot.isConnecte() == false) {
        JOptionPane.showMessageDialog(frame, "le robot n'est pas connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        robot.deconnecter(); 
        JOptionPane.showMessageDialog(frame, "Robot déconnecté.", "Déconnexion", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(frame, "Erreur lors de la déconnexion : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
});
btnMaintenance.addActionListener(e -> {
    if (robot == null) {
        JOptionPane.showMessageDialog(frame, "Créez d'abord le robot.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (robot.getHeuresUtilisation() < 5) {
        JOptionPane.showMessageDialog(frame, "Le robot n'a pas encore besoin de maintenance.", "Information", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    try {
        
        int destX = 8;
        int destY = 7;
        int currentX = robot.getX();
        int currentY = robot.getY();
        double distance = Math.sqrt(Math.pow(destX - currentX, 2) + Math.pow(destY - currentY, 2));
        double energieRequise = distance * 0.3;

        robot.verifierEnergie(energieRequise);
        robot.deplacer(destX, destY);
        map.setRobotPosition(destX, destY);
        robot.setHeuresUtilisation(0);
        robot.ajouterHistorique("Maintenance effectuée (heures remises à 0)");
        energyBar.setValue((int) robot.getEnergie());
        JOptionPane.showMessageDialog(frame, "Maintenance effectuée ! Les heures d'utilisation sont remises à 0.", "Maintenance", JOptionPane.INFORMATION_MESSAGE);
    } catch (RobotException ex1) {
        JOptionPane.showMessageDialog(frame, ex1.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
});
        controlPanel.add(btnCreer);
        controlPanel.add(btnDemarrer);
        controlPanel.add(btnCharger);
        controlPanel.add(btnLivrer);
        controlPanel.add(btnRecharger);
        controlPanel.add(btnRechargeEcologique);
        controlPanel.add(btnConnect);
        controlPanel.add(btnDeconnect);
        controlPanel.add(btnLibererColis);
        controlPanel.add(btnMaintenance);
        controlPanel.add(btnHistorique);
        controlPanel.add(btnRegleLivraison);
        
        frame.add(statusPanel, BorderLayout.NORTH);
        frame.add(map, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(map, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        controlPanel.revalidate();
        controlPanel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);  
    }
     
}

