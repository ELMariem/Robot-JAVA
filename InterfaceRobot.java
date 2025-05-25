import java.awt.*;
import javax.swing.*;

public class InterfaceRobot extends JFrame {
    private RobotLivraison robot = new RobotLivraison("R1", 0, 0);
    private JTextArea historique = new JTextArea();

    public InterfaceRobot() {
        setTitle("Simulateur Robot Livraison");
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton demarrerBtn = new JButton("Démarrer");
        JButton connecterBtn = new JButton("Connecter");
        JButton effectuerBtn = new JButton("Effectuer Tâche");

        topPanel.add(demarrerBtn);
        topPanel.add(connecterBtn);
        topPanel.add(effectuerBtn);
        add(topPanel, BorderLayout.NORTH);

        historique.setEditable(false);
        add(new JScrollPane(historique), BorderLayout.CENTER);

        demarrerBtn.addActionListener(e -> {
            try {
                robot.demarrer();
                historique.append("Robot démarré\n");
            } catch (RobotException ex) {
                historique.append("Erreur: " + ex.getMessage() + "\n");
            }
        });

        connecterBtn.addActionListener(e -> {
            try {
                robot.connecter("reseau tt");
                historique.append("Robot connecté\n");
            } catch (EnergieInsuffisanteException ex) {
                historique.append("Erreur: " + ex.getMessage() + "\n");
            } catch (MaintenanceRequiseException ex) {
                historique.append("Erreur: " + ex.getMessage() + "\n");
            }
        });

        effectuerBtn.addActionListener(e -> {
            try {
                robot.effectuerTache();
                historique.append("Tâche effectuée\n");
            } catch (RobotException ex) {
                historique.append("Erreur: " + ex.getMessage() + "\n");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceRobot().setVisible(true));
    }
}