package com.example;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LissajouxPanel {
    JPanel panel;
    private JPanel drawPanel;
    private JSlider sldHorizontal;
    private JSlider sldVertical;
    private JLabel lblHorizontal;
    private JLabel lblVetical;
    private JTextField txtHorizontal;
    private JTextField txtVertical;
    private JButton disegnaButton;
    private JButton btnOscillaV;
    private JButton btnOscillaH;
    private JCheckBox cbInvertH;
    private JCheckBox cbInvertV;
    private JButton btnAnimazione;
    private JCheckBox cbSfondo;

    double deltaHoriz = 0.0001d; // delta per variazione parametro orizzontale (bottone oscillaH)
    double deltaVert = 0.0001d; // delta per variazione parametro verticale   (bottone oscillaV)

    LissaPanel lissaPanel;

    public LissajouxPanel() {
        initLissaPanel();
//        btnOscillaV.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                //JOptionPane.showMessageDialog(null,"ok","ok dialog", JOptionPane.OK_CANCEL_OPTION);
//                if (lissaPanel == null) {
//        //            lissaPanel = new LissaPanel(0.5,0.5); //TODO: parametrizzare con param sin/max ecc.
//                    drawPanel.removeAll();
//                    drawPanel.add(lissaPanel);
//                }
////                drawPanel.invalidate();
//                drawPanel.revalidate();
//                drawPanel.repaint();
//            }
//        });
        sldHorizontal.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                double param = (double) sldHorizontal.getValue() / (double) sldHorizontal.getMaximum();
                lblHorizontal.setText(String.valueOf(param));
                txtHorizontal.setText(String.valueOf(param));
                lissaPanel.setParamSin(param);
                lissaPanel.revalidate();
                lissaPanel.repaint();

            }
        });
        sldVertical.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                double param = (double) sldVertical.getValue() / (double) sldVertical.getMaximum();
                lblVetical.setText(String.valueOf(param));
                txtVertical.setText(String.valueOf(param));
                lissaPanel.setParamCos(param);
                lissaPanel.revalidate();
                lissaPanel.repaint();
            }
        });

        // Mantengo l'aspect ratio
        drawPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                super.componentResized(componentEvent);
                int w = drawPanel.getParent().getWidth();
                int h = drawPanel.getParent().getHeight();
                int size = Math.min(w, h);
                drawPanel.setPreferredSize(new Dimension(size, size));
                drawPanel.getParent().revalidate();
            }
        });

        disegnaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double paramH = Double.valueOf(txtHorizontal.getText());
                double paramV = Double.valueOf(txtVertical.getText());

                lissaPanel.setParamSin(paramH);
                lissaPanel.setParamCos(paramV);
                lissaPanel.revalidate();
                lissaPanel.repaint();


            }
        });

        btnOscillaH.addActionListener(new ActionListener() {
            boolean statePressed = false;
            int delay = 10; // millis
            double val = 0.0d;

            ActionListener repainter = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    if (val > 1.0d || val < 0d) {
                        deltaHoriz = -deltaHoriz;
                    }

                    val += deltaHoriz;
                    val = Math.floor(val * 100000) / 100000;

                    txtHorizontal.setText(String.valueOf(val));
                    lissaPanel.setParamSin(val);
                    lissaPanel.revalidate();
                    lissaPanel.repaint();
                }
            };
            Timer tH = new Timer(delay, repainter);

            Color color_background = btnOscillaH.getBackground();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                statePressed = !statePressed;

                val = Double.valueOf(txtHorizontal.getText());
                if (statePressed) {
                    tH.start();
                    btnOscillaH.setBackground(Color.LIGHT_GRAY);

                } else {
                    tH.stop();
                    btnOscillaH.setBackground(color_background);
                }

            }
        });

        cbInvertH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                deltaHoriz = -deltaHoriz;
            }
        });
        cbInvertV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                deltaVert = -deltaVert;
            }
        });


        btnOscillaV.addActionListener(new ActionListener() {
            boolean statePressed = false;
            int delay = 10; // millis
            double val = 0.0d;

            ActionListener repainter = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    if (val > 1.0d || val < 0d) {
                        deltaVert = -deltaVert;
                    }

                    val += deltaVert;
                    val = Math.floor(val * 100000) / 100000;

                    txtVertical.setText(String.valueOf(val));
                    lissaPanel.setParamCos(val);
                    lissaPanel.revalidate();
                    lissaPanel.repaint();
                }
            };
            Timer tV = new Timer(delay, repainter);

            Color color_background = btnOscillaV.getBackground();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                statePressed = !statePressed;

                val = Double.valueOf(txtVertical.getText());
                if (statePressed) {
                    tV.start();
                    btnOscillaV.setBackground(Color.LIGHT_GRAY);

                } else {
                    tV.stop();
                    btnOscillaV.setBackground(color_background);
                }

            }

        });
        btnAnimazione.addActionListener(new ActionListener() {
            boolean started = false;
            Color color_background = btnAnimazione.getBackground();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                started = !started; // Per trattarlo come un jtogglebutton

                if (started) {
                    btnAnimazione.setBackground(Color.LIGHT_GRAY);
                    lissaPanel.animateLissa(true);
                } else {
                    btnAnimazione.setBackground(color_background);
                    lissaPanel.animateLissa(false);
                }
            }
        });

        cbSfondo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lissaPanel.setDrawBackgroundLissa(cbSfondo.isSelected());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LissajouxPanel...");

        JPanel panel = new LissajouxPanel().panel;

        frame.setContentPane(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * VERAMENTE BRUTTO, DEVO IMPOSTARE INTELLIJ PER MOSTRARE I METODI GENERATI AUTOMATICAMENTE ALTRIMENTI NON POSSO CHIAMARLO
     * (NULLPOINTER EXCEPTION) CI SARà UNA SOLUZIONE MIGLIORE... DEVO AGGIUNGERE UN PANEL AD UN ALTRO JPANEL A PROGRAMMA
     * DOPO CHE IL PRIMO è STATO GIà CREATO. MANCA UNA FUNZIONE SIMILE A INITCOMPONENTS CHIAMATA NEL COSTRUTTORE
     * COME IN NETBEANS
     */
    private void initLissaPanel() {

        double paramH;
        double paramV;

        lissaPanel = new LissaPanel(0.5, 0.5);
        paramH = (double) sldHorizontal.getValue() / (double) sldHorizontal.getMaximum();
        paramV = (double) sldVertical.getValue() / (double) sldVertical.getMaximum();
        lblHorizontal.setText(String.valueOf(paramH));
        txtHorizontal.setText(String.valueOf(paramH));
        lblVetical.setText(String.valueOf(paramV));
        txtVertical.setText(String.valueOf(paramV));
        lissaPanel.setParamSin(paramH);
        lissaPanel.setParamCos(paramV);
        drawPanel.removeAll();
        drawPanel.add(lissaPanel);
        drawPanel.revalidate();
        drawPanel.repaint();
    }

    private void createUIComponents() {
        double paramH = 0.5;
        double paramV = 0.5;
        lissaPanel = new LissaPanel(paramH, paramV);
        drawPanel = new JPanel();
        drawPanel.setPreferredSize(new Dimension(256, 256));
        drawPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        lissaPanel.setPreferredSize(drawPanel.getPreferredSize());
        drawPanel.removeAll();
        drawPanel.add(lissaPanel);
        drawPanel.revalidate();
        drawPanel.repaint();


    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 10, new Insets(10, 10, 10, 10), -1, -1));
        panel.setName("TestjpanelName");
        panel.setPreferredSize(new Dimension(-1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 10, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblHorizontal = new JLabel();
        lblHorizontal.setText("text");
        panel1.add(lblHorizontal, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sldHorizontal = new JSlider();
        sldHorizontal.setMaximum(1000000);
        sldHorizontal.setValue(500000);
        panel1.add(sldHorizontal, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 2, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblVetical = new JLabel();
        lblVetical.setText("text");
        panel1.add(lblVetical, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 10, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        drawPanel = new JPanel();
        drawPanel.setLayout(new BorderLayout(0, 0));
        drawPanel.setMinimumSize(new Dimension(256, 256));
        drawPanel.setName("DrawPanel");
        drawPanel.setPreferredSize(new Dimension(256, 256));
        panel2.add(drawPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        drawPanel.setBorder(BorderFactory.createTitledBorder("Lissajoux"));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        sldVertical = new JSlider();
        sldVertical.setMaximum(1000000);
        sldVertical.setOrientation(1);
        sldVertical.setValue(500000);
        sldVertical.putClientProperty("JSlider.isFilled", Boolean.FALSE);
        panel3.add(sldVertical, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Parametro orizzontale");
        panel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtHorizontal = new JTextField();
        panel.add(txtHorizontal, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Parametro verticale");
        panel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtVertical = new JTextField();
        panel.add(txtVertical, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 2, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnOscillaH = new JButton();
        btnOscillaH.setText("Varia autom.");
        panel.add(btnOscillaH, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnOscillaV = new JButton();
        btnOscillaV.setText("Varia autom.");
        panel.add(btnOscillaV, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbInvertH = new JCheckBox();
        cbInvertH.setText("Inverte");
        panel.add(cbInvertH, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbInvertV = new JCheckBox();
        cbInvertV.setText("Inverte");
        panel.add(cbInvertV, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        disegnaButton = new JButton();
        disegnaButton.setText("Disegna");
        panel.add(disegnaButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAnimazione = new JButton();
        btnAnimazione.setText("Animazione");
        panel.add(btnAnimazione, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbSfondo = new JCheckBox();
        cbSfondo.setText("Sfondo");
        panel.add(cbSfondo, new com.intellij.uiDesigner.core.GridConstraints(1, 5, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
