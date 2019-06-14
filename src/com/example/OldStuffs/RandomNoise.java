package com.example.OldStuffs;

import com.example.EventsPerSecond;
import com.sun.imageio.plugins.common.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Random;

public class RandomNoise extends JPanel {
    //SecureRandom sr = new SecureRandom();
    Random sr = new Random();
    float m_fps;
    EventsPerSecond fps_obj = new EventsPerSecond();

    int m_width = 256;
    int m_height = 256;
    // image creation
    VolatileImage vImg;

    BufferedImage bufferedImage;

    public RandomNoise() {
        this(256, 256);
    }

    RandomNoise(int width, int height) {
        this.setDoubleBuffered(false);
        this.m_width = width;

        this.m_height = height;
        this.setPreferredSize(new Dimension(width, height));

        vImg = createVolatileImage(width, height);

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                super.componentResized(componentEvent);

                m_height = getHeight();
                m_width = getWidth();

                getParent().revalidate();
                //bufferedImage = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_RGB);
            }
        });
    }

//    @Override
//    public void update(Graphics graphics) {
//        super.update(graphics);
//        drawRandomStuffs(graphics);
//    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

// Metodo 1
        //drawRandomStuffsOrdinary(graphics);

// MEtodo 2
        //renderVolatileImage(graphics);

// Metodo 3 - Sembra il migliore in termini di velocità con Random anziché SecureRandom
        generateBufferedImage();
        graphics.drawImage(bufferedImage, 0,0, this);
        //graphics.drawString("pippo",100,100);
// Metodo 4
//        try {
//            drawRandomStuffsBufferedImage(graphics);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        m_fps=fps_obj.updateFrameCounterGetFPS();

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,100,20);
        g2.getFont().deriveFont(Font.BOLD);
        g2.setColor(Color.BLACK);
        g2.drawString("FPS: "+ m_fps, 5, 10);
    }

    private void drawRandomStuffsOrdinary(Graphics graphics) {
//        int maxX = this.getWidth();
//        int maxY = this.getHeight();
        int temp;
        byte mem[][] = new byte[m_width][m_height];
        for (int i = 0; i < m_width; i++) {
            for (int j = 0; j < m_height; j++) {
// Inutile in questo contesto
//                temp=sr.nextInt();
//                mem[i][j] = (byte) (temp & 0xff);
//                mem[i][j+1] = (byte) ((temp>>8) & 0xff);
//                mem[i][j+2] = (byte) ((temp>>16) & 0xff);
//                mem[i][j+3] = (byte) ((temp>>24) & 0xff);

                if (sr.nextBoolean()) {
                    graphics.setColor(Color.DARK_GRAY);
                } else {
                    graphics.setColor(Color.BLACK);
                }
                graphics.drawLine(i, j, i, j);
            }
        }
        graphics.drawString("Pippo",10,10);
    }

    private void drawRandomStuffsBufferedImage(Graphics graphics) {
//        int maxX = this.getWidth();
//        int maxY = this.getHeight();
        byte mem[] = new byte[m_width * m_height];
        for (int i = 0; i < m_width; i++) {
            for (int j = 0; j < m_height; j++) {
                mem[i * m_height + j] = (byte) sr.nextInt();
            }
        }

       // BufferedImage inputImage = ImageUtil.getImageFromBytes(inputImageBytes);

        DataBuffer buffer = new DataBufferByte(mem, mem.length);

//3 bytes per pixel: red, green, blue
        WritableRaster raster = Raster.createInterleavedRaster(buffer, m_width, m_height, 3 * m_width, 3, new int[] {0, 1, 2}, (Point)null);
        ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(cm, raster, true, null);


        graphics.drawImage(image,0,0,this);
    }


    private void generateBufferedImage() {
        Graphics g = bufferedImage.getGraphics();
        drawRandomStuffsOrdinary(g);

    }


    ////////// Volatile image da testare meglio, per ora torna null perchè genero l'immagine nel costruttore

    // rendering to the image
    void renderOffscreen() {
        do {
            if (vImg.validate(getGraphicsConfiguration()) ==
                    VolatileImage.IMAGE_INCOMPATIBLE) {
                // old vImg doesn't work with new GraphicsConfig; re-create it
                vImg = createVolatileImage(m_width, m_height);
            }
            Graphics2D g = vImg.createGraphics();
            int maxX = this.getWidth();
            int maxY = this.getHeight();
            for (int i = 0; i < maxX; i += 2) {
                for (int j = 0; j < maxY; j += 2) {
                    if (sr.nextBoolean()) {
                        g.setColor(Color.DARK_GRAY);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    g.drawLine(i, j, i, j);
                }
            }
            g.dispose();
        } while (vImg.contentsLost());
    }


    private void renderVolatileImage(Graphics graphics) {
        // copying from the image (here, gScreen is the Graphics
        // object for the onscreen window)
        do {
            int returnCode = vImg.validate(getGraphicsConfiguration());
            if (returnCode == VolatileImage.IMAGE_RESTORED) {
                // Contents need to be restored
                renderOffscreen();      // restore contents
            } else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                // old vImg doesn't work with new GraphicsConfig; re-create it
                vImg = createVolatileImage(m_width, m_height);
                renderOffscreen();
            }
            graphics.drawImage(vImg, 0, 0, this);
        } while (vImg.contentsLost());
    }
}
