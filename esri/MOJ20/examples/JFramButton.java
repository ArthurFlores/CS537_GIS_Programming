// This program explores buttons and listeners.  It uses the swing set. The
// swing set is not 'peer-based'.  This means that instead of translating
// java stuff into host machine window stuff, java just draws pictures onto
// blank host machine windows.   This takes more time, but makes the look
// and feel of swing set applications much more platform independent.
// JFrame is actually a subclass of Frame.    And JPanel is a subframe of
// JComponent, which is a subclass of Component.

// In Java 1.2 there are 10 Event classes, like WindowEvent, descended
// from AWTEvent, which is a kid of EventObject.  EventObject has setSource(),
// and AWTEvent has getID().   There are 11 Listener interfaces, such as
// WindowListener, and 7 of those have Adapter classes, e.g. WindowAdapter.

// The following events are classified as 'semantic' (the others are
// 'low level'): Action, Adjustment, Item, and Text.

import javax.swing.*; // javax indicates an extension, rather than a core
    // package;  there was a lot of debate over the name
import java.awt.*;
import java.awt.event.*;
public class JFramButton extends JFrame{
  public JFramButton() {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        dispose();
        System.exit(0);
      }
    } );
    // the best way to place things on a JFrame is to create a contentPane,
    // which is like an overlay of the JFrame, and place stuff there!
    // The 'stuff' is usually an extension of a JPanel.   So a JPanel goes
    // on a contentPane which overlays a JFrame
    Container contentPane = getContentPane();
    contentPane.add(new myPanel());
  }
  public static void main(String args[]) {
    JFramButton f = new JFramButton();
    f.setTitle("ducks");
    f.setBounds(20,20,400,350);
    Color color = f.getBackground();
    System.out.println("Background color is " + color);
    f.setVisible(true);
  }
}

class myPanel extends JPanel implements ActionListener{
  public myPanel () {
    yellowButton = new JButton("yellow");
    redButton = new JButton("red");
    blueButton = new JButton("blue");
    stopButton = new JButton("exit");
    add(yellowButton); add(redButton); add(blueButton); add(stopButton);
    yellowButton.addActionListener(this); // panel will listen for button click
    redButton.addActionListener(this);
    blueButton.addActionListener(this);
    stopButton.addActionListener(this);
  }
  public void actionPerformed(ActionEvent evt) {
    Object source =  evt.getSource();  // returns name of button where event
                                     // occurred
    Color color = getBackground(); // must initialize
    if (source == yellowButton) color = Color.yellow;
    else if (source == redButton) color = new Color(255,0,0); // pure red
    else if (source == blueButton) color = Color.blue;
    else System.exit(0);
    setBackground(color);
    repaint();
    System.out.println("Background color is " + color); // toString is
                 // defined for the class Color
  }
  private JButton yellowButton, redButton, blueButton, stopButton;
}