package com.example.OldStuffs;

import com.example.EventsPerSecond;
import sun.awt.image.IntegerInterleavedRaster;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.security.SecureRandom;

/**
 * This class can read chunks of RGB image data out of a file and return a BufferedImage.
 * It may use an optimized technique for loading images that relies on assumptions about the
 * default image format on Windows.
 */
public class RGBImageLoader extends JPanel {
    private int m_width = 512;
    private int m_height = 512;
    private byte[] randomMemBuffer;
    private byte[] tempBuffer_;
    private boolean fastLoading_;

    private float m_fps ;
    private EventsPerSecond fps_obj = new EventsPerSecond();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Scratch");
        RGBImageLoader scratch = new RGBImageLoader();
        frame.setContentPane(scratch);

        int delay = 25; //millisecs
        ActionListener repainter = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scratch.invalidate();
                scratch.repaint();

            }
        };
        new Timer(delay, repainter).start();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private void initBuffer(){
        randomMemBuffer = new byte[m_width * m_height];

    }
    public RGBImageLoader() {
        this.setPreferredSize(new Dimension(m_width, m_height));
        initBuffer();
        fastLoading_ = canUseFastLoadingTechnique();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                super.componentResized(componentEvent);

                m_height = getHeight();
                m_width = getWidth();
                initBuffer();
                getParent().revalidate();
                //bufferedImage = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_RGB);
            }
        });

    }

    public void paintComponent(Graphics g) {

        generateData(m_width, m_height);
        //drawImage(g, randomMemBuffer);
        g.drawImage(
                drawImage(null, m_width, m_height, 0),
                0,0,null
        );

        m_fps=fps_obj.updateFrameCounterGetFPS();

        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.WHITE);
        g.fillRect(0,0,100,20);
        g.getFont().deriveFont(Font.BOLD);
        g.setColor(Color.BLACK);
        g2.drawString("FPS: "+ m_fps, 5, 10);
    }

    private boolean canUseFastLoadingTechnique() {
        // Create an image that's compatible with the screen
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(100, 100, Transparency.TRANSLUCENT);

        // On windows this should be an ARGB integer packed raster. If it is then we can
        // use our optimization technique

        if (image.getType() != BufferedImage.TYPE_INT_ARGB)
            return false;

        WritableRaster raster = image.getRaster();

        if (!(raster instanceof IntegerInterleavedRaster))
            return false;

        if (!(raster.getDataBuffer() instanceof DataBufferInt))
            return false;

        if (!(image.getColorModel() instanceof DirectColorModel))
            return false;

        DirectColorModel colorModel = (DirectColorModel) image.getColorModel();

        if (!(colorModel.getColorSpace() instanceof ICC_ColorSpace) ||
                colorModel.getNumComponents() != 4 ||
                colorModel.getAlphaMask() != 0xff000000 ||
                colorModel.getRedMask() != 0xff0000 ||
                colorModel.getGreenMask() != 0xff00 ||
                colorModel.getBlueMask() != 0xff)
            return false;

        if (raster.getNumBands() != 4 ||
                raster.getNumDataElements() != 1 ||
                !(raster.getSampleModel() instanceof SinglePixelPackedSampleModel))
            return false;

        return true;
    }

    public BufferedImage drawImage(File file, int width, int height, long imageOffset) {
        if (fastLoading_)
            return loadImageUsingFastTechnique(file, width, height, imageOffset);
        else
            return loadImageUsingCompatibleTechnique(file, width, height, imageOffset);
    }


    SecureRandom sr = new SecureRandom();

    private void generateData(int width, int height) { // actually calculate the pixel values
        //byte[] mem = new byte[width*height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                randomMemBuffer[i * width + j] = (byte) sr.nextInt();
                //randomMemBuffer[i * width + j] = 0x00;
            }
        }

        //return mem;
    }


    private BufferedImage loadImageUsingFastTechnique(File file, int width, int height, long imageOffset) {
        int sizeBytes = width * height * 1;

        // Make sure buffer is big enough
//        if (tempBuffer_ == null || tempBuffer_.length < sizeBytes)
//            tempBuffer_ = new byte[sizeBytes];

        generateData(m_width, m_height);
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        WritableRaster raster = image.getRaster();
        DataBufferInt dataBuffer = (DataBufferInt) raster.getDataBuffer();

        addAlphaChannel(randomMemBuffer, sizeBytes, dataBuffer.getData());

        return image;

    }

    private BufferedImage loadImageUsingCompatibleTechnique(File file, int width, int height, long imageOffset) {
        int sizeBytes = width * height * 1;

        RandomAccessFile raf = null;

        DataBufferByte dataBuffer = new DataBufferByte(sizeBytes);
       // byte[] bytes = dataBuffer.getData();
        WritableRaster raster = Raster.createInterleavedRaster(dataBuffer, // dataBuffer
                width, // width
                height, // height
                width * 1, // scanlineStride
                1, // pixelStride
                new int[]{0, 1, 2}, // bandOffsets
                null); // location

        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), // ColorSpace
                new int[]{8, 8, 8}, // bits
                false, // hasAlpha
                false, // isPreMultiplied
                ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);

        BufferedImage loadImage = new BufferedImage(colorModel, raster, false, null);

        // Convert it into a buffered image that's compatible with the current screen.
        // Not ideal creating this image twice....
        BufferedImage image = createCompatibleImage(loadImage);

        return image;

    }

    private BufferedImage createCompatibleImage(BufferedImage image) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

        BufferedImage newImage = gc.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);

        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return newImage;
    }


    private void addAlphaChannel(byte[] rgbBytes, int bytesLen, int[] argbInts) {
        for (int i = 0; i < bytesLen; i++) {
            argbInts[i] = ((byte) 0xff) << 24 | rgbBytes[i];

// Caso ordinario in cui in rgbBytes ci sono i tre colori
            //for (int i = 0, j = 0; i < bytesLen; i += 3, j++) {
//            argbInts[j] = ((byte) 0xff) << 24 |                 // Alpha
//                    (rgbBytes[i] << 16) & (0xff0000) |      // Red
//                    (rgbBytes[i + 1] << 8) & (0xff00) |       // Green
//                    (rgbBytes[i + 2]) & (0xff);               // Blue
        }
    }
}