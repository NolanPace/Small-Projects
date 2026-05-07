import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class AsymmetricalFractalTree extends JPanel
        implements KeyListener {

    // Separate branch angles
    private double leftAngle = 18;
    private double rightAngle = 38;

    // Separate branch scaling
    private double leftScale = 0.72;
    private double rightScale = 0.63;

    private int depth = 11;

    // Sliders
    private JSlider leftSlider;
    private JSlider rightSlider;

    public AsymmetricalFractalTree(
            JSlider leftSlider,
            JSlider rightSlider
    ) {

        this.leftSlider = leftSlider;
        this.rightSlider = rightSlider;

        setPreferredSize(new Dimension(1200, 850));

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

        drawTree(
                g2,
                startX,
                startY,
                210,
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

        double radians =
                Math.toRadians(angle);

        double x2 =
                x1 + Math.cos(radians) * length;

        double y2 =
                y1 + Math.sin(radians) * length;

        // Branch thickness
        g2.setStroke(
                new BasicStroke(
                        Math.max(1f,
                                depth * 0.85f),
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        g2.drawLine(
                (int)x1,
                (int)y1,
                (int)x2,
                (int)y2
        );

        // LEFT branch
        drawTree(
                g2,
                x2,
                y2,
                length * leftScale,
                angle - leftAngle,
                depth - 1
        );

        // RIGHT branch
        drawTree(
                g2,
                x2,
                y2,
                length * rightScale,
                angle + rightAngle,
                depth - 1
        );
    }

    private void drawUI(Graphics2D g2) {

        g2.setFont(
                new Font("Arial",
                        Font.BOLD,
                        18)
        );

        g2.drawString(
                "A / D = Left Angle",
                20,
                35
        );

        g2.drawString(
                "LEFT / RIGHT = Right Angle",
                20,
                65
        );

        g2.drawString(
                "Left Angle: " + leftAngle,
                20,
                110
        );

        g2.drawString(
                "Right Angle: " + rightAngle,
                20,
                140
        );
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            // LEFT branch controls
            case KeyEvent.VK_A:
                leftAngle -= 2;
                break;

            case KeyEvent.VK_D:
                leftAngle += 2;
                break;

            // RIGHT branch controls
            case KeyEvent.VK_LEFT:
                rightAngle -= 2;
                break;

            case KeyEvent.VK_RIGHT:
                rightAngle += 2;
                break;
        }

        // Sync sliders
        leftSlider.setValue((int) leftAngle);

        rightSlider.setValue((int) rightAngle);

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {

        JFrame frame =
                new JFrame(
                        "Asymmetrical Fractal Tree"
                );

        // Left branch slider
        JSlider leftSlider =
                new JSlider(
                        JSlider.HORIZONTAL,
                        0,
                        90,
                        18
                );

        leftSlider.setPaintTicks(true);
        leftSlider.setPaintLabels(true);
        leftSlider.setMajorTickSpacing(10);

        // Right branch slider
        JSlider rightSlider =
                new JSlider(
                        JSlider.HORIZONTAL,
                        0,
                        90,
                        38
                );

        rightSlider.setPaintTicks(true);
        rightSlider.setPaintLabels(true);
        rightSlider.setMajorTickSpacing(10);

        AsymmetricalFractalTree panel =
                new AsymmetricalFractalTree(
                        leftSlider,
                        rightSlider
                );

        // Left slider updates
        leftSlider.addChangeListener(
                new ChangeListener() {
                    @Override
                    public void stateChanged(
                            ChangeEvent e
                    ) {

                        panel.leftAngle =
                                leftSlider.getValue();

                        panel.repaint();
                    }
                }
        );

        // Right slider updates
        rightSlider.addChangeListener(
                new ChangeListener() {
                    @Override
                    public void stateChanged(
                            ChangeEvent e
                    ) {

                        panel.rightAngle =
                                rightSlider.getValue();

                        panel.repaint();
                    }
                }
        );

        JPanel sliderPanel =
                new JPanel(
                        new GridLayout(2, 1)
                );

        sliderPanel.add(leftSlider);

        sliderPanel.add(rightSlider);

        frame.setLayout(new BorderLayout());

        frame.add(panel, BorderLayout.CENTER);

        frame.add(sliderPanel,
                BorderLayout.SOUTH);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setVisible(true);

        panel.requestFocusInWindow();
    }
}