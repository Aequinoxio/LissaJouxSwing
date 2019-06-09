package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.*;
import java.util.Random;

/**
 * Classe più veloce di tutte le altre provate
 */
public class Scratch extends JPanel {
    byte[] randomMemBuffer;
    int m_width = 512;
    int m_height = 512;
    boolean toBeRepaint = true;

    float m_fps=0.0f;

    EventsPerSecond fps_obj = new EventsPerSecond();

    Timer timer;

    public Scratch() {
        randomMemBuffer = new byte[(this.getWidth() +4)* (this.getHeight()+4)];

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                super.componentResized(componentEvent);

                reinitializeBuffer();
//                // Per rapidità di calcolo alloco una 4 bytes per riga in più
//                m_height = getHeight()+4;
//                m_width = getWidth()+4;
//
//                randomMemBuffer = new byte[m_width * m_height];
//                // Riporto tutto al valore corretto per lasciare un buffer maggiore della viewport
//                m_height -=4;
//                m_width -=4;

                toBeRepaint = true;
                getParent().revalidate();
                //bufferedImage = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_RGB);
            }
        });

        this.setPreferredSize(new Dimension(m_width, m_height));

        int delay = 10; //millisecs tra due chiamate dell'aggiornamento
        ActionListener repainter = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                invalidate();
                repaint();

            }
        };


        timer = new Timer(delay, repainter);
    }

    private void reinitializeBuffer() {
        // Per rapidità di calcolo alloco una 4 bytes per riga in più
        m_height = getHeight()+4;
        m_width = getWidth()+4;

        randomMemBuffer = new byte[m_width * m_height];
        // Riporto tutto al valore corretto per lasciare un buffer maggiore della viewport
        m_height -=4;
        m_width -=4;

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Scratch");
        Scratch scratch = new Scratch();
        frame.setContentPane(scratch);

//        int delay = 10; //millisecs tra due chiamate dell'aggiornamento
//        ActionListener repainterTimer = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                scratch.invalidate();
//                scratch.repaint();
//
//            }
//        };
//        new Timer(delay, repainterTimer).start();

        scratch.startDraw();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
// lots of code to set up the config for generating the image

    private ColorModel colorModel = createColorModel();

    public void paintComponent(Graphics g) {
        // TODO: Sarebbe meglio farlo quando c'è l'evento di associazione ed un parent ma non so ancora farlo

        if (m_height==0 && m_height==0){
            reinitializeBuffer();
        }
        generateData(m_width, m_height);
        drawImage(g, randomMemBuffer);
        if (needsRepaint()) {
//            int m_width = getWidth(); // Get actual current dimensions of panel
//            int m_height = getHeight();
            //byte[] data = generateData(m_width, m_height);

            toBeRepaint = false;
        }


        m_fps=fps_obj.updateFrameCounterGetFPS();

        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.WHITE);
        g.fillRect(0,0,100,20);
        g.getFont().deriveFont(Font.BOLD);
        g.setColor(Color.BLACK);
        g2.drawString("FPS: "+ m_fps, 5, 10);
    }

    BufferedImage img;

    private void drawImage(Graphics g, byte[] data) {
        DataBuffer buffer = new DataBufferByte(data, data.length);

        SampleModel sm = colorModel.createCompatibleSampleModel(m_width, m_height);
        WritableRaster raster = Raster.createWritableRaster(sm, buffer, null);

        // TODO: se non rigenero l'immagine non prende le nuove dimensioni in caso di resizing ?!?!?!
        if (img == null || toBeRepaint) {
            img = new BufferedImage(colorModel, raster, false, null);
        } else {
            img.setData(raster);
        }

        g.drawImage(img, 0, 0, null);
    }


    private ColorModel createColorModel() {
        int colors = 256;
        byte[] reds = new byte[colors];
        byte[] greens = new byte[colors];
        byte[] blues = new byte[colors];

        for (int i = 0; i < colors; i++) {
            reds[i] = (byte) i;
            greens[i] = (byte) i;
            blues[i] = (byte) i;
        }
        return new IndexColorModel(8, colors, reds, greens, blues);
    }

    private boolean needsRepaint() {
// determine whether I’ve already painted these bounds and image to avoid repaint
        return toBeRepaint;
    }

    //SecureRandom sr = new SecureRandom();
    Random sr = new Random();

    private void generateData(int width, int height) { // actually calculate the pixel values
        int temp;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j += 4) {
                temp = sr.nextInt();

                //randomMemBuffer[i * width + j] = (byte) sr.nextInt();
                randomMemBuffer[i * width + j] = (byte) ((temp) & 0xff);
                randomMemBuffer[i * width + j + 1] = (byte) ((temp >> 8) & 0xff);
                randomMemBuffer[i * width + j + 2] = (byte) ((temp >> 16) & 0xff);
                randomMemBuffer[i * width + j + 3] = (byte) ((temp >> 24) & 0xff);
            }
        }
//        sr.nextBytes(randomMemBuffer);
    }

    public void startDraw(){
        reinitializeBuffer();
        timer.start();
    }

    public void stopDraw(){
        timer.stop();
    }


}