import java.awt.*;
import javax.swing.*;

public class MAP extends JPanel {

    private final int rows = 9;
    private final int cols = 9;
    private Image TunisIcon, AlgerieIcon, turkIcon, croatieIcon;
    private Image ChamsIcon, robotIcon, PalestineIcon, RussieIcon, ItalyIcon,maintenanceIcon, ConnectIcon;

    private final int TunisRow = 6, TunisCol = 3;
    private final int AlgerieRow = 6, AlgerieCol = 1;
    private final int CroatieRow = 3, CroatieCol = 4;
    private final int TurkRow = 3, TurkCol = 6;
    private final int PalestineRow = 6, PalestineCol = 6;
    private final int RussieRow = 2, RussieCol = 7;
    private final int ItalyRow = 4, ItalyCol = 3;
    private final int ConnectRow = 8, ConnectCol = 0;
    private final int maintenanceRow = 8, maintenanceCol = 7;   
    private int RobotRow = -1, RobotCol = -1;
    public int ChamsRow = 0, ChamsCol = 8;

    public MAP() {
        TunisIcon = loadImage("tunis.png");
        ChamsIcon = loadImage("soleil.png");
        AlgerieIcon = loadImage("algerie.png");
        turkIcon = loadImage("turquie.png");
        croatieIcon = loadImage("croatie.png");
        robotIcon = loadImage("assistant-robot.png");
        PalestineIcon = loadImage("palestine.png");
        RussieIcon = loadImage("russie.png");
        ItalyIcon = loadImage("italie.png");
        ConnectIcon = loadImage("lie.png");
        maintenanceIcon = loadImage("reparation.png");
    }

    private Image loadImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        return icon.getIconWidth() == -1 ? null : icon.getImage();
    }

    public void resetRobot() {
        RobotRow = -1;
        RobotCol = -1;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int cellWidth = width / cols;
        int cellHeight = height / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * cellWidth;
                int y = row * cellHeight;

                g.setColor(new Color(237, 187, 153));
                g.fillRect(x, y, cellWidth, cellHeight);
                g.setColor(new Color(200, 200, 200));
                g.fillRect(x, y, cellWidth, cellHeight);
                g.setColor(Color.white);
                g.drawRect(x, y, cellWidth, cellHeight);

                if (row == TunisRow && col == TunisCol && TunisIcon != null)
                    g.drawImage(TunisIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == AlgerieRow && col == AlgerieCol && AlgerieIcon != null)
                    g.drawImage(AlgerieIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == TurkRow && col == TurkCol && turkIcon != null)
                    g.drawImage(turkIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == CroatieRow && col == CroatieCol && croatieIcon != null)
                    g.drawImage(croatieIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == RobotRow && col == RobotCol && robotIcon != null)
                    g.drawImage(robotIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == ChamsRow && col == ChamsCol && ChamsIcon != null)
                    g.drawImage(ChamsIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == PalestineRow && col == PalestineCol && PalestineIcon != null)
                    g.drawImage(PalestineIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == RussieRow && col == RussieCol && RussieIcon != null)
                    g.drawImage(RussieIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == ItalyRow && col == ItalyCol && ItalyIcon != null)
                    g.drawImage(ItalyIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);

                if (row == ConnectRow && col == ConnectCol && ConnectIcon != null)
                    g.drawImage(ConnectIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);
                if (row == maintenanceRow && col == maintenanceCol && maintenanceIcon != null)
                    g.drawImage(maintenanceIcon, x + 5, y + 5, cellWidth - 10, cellHeight - 10, this);    
            }
        }
    }

    public void setRobotPosition(int row, int col) {
        this.RobotRow = row;
        this.RobotCol = col;
        repaint();
    }
}
