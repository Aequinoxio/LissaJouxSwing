package com.example;

import javax.swing.*;
import java.awt.*;

public class LissaPanel extends JPanel {
    double paramSin=0.5;
    double paramCos=0.5;

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
        drawGraphics(graphics);
    }


    private void drawGraphics(Graphics g){
        Graphics2D graphics = (Graphics2D) g;

        graphics.setColor(Color.BLACK);

        int centerX = this.getWidth()/2;
        int centerY = this.getHeight()/2;
        int maxX= (int)(this.getWidth()*0.4);
        int maxY= (int)(this.getHeight()*0.4);

        int x;
        int y;

        int x0=0;
        int y0=0;
//        float paramSin=2.3f;
//        float paramCos=0.7f;
//        SecureRandom sr = new SecureRandom();
//        float paramSin=sr.nextFloat();
//        float paramCos=sr.nextFloat();
        x0=(int)(maxX*Math.sin(paramSin*0/ Math.PI)+centerX);
        y0=(int)(maxY*Math.cos(paramCos*0/ Math.PI)+centerY);

        for (int i=0;i<1000;i++) {
            x=(int)(maxX*Math.sin(paramSin*i/ Math.PI)+centerX);
            y=(int)(maxY*Math.cos(paramCos*i/ Math.PI)+centerY);
            graphics.drawLine(x0,y0,x,y);
            x0=x;
            y0=y;
            //graphics.drawOval(10, 10, 5, 5);
        }

    }
}
