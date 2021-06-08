package mapGUI;

import run.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ControlPanel extends JPanel implements ActionListener {
    private final JButton start;

    private final JRadioButton infantryButton;
    private final JRadioButton heavyInfantryButton;
    private final JRadioButton cavalryButton;
    private final JRadioButton archersButton;

    private final JRadioButton redButton;
    private final JRadioButton blueButton;

    public boolean wait = true;

    public ControlPanel() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setLayout(null);

        this.setLayout(null);

        start = new JButton("START");
        start.setSize(200, 30);
        start.setLocation(100, 220);
        start.addActionListener(this);

        this.add(start);

        infantryButton = new JRadioButton("Infantry");
        infantryButton.setActionCommand("Infantry");
        infantryButton.setBounds(50, 20, 150, 30);
        infantryButton.setSelected(true);

        heavyInfantryButton = new JRadioButton("Heavy infantry");
        heavyInfantryButton.setBounds(50, 60, 150, 30);
        heavyInfantryButton.setActionCommand("Heavy infantry");

        cavalryButton = new JRadioButton("Cavalry");
        cavalryButton.setBounds(50, 100, 150, 30);
        cavalryButton.setActionCommand("Cavalry");

        archersButton = new JRadioButton("Archers");
        archersButton.setBounds(50, 140, 150, 30);
        archersButton.setActionCommand("Archers");

        ButtonGroup groupType = new ButtonGroup();
        groupType.add(infantryButton);
        groupType.add(heavyInfantryButton);
        groupType.add(cavalryButton);
        groupType.add(archersButton);

        this.add(infantryButton);
        this.add(heavyInfantryButton);
        this.add(cavalryButton);
        this.add(archersButton);

        infantryButton.addActionListener(this);
        heavyInfantryButton.addActionListener(this);
        cavalryButton.addActionListener(this);
        archersButton.addActionListener(this);

        redButton = new JRadioButton("Red");
        redButton.setActionCommand("Red");
        redButton.setBounds(250, 60, 75, 30);
        redButton.setSelected(true);

        blueButton = new JRadioButton("Blue");
        blueButton.setBounds(250, 100, 75, 30);
        blueButton.setActionCommand("Blue");

        ButtonGroup groupTeam = new ButtonGroup();
        groupTeam.add(redButton);
        groupTeam.add(blueButton);

        this.add(redButton);
        this.add(blueButton);

        redButton.addActionListener(this);
        blueButton.addActionListener(this);

        frame.setVisible(true);
    }

    public int getType() {
        if(infantryButton.isSelected())
            return 0;
        else if(heavyInfantryButton.isSelected())
            return 1;
        else if(cavalryButton.isSelected())
            return 2;
        else
            return 3;
    }

    public int getTeam() {
        if(redButton.isSelected())
            return 0;
        return 1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start)
            wait = false;
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }
}
