package com.unipi.vsmyris.mscjavaproject2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumberOfEntriesForm extends JFrame {
    private JPanel mainPanel;
    private JRadioButton option2Radio;
    private JRadioButton option3Radio;
    private JRadioButton option4Radio;
    private JRadioButton option5Radio;
    private JButton okBtn;
    private JButton cancelBtn;

    private ButtonGroup buttonGroup;

    int numberOfEntries;

    public NumberOfEntriesForm(){
        buttonGroup = new ButtonGroup();
        buttonGroup.add(option2Radio);
        buttonGroup.add(option3Radio);
        buttonGroup.add(option4Radio);
        buttonGroup.add(option5Radio);

        numberOfEntries = 2;

        setContentPane(mainPanel);
        setTitle("Select Number Of New Entries");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        //btn click listeners
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Main();
            }
        });

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new NewProductForm(numberOfEntries);
            }
        });

        //radio btn listeners
        option2Radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfEntries = 2;
            }
        });

        option3Radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfEntries = 3;
            }
        });

        option4Radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfEntries = 4;
            }
        });

        option5Radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfEntries = 5;
            }
        });
    }
}
