
import com.esri.mo2.data.feat.Field;
import com.esri.mo2.data.feat.Fields;
import com.esri.mo2.ui.bean.Layer;
import com.esri.mo2.ui.bean.Map;
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import com.esri.mo2.ui.toc.TreeToc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class SimpleShieldFrame extends JFrame {
  TreeToc treeToc1 = new TreeToc();
  Map map1 = new Map();
  ZoomPanToolBar zoomPanToolBar1 = new ZoomPanToolBar();
  JButton jButton1 = new JButton();
  Layer layer1 = new Layer();
  Layer layer2 = new Layer();
  Layer layer3 = new Layer();
  com.esri.mo2.map.dpy.FeatureLayer flayer = null;

  public SimpleShieldFrame() {
    try {
      init();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static void main(String[] args) {
    SimpleShieldFrame frame1 = new SimpleShieldFrame();
    frame1.show();
  }
  private void init() throws Exception {
    this.setTitle("SimpleShield Symbols");
    this.getContentPane().setLayout(null);
    treeToc1.setMap(map1);
    treeToc1.setBounds(new Rectangle(0, 52, 119, 309));
    zoomPanToolBar1.setMap(map1);
    zoomPanToolBar1.setBounds(new Rectangle(200, 5, 244, 43));
    map1.setBounds(new Rectangle(122, 51, 454, 309));
    jButton1.setBounds(new Rectangle(4, 2, 155, 48));
    jButton1.setText("Create Symbols");
    jButton1.addActionListener(new Frame1_jButton1_actionAdapter(this));
    layer1.setDataset("com.esri.mo2.src.file.FileSystemConnection!C:/ESRI/MOJ20/Samples/Data/USA/states.shp!");
    layer1.setBounds(new Rectangle(125, 91, 32, 32));
    layer2.setDataset("com.esri.mo2.src.file.FileSystemConnection!C:/ESRI/MOJ20/Samples/Data/USA/ushigh.shp!");
    layer2.setBounds(new Rectangle(261, 153, 32, 32));
    layer3.setDataset("com.esri.mo2.src.file.FileSystemConnection!C:/ESRI/MOJ20/Samples/Data/USA/capitals.shp!");
    layer3.setBounds(new Rectangle(274, 93, 32, 32));
    this.getContentPane().add(treeToc1, null);
    this.getContentPane().add(map1, null);
    this.getContentPane().add(zoomPanToolBar1, null);
    this.setSize(new Dimension(580, 363));
    map1.add(layer1, null);
    this.getContentPane().add(jButton1, null);
    map1.add(layer2, null);
    map1.add(layer3, null);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

   void jButton1_actionPerformed(ActionEvent e) {
     try {
     flayer = (com.esri.mo2.map.dpy.FeatureLayer)layer2.getLayer();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }

     com.esri.mo2.map.draw.SimpleShieldSymbol ssym =
         new com.esri.mo2.map.draw.SimpleShieldSymbol();
     com.esri.mo2.map.draw.BaseSimpleLabelRenderer bslr =
         new com.esri.mo2.map.draw.BaseSimpleLabelRenderer();
     String fname1 = "ROUTE";
     Fields fields = flayer.getFeatureClass().getFields();
     int intF1 = fields.findField(fname1);
     Field f1 = fields.getField(intF1);

     bslr.setLabelField(f1);
     ssym.setType(ssym.SIMPLESHIELD_USROAD);
     ssym.setFont(new Font("Times Roman", 0, 12));
     bslr.setSymbol(ssym);
     flayer.setLabelRenderer(bslr);
     map1.redraw();

  }


  class Frame1_jButton1_actionAdapter implements java.awt.event.ActionListener {
    SimpleShieldFrame adaptee;

    Frame1_jButton1_actionAdapter(SimpleShieldFrame adaptee) {
      this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
      adaptee.jButton1_actionPerformed(e);
    }
  }
}

