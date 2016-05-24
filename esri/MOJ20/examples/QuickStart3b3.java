// goal is to examine java button, and maybe
// control checkboxes by a button(their visibility)
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.text.*;
import com.esri.mo2.ui.bean.*;
import com.esri.mo2.ui.tb.*;
import com.esri.mo2.cs.geom.InvalidValuesException;

public class QuickStart3b3 extends JFrame {
  Map map = new Map();
  Layer layer = new Layer();
  Layer layer2 = new Layer();
  Toc toc = new Toc();
  String s1 = "C:\\ESRI\\MOJ20\\Samples\\Data\\USA\\States.shp";
  String s2 = "C:\\ESRI\\MOJ20\\Samples\\Data\\USA\\capitals.shp";
  ZoomPanToolBar zptb = new ZoomPanToolBar();
  SelectionToolBar stb = new SelectionToolBar();
  JButton uncheck = new JButton("uncheckCities");
  JPanel myjp = new JPanel();
  TocAdapter mytocadapter;
  ComponentListener complistener;

  public QuickStart3b3() throws ParseException {
    //add a title to the window
    super("QuickStart3b3");
    //set the size
    setSize(750, 540);
    //add the map to the frame
    zptb.setMap(map);
    stb.setMap(map);

    complistener = new ComponentAdapter () {
          public void componentResized(ComponentEvent ce) {
            Dimension d = ce.getComponent().getSize();
            System.out.println(map.getMapExtent());
            System.out.println(d.width + "  " + d.height);
          }
    };
    addComponentListener(complistener);
    toc.setMap(map);
    // The following is a longcut mandated by an ESRI decision to
    // garbage collect immediately any non-hard references;  it is
    // discussed in the FAQ in MOJ10;   the code may not work without
    // this hard reference to the TocAdapter object!!!!
    mytocadapter = new TocAdapter() {
          public void click(TocEvent e) {
            stb.setSelectedLayer((e.getLegend()).getLayer());
          }
    };
    toc.addTocListener(mytocadapter);
    uncheck.addActionListener(lis);

    myjp.add(zptb); myjp.add(stb); myjp.add(uncheck);
    getContentPane().add(map, BorderLayout.CENTER);
    getContentPane().add(myjp,BorderLayout.NORTH);
    //getContentPane().add(stb,BorderLayout.SOUTH);
    //add a shape file to the map
    addShapefileToMap(layer,s1);
    addShapefileToMap(layer2,s2);
    //add a Toc

    stb.setSelectedLayer(map.getLayer(0));
    //stb.selectedLayer = map.getLayer(1);// error: variable is protected
    getContentPane().add(toc, BorderLayout.WEST);
  }
  private void addShapefileToMap(Layer layer,String s) {
    String datapath = s; //"C:\\ESRI\\MOJ20\\Samples\\Data\\USA\\States.shp";
    layer.setDataset("0;"+datapath);
    map.add(layer);
  }
  ActionListener lis = new ActionListener(){public void actionPerformed(ActionEvent evt) {
    Object source =  evt.getSource();
    if (source == uncheck) System.out.println("gotcha");
    java.util.List list = toc.getAllLegends();
    Legend legend = (Legend)list.get(0);System.out.println(legend.getClass().getName());
    JComponent jcb = legend.getCheckBox();jcb.setVisible(false);
    //AbstractButton ab = (AbstractButton) jcb;
  }};
  public static void main(String[] args) throws ParseException {
    QuickStart3b3 qstart = new QuickStart3b3();
    qstart.setIconImage(com.esri.mo2.util.Resource.getIcon("print.gif"));
    qstart.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.out.println("Thanks, Quick Start exits");
        System.exit(0);
      }
    });
    qstart.setVisible(true);
  }
}