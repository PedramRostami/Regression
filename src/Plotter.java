import javafx.beans.binding.DoubleExpression;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plotter extends JPanel {
    private List<Double> scores1;
    private List<Double> scores2;
    private List<Double> scores3;
    private int padding = 20;
    private double labelPadding = 75;
    private int numberYDivision = 6;
    private int numberXDivision = 10;
    private int pointWidth = 6;
    private Color gridColor = new Color(200, 200, 200);
    private Stroke GRAPH_STROKE = new BasicStroke(2f);

    public Plotter(List<Double> scores1, List<Double> scores2, List<Double> scores3) {
        this.scores1 = scores1;
        this.scores2 = scores2;
        this.scores3 = scores3;
    }

    private List<Point> scalePoints(List<Double> scores) {
        double xScale = ((double) getWidth() - (2 * padding) - labelPadding)
                / (scores.size());
        double yScale = ((double) getHeight() - (2 * padding) - labelPadding)
                / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }
        return graphPoints;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        List<Point> graphPoints1 = scalePoints(scores1);
        List<Point> graphPoints2 = scalePoints(scores2);
        List<Point> graphPoints3 = scalePoints(scores3);
        g2.setColor(Color.WHITE);
        g2.fillRect((int) (padding + labelPadding), padding, (int) (getWidth() -
                (2 * padding) - labelPadding),
                (int) (getHeight() - (2 * padding) - labelPadding));
        g2.setColor(Color.BLUE);
        for (int j = 0; j <= numberYDivision; j++) {
            int x0 = (int) (padding + labelPadding);
            int x1 = (int) (pointWidth + padding + labelPadding);
            int y0 = (int) (getHeight() - ((j * (getHeight() - 2 * padding - labelPadding))
                    / numberYDivision + padding + labelPadding));

            int y1 = y0;
            if (scores1.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine((int) (padding + labelPadding + 1 + pointWidth),
                        y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                double dd = Math.floor((getMinScore() + (j * ((getMaxScore() - getMinScore()) / numberYDivision))) * 1000000000) / 1000000000;
                String yLabel = Double.toString(dd);
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 6,
                        y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);

        }

        for (int j = 0; j <= numberXDivision; j++) {
            int x0 = (int) (j * ((getWidth() - padding * 2 - labelPadding) /
                    (numberXDivision)) + padding + labelPadding);
            int x1 = x0;
            int y0 = (int) (getHeight() - padding - labelPadding);
            int y1 = y0 - pointWidth;
            g2.setColor(gridColor);
            g2.drawLine(x0, (int) (getHeight() - padding - labelPadding - 1 - pointWidth),
                    x1, padding);
            g2.setColor(Color.BLACK);
            String xLabel = (j *(scores1.size() / numberXDivision))  + "";
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(xLabel);
            g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            g2.drawLine(x0, y0, x1, y1);
        }


        g2.drawLine((int) (padding + labelPadding),
                (int) (getHeight() - padding - labelPadding),
                (int) (padding + labelPadding),
                padding);

        g2.drawLine((int) (padding + labelPadding),
                (int) (getHeight() - padding - labelPadding),
                getWidth() - padding,
                (int) (getHeight() - padding - labelPadding));


        g2.setColor(Color.BLUE);
        g2.setStroke(GRAPH_STROKE);
        for (int j = 0; j < graphPoints1.size(); j++) {
            int xPoint = graphPoints1.get(j).x - pointWidth / 2;
            int yPoint = graphPoints1.get(j).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(xPoint, yPoint, ovalW, ovalH);
        }

        g2.setColor(new Color(0, 255, 0));
        g2.setStroke(GRAPH_STROKE);
        for (int j = 0; j < graphPoints2.size(); j++) {
            int xPoint = graphPoints2.get(j).x - pointWidth / 2;
            int yPoint = graphPoints2.get(j).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(xPoint, yPoint, ovalW, ovalH);
        }

        g2.setColor(Color.RED);
        g2.setStroke(GRAPH_STROKE);
        for (int j = 0; j < graphPoints3.size(); j++) {
            int xPoint = graphPoints3.get(j).x - pointWidth / 2;
            int yPoint = graphPoints3.get(j).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(xPoint, yPoint, ovalW, ovalH);
        }


    }

    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (int i = 0; i < scores1.size(); i++) {
            minScore = Math.min(minScore, scores1.get(i));
            minScore = Math.min(minScore, scores2.get(i));
            minScore = Math.min(minScore, scores3.get(i));
        }
        return minScore;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (int i = 0; i < scores1.size(); i++) {
            maxScore = Math.max(maxScore, scores1.get(i));
            maxScore = Math.max(maxScore, scores2.get(i));
            maxScore = Math.max(maxScore, scores3.get(i));
        }
        return maxScore;
    }


//    public static void createAndShowGui() {
//        List<Double> scores1 = new ArrayList<>();
//        List<Double> scores2 = new ArrayList<>();
//        List<Double> scores3 = new ArrayList<>();
//        Random random = new Random();
//        int maxDataPoint = 20;
//        int maxScore = 1;
//        for (int i = 0; i < maxDataPoint; i++) {
//            scores1.add(random.nextDouble() * maxScore);
//            scores2.add(random.nextDouble() * maxScore);
//            scores3.add(random.nextDouble() * maxScore);
//        }
//
//        Plotter plotter = new Plotter(scores1, scores2, scores3);
//        plotter.setPreferredSize(new Dimension(1800, 900));
//
//        JFrame frame = new JFrame("Plotter");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(plotter);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }

    public static void createAndShowGui(List<Double> scores1, List<Double> scores2, List<Double> scores3) {
        Plotter plotter = new Plotter(scores1, scores2, scores3);
        plotter.setPreferredSize(new Dimension(1800, 900));

        JFrame frame = new JFrame("Plotter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(plotter);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}
