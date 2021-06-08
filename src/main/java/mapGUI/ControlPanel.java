package mapGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel implements ActionListener {
    private final JButton start;

    public ControlPanel() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setLayout(null);

        this.setLayout(null);
        frame.setVisible(true);

        start = new JButton("START");
        start.setSize(200, 30);
        start.setLocation(100, 220);
        start.addActionListener(this);

        this.add(start);

        JRadioButton infantryButton = new JRadioButton("Infantry");
        //infantryButton.setMnemonic(KeyEvent.VK_B);
        infantryButton.setActionCommand("Infantry");
        infantryButton.setBounds(150, 20, 200, 30);
        infantryButton.setSelected(true);

        JRadioButton heavyInfantryButton = new JRadioButton("Heavy infantry");
        //heavyInfantryButton.setMnemonic(KeyEvent.VK_C);
        heavyInfantryButton.setBounds(150, 60, 200, 30);
        heavyInfantryButton.setActionCommand("Heavy infantry");

        JRadioButton cavalryButton = new JRadioButton("Cavalry");
        //cavalryButton.setMnemonic(KeyEvent.VK_D);
        cavalryButton.setBounds(150, 100, 200, 30);
        cavalryButton.setActionCommand("Cavalry");

        JRadioButton archersButton = new JRadioButton("Archers");
        //archersButton.setMnemonic(KeyEvent.VK_R);
        archersButton.setBounds(150, 140, 200, 30);
        archersButton.setActionCommand("Archers");

        ButtonGroup group = new ButtonGroup();
        group.add(infantryButton);
        group.add(heavyInfantryButton);
        group.add(cavalryButton);
        group.add(archersButton);

        this.add(infantryButton);
        this.add(heavyInfantryButton);
        this.add(cavalryButton);
        this.add(archersButton);

        infantryButton.addActionListener(this);
        heavyInfantryButton.addActionListener(this);
        cavalryButton.addActionListener(this);
        archersButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start)
            System.out.println("start");
        else
            System.out.println(e.getSource());
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }
}
