//Arthur Flores
//Red ID: 816317977
//Homework #2
//GIS Professor Eckberg
   import javax.swing.*;
   import javax.swing.event.*;
   import java.awt.*;
   import java.awt.event.*;

   public class Homework2 extends JFrame{
		JPanel myjp= new JPanel();
	   	public Homework2(){
			addWindowListener(new WindowAdapter() {
			      public void windowClosing(WindowEvent we) {
			        dispose();
			        System.exit(0);
			      }
    		} );
    		//Add ContentPane
 			Container contentPane = getContentPane();
    		contentPane.add(new myPanel());
    		//Set Things for Frame
			setTitle("Favorite Movies");
			setBounds(20,20,400,350);
			setVisible(true);
	   }

	   public static void main(String args[]){
			new Homework2();
	   }

	class myPanel extends JPanel implements ActionListener{
		JButton buttonLink= new JButton();
		//holds link of particular button
		String link=null;
		//holds description of movie
		String description=null;
	  public myPanel () {
		//Button 1
	    buttonPolice1 = new JButton();
	    buttonPolice1.setIcon(new ImageIcon("C:\\esri\\MOJ20\\examples\\PoliceDuke.gif"));
	    //Button 2
	    buttonSoccer2 = new JButton();
	    buttonSoccer2.setIcon(new ImageIcon("C:\\esri\\MOJ20\\examples\\SoccerDuke.gif"));
	    //Button 3
	    buttonIndiana3 = new JButton();
	    buttonIndiana3.setIcon(new ImageIcon("C:\\esri\\MOJ20\\examples\\IndianaJonesDuke.jpg"));
	    //Button 4
	    buttonTeacher4 = new JButton();
	    buttonTeacher4.setIcon(new ImageIcon("C:\\esri\\MOJ20\\examples\\TeacherDuke.jpg"));
	    //Add buttons to panel
	    add(buttonPolice1); add(buttonSoccer2); add(buttonIndiana3); add(buttonTeacher4);
	    //Panel listens for button
	    buttonPolice1.addActionListener(this);
	    buttonSoccer2.addActionListener(this);
	    buttonIndiana3.addActionListener(this);
	    buttonTeacher4.addActionListener(this);
	    buttonLink.addActionListener(this);
	  }
	  public void actionPerformed(ActionEvent evt) {
	    Object source =  evt.getSource();  // returns name of button where event occurred
	    JDialog dialog = new JDialog();
	    JPanel panel = new JPanel(new BorderLayout());
	    JLabel label= new JLabel();
	    //Array for Youtube Trailer links
		String linklist[]= {"https://www.youtube.com/watch?v=GUorM4nTX7k","https://www.youtube.com/watch?v=tl_gzZZspG4","https://www.youtube.com/watch?v=nMhfESAa4tw","https://www.youtube.com/watch?v=3PsUJFEBC74"};
	    //if button1 is clicked
	    if (source == buttonPolice1){
			buttonLink.setText("Movie Trailer for Lethal Weapon");
			dialog.setTitle("Lethal Weapon");
			link=linklist[0];
			description= "A veteran cop, Murtaugh, is partnered with a young suicidal cop, Riggs. Both having one thing in common; hating working in pairs. Now they must learn to work with one another to stop a gang of drug smugglers.";
		}
		//if button2 is clicked
	    else if (source == buttonSoccer2){
			buttonLink.setText("Movie Trailer for Kicking and Screaming");
			dialog.setTitle("Kicking and Screaming");
			link=linklist[1];
			description="Family man Phil Weston, a lifelong victim of his father's competitive nature, takes on the coaching duties of a kids' soccer team, and soon finds that he's also taking on his father's dysfunctional way of relating";
		}
		//if button3 is clicked
	    else if (source == buttonIndiana3){
			buttonLink.setText("Movie Trailer for Indiana Jones");
			dialog.setTitle("Indiana Jones");
			link=linklist[2];
			description="Famed archaeologist/adventurer Dr. Henry Indiana Jones is called back into action when he becomes entangled in a Soviet plot to uncover the secret behind mysterious artifacts known as the Crystal Skulls.";
		}
		//if button4 is clicked
	    else if(source == buttonTeacher4){
			buttonLink.setText("Movie Trailer for School of Rock");
			dialog.setTitle("School of Rock");
			link=linklist[3];
			description="After being kicked out of a rock band, Dewey Finn becomes a substitute teacher of a strict elementary private school, only to try and turn it into a rock band.";
		}
		//if link button is clicked it will launch browser
		else{
			launchPreferredBrowser(link);
		}
		//if it is not the link button pressed then open new JDialog
		if(source != buttonLink){
			label.setText("<html>"+description+"</html>");
			dialog.setBounds(100,100,400,400);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        	panel.add(buttonLink,BorderLayout.NORTH);
        	panel.add(label,BorderLayout.CENTER);
        	label.setLocation(123,20);
        	dialog.add(panel);
        	dialog.setVisible(true);
	   	}
	  }
	  private JButton buttonPolice1, buttonSoccer2, buttonIndiana3, buttonTeacher4;
	}
	//Opens a web browser with a particual url
	private static void launchPreferredBrowser(String url){
		String osName= System.getProperty("os.name");
		try{
			if(osName.startsWith("Windows")){
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+url);
			}
		}catch(Exception e){System.out.println(e.getMessage());}
	}

   }