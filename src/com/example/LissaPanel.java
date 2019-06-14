package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LissaPanel extends JPanel {
    private double paramSin = 0.5;
    private double paramCos = 0.5;
    private boolean drawAnimation = false;
    private int maxAnimPointAlreadyCalculated = 0; // Contatore per indicare il numero di punti calcolati ed evitare di disegnare linee spurie
    private final int maxAnimPoints = 15; // Numero massimo di punti calcolati per l'animazione della figura
    private final int maxLissaGraphPoints = 100; // Numero massimo di punti da calcolare per il grafico statico
    private final int timeLissaFormula=0;             // variabile tempo nella formula di lissajoux

    public void setMaxPeriods(int maxPeriods) {
        this.maxPeriods = maxPeriods;
    }

    private int maxPeriods=1;

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
            int currIndex = (indexCounter <= 0 ? maxAnimPoints - 1 : indexCounter - 1);
            int prevIndex = (currIndex <= 0 ? maxAnimPoints - 1 : currIndex - 1);
            for (int i = 0; i < (maxAnimPointAlreadyCalculated - 1); i++) {
                graphics2D.drawLine(Xes[prevIndex], Yes[prevIndex], Xes[currIndex], Yes[currIndex]);

                currIndex = (currIndex <= 0 ? maxAnimPoints - 1 : currIndex - 1);
                //currIndex = currIndex%10;
                prevIndex = (currIndex <= 0 ? maxAnimPoints - 1 : currIndex - 1);
            }

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
        int x0 ;
        int y0 ;

//        x0 = (int) (maxX * Math.cos(paramCos * 0 / Math.PI) + centerX);
//        y0 = (int) (maxY * Math.sin(paramSin * 0 / Math.PI) + centerY);
        x0 = (int) (maxX * Math.cos((2*Math.PI/paramCos)/maxLissaGraphPoints * 0 ) + centerX);
        y0 = (int) (maxY * Math.sin((2*Math.PI/paramSin)/maxLissaGraphPoints * 0 ) + centerY);

        for (int j=0;j<maxPeriods;j++) {
            for (int i = 1; i < maxLissaGraphPoints; i++) {
                x = (int) (maxX * Math.cos((2 * Math.PI / maxLissaGraphPoints) * paramCos * (i +(j*maxLissaGraphPoints))) + centerX);
                y = (int) (maxY * Math.sin((2 * Math.PI / maxLissaGraphPoints) * paramSin * (i +(j*maxLissaGraphPoints))) + centerY);

//            x = (int) (maxX * Math.cos(paramCos * i / Math.PI) + centerX);
//            y = (int) (maxY * Math.sin(paramSin * i / Math.PI) + centerY);
                graphics.drawLine(x0, y0, x, y);
                x0 = x;
                y0 = y;
            }
        }
    }

    // Array di int per conservare i vecchi valori
    private int[] Xes = new int[maxAnimPoints];
    private int[] Yes = new int[maxAnimPoints];
    private int indexCounter = 0; // Contatore circolare per indirizzare i punti negli array Xes, Yes
    private int sinCosCounter = 0;  // Contatore del Tempo per disegnare la figura
    private int sinCosCounter_delta =1 ; // sommatore per tener conto dell'inversione quando si arriva alla fine del grafico
    private void animateLissaDrawCalc() {
        // Buffer circolare

        // TODO: codice duplicato, farlo meglio
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;
        int maxX = (int) (this.getWidth() * 0.4);
        int maxY = (int) (this.getHeight() * 0.4);


        Xes[indexCounter] = (int) (maxX * Math.cos((2 * Math.PI / maxLissaGraphPoints) * paramCos * sinCosCounter) + centerX);
        Yes[indexCounter] = (int) (maxY * Math.sin((2 * Math.PI / maxLissaGraphPoints) * paramSin * sinCosCounter) + centerY);
//        Xes[indexCounter] = (int) (maxX * Math.cos(paramCos * sinCosCounter / Math.PI) + centerX);
//        Yes[indexCounter] = (int) (maxY * Math.sin(paramSin * sinCosCounter / Math.PI) + centerY);
        indexCounter++;
        sinCosCounter+=sinCosCounter_delta;
        if (sinCosCounter>maxLissaGraphPoints*maxPeriods || sinCosCounter<0){ // TODO: proseguire oltre 2 pigreco??? da verificare in accordo alla formula per il grafico
            sinCosCounter_delta=-sinCosCounter_delta;
        }
        if (indexCounter >= maxAnimPoints) {
            indexCounter = 0;
        }

        // Gestione del primo avvio
        maxAnimPointAlreadyCalculated++;
        if (maxAnimPointAlreadyCalculated >= maxAnimPoints) {
            maxAnimPointAlreadyCalculated = maxAnimPoints;
        }
    }

    public void setAnimDelay(int animDelay) {
        this.animDelay = animDelay;
        timer.stop();
        timer.setDelay(animDelay);
        timer.start();
    }

    // timer per disegnare il pezzo del grafico dinamicamente (millisecondi)
    private int animDelay = 3;// millis
    ActionListener repainterTimer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            animateLissaDrawCalc(); // Ricalcola il punto
            revalidate();
            repaint();
        }
    };

    private Timer timer = new Timer(animDelay, repainterTimer);

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
