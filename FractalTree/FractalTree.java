import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FractalTree extends JPanel
        implements KeyListener {

    // Main branch spread angle
    private double branchAngle = 25;

    // Tree settings
    private int depth = 11;
    private double scaleFactor = 0.72;

    // Slider
    private JSlider angleSlider;

    public FractalTree(JSlider slider) {

        this.angleSlider = slider;

        setPreferredSize(new Dimension(1000, 800));

        setBackground(Color.WHITE);

        setFocusable(true);

        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(Color.BLACK);

        int startX = getWidth() / 2;
        int startY = getHeight() - 100;

        // Initial trunk
        drawTree(
                g2,
                startX,
                startY,
                180,
                -90,
                depth
        );

        drawUI(g2);
    }

    private void drawTree(Graphics2D g2,
                          double x1,
                          double y1,
                          double length,
                          double angle,
                          int depth) {

        if (depth == 0 || length < 2) {
            return;
        }

        double radians = Math.toRadians(angle);

        double x2 = x1 + Math.cos(radians) * length;
        double y2 = y1 + Math.sin(radians) * length;

        // Branch thickness decreases with depth
        g2.setStroke(new BasicStroke(
                Math.max(1f, depth * 0.8f),
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));

        g2.drawLine(
                (int)x1,
                (int)y1,
                (int)x2,
                (int)y2
        );

        // Left branch
        drawTree(
                g2,
                x2,
                y2,
                length * scaleFactor,
                angle - branchAngle,
                depth - 1
        );

        // Right branch
        drawTree(
                g2,
                x2,
                y2,
                length * scaleFactor,
                angle + branchAngle,
                depth - 1
        );
    }

    private void drawUI(Graphics2D g2) {

        g2.setFont(new Font("Arial", Font.BOLD, 18));

        g2.drawString(
                "LEFT / RIGHT = Rotate Branch Spread",
                20,
                35
        );

        g2.drawString(
                "Use Slider Below To Adjust Angle",
                20,
                65
        );

        g2.drawString(
                "Current Branch Angle: " + branchAngle,
                20,
                95
        );
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                branchAngle -= 2;
                break;

            case KeyEvent.VK_RIGHT:
                branchAngle += 2;
                break;
        }

        // Keep slider synced with keys
        angleSlider.setValue((int) branchAngle);

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {

        JFrame frame =
                new JFrame("Interactive Fractal Tree");

        // Slider setup
        JSlider slider =
                new JSlider(JSlider.HORIZONTAL,
                        0,
                        90,
                        25);

        slider.setMajorTickSpacing(10);

        slider.setMinorTickSpacing(2);

        slider.setPaintTicks(true);

        slider.setPaintLabels(true);

        FractalTree panel =
                new FractalTree(slider);

        // Slider listener
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                panel.branchAngle =
                        slider.getValue();

                panel.repaint();
            }
        });

        frame.setLayout(new BorderLayout());

        frame.add(panel, BorderLayout.CENTER);

        frame.add(slider, BorderLayout.SOUTH);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setVisible(true);

        panel.requestFocusInWindow();
    }
}