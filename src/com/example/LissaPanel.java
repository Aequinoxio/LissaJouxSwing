package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LissaPanel extends JPanel {
    private double paramSin = 0.5;
    private double paramCos = 0.5;
    private boolean drawAnimation = false;
    private int maxAnimPointCalculated = 0; // Contatore per indicare il numero di punti calcolati ed evitare di disegnare linee spurie
    private final int maxPoints = 15; // Numero massimo di punti calcolati per l'animazione della figura

    public void setDrawBackgroundLissa(boolean drawBackgroundLissa) {
        this.drawBackgroundLissa = drawBackgroundLissa;
    }

    private boolean drawBackgroundLissa = false;

    public LissaPanel(double paramSin, double paramCos) {
        this.paramSin = paramSin;
        this.paramCos = paramCos;
    }

    public void setParamSin(double paramSin) {
        this.paramSin = paramSin;
    }

    public void setParamCos(double paramCos) {
        this.paramCos = paramCos;
    }


    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (!drawAnimation) {
            drawGraphics(graphics, Color.BLACK);
        } else {

//TODO: Aggiungere il disegno dai vettori Xes Yes, gestire il buffer circolare
            Graphics2D graphics2D = (Graphics2D) graphics;

            if (drawBackgroundLissa) {
                graphics2D.setStroke(new BasicStroke(1.0f));
                drawGraphics(graphics2D, Color.RED);  // Disegno la base si cui animare la linea
            }
            graphics2D.setStroke(new BasicStroke(3.0f));
            graphics2D.setColor(Color.BLACK);
            //boolean continueDraw = true;
            int currIndex = (indexCounter <= 0 ? maxPoints - 1 : indexCounter - 1);
            int prevIndex = (currIndex <= 0 ? maxPoints - 1 : currIndex - 1);
            for (int i = 0; i < (maxAnimPointCalculated - 1); i++) {
                graphics2D.drawLine(Xes[prevIndex], Yes[prevIndex], Xes[currIndex], Yes[currIndex]);

                currIndex = (currIndex <= 0 ? maxPoints - 1 : currIndex - 1);
                //currIndex = currIndex%10;
                prevIndex = (currIndex <= 0 ? maxPoints - 1 : currIndex - 1);
            }
//            for (int i=1;i<10;i++){
//                graphics.drawLine(Xes[i-1],Yes[i-1], Xes[i], Yes[i]);
//            }

        }
    }


    private void drawGraphics(Graphics g, Color color) {
        Graphics2D graphics = (Graphics2D) g;

        graphics.setColor(color);

        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;
        int maxX = (int) (this.getWidth() * 0.4);
        int maxY = (int) (this.getHeight() * 0.4);

        int x;
        int y;

        int x0 = 0;
        int y0 = 0;
        x0 = (int) (maxX * Math.sin(paramSin * 0 / Math.PI) + centerX);
        y0 = (int) (maxY * Math.cos(paramCos * 0 / Math.PI) + centerY);

        for (int i = 0; i < 1000; i++) {
            x = (int) (maxX * Math.sin(paramSin * i / Math.PI) + centerX);
            y = (int) (maxY * Math.cos(paramCos * i / Math.PI) + centerY);
            graphics.drawLine(x0, y0, x, y);
            x0 = x;
            y0 = y;
        }
    }

    // Array di int per conservare i vecchi valori
    private int[] Xes = new int[maxPoints];
    private int[] Yes = new int[maxPoints];
    private int indexCounter = 0; // Contatore circolare per indirizzare i punti negli array Xes, Yes
    private int sinCosCounter = 0;  // Contatore del Tempo per disegnare la figura

    private void animateLissaDrawCalc() {
        // Buffer circolare
//        int oldcounter = (indexCounter == 0 ? 9 : indexCounter - 1);

        // TODO: codice duplicato, farlo meglio
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;
        int maxX = (int) (this.getWidth() * 0.4);
        int maxY = (int) (this.getHeight() * 0.4);

//        int x;
//        int y;
//
//        int x0 = 0;
//        int y0 = 0;

//        x0 = Xes[oldcounter];
//        y0 = Yes[oldcounter];


        Xes[indexCounter] = (int) (maxX * Math.sin(paramSin * sinCosCounter / Math.PI) + centerX);
        Yes[indexCounter] = (int) (maxY * Math.cos(paramCos * sinCosCounter / Math.PI) + centerY);
        indexCounter++;
        sinCosCounter++;
        if (indexCounter >= maxPoints) {
            indexCounter = 0;
        }

        // Gestione del primo avvio
        maxAnimPointCalculated++;
        if (maxAnimPointCalculated >= maxPoints) {
            maxAnimPointCalculated = maxPoints;
        }
    }

    // timer per disegnare il pezzo del grafico dinamicamente
    private int delay = 100;
    ActionListener repainterTimer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            animateLissaDrawCalc(); // Ricalcola il punto
            revalidate();
            repaint();
        }
    };

    private Timer timer = new Timer(delay, repainterTimer);

    public void animateLissa(boolean start) {
        if (start) {
            drawAnimation = true;
            timer.start();
        } else {
            drawAnimation = false;
            timer.stop();
        }
    }
}
