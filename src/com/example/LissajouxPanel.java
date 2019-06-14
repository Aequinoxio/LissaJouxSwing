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
    private JTextField txtMinFase;
    private JTextField txtMaxFase;
    private JSpinner spnNumeroRidisegno;
    private JSpinner spnSpeedAnim;
    private JButton btnResetAll;
    private JLabel lblPassoVariazione;
    private JButton impostaEstremiButton;

    private int stepsVariazione = 1000; // Passi per il calcolo dei vari punti
    private double deltaHoriz = 1d / stepsVariazione; // delta per variazione parametro orizzontale (bottone oscillaH)
    private double deltaVert = 1d / stepsVariazione; // delta per variazione parametro verticale   (bottone oscillaV)
//    private double deltaHoriz = 0.0001d; // delta per variazione parametro orizzontale (bottone oscillaH)
//    private double deltaVert = 0.0001d; // delta per variazione parametro verticale   (bottone oscillaV)
    //private double deltaPassoVariazione = 0.0001d;  // Delta comune er entrambe le direzioni

    private double maxPulsazione = 1.0d;
    private double minPulsazione = 0.0d;

    private final String numFormat = "%#10.6f";

    LissaPanel lissaPanel;

    public LissajouxPanel() {
        initLissaPanel();
        initInterface();
    }


    private void removeAllActionListenersFromButton(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
    }


    private void removeAllActionListeners() {
        removeAllActionListenersFromButton(btnAnimazione);
        removeAllActionListenersFromButton(btnOscillaH);
        removeAllActionListenersFromButton(btnOscillaV);

    }

    private void initInterface() {
        // removeAllActionListeners();  // Rimosso in quanto non resetta lo stato dei bottoni, chiamo la funzione
        // initInterface solo una volta nel costruttore
        sldHorizontal.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                double param = (maxPulsazione - minPulsazione) * ((double) sldHorizontal.getValue() / (double) sldHorizontal.getMaximum());
                lblHorizontal.setText(String.format(numFormat, param));
                txtHorizontal.setText(String.valueOf(param));
                lissaPanel.setParamSin(param);
                lissaPanel.revalidate();
                lissaPanel.repaint();

            }
        });

        sldVertical.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                double param = (maxPulsazione - minPulsazione) * ((double) sldVertical.getValue() / (double) sldVertical.getMaximum());
                lblVetical.setText(String.format(numFormat, param));
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

                    if (val > maxPulsazione || val < minPulsazione) {
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

                    if (val > maxPulsazione || val < minPulsazione) {
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
            public boolean started = false;
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

        btnResetAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                initLissaPanel();
                updateDaImpostaEstremi();
                sldHorizontal.setValue(sldHorizontal.getMaximum() / 2);
                sldHorizontal.updateUI();
                sldVertical.setValue(sldVertical.getMaximum() / 2);
                sldVertical.updateUI();


                //initInterface();  // Aggiunge di nuovo i listener. Ho rimosso le chiamate a remove listener in quanto non resetta lo stato dei bottoni
                panel.invalidate();
                panel.repaint();
            }
        });

        impostaEstremiButton.addActionListener(new ActionListener() {
            // TODO: Aggiornare gli slider, le textarea ed il grafico, fare refresh dell'interfaccia?
            // vedi sopra
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                double valMax = Double.valueOf(txtMaxFase.getText());
//                double valMin = Double.valueOf(txtMinFase.getText());
//                //deltaPassoVariazione = (valMax - valMin) / stepsVariazione;
//                int fattoreNuovoSteps = Math.max(1, (int) ((valMax - valMin) / (maxPulsazione - minPulsazione))); // Formula errata, non si adatta. TODO:
//                deltaVert =  (valMax - valMin) / (fattoreNuovoSteps * stepsVariazione);
//                deltaHoriz = (valMax - valMin) / (fattoreNuovoSteps * stepsVariazione);
//                maxPulsazione = valMax;
//                minPulsazione = valMin;
//
//                // Aggiorno tutto in base alla posizione degli slider
//                double param = (maxPulsazione - minPulsazione) * ((double) sldHorizontal.getValue() / (double) sldHorizontal.getMaximum());
//                lblHorizontal.setText(String.format(numFormat, param));
//                txtHorizontal.setText(String.valueOf(param));
//                param = (maxPulsazione - minPulsazione) * ((double) sldVertical.getValue() / (double) sldVertical.getMaximum());
//                lblVetical.setText(String.format(numFormat, param));
//                txtVertical.setText(String.valueOf(param));
//
//                // Dal codice del bottone disegna. TODO: esportare in un metodo privato da richiamare alla bisogna
//                double paramH = Double.valueOf(txtHorizontal.getText());
//                double paramV = Double.valueOf(txtVertical.getText());
//                lissaPanel.setParamSin(paramH);
//                lissaPanel.setParamCos(paramV);

                updateDaImpostaEstremi();
                lissaPanel.revalidate();
                lissaPanel.repaint();

//                sldHorizontal.setMaximum((int) maxPulsazione);
//                sldHorizontal.setMinimum((int) minPulsazione);
//                sldVertical.setMaximum((int) maxPulsazione);
//                sldVertical.setMinimum((int) minPulsazione);
            }
        });
    }

    private void updateDaImpostaEstremi() {
        double valMax = Double.valueOf(txtMaxFase.getText());
        double valMin = Double.valueOf(txtMinFase.getText());
        //deltaPassoVariazione = (valMax - valMin) / stepsVariazione;
        int fattoreNuovoSteps = Math.max(1, (int) ((valMax - valMin) / (maxPulsazione - minPulsazione))); // Formula errata, non si adatta. TODO:
        deltaVert = (valMax - valMin) / (fattoreNuovoSteps * stepsVariazione);
        deltaHoriz = (valMax - valMin) / (fattoreNuovoSteps * stepsVariazione);

        maxPulsazione = valMax;
        minPulsazione = valMin;

        // Aggiorno tutto in base alla posizione degli slider
        double param = (maxPulsazione - minPulsazione) * ((double) sldHorizontal.getValue() / (double) sldHorizontal.getMaximum());
        lblHorizontal.setText(String.format(numFormat, param));
        txtHorizontal.setText(String.valueOf(param));
        param = (maxPulsazione - minPulsazione) * ((double) sldVertical.getValue() / (double) sldVertical.getMaximum());
        lblVetical.setText(String.format(numFormat, param));
        txtVertical.setText(String.valueOf(param));

        // Dal codice del bottone disegna. TODO: esportare in un metodo privato da richiamare alla bisogna
        double paramH = Double.valueOf(txtHorizontal.getText());
        double paramV = Double.valueOf(txtVertical.getText());
        lissaPanel.setParamSin(paramH);
        lissaPanel.setParamCos(paramV);
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

        paramH = (double) sldHorizontal.getValue() / (double) sldHorizontal.getMaximum();
        paramV = (double) sldVertical.getValue() / (double) sldVertical.getMaximum();
        lissaPanel = new LissaPanel(paramH, paramV);
        lblHorizontal.setText(String.format(numFormat, paramH));
        txtHorizontal.setText(String.valueOf(paramH));
        lblVetical.setText(String.format(numFormat, paramV));
        txtVertical.setText(String.valueOf(paramV));

        txtMinFase.setText(String.valueOf(minPulsazione));
        txtMaxFase.setText(String.valueOf(maxPulsazione));
        lblPassoVariazione.setText(String.valueOf(deltaHoriz));

        lissaPanel.setParamSin(paramH);
        lissaPanel.setParamCos(paramV);
        drawPanel.removeAll();
        drawPanel.add(lissaPanel);
        drawPanel.revalidate();
        drawPanel.repaint();
        SpinnerModel spinnerNumberModel =
                new SpinnerNumberModel(1, //initial value
                        1, //min
                        100, //max
                        1);                //step

        spnNumeroRidisegno.setModel(spinnerNumberModel);
        spinnerNumberModel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int val = (int) spnNumeroRidisegno.getModel().getValue();
                stepsVariazione = 1000 * val; // Ogni periodo viene disegnato con lo stesso numero di steps
                updateDaImpostaEstremi();
                lissaPanel.setMaxPeriods(val);
                drawPanel.revalidate();
                drawPanel.repaint();
            }
        });

        SpinnerModel spinnerSpeedAnimModel =
                new SpinnerNumberModel(3, //initial value
                        1, //min
                        10, //max
                        1);                //step

        spnSpeedAnim.setModel(spinnerSpeedAnimModel);
        spnSpeedAnim.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int val = (int) spnSpeedAnim.getModel().getValue();
                lissaPanel.setAnimDelay(val);
                drawPanel.revalidate();
                drawPanel.repaint();
            }
        });

    }

    private void createUIComponents() {
        double paramH = 1;
        double paramV = 1;
        //lissaPanel = new LissaPanel(paramH, paramV);
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
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 15, new Insets(10, 10, 10, 10), -1, -1));
        panel.setName("TestjpanelName");
        panel.setPreferredSize(new Dimension(-1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 15, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        panel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 15, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 4, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Pulsazione"));
        final JLabel label1 = new JLabel();
        label1.setText("Massimo");
        panel4.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtMaxFase = new JTextField();
        panel4.add(txtMaxFase, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Minimo");
        panel4.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtMinFase = new JTextField();
        panel4.add(txtMinFase, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        btnOscillaH = new JButton();
        btnOscillaH.setText("Varia autom.");
        panel4.add(btnOscillaH, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnOscillaV = new JButton();
        btnOscillaV.setText("Varia autom.");
        panel4.add(btnOscillaV, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Passo variazione");
        panel4.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblPassoVariazione = new JLabel();
        lblPassoVariazione.setText("000000000");
        panel4.add(lblPassoVariazione, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        impostaEstremiButton = new JButton();
        impostaEstremiButton.setText("Imposta estremi");
        panel4.add(impostaEstremiButton, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Periodi");
        panel4.add(label4, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbInvertV = new JCheckBox();
        cbInvertV.setText("Inverte");
        panel4.add(cbInvertV, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbInvertH = new JCheckBox();
        cbInvertH.setText("Inverte");
        panel4.add(cbInvertH, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spnNumeroRidisegno = new JSpinner();
        panel4.add(spnNumeroRidisegno, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 4, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Pulsazione"));
        final JLabel label5 = new JLabel();
        label5.setText("Orizzontale");
        panel5.add(label5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Verticale");
        panel5.add(label6, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtHorizontal = new JTextField();
        panel5.add(txtHorizontal, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        txtVertical = new JTextField();
        panel5.add(txtVertical, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        disegnaButton = new JButton();
        disegnaButton.setText("Disegna");
        panel5.add(disegnaButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Animazione"));
        btnAnimazione = new JButton();
        btnAnimazione.setText("Animazione");
        panel6.add(btnAnimazione, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbSfondo = new JCheckBox();
        cbSfondo.setText("Disegna figura di sfondo");
        panel6.add(cbSfondo, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spnSpeedAnim = new JSpinner();
        panel6.add(spnSpeedAnim, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Velocità");
        panel6.add(label7, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnResetAll = new JButton();
        btnResetAll.setText("Reset all");
        panel.add(btnResetAll, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
