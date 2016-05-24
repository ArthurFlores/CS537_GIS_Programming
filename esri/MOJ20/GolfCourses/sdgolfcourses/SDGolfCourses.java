package sdgolfcourses;
import javax.swing.*;

import java.io.*;
import java.sql.Array;
import java.math.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;

import com.esri.mo2.ui.bean.*;
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import com.esri.mo2.ui.tb.SelectionToolBar;
import com.esri.mo2.ui.ren.LayerProperties;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import com.esri.mo2.data.feat.*;
import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.map.dpy.BaseFeatureLayer;
import com.esri.mo2.map.draw.AoLineStyle;
import com.esri.mo2.map.draw.SimpleMarkerSymbol;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.draw.TrueTypeMarkerSymbol;
import com.esri.mo2.file.shp.*;
import com.esri.mo2.map.dpy.Layerset;
import com.esri.mo2.ui.bean.Tool;
import java.awt.geom.*;
import com.esri.mo2.ui.dlg.AboutBox;
import com.esri.mo2.cs.geom.*;

public class SDGolfCourses extends JFrame {
	static Map map = new Map();
	static boolean fullMap = true;
	static boolean helpToolOn;
	Legend legend;
	Legend legend2;
	Layer layer = new Layer();
	Layer layer2 = new Layer();
	Layer layer3 = null;
	static AcetateLayer acetLayer;
	static com.esri.mo2.map.dpy.Layer layer4;
	com.esri.mo2.map.dpy.Layer activeLayer;
	int activeLayerIndex;
	com.esri.mo2.cs.geom.Point initPoint, endPoint;
	double distance;
	JMenuBar mbar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu theme = new JMenu("Theme");
	JMenu layercontrol = new JMenu("LayerControl");
	JMenu help = new JMenu("Help");

	//TODO use this if you want make jar relocatable. Its incomplete
	java.net.URL urlattribitem = getClass().getResource("data/tableview.gif");
	java.net.URL urlcreatelayeritem = getClass().getResource("data/Icon0915b.jpg");
	java.net.URL urlpromoteitem = getClass().getResource("data/promote.jpg");
	java.net.URL urldemoteitem = getClass().getResource("data/demote.jpg");
	java.net.URL urlprintitem = getClass().getResource("data/print.gif");
	java.net.URL urladdlyritem = getClass().getResource("data/addtheme.gif");
	java.net.URL urlremlyritem = getClass().getResource("data/delete.gif");
	java.net.URL urlpropsitem = getClass().getResource("data/properties.gif");
	java.net.URL urlhelptopics = getClass().getResource("data/helptopic.gif");
	java.net.URL urllegenditem = getClass().getResource("data/helptopic.gif");
	java.net.URL urllayercontrolitem = getClass().getResource("data/helptopic.gif");
	java.net.URL urlhelptoolitem = getClass().getResource("/data/help2.gif");

	JMenuItem attribitem = new JMenuItem("open attribute table", new ImageIcon(
                                                                               "data/tableview.gif"));
	JMenuItem createlayeritem = new JMenuItem("create layer from selection",
                                              new ImageIcon("data/Icon0915b.jpg"));
	static JMenuItem promoteitem = new JMenuItem("promote selected layer",
                                                 new ImageIcon("data/promote.jpg"));
	JMenuItem demoteitem = new JMenuItem("demote selected layer",
                                         new ImageIcon("data/demote.jpg"));
	JMenuItem printitem = new JMenuItem("print",
                                        new ImageIcon("data/print.gif"));
	JMenuItem addlyritem = new JMenuItem("add layer", new ImageIcon(
                                                                    "data/addtheme.gif"));
	JMenuItem remlyritem = new JMenuItem("/data/remove layer", new ImageIcon(
                                                                       "data/delete.gif"));
	JMenuItem propsitem = new JMenuItem("Legend Editor", new ImageIcon(
                                                                       "data/properties.gif"));
	JMenu helptopics = new JMenu("Help Topics");
	JMenuItem tocitem = new JMenuItem("Table of Contents", new ImageIcon(
                                                                         "data/helptopic.gif"));
	JMenuItem legenditem = new JMenuItem("Legend Editor", new ImageIcon(
                                                                        "data/helptopic.gif"));
	JMenuItem layercontrolitem = new JMenuItem("Layer Control", new ImageIcon(
                                                                              "data/helptopic.gif"));
	JMenuItem helptoolitem = new JMenuItem("Help Tool");
	JMenuItem contactitem = new JMenuItem("Contact us");
	JMenuItem aboutitem = new JMenuItem("About MOJO...");
	Toc toc = new Toc();
	String SDCountyShp = "shapefiles/sdcounty.shp";
	String GolfCoursesShp = "shapefiles/GolfCourses.shp";
	// String SDCountyShp = "sdcounty.shp";
	String datapathname = "";
	String legendname = "";
	ZoomPanToolBar zptb = new ZoomPanToolBar();
	static SelectionToolBar stb = new SelectionToolBar();
	JToolBar jtb = new JToolBar();
	ComponentListener complistener;
	JLabel statusLabel = new JLabel("status bar    LOC");
	static JLabel milesLabel = new JLabel("   DIST:  0 mi    ");
	static JLabel kmLabel = new JLabel("  0 km    ");
	java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
	JPanel myjp = new JPanel();
	JPanel myjp2 = new JPanel();
	JButton prtjb = new JButton(new ImageIcon("data/print.gif"));
	JButton addlyrjb = new JButton(new ImageIcon("data/addtheme.gif"));
	JButton ptrjb = new JButton(new ImageIcon("data/pointer.gif"));
	JButton distjb = new JButton(new ImageIcon("data/measure_1.gif"));
	JButton XYjb = new JButton("XY");
	JButton helpjb = new JButton(new ImageIcon("data/help2.gif"));
	JButton hotjb = new JButton(new ImageIcon("data/hotlink.gif"));
	Arrow arrow = new Arrow();
	static HelpTool helpTool = new HelpTool();
	ActionListener lis;
	ActionListener layerlis;
	ActionListener layercontrollis;
	ActionListener helplis;
	TocAdapter mytocadapter;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image bolt = tk.getImage("data/hotlink_32x32-32.gif");
	java.awt.Cursor boltCursor = tk.createCustomCursor(bolt,
                                                       new java.awt.Point(6, 30), "bolt");
	MyPickAdapter picklis = new MyPickAdapter();
	Identify hotlink = new Identify();
	static String golfCourse = null;

	class MyPickAdapter implements PickListener {
		public void beginPick(PickEvent pe) {
		};

		// this fires even when you click outside the layer
		public void endPick(PickEvent pe) {
		}

		public void foundData(PickEvent pe) {
			com.esri.mo2.data.feat.Cursor c = pe.getCursor();
			Feature f = null;
			// Fields fields = null;
			if (c != null)
				f = (Feature) c.next();

			//TODO remove this after testing
			/*Fields fields = f.getFields();

			for (int j = 1; j < fields.getNumFields(); j++) {
				 System.out.println("Attr Table " + j);
				 System.out.println("::" + (String)f.getValue(j));
			}*/

			try {
				HotPick hotpick = new HotPick(f);
			} catch (Exception e) {
			  e.printStackTrace();
			}
		}
	};

	static Envelope env;

	public SDGolfCourses() {

		super("San Diego County Golf Courses");
		helpToolOn = false;
		this.setSize(700, 450);
		zptb.setMap(map);
		stb.setMap(map);
		setJMenuBar(mbar);
		ActionListener lisZoom = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fullMap = false;
			}
		};
		ActionListener lisFullExt = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fullMap = true;
			}
		};

		MouseAdapter mlLisZoom = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					try {
						HelpDialog helpdialog = new HelpDialog(helpText.get(4));
						helpdialog.setVisible(true);
					} catch (IOException e) {
					}
				}
			}
		};
		MouseAdapter mlLisZoomActive = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					try {
						HelpDialog helpdialog = new HelpDialog(helpText.get(5));
						helpdialog.setVisible(true);
					} catch (IOException e) {
					}
				}
			}
		};
		MouseAdapter mlLisMeasureTool = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					try {
						HelpDialog helpdialog = new HelpDialog(helpText.get(6));
						helpdialog.setVisible(true);
					} catch (IOException e) {
					}
				}
			}
		};
		MouseAdapter mlLisXY = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					try {
						HelpDialog helpdialog = new HelpDialog(helpText.get(7));
						helpdialog.setVisible(true);
					} catch (IOException e) {
					}
				}
			}
		};
		MouseAdapter mlHotLink = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					try {
						HelpDialog helpdialog = new HelpDialog(helpText.get(8));
						helpdialog.setVisible(true);
					} catch (IOException e) {
					}
				}
			}
		};

		JButton zoomInButton = (JButton) zptb.getActionComponent("ZoomIn");
		JButton zoomFullExtentButton = (JButton) zptb.getActionComponent("ZoomToFullExtent");
		JButton zoomToSelectedLayerButton = (JButton) zptb.getActionComponent("ZoomToSelectedLayer");
		zoomInButton.addActionListener(lisZoom);
		zoomInButton.addMouseListener(mlLisZoom);
		zoomFullExtentButton.addActionListener(lisFullExt);
	    //zoomFullExtentButton.addMouseListener(mlLisZoomFullExtent);
		zoomToSelectedLayerButton.addActionListener(lisZoom);
		zoomToSelectedLayerButton.addMouseListener(mlLisZoomActive);




		complistener = new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				if (fullMap) {
					map.setExtent(env);
					map.zoom(1.0);
					map.redraw();
				}
			}
		};
		addComponentListener(complistener);
		lis = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source == prtjb || source instanceof JMenuItem) {
					com.esri.mo2.ui.bean.Print mapPrint = new com.esri.mo2.ui.bean.Print();
					mapPrint.setMap(map);
					mapPrint.doPrint();// prints the map
				} else if (source == ptrjb) {
					arrow.arrowChores();
					map.setSelectedTool(arrow);
					DistanceTool.resetDist();
				} else if (source == distjb) {
					DistanceTool distanceTool = new DistanceTool();
					map.setSelectedTool(distanceTool);
				} else if (source == XYjb) {
					try {
						AddXYtheme addXYtheme = new AddXYtheme();
						addXYtheme.setMap(map);
						addXYtheme.setVisible(false);
						map.redraw();
					} catch (IOException e) {
					}
				} else if (source == helpjb) {
					helpToolOn = true;
					map.setSelectedTool(helpTool);
				} else if (source == hotjb) {
					hotlink.setCursor(boltCursor);
					map.setSelectedTool(hotlink);
				} else {
					try {
						AddLyrDialog aldlg = new AddLyrDialog();
						aldlg.setMap(map);
						aldlg.setVisible(true);
					} catch (IOException e) {
					}
				}
			}
		};
		layercontrollis = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String source = ae.getActionCommand();
				System.out.println(activeLayerIndex + " active index");
				if (source == "promote selected layer")
					map.getLayerset().moveLayer(activeLayerIndex,++activeLayerIndex);
				else
					map.getLayerset().moveLayer(activeLayerIndex,--activeLayerIndex);
				enableDisableButtons();
				map.redraw();
			}
		};
		helplis = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source instanceof JMenuItem) {
					String arg = ae.getActionCommand();
					if (arg == "About MOJO...") {
						AboutBox aboutbox = new AboutBox();
						aboutbox.setLogo(new ImageIcon("data/about.jpg"));
						aboutbox.setProductName("MOJO");
						aboutbox.setProductVersion("2.0");
						aboutbox.setVisible(true);
					} else if (arg == "Contact us") {
						try {
							String s = "\n\n\n\n              Any enquiries should be addressed to "
                            + "\n\n\n                         mokashi.pallavi.n@gmail.com";
							HelpDialog helpdialog = new HelpDialog(s);
							helpdialog.setVisible(true);
						} catch (IOException e) {
						}
					} else if (arg == "Table of Contents") {
						try {
							HelpDialog helpdialog = new HelpDialog(
                                                                   helpText.get(0));
							helpdialog.setVisible(true);
						} catch (IOException e) {
						}
					} else if (arg == "Legend Editor") {
						try {
							HelpDialog helpdialog = new HelpDialog(
                                                                   helpText.get(1));
							helpdialog.setVisible(true);
						} catch (IOException e) {
						}
					} else if (arg == "Layer Control") {
						try {
							HelpDialog helpdialog = new HelpDialog(
                                                                   helpText.get(2));
							helpdialog.setVisible(true);
						} catch (IOException e) {
						}
					} else if (arg == "Help Tool") {
						try {
							HelpDialog helpdialog = new HelpDialog(
                                                                   helpText.get(3));
							helpdialog.setVisible(true);
						} catch (IOException e) {
						}
					}
				}
			}
		};
		layerlis = new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source instanceof JMenuItem) {
					String arg = ae.getActionCommand();
					if (arg == "add layer") {
						try {
							AddLyrDialog aldlg = new AddLyrDialog();
							aldlg.setMap(map);
							aldlg.setVisible(true);
						} catch (IOException e) {
						}
					} else if (arg == "remove layer") {
						try {
							com.esri.mo2.map.dpy.Layer dpylayer = legend
                            .getLayer();
							map.getLayerset().removeLayer(dpylayer);
							map.redraw();
							remlyritem.setEnabled(false);
							propsitem.setEnabled(false);
							attribitem.setEnabled(false);
							promoteitem.setEnabled(false);
							demoteitem.setEnabled(false);
							stb.setSelectedLayer(null);
							zptb.setSelectedLayer(null);
						} catch (Exception e) {
						}
					} else if (arg == "Legend Editor") {
						LayerProperties lp = new LayerProperties();
						lp.setLegend(legend);
						lp.setSelectedTabIndex(0);
						lp.setVisible(true);
					} else if (arg == "open attribute table") {
						try {
							layer4 = legend.getLayer();
							AttrTab attrtab = new AttrTab();
							attrtab.setVisible(true);
						} catch (IOException ioe) {
						}
					} else if (arg == "create layer from selection") {
						layer4 = legend.getLayer();
						FeatureLayer flayer2 = (FeatureLayer) layer4;

						// select, e.g., Montana and then click the
						// create layer menuitem; next line verifies a selection was made
						System.out.println("has selected"+ flayer2.hasSelection());
						System.out.println("has selected: " + flayer2.hasSelection());
						System.out.println("layer info: " + flayer2.getLayerInfo());
						System.out.println("ShapeFileType: " + flayer2.getLayerInfo().getType()); // to get ShapeFileType

						int shT = flayer2.getLayerInfo().getType();

						com.esri.mo2.map.draw.BaseSimpleRenderer sbr = new com.esri.mo2.map.draw.BaseSimpleRenderer();
						com.esri.mo2.map.draw.SimpleFillSymbol sfs = null; // for polygon
						com.esri.mo2.map.draw.SimpleMarkerSymbol sms = null; // for points
						com.esri.mo2.map.draw.SimpleLineSymbol sls = null; // for lines
						if (shT == 2) {
							sfs = new com.esri.mo2.map.draw.SimpleFillSymbol();// for polygons
							sfs.setSymbolColor(new Color(255, 255, 0)); // mellow yellow
							sfs.setType(com.esri.mo2.map.draw.SimpleFillSymbol.FILLTYPE_SOLID);
							sfs.setBoundary(true);
							sbr.setSymbol(sfs);
						} else if (shT == 0) {
							sms = new com.esri.mo2.map.draw.SimpleMarkerSymbol();// for points
							sms.setType(com.esri.mo2.map.draw.SimpleMarkerSymbol.CIRCLE_MARKER);
							sms.setWidth(6);
							sms.setSymbolColor(Color.red);
							sbr.setSymbol(sms);
						} else if (shT == 1) {
							sls = new com.esri.mo2.map.draw.SimpleLineSymbol();// for lines
							sls.setLineColor(new Color(0, 255, 0)); // green
							sls.setStroke(AoLineStyle.getStroke(AoLineStyle.SOLID_LINE, 4));
							sbr.setSymbol(sls);
						}
						if (flayer2.hasSelection()) {
							SelectionSet selectset = flayer2.getSelectionSet();
							// next line makes a new feature layer of the selections

							FeatureLayer selectedlayer = flayer2.createSelectionLayer(selectset);
							sbr.setLayer(selectedlayer);
							sbr.setSymbol(sfs);
							selectedlayer.setRenderer(sbr);
							Layerset layerset = map.getLayerset();
							// next line places a new visible layer, e.g.
							// Montana, on the map
							layerset.addLayer(selectedlayer);
							// selectedlayer.setVisible(true);
							if (stb.getSelectedLayers() != null)
								promoteitem.setEnabled(true);
							try {
								legend2 = toc.findLegend(selectedlayer);
							} catch (Exception e) {
							}

							CreateShapeDialog csd = new CreateShapeDialog(
                                                                          selectedlayer);
							csd.setVisible(true);
							Flash flash = new Flash(legend2);
							flash.start();
							map.redraw(); // necessary to see color immediately

						}
					}
				}
			}
		};
		toc.setMap(map);
		mytocadapter = new TocAdapter() {
			public void click(TocEvent e) {
				System.out.println(activeLayerIndex + "dex");
				legend = e.getLegend();
				activeLayer = legend.getLayer();
				stb.setSelectedLayer(activeLayer);
				zptb.setSelectedLayer(activeLayer);
				// get acive layer index for promote and demote
				activeLayerIndex = map.getLayerset().indexOf(activeLayer);
				// layer indices are in order added, not toc order.
				System.out.println(activeLayerIndex + "active ndex");
				com.esri.mo2.map.dpy.Layer[] layers = { activeLayer };
				hotlink.setSelectedLayers(layers);// replaces setToc from MOJ10
				remlyritem.setEnabled(true);
				propsitem.setEnabled(true);
				attribitem.setEnabled(true);
				enableDisableButtons();
			}
		};
		map.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent me) {
				com.esri.mo2.cs.geom.Point worldPoint = null;
				if (map.getLayerCount() > 0) {
					worldPoint = map.transformPixelToWorld(me.getX(), me.getY());
					String s = "X:" + df.format(worldPoint.getX()) + " " + "Y:"
                    + df.format(worldPoint.getY());
					statusLabel.setText(s);
				} else
					statusLabel.setText("X:0.000 Y:0.000");
			}
		});

		toc.addTocListener(mytocadapter);
		remlyritem.setEnabled(false); // assume no layer initially selected
		propsitem.setEnabled(false);
		attribitem.setEnabled(false);
		promoteitem.setEnabled(false);
		demoteitem.setEnabled(false);
		printitem.addActionListener(lis);
		addlyritem.addActionListener(layerlis);
		remlyritem.addActionListener(layerlis);
		propsitem.addActionListener(layerlis);
		attribitem.addActionListener(layerlis);
		createlayeritem.addActionListener(layerlis);
		promoteitem.addActionListener(layercontrollis);
		demoteitem.addActionListener(layercontrollis);
		tocitem.addActionListener(helplis);
		legenditem.addActionListener(helplis);
		layercontrolitem.addActionListener(helplis);
		helptoolitem.addActionListener(helplis);
		contactitem.addActionListener(helplis);
		aboutitem.addActionListener(helplis);
		file.add(addlyritem);
		file.add(printitem);
		file.add(remlyritem);
		file.add(propsitem);
		theme.add(attribitem);
		theme.add(createlayeritem);
		layercontrol.add(promoteitem);
		layercontrol.add(demoteitem);
		help.add(helptopics);
		helptopics.add(tocitem);
		helptopics.add(legenditem);
		helptopics.add(layercontrolitem);
		help.add(helptoolitem);
		help.add(contactitem);
		help.add(aboutitem);
		mbar.add(file);
		mbar.add(theme);
		mbar.add(layercontrol);
		mbar.add(help);
		prtjb.addActionListener(lis);
		prtjb.setToolTipText("print map");
		addlyrjb.addActionListener(lis);
		addlyrjb.setToolTipText("add layer");
		ptrjb.addActionListener(lis);
		distjb.addActionListener(lis);
		distjb.addMouseListener(mlLisMeasureTool);
		XYjb.addActionListener(lis);
		XYjb.addMouseListener(mlLisXY);
		helpjb.addActionListener(lis);
		XYjb.setToolTipText("add a layer of points from a file");
		prtjb.setToolTipText("pointer");
		distjb.setToolTipText("press-drag-release to measure a distance");
		helpjb.setToolTipText("left click here, then right click on a tool to learn about that tool");
		hotlink.addPickListener(picklis);
		hotlink.setPickWidth(15);// sets tolerance for hotlink clicks
		hotjb.addActionListener(lis);
		hotjb.addMouseListener(mlHotLink);
		hotjb.setToolTipText("hotlink tool--click somthing to get information about it.");
		jtb.add(prtjb);
		jtb.add(addlyrjb);
		jtb.add(ptrjb);
		jtb.add(distjb);
		jtb.add(XYjb);
		jtb.add(hotjb);
		jtb.add(helpjb);
		myjp.add(jtb);
		myjp.add(zptb);
		myjp.add(stb);
		myjp2.add(statusLabel);
		myjp2.add(milesLabel);
		myjp2.add(kmLabel);
		setuphelpText();
		getContentPane().add(map, BorderLayout.CENTER);
		getContentPane().add(myjp, BorderLayout.NORTH);
		getContentPane().add(myjp2, BorderLayout.SOUTH);
		addShapefileToMap(layer, SDCountyShp);
		addShapefileToMap(layer2,GolfCoursesShp);
		// addShapefileToMap(layer2,s2);
		getContentPane().add(toc, BorderLayout.WEST);
	}

	private void addShapefileToMap(Layer layer, String s) {
		String datapath = s;
		layer.setDataset("0;" + datapath);
		map.add(layer);
	}

	private void setuphelpText() {
		String s0 = "    The toc, or table of contents, is to the left of the map. \n"
        + "    Each entry is called a 'legend' and represents a map 'layer' or \n"
        + "    'theme'.  If you click on a legend, that layer is called the \n"
        + "    active layer, or selected layer.  Its display (rendering) properties \n"
        + "    can be controlled using the Legend Editor, and the legends can be \n"
        + "    reordered using Layer Control.  Both Legend Editor and Layer Control \n"
        + "    are separate Help Topics.  This line is e... x... t... e... n... t... e... d"
        + "    to test the scrollpane.";
		helpText.add(s0);
		String s1 = "  The Legend Editor is a menu item found under the File menu. \n"
        + "    Given that a layer is selected by clicking on its legend in the table of \n"
        + "    contents, clicking on Legend Editor will open a window giving you choices \n"
        + "    about how to display that layer.  For example you can control the color \n"
        + "    used to display the layer on the map, or whether to use multiple colors ";
		helpText.add(s1);
		String s2 = "  Layer Control is a Menu on the menu bar.  If you have selected a \n"
        + " layer by clicking on a legend in the toc (table of contents) to the left of \n"
        + " the map, then the promote and demote tools will become usable.  Clicking on \n"
        + " promote will raise the selected legend one position higher in the toc, and \n"
        + " clicking on demote will lower that legend one position in the toc.";
		helpText.add(s2);
		String s3 = "    This tool will allow you to learn about certain other tools. \n"
        + "    You begin with a standard left mouse button click on the Help Tool itself. \n"
        + "    RIGHT click on another tool and a window may give you information about the  \n"
        + "    intended usage of the tool.  Click on the arrow tool to stop using the \n"
        + "    help tool.";
		helpText.add(s3);
		String s4 = "If you click on the Zoom In tool, and then click on the map, you \n"
        + " will see a part of the map in greater detail.  You can zoom in multiple times. \n"
        + " You can also sketch a rectangular part of the map, and zoom to that.  You can \n"
        + " undo a Zoom In with a Zoom Out or with a Zoom to Full Extent";
		helpText.add(s4);
		String s5 = "You must have a selected layer to use the Zoom to Active Layer tool.\n"
        + "    If you then click on Zoom to Active Layer, you will be shown enough of \n"
        + "    the full map to see all of the features in the layer you select.  If you \n"
        + "    select a layer that shows where glaciers are, then you do not need to \n"
        + "    see Hawaii, or any southern states, so you will see Alaska, and northern \n"
        + "    mainland states.";
		helpText.add(s5);
		String s6 = "If you click on the MOJO MeasureTool Measuring Tool and then.\n"
		+ " you click anywhere on the map, and drag and release.\n " +
		"This will give the distance between the mouse click and release.";
        helpText.add(s6);
        String s7 = " If you click on the XY icon tool it will allow us to add a CSV file.\n" +
				"    If you click on the XY, it will let us to move to the path where csv file is stored. \n" +
				"    once we add the csv file, the points considered in that file will be displayed \n" +
				"    on the map";
		helpText.add(s7);
		String s8 = "If you click on Bolt also called Hotlink Tool\n "
		+ "is a pointer tool, if you click on any points on \n map you can see information about that point" ;
		helpText.add(s8);
	}

	public static void main(String[] args) {
		SDGolfCourses SDGolfCourses = new SDGolfCourses();
		SDGolfCourses.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Thank you for using this tool.");
				System.exit(0);
			}
		});
		SDGolfCourses.setVisible(true);
		env = map.getExtent();
	}

	private void enableDisableButtons() {
		int layerCount = map.getLayerset().getSize();
		if (layerCount < 2) {
			promoteitem.setEnabled(false);
			demoteitem.setEnabled(false);
		} else if (activeLayerIndex == 0) {
			demoteitem.setEnabled(false);
			promoteitem.setEnabled(true);
		} else if (activeLayerIndex == layerCount - 1) {
			promoteitem.setEnabled(false);
			demoteitem.setEnabled(true);
		} else {
			promoteitem.setEnabled(true);
			demoteitem.setEnabled(true);
		}
	}

	private ArrayList<String> helpText = new ArrayList<String>(3);
}

// following is an Add Layer dialog window
class AddLyrDialog extends JDialog {
	Map map;
	ActionListener lis;
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	JPanel panel1 = new JPanel();
	com.esri.mo2.ui.bean.CustomDatasetEditor cus = new com.esri.mo2.ui.bean.CustomDatasetEditor();

	AddLyrDialog() throws IOException {
		setBounds(50, 50, 520, 430);
		setTitle("Select a theme/layer");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		lis = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source == cancel)
					setVisible(false);
				else {
					try {
						setVisible(false);
						map.getLayerset().addLayer(cus.getLayer());
						map.redraw();
						if (SDGolfCourses.stb.getSelectedLayers() != null)
							SDGolfCourses.promoteitem.setEnabled(true);
					} catch (IOException e) {
					}
				}
			}
		};
		ok.addActionListener(lis);
		cancel.addActionListener(lis);
		getContentPane().add(cus, BorderLayout.CENTER);
		panel1.add(ok);
		panel1.add(cancel);
		getContentPane().add(panel1, BorderLayout.SOUTH);
	}

	public void setMap(com.esri.mo2.ui.bean.Map map1) {
		map = map1;
	}
}

class AddXYtheme extends JDialog {
	Map map;
	Vector<String> s2 = new Vector<String>();
	JFileChooser jfc = new JFileChooser();
	BasePointsArray bpa = new BasePointsArray();

	AddXYtheme() throws IOException {
		setBounds(50, 50, 520, 430);
		jfc.showOpenDialog(this);
		if (jfc.getSelectedFile() != null) {
			try {

				File file = jfc.getSelectedFile();
				FileReader fred = new FileReader(file);
				BufferedReader in = new BufferedReader(fred);
				String s; // = in.readLine();
				double x, y;
				int n = 0;
				while ((s = in.readLine()) != null) {

					StringTokenizer st = new StringTokenizer(s, ",");
					System.out.println("St size::" + st.countTokens());
					x = Double.parseDouble(st.nextToken());
					y = Double.parseDouble(st.nextToken());
					bpa.insertPoint(n++, new com.esri.mo2.cs.geom.Point(x, y));
					s2.addElement(st.nextToken());
					s2.addElement(st.nextToken());
					s2.addElement(st.nextToken());
					s2.addElement(st.nextToken());
					s2.addElement(st.nextToken());
					s2.addElement(st.nextToken());
					s2.addElement(st.nextToken());
					s2.addElement(st.nextToken());

					System.out.println("S2 size::"+s2.size());
				}

			} catch (IOException e) {
			}
			System.out.println("Before XY Feature layer");
			XYfeatureLayer xyfl = new XYfeatureLayer(bpa, map, s2);
			System.out.println("After XY Feature layer");
			xyfl.setVisible(true);
			map = SDGolfCourses.map;
			map.getLayerset().addLayer(xyfl);
			map.redraw();
		}
	}

	public void setMap(com.esri.mo2.ui.bean.Map map1) {
		map = map1;
	}
}

class XYfeatureLayer extends BaseFeatureLayer {
	BaseFields fields;
	private java.util.Vector<BaseFeature> featureVector;
	static int lyrCnt = 0;

	public XYfeatureLayer(BasePointsArray bpa, Map map, Vector<String> s2) {
		lyrCnt++;
		createFeaturesAndFields(bpa, map, s2);

		BaseFeatureClass bfc = getFeatureClass("MyPoints" + lyrCnt, bpa);
		setFeatureClass(bfc);
		BaseSimpleRenderer srd = new BaseSimpleRenderer();

		try {
			TrueTypeMarkerSymbol ttm = new TrueTypeMarkerSymbol();
			ttm.setFont(new Font("ESRI Crime Analysis", Font.PLAIN,
                                    30));
			ttm.setColor(new Color(0, 104, 10));
			ttm.setCharacter("117");


			srd.setSymbol(ttm);
		} catch (Exception e) {
			System.out.println("No ESRI font found.Setting Default font");
			SimpleMarkerSymbol sms = new SimpleMarkerSymbol();
			sms.setType(SimpleMarkerSymbol.CIRCLE_MARKER);
			sms.setSymbolColor(new Color(255, 0, 0));
			sms.setWidth(5);
			srd.setSymbol(sms);
		}
		setRenderer(srd);
		// without setting layer capabilities, the points will not
		// display (but the toc entry will still appear)
		XYLayerCapabilities lc = new XYLayerCapabilities();
		setCapabilities(lc);

	}

	private void createFeaturesAndFields(BasePointsArray bpa, Map map,
                                         Vector<String> s2) {
	    System.out.println("In createFeaturesAndFields");
		featureVector = new java.util.Vector<BaseFeature>();
		fields = new BaseFields();
		createDbfFields();
		int j = 0;
		System.out.println("BPA Size" + bpa.size());
		for (int i = 0; i < bpa.size(); i++) {
			System.out.println("Inside loop::" + i + ":: and j is ::" + j);
			BaseFeature feature = new BaseFeature(); // feature is a row
			feature.setFields(fields);
			com.esri.mo2.cs.geom.Point p = new com.esri.mo2.cs.geom.Point(
                                                                          bpa.getPoint(i));


			 System.out.println("S2 values 1:" + s2.elementAt(j));
			 System.out.println("S2 values 2:" + s2.elementAt(j + 1));
			 System.out.println("S2 values 3:" + s2.elementAt(j + 2));
			 System.out.println("S2 values 4:" + s2.elementAt(j + 3));
			 System.out.println("S2 values 5:" + s2.elementAt(j + 4));
			 System.out.println("S2 values 6:" + s2.elementAt(j + 5));
			 System.out.println("S2 values 7:" + s2.elementAt(j + 6));
			 System.out.println("S2 values 8:" + s2.elementAt(j + 7));



			feature.setValue(0, p); // p = gets sequence numbers like 0,1,2,3....
			// feature.setValue(1, new Integer(0)); // point data
			feature.setValue(1, s2.elementAt(j)); // 1 = Golf Course ID ; 1 = DBF number
			feature.setValue(2, s2.elementAt(j + 1));// 2 = Golf Course name
			feature.setValue(3, s2.elementAt(j + 2));
			feature.setValue(4, s2.elementAt(j + 3));
			feature.setValue(5, s2.elementAt(j + 4));
			feature.setValue(6, s2.elementAt(j + 5));
			feature.setValue(7, s2.elementAt(j + 6));
			feature.setValue(8, s2.elementAt(j + 7));

			feature.setDataID(new BaseDataID("MyPoints", i));
			featureVector.addElement(feature);
			j = j + 8; // when u add or del any column in csv change here .
		}
	}

	private void createDbfFields() {
		fields.addField(new BaseField("#", Field.ESRI_SHAPE, 0, 0));
		fields.addField(new BaseField("GolfcourseID", java.sql.Types.VARCHAR, 10,0));
		fields.addField(new BaseField("Name", java.sql.Types.VARCHAR, 50, 0));
		fields.addField(new BaseField("Description", java.sql.Types.VARCHAR, 500, 0));
		fields.addField(new BaseField("Phone", java.sql.Types.VARCHAR, 16, 0));
		fields.addField(new BaseField("Website", java.sql.Types.VARCHAR, 100, 0));
		fields.addField(new BaseField("Address", java.sql.Types.VARCHAR, 100, 0));
		fields.addField(new BaseField("Image", java.sql.Types.VARCHAR, 100, 0));
		fields.addField(new BaseField("VideoLink", java.sql.Types.VARCHAR, 100,0));

	}

	public BaseFeatureClass getFeatureClass(String name, BasePointsArray bpa) {
		com.esri.mo2.map.mem.MemoryFeatureClass featClass = null;
		try {
			featClass = new com.esri.mo2.map.mem.MemoryFeatureClass(
                                                                    MapDataset.POINT, fields);
		} catch (IllegalArgumentException iae) {
		}
		featClass.setName(name);
		for (int i = 0; i < bpa.size(); i++) {
			featClass.addFeature(featureVector.elementAt(i));
		}
		return featClass;
	}

	private final class XYLayerCapabilities extends
    com.esri.mo2.map.dpy.LayerCapabilities {
		XYLayerCapabilities() {
			for (int i = 0; i < this.size(); i++) {
				setAvailable(this.getCapabilityName(i), true);
				setEnablingAllowed(this.getCapabilityName(i), true);
				getCapability(i).setEnabled(true);
			}
		}
	}
}

class AttrTab extends JDialog {
	JPanel panel1 = new JPanel();
	com.esri.mo2.map.dpy.Layer layer = SDGolfCourses.layer4;
	JTable jtable = new JTable(new MyTableModel());
	JScrollPane scroll = new JScrollPane(jtable);

	public AttrTab() throws IOException {
		setBounds(70, 70, 450, 350);
		setTitle("Attribute Table");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// next line necessary for horiz scrollbar to work
		jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableColumn tc = null;
		int numCols = jtable.getColumnCount();
		// jtable.setPreferredScrollableViewportSize(
		// new java.awt.Dimension(440,340));
		for (int j = 0; j < numCols; j++) {
			tc = jtable.getColumnModel().getColumn(j);
			tc.setMinWidth(50);
		}
		getContentPane().add(scroll, BorderLayout.CENTER);
	}
}

class MyTableModel extends AbstractTableModel {
	// the required methods to implement are getRowCount,
	// getColumnCount, getValueAt
	com.esri.mo2.map.dpy.Layer layer = SDGolfCourses.layer4;

	MyTableModel() {
		qfilter.setSubFields(fields);
		com.esri.mo2.data.feat.Cursor cursor = flayer.search(qfilter);
		while (cursor.hasMore()) {
			ArrayList<String> inner = new ArrayList<String>();
			Feature f = (com.esri.mo2.data.feat.Feature) cursor.next();
			inner.add(0, String.valueOf(row));
			for (int j = 1; j < fields.getNumFields(); j++) {
				// System.out.println("Attr Table::" + j);
				// System.out.println("::" + f.getValue(j).toString());
				inner.add(f.getValue(j).toString());
			}
			data.add(inner);
			row++;
		}
	}

	FeatureLayer flayer = (FeatureLayer) layer;
	FeatureClass fclass = flayer.getFeatureClass();
	String columnNames[] = fclass.getFields().getNames();
	ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	int row = 0;
	int col = 0;
	BaseQueryFilter qfilter = new BaseQueryFilter();
	Fields fields = fclass.getFields();

	public int getColumnCount() {
		return fclass.getFields().getNumFields();
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int colIndx) {
		return columnNames[colIndx];
	}

	public Object getValueAt(int row, int col) {
		ArrayList temp = new ArrayList();
		temp = data.get(row);
		return temp.get(col);
	}
}

class CreateShapeDialog extends JDialog {
	String name = "";
	String path = "";
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	JTextField nameField = new JTextField("enter layer name here, then hit ENTER", 25);
	JTextField pathField = new JTextField("enter full path name here, then hit ENTER",35);
	com.esri.mo2.map.dpy.FeatureLayer selectedlayer;
	ActionListener lis = new ActionListener() {
    public void actionPerformed(ActionEvent ae) {
    Object o = ae.getSource();
    if (o == nameField) {
        name = nameField.getText().trim();
        //path = ((ShapefileFolder) (SDGolfCourses.layer4.getLayerSource())).getPath();
        path = "/Documents/workspace";
        System.out.println(path + "    " + name);
    } else if (o == cancel)
        setVisible(false);
        else {
            try {
                ShapefileWriter.writeFeatureLayer(selectedlayer, path, name, 2);
            } catch (Exception e) {
                System.out.println("write error");
            }
            setVisible(false);
        }
}
};

JPanel panel1 = new JPanel();
JLabel centerlabel = new JLabel();

// centerlabel;
CreateShapeDialog(com.esri.mo2.map.dpy.FeatureLayer layer5) {
selectedlayer = layer5;
setBounds(40, 350, 450, 150);
setTitle("Create new shapefile?");
addWindowListener(new WindowAdapter() {
public void windowClosing(WindowEvent e) {
setVisible(false);
}
});
nameField.addActionListener(lis);
ok.addActionListener(lis);
cancel.addActionListener(lis);
String s = "<HTML> To make a new shapefile from the new layer, enter<BR>"
+ "the new name you want for the layer and click OK.<BR>"
+ "You can then add it to the map in the usual way.<BR>"
+ "Click ENTER after replacing the text with your layer name";
centerlabel.setHorizontalAlignment(JLabel.CENTER);
centerlabel.setText(s);
getContentPane().add(centerlabel, BorderLayout.CENTER);
panel1.add(nameField);
panel1.add(ok);
panel1.add(cancel);
getContentPane().add(panel1, BorderLayout.SOUTH);
}
}

class CreateXYShapeDialog extends JDialog {
	String name = "";
	String path = "";
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	JTextField nameField = new JTextField(
                                          "enter layer name here, then hit ENTER", 35);
	JTextField pathField = new JTextField(
                                          "enter full path name here, then hit ENTER", 35);
	com.esri.mo2.map.dpy.FeatureLayer XYlayer;
	ActionListener lis = new ActionListener() {
    public void actionPerformed(ActionEvent ae) {
    Object o = ae.getSource();
    if (o == pathField) {
        path = pathField.getText().trim();
        System.out.println(path);
    } else if (o == nameField) {
        name = nameField.getText().trim();
        // path =
        // ((ShapefileFolder)(Earthquake.layer4.getLayerSource())).getPath();
        System.out.println(path + "    " + name);
    } else if (o == cancel)
        setVisible(false);
        else { // ok button clicked
            try {
                //ShapefileWriter.writeFeatureLayer(XYlayer, path, name, 0);
                System.out.println("im here");
				System.out.println("Printing...: "+ "XYlayer: " + XYlayer.getName() + "Path: "+ path + "name: " +name);
				 ShapefileWriter.writeFeatureLayer(XYlayer,path,name);

				System.out.println("2 here");
            } catch (Exception e) {
                System.out.println("write error");
            }
            setVisible(false);
        }
}
};

JPanel panel1 = new JPanel();
JPanel panel2 = new JPanel();
JLabel centerlabel = new JLabel();

// centerlabel;
CreateXYShapeDialog(com.esri.mo2.map.dpy.FeatureLayer layer5) {
XYlayer = layer5;
setBounds(40, 250, 600, 300);
setTitle("Create new shapefile?");
addWindowListener(new WindowAdapter() {
public void windowClosing(WindowEvent e) {
setVisible(false);
}
});
nameField.addActionListener(lis);
pathField.addActionListener(lis);
ok.addActionListener(lis);
cancel.addActionListener(lis);
String s = "<HTML> To make a new shapefile from the new layer, enter<BR>"
+ "the new name you want for the layer and hit ENTER.<BR>"
+ "then enter a path to the folder you want to use <BR>"
+ "and hit ENTER once again <BR>"
+ "As an example type C:\\mylayers<BR>"
+ "You can then add it to the map in the usual way.<BR>"
+ "Click ENTER after replacing the text with your layer name";
centerlabel.setHorizontalAlignment(JLabel.CENTER);
centerlabel.setText(s);
// getContentPane().add(centerlabel,BorderLayout.CENTER);
panel1.add(centerlabel);
panel1.add(nameField);
panel1.add(pathField);
panel2.add(ok);
panel2.add(cancel);
getContentPane().add(panel2, BorderLayout.SOUTH);
getContentPane().add(panel1, BorderLayout.CENTER);
}
}

class HelpDialog extends JDialog {
	JTextArea helptextarea;

	public HelpDialog(String inputText) throws IOException {
		setBounds(70, 70, 460, 250);
		setTitle("Help");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		helptextarea = new JTextArea(inputText, 7, 40);
		JScrollPane scrollpane = new JScrollPane(helptextarea);
		helptextarea.setEditable(false);
		getContentPane().add(scrollpane, "Center");
	}
}

class HelpTool extends Tool {
}

class Arrow extends Tool {
	public void arrowChores() { // undo measure tool residue
		SDGolfCourses.milesLabel.setText("DIST   0 mi   ");
		SDGolfCourses.kmLabel.setText("   0 km    ");
		if (SDGolfCourses.acetLayer != null)
			SDGolfCourses.map.remove(SDGolfCourses.acetLayer);
		SDGolfCourses.acetLayer = null;
		SDGolfCourses.map.repaint();
		SDGolfCourses.helpToolOn = false;
	}
}

class Flash extends Thread {
	Legend legend;

	Flash(Legend legendin) {
		legend = legendin;
	}

	public void run() {
		for (int i = 0; i < 12; i++) {
			try {
				Thread.sleep(500);
				legend.toggleSelected();
			} catch (Exception e) {
			}
		}
	}
}

class DistanceTool extends DragTool {
	int startx, starty, endx, endy, currx, curry;
	com.esri.mo2.cs.geom.Point initPoint, endPoint, currPoint;
	double distance;

	public static void resetDist() { // undo measure tool residue
		SDGolfCourses.milesLabel.setText("DIST   0 mi   ");
		SDGolfCourses.kmLabel.setText("   0 km    ");
		if (SDGolfCourses.acetLayer != null) {
			SDGolfCourses.map.remove(SDGolfCourses.acetLayer);
			SDGolfCourses.acetLayer = null;
		}
		SDGolfCourses.map.repaint();
	}

	public void mousePressed(MouseEvent me) {
		startx = me.getX();
		starty = me.getY();
		initPoint = SDGolfCourses.map.transformPixelToWorld(me.getX(), me.getY());
	}

	public void mouseReleased(MouseEvent me) {
		// now we create an acetatelayer instance and draw a line on it
		endx = me.getX();
		endy = me.getY();
		endPoint = SDGolfCourses.map.transformPixelToWorld(me.getX(), me.getY());
		distance = (69.44 / (2 * Math.PI))
        * 360
        * Math.acos(Math.sin(initPoint.y * 2 * Math.PI / 360)
                    * Math.sin(endPoint.y * 2 * Math.PI / 360)
                    + Math.cos(initPoint.y * 2 * Math.PI / 360)
                    * Math.cos(endPoint.y * 2 * Math.PI / 360)
                    * (Math.abs(initPoint.x - endPoint.x) < 180 ? Math
                       .cos((initPoint.x - endPoint.x) * 2 * Math.PI
                            / 360) : Math.cos((360 - Math
                                               .abs(initPoint.x - endPoint.x))
                                              * 2
                                              * Math.PI
                                              / 360)));
		System.out.println(distance);
		SDGolfCourses.milesLabel.setText("DIST: "
                                      + new Float((float) distance).toString() + " mi  ");
		SDGolfCourses.kmLabel.setText(new Float((float) (distance * 1.6093))
                                   .toString() + " km");
		if (SDGolfCourses.acetLayer != null)
			SDGolfCourses.map.remove(SDGolfCourses.acetLayer);
		SDGolfCourses.acetLayer = new AcetateLayer() {
			public void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
				Line2D.Double line = new Line2D.Double(startx, starty, endx,
                                                       endy);
				g2d.setColor(new Color(0, 0, 250));
				g2d.draw(line);
			}
		};
		Graphics g = super.getGraphics();
		SDGolfCourses.map.add(SDGolfCourses.acetLayer);
		SDGolfCourses.map.redraw();
	}

	public void cancel() {
	};
}

class HotPick extends JDialog {

	JPanel btnPanel = new JPanel();
	JPanel northPanel = new JPanel();
	JPanel descPanel = new JPanel();
	JPanel infoPanel = new JPanel();
	JPanel imagePanel = new JPanel();
	static String strOsType = null;

	JLabel image = new JLabel();

	ActionListener btnlis;

	HotPick(Feature f) throws IOException {

		if (strOsType == null) {
			strOsType = System.getProperty("os.name");
		}

		if (f.getDataID().getSource().trim().equalsIgnoreCase("GolfCourses")) {

			System.out.println("F1 ::" + f.getValue(2));
			System.out.println("F2 Golf Course is::" + f.getValue(3));
			System.out.println("F3 is::" + f.getValue(4));
			System.out.println("F4 is::" + f.getValue(5));
			System.out.println("F5 is::" + f.getValue(6));
			System.out.println("F6 is::" + f.getValue(7));
			System.out.println("F7 is::" + f.getValue(8));
			System.out.println("F8 is::" + f.getValue(9));

			final String imageName = f.getValue(8).toString();
			System.out.println("ImageName" + imageName);
			final ImageIcon theaterImg = new ImageIcon(imageName);
			image.setIcon(theaterImg);

            this.setTitle((String) f.getValue(3));

			JTextArea descriptionArea = new JTextArea(10,25);
			descriptionArea.setPreferredSize(new Dimension(100, 100));
			descriptionArea.setText((String) f.getValue(3) );
        	descriptionArea.setLineWrap(true);
        	descriptionArea.setWrapStyleWord(true);
        	descriptionArea.setOpaque(false);
        	descriptionArea.setEditable(false);
        	descriptionArea.setLineWrap(true);



        	JTextArea addressArea = new JTextArea(1,25);
        	addressArea.setText("   Address : " + (String) f.getValue(7));
        	addressArea.setLineWrap(true);
        	addressArea.setWrapStyleWord(true);
        	addressArea.setLineWrap(true);
        	addressArea.setOpaque(false);
        	addressArea.setEditable(false);


			JTextArea phoneArea = new JTextArea(1,25);
			phoneArea.setText("   Phone Number : " + (String) f.getValue(5));
        	phoneArea.setLineWrap(true);
        	phoneArea.setWrapStyleWord(true);
        	phoneArea.setOpaque(false);
        	phoneArea.setEditable(false);


			final JButton phoneBtn = new JButton("Phone Number : \n"
                                                 + (String) f.getValue(5), new ImageIcon("data/phone.gif"));
			// phoneBtn.setBackground(Color.WHITE);
			phoneBtn.setOpaque(true);
			phoneBtn.setBorderPainted(false);

			final JButton websiteBtn = new JButton("WebSite : ",
                                                   new ImageIcon("data/website.gif"));

			final JButton videoBtn = new JButton("Watch my video",
                                                 new ImageIcon("data/video.gif"));
			final String webUrl = (String) f.getValue(6);
			final String videoLink = (String) f.getValue(9);
			// final String command =
			// "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE "
			// + (String) f.getValue(8);


			btnlis = new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					Object source = ae.getSource();
					try {
						if (source == websiteBtn) {
							try {
								// String command = googlePath[i];
								if (strOsType.indexOf("Windows") != -1) {
									Runtime.getRuntime().exec(
                                                              "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE "
                                                              + webUrl);
								} else if (strOsType.indexOf("Mac") != -1) {
									Runtime.getRuntime().exec(
                                                              new String[] { "open", "-a",
                                                                  "Safari", webUrl });

								}
							} catch (Exception ex) {
								System.out.println("cannot execute command. "
                                                   + ex);
								ex.printStackTrace();
							}

						} else if (source == videoBtn) {
							if (!videoLink.trim().equalsIgnoreCase("None")) {
								try {
									// String command = googlePath[i];
									if (strOsType.indexOf("Windows") != -1) {
										Runtime.getRuntime().exec(
                                                                  "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE "
                                                                  + videoLink);
									} else if (strOsType.indexOf("Mac") != -1) {
										Runtime.getRuntime().exec(
                                                                  new String[] { "open", "-a",
                                                                      "Safari", videoLink });

									}
								} catch (Exception ex) {
									System.out
                                    .println("cannot execute command. "
                                             + ex);
									ex.printStackTrace();
								}

							}

						}
					} catch (Exception e) {
						System.out.println("Error:::");
						e.printStackTrace();
					}
				}
			};

			websiteBtn.addActionListener(btnlis);
			videoBtn.addActionListener(btnlis);

			if (videoLink.trim().equalsIgnoreCase("None")) {
				videoBtn.setEnabled(false);
			}

			imagePanel.add(image);
			descPanel.setLayout(new GridLayout(1, 1));
			descPanel.add(descriptionArea);
			northPanel.add(imagePanel);
			northPanel.add(descPanel);


			infoPanel.setLayout(new GridLayout(2, 1));// last Panel as information.
			infoPanel.add(addressArea);
			infoPanel.add(phoneArea);

			btnPanel.setLayout(new GridLayout(1, 2));// last Panel as information.
            btnPanel.add(websiteBtn);
			btnPanel.add(videoBtn);

			this.getContentPane().add(northPanel, BorderLayout.NORTH);
			this.getContentPane().add(infoPanel, BorderLayout.CENTER);
			this.getContentPane().add(btnPanel, BorderLayout.SOUTH);
			this.setBounds(200,200,640, 350);
			this.setResizable(false);
			setVisible(true);

		}
	}
}