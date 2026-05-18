import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.Stack;

public class LSystemFractalTree extends JPanel implements KeyListener {

    private String axiom = "F";
    private String sentence = axiom;

    private int iterations = 5;
    private double angle = 25;
    private double length = 120;

    public LSystemFractalTree() {
        setPreferredSize(new Dimension(1100, 850));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        generateSystem();
    }

    private void generateSystem() {
        sentence = axiom;

        for (int i = 0; i < iterations; i++) {
            StringBuilder next = new StringBuilder();

            for (char c : sentence.toCharArray()) {
                if (c == 'F') {
                    next.append("F[+F]F[-F]F");
                } else {
                    next.append(c);
                }
            }

            sentence = next.toString();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(Color.BLACK);

        drawLSystem(g2);
        drawUI(g2);
    }

    private void drawLSystem(Graphics2D g2) {
        double x = getWidth() / 2.0;
        double y = getHeight() - 100.0;
        double currentAngle = -90;

        Stack<State> stack = new Stack<>();

        double drawLength = length / Math.pow(1.75, iterations);

        g2.setStroke(new BasicStroke(1.2f));

        for (char c : sentence.toCharArray()) {
            if (c == 'F') {
                double radians = Math.toRadians(currentAngle);

                double newX = x + Math.cos(radians) * drawLength;
                double newY = y + Math.sin(radians) * drawLength;

                g2.draw(new Line2D.Double(x, y, newX, newY));

                x = newX;
                y = newY;
            }

            else if (c == '+') {
                currentAngle += angle;
            }

            else if (c == '-') {
                currentAngle -= angle;
            }

            else if (c == '[') {
                stack.push(new State(x, y, currentAngle));
            }

            else if (c == ']') {
                State previous = stack.pop();
                x = previous.x;
                y = previous.y;
                currentAngle = previous.angle;
            }
        }
    }

    private void drawUI(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 17));

        g2.drawString("L-System Fractal Tree", 20, 35);
        g2.drawString("Rule: F → F[+F]F[-F]F", 20, 65);
        g2.drawString("LEFT / RIGHT = Change Angle", 20, 95);
        g2.drawString("UP / DOWN = Change Iterations", 20, 125);

        g2.drawString("Angle: " + angle, 20, 170);
        g2.drawString("Iterations: " + iterations, 20, 200);
        g2.drawString("Sentence Length: " + sentence.length(), 20, 230);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            angle -= 2;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            angle += 2;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (iterations < 7) {
                iterations++;
                generateSystem();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (iterations > 1) {
                iterations--;
                generateSystem();
            }
        }

        repaint();
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    private static class State {
        double x;
        double y;
        double angle;

        State(double x, double y, double angle) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("L-System Fractal Tree");

        LSystemFractalTree panel = new LSystemFractalTree();

        frame.add(panel);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        panel.requestFocusInWindow();
    }
}