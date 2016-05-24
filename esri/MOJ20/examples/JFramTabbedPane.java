// This program explores JTabbedPane, familiar to us from its frequent
// use in the windows operating systems interfaces, and other places.
// The idea is to create a kind of index by name into a set of Panes.
// We delay adding components until a tab gets clicked.  This component
// is related in functionality to JSplitPane and Card Layout.

import javax.swing.*; // javax indicates an extension, rather than a core
    // package;  there was a lot of debate over the name
import javax.swing.event.*; // nec. for ListSelectionEvent, a swing event
import java.awt.*;
import java.awt.event.*;
public class JFramTabbedPane extends JFrame{
  public JFramTabbedPane() {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        dispose();
        System.exit(0);
      }
    } );
    final JTabbedPane jtp = new JTabbedPane();
    jtp.add("Software",null);
    jtp.add("Hardware",null);
    jtp.add("Security",null);
    Container contentPane = getContentPane();
    contentPane.add(jtp,"Center");
    final String [] s = {"duke09.gif","duke13.gif","duke11.gif"};
    jtp.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent ce) {
                if (jtp.getSelectedComponent()==null) {
              int n = jtp.getSelectedIndex();
              ImageIcon imic = new ImageIcon(s[n]);
              jtp.setComponentAt(n,new JLabel(imic));// trick here is that
                     // an image can be made the display value of a JLabel
            }
      }
    });

  }
  public static void main(String args[]) {
    JFramTabbedPane f = new JFramTabbedPane();
    f.setTitle("ducks");
    f.setBounds(20,20,400,350);
    f.setVisible(true);
  }
}