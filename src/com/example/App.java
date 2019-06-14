package com.example;

import com.example.OldStuffs.RandomNoise;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Random stuff");
        //LissaPanel lissaPanel = new LissaPanel();
//        LissajouxPanel lissajoux = new LissajouxPanel();
/*
        frame.setContentPane(lissa.getPanelMain());
*/
        //frame.setContentPane(lissa.getMainPanel());
        //frame.add(lissa.getPanelMain());

        //frame.setContentPane(lissaPanel);
        //frame.add(lissajoux);

        RandomNoise randomNoise = new RandomNoise();
        frame.setContentPane(randomNoise);
        int delay = 10; //millisecs
        ActionListener repainter = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    randomNoise.invalidate();
                    randomNoise.repaint();

            }
        };

        new Timer(delay, repainter).start();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
