package me.dufek.securitydrones.gui.menu.help;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Jan Dufek
 */
public class AboutDialog {

    private final JFrame frame;

    public AboutDialog(JFrame frame) {
        this.frame = frame;
    }

    public void create() {
        JPanel panel = new JPanel(new GridBagLayout());

        createIcon(panel);

        createTitle(panel);

        createVersion(panel);

        createAuthorTitle(panel);
        createAuthor(panel);

        createYearTitle(panel);
        createYear(panel);

        createWebsiteTitle(panel);
        createWebsite(panel);

        createEmailTitle(panel);
        createEmail(panel);

        createCopyright(panel);
        createReservation(panel);

//        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
//        labels.add(new JLabel("Version", SwingConstants.RIGHT));
//        labels.add(new JLabel("Author", SwingConstants.RIGHT));
//        labels.add(new JLabel("Year", SwingConstants.RIGHT));
//        panel.add(labels, BorderLayout.WEST);
//
//        JPanel fields = new JPanel(new GridLayout(0, 1, 2, 2));
//        fields.add(new JLabel("1.0.0", SwingConstants.RIGHT));
//        fields.add(new JLabel("Jan Dufek", SwingConstants.RIGHT));
//        fields.add(new JLabel("2013 - 2014", SwingConstants.RIGHT));
//        panel.add(fields, BorderLayout.CENTER);
        String[] buttons = {"OK"};
        JOptionPane.showOptionDialog(
                frame,
                panel,
                "About",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                buttons[0]
        );
    }

    private void createIcon(JPanel panel) {
        ImageIcon titleIcon = new ImageIcon("icon.png");

        JLabel iconLabel = new JLabel(titleIcon);

        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(iconLabel, constraints);
    }

    private void createTitle(JPanel panel) {
        JLabel title = new JLabel("<html><font size=+2><b>Security Drones</b><font></html>");

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createVersion(JPanel panel) {
        JLabel title = new JLabel("Version 1.0.0");

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 10, 20, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createAuthorTitle(JPanel panel) {
        JLabel title = new JLabel("Author");

        title.setHorizontalAlignment(SwingConstants.RIGHT);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(10, 10, 2, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createAuthor(JPanel panel) {
        JLabel title = new JLabel("Jan Dufek");

        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(10, 10, 2, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createYearTitle(JPanel panel) {
        JLabel title = new JLabel("Year");

        title.setHorizontalAlignment(SwingConstants.RIGHT);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 10, 2, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createYear(JPanel panel) {
        JLabel title = new JLabel("2013 - 2014");

        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 10, 2, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createWebsiteTitle(JPanel panel) {
        JLabel title = new JLabel("Website");

        title.setHorizontalAlignment(SwingConstants.RIGHT);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 10, 2, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createWebsite(JPanel panel) {
        JLabel title = new JLabel("www.dufek.me");

        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 10, 2, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createEmailTitle(JPanel panel) {
        JLabel title = new JLabel("E-mail");

        title.setHorizontalAlignment(SwingConstants.RIGHT);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 10, 20, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createEmail(JPanel panel) {
        JLabel title = new JLabel("jan@dufek.me");

        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 10, 20, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createCopyright(JPanel panel) {
        JLabel title = new JLabel("Copyright \u00a9 2013 - 2014 Jan Dufek.");

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(10, 10, 2, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }

    private void createReservation(JPanel panel) {
        JLabel title = new JLabel("All Rights Reserved.");

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 10, 10, 10);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(title, constraints);
    }
}
