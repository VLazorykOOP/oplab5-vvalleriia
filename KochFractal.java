import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KochFractal extends JFrame {
    private JTextField textBox1, textBox2, textBox3, textBox4, textBox5, textBox6, textBox7;
    private JButton drawButton;
    private JPanel drawPanel;

    public KochFractal() {
        setTitle("Koch Fractal Drawer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 7));

        textBox1 = new JTextField("100");
        textBox2 = new JTextField("100");
        textBox3 = new JTextField("400");
        textBox4 = new JTextField("100");
        textBox5 = new JTextField("250");
        textBox6 = new JTextField("400");
        textBox7 = new JTextField("4");

        inputPanel.add(new JLabel("P1 X:"));
        inputPanel.add(textBox1);
        inputPanel.add(new JLabel("P1 Y:"));
        inputPanel.add(textBox2);
        inputPanel.add(new JLabel("P2 X:"));
        inputPanel.add(textBox3);
        inputPanel.add(new JLabel("P2 Y:"));
        inputPanel.add(textBox4);
        inputPanel.add(new JLabel("P3 X:"));
        inputPanel.add(textBox5);
        inputPanel.add(new JLabel("P3 Y:"));
        inputPanel.add(textBox6);
        inputPanel.add(new JLabel("Depth:"));
        inputPanel.add(textBox7);

        drawButton = new JButton("Draw Koch Fractal");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.repaint();
            }
        });

        inputPanel.add(drawButton);
        add(inputPanel, BorderLayout.NORTH);

        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawKochFractal(g);
            }
        };
        add(drawPanel, BorderLayout.CENTER);
    }

    public static float crossProductLength(PointF A, PointF B, PointF C) {
        float BAx = A.x - B.x;
        float BAy = A.y - B.y;
        float BCx = C.x - B.x;
        float BCy = C.y - B.y;
        return (BAx * BCy - BAy * BCx);
    }

    private void drawKochFractal(Graphics g) {
        try {
            Point p1 = new Point(Integer.parseInt(textBox1.getText()), Integer.parseInt(textBox2.getText()));
            Point p2 = new Point(Integer.parseInt(textBox3.getText()), Integer.parseInt(textBox4.getText()));
            Point p3 = new Point(Integer.parseInt(textBox5.getText()), Integer.parseInt(textBox6.getText()));
            int depth = Integer.parseInt(textBox7.getText());

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
            g2d.setColor(Color.BLACK);

            drawKochFractal(g2d, p1, p2, p3, depth);
            drawKochFractal(g2d, p2, p3, p1, depth);
            drawKochFractal(g2d, p3, p1, p2, depth);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Wrong input");
        }
    }

    private void drawKochFractal(Graphics g, Point P1, Point P2, Point PControl, int K) {
        if (K == 0) {
            g.drawLine(P1.x, P1.y, P2.x, P2.y);
        } else {
            float dx = P2.x - P1.x;
            float dy = P2.y - P1.y;

            Point P3 = new Point((int) (P1.x + dx / 3), (int) (P1.y + dy / 3));
            Point P4 = new Point((int) (P1.x + 2 * dx / 3), (int) (P1.y + 2 * dy / 3));

            float angle = (float) (Math.PI / 3) * Math.signum(crossProductLength(new PointF(P1.x, P1.y), new PointF(P2.x, P2.y), new PointF(PControl.x, PControl.y)));

            float cosAngle = (float) Math.cos(angle);
            float sinAngle = (float) Math.sin(angle);

            Point P5 = new Point(
                (int) (P3.x + (dx / 3) * cosAngle - (dy / 3) * sinAngle),
                (int) (P3.y + (dx / 3) * sinAngle + (dy / 3) * cosAngle)
            );

            drawKochFractal(g, P1, P3, PControl, K - 1);
            drawKochFractal(g, P3, P5, PControl, K - 1);
            drawKochFractal(g, P5, P4, PControl, K - 1);
            drawKochFractal(g, P4, P2, PControl, K - 1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new KochFractal().setVisible(true);
            }
        });
    }

    private class PointF {
        float x, y;

        PointF(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
