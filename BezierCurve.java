import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BezierCurve extends JFrame {
    private JTextField textBox1, textBox2, textBox3, textBox4, textBox5, textBox6, textBox7, textBox8;
    private JButton drawButton;
    private JPanel drawPanel;

    public BezierCurve() {
        setTitle("Bezier Curve Drawer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 8));

        textBox1 = new JTextField("0");
        textBox2 = new JTextField("0");
        textBox3 = new JTextField("50");
        textBox4 = new JTextField("100");
        textBox5 = new JTextField("150");
        textBox6 = new JTextField("100");
        textBox7 = new JTextField("200");
        textBox8 = new JTextField("0");

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
        inputPanel.add(new JLabel("P4 X:"));
        inputPanel.add(textBox7);
        inputPanel.add(new JLabel("P4 Y:"));
        inputPanel.add(textBox8);

        drawButton = new JButton("Draw Bezier Curve");
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
                drawBezierCurve(g);
            }
        };
        add(drawPanel, BorderLayout.CENTER);
    }

    private void drawBezierCurve(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());

        g2d.setColor(Color.BLACK);
        g2d.drawLine(drawPanel.getWidth() / 2, 0, drawPanel.getWidth() / 2, drawPanel.getHeight());
        g2d.drawLine(0, drawPanel.getHeight() / 2, drawPanel.getWidth(), drawPanel.getHeight() / 2);

        for (int i = -Math.max(drawPanel.getWidth() / 2, drawPanel.getWidth() / 2); i <= Math.max(drawPanel.getWidth() / 2, drawPanel.getWidth() / 2); i++) {
            g2d.drawLine(drawPanel.getWidth() / 2 + 10 * i, drawPanel.getHeight() / 2 - 5, drawPanel.getWidth() / 2 + 10 * i, drawPanel.getHeight() / 2 + 5);
        }
        for (int i = -Math.max(drawPanel.getHeight() / 2, drawPanel.getHeight() / 2); i <= Math.max(drawPanel.getHeight() / 2, drawPanel.getHeight() / 2); i++) {
            g2d.drawLine(drawPanel.getWidth() / 2 - 5, drawPanel.getHeight() / 2 + 10 * i, drawPanel.getWidth() / 2 + 5, drawPanel.getHeight() / 2 + 10 * i);
        }

        try {
            PointF P1 = new PointF(Integer.parseInt(textBox1.getText()), Integer.parseInt(textBox2.getText()));
            PointF P2 = new PointF(Integer.parseInt(textBox3.getText()), Integer.parseInt(textBox4.getText()));
            PointF P3 = new PointF(Integer.parseInt(textBox5.getText()), Integer.parseInt(textBox6.getText()));
            PointF P4 = new PointF(Integer.parseInt(textBox7.getText()), Integer.parseInt(textBox8.getText()));

            drawBezier(g2d, P1, P2, P3, P4);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Wrong input");
        }
    }

    private void drawBezier(Graphics2D g2d, PointF P1, PointF P2, PointF P3, PointF P4) {
        int steps = 100;
        Point[] points = new Point[steps + 1];

        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps;
            points[i] = calculateBezierPoint(t, P1, P2, P3, P4);
        }

        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{5, 5}, 0);
        g2d.setStroke(dashed);

        P1.x += drawPanel.getWidth() / 2;
        P2.x += drawPanel.getWidth() / 2;
        P3.x += drawPanel.getWidth() / 2;
        P4.x += drawPanel.getWidth() / 2;
        P1.y = drawPanel.getHeight() / 2 - P1.y;
        P2.y = drawPanel.getHeight() / 2 - P2.y;
        P3.y = drawPanel.getHeight() / 2 - P3.y;
        P4.y = drawPanel.getHeight() / 2 - P4.y;

        g2d.drawLine((int) P1.x, (int) P1.y, (int) P2.x, (int) P2.y);
        g2d.drawLine((int) P2.x, (int) P2.y, (int) P3.x, (int) P3.y);
        g2d.drawLine((int) P3.x, (int) P3.y, (int) P4.x, (int) P4.y);
        g2d.drawLine((int) P4.x, (int) P4.y, (int) P1.x, (int) P1.y);

        g2d.setStroke(new BasicStroke());
        for (int i = 0; i < points.length - 1; i++) {
            g2d.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
        }
    }

    private Point calculateBezierPoint(float t, PointF P1, PointF P2, PointF P3, PointF P4) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;

        float x = uuu * P1.x;
        x += 3 * uu * t * P2.x;
        x += 3 * u * tt * P3.x;
        x += ttt * P4.x;

        float y = uuu * P1.y;
        y += 3 * uu * t * P2.y;
        y += 3 * u * tt * P3.y;
        y += ttt * P4.y;

        return new Point((int) (drawPanel.getWidth() / 2 + x), (int) (drawPanel.getHeight() / 2 - y));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BezierCurve().setVisible(true);
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
