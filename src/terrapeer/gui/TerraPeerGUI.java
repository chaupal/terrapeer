package terrapeer.gui;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;

import terrapeer.*;
import terrapeer.gui.helpers.*;
import terrapeer.net.*;
import terrapeer.vui.*;
import terrapeer.vui.space.*;
import javax.swing.event.*;
import java.beans.*;
import terrapeer.vui.helpers.*;

/**
 * Main GUI
 *   { MySpace }
 *
 * ______
 * PUBLIC
 *
 * startA() = start an environment/procedure
 *  - can showX
 *  - can flipX
 * showA() = show a new window for A
 * flipA() = flip south panel to A
 *  SpaceNav
 *  SpaceFilter
 *  ZoneNav
 *  ZoneBuilder
 *  Project
 *
 * updateStatus()
 *
 * _______
 * PRIVATE
 *
 * GUI
 *   jbInit()
 *   loadImages()
 *   loadMenu()
 *   showHide...
 *   enlighten..
 *   resetGUI()
 *   updateSpaceMap()
 * Network
 *   connectJXTA()
 *   disconnectJXTA()
 *
 * _______
 * Actions
 *
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class TerraPeerGUI extends JFrame
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private static TerraSideThread sideThread = null;

  static boolean isToolbarTextVisible = false;
  static boolean southPanelUp = false;
  static boolean westPanelUp = true;
  static boolean eastPanelUp = true;
  static boolean navPanelUp = true;

  static final boolean[] isMenuVisible = new boolean[vars.MENU_COUNT];
  static final boolean[] isMenuMouseOver = new boolean[vars.MENU_COUNT];
  static final boolean[] isBlockMouseOver = new boolean[vars.BLOCK_COUNT];
  static final UISceneTree SceneTree = new UISceneTree();

  static public PeerCore TERRA_NetPeer = null;

  static boolean peerIsConfigured = false;
  static boolean connectionOK = false;
  static boolean currentViewPointPersonal = true;
  static boolean currentViewPointGrid = true;
  static boolean zonegridAutomoveOn = false;
  static boolean isCyberspaceRunning = false;

  //static final Integer ControlDirDegree = new Integer(0);

  terrapeer.vui.space.MySpace mySpace = new terrapeer.vui.space.MySpace();

  JPanel jPanelSpaceNavAvatView = new JPanel();
  JPanel contentPane;

  //Images
  ImageIcon[] images = new ImageIcon[vars.IMG_ARRAY];

  //MENU
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuPeer = new JMenu();
  JMenu jMenuView = new JMenu();
  JMenu jMenuFeedback = new JMenu();
  JMenu jMenuPersonal = new JMenu();
  JMenu jMenuCyberspace = new JMenu();
  JMenu jMenuServices = new JMenu();
  JMenu jMenuProject = new JMenu();
  JMenu jMenuHelp = new JMenu();

  JMenu jMenuItemPeerPrefs = new JMenu();
  JMenu jMenuCyberFilters = new JMenu();
  JMenu jMenuServSearch = new JMenu();
  JMenu jMenuCommunication = new JMenu();
  JMenu jMenuViewSouthPanel = new JMenu();

  JMenuItem jMenuItemViewWorldSepWin = new JMenuItem();
  JMenuItem jMenuItemViewZoneGridSepWin = new JMenuItem();
  JMenuItem jMenuItemExit = new JMenuItem();
  JMenuItem jMenuItemHelpAbout = new JMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemViewExpandAll = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemShowSouthPanel = new JCheckBoxMenuItem();
  JMenuItem jMenuItemCyberStart = new JMenuItem();
  JMenuItem jMenuItemCyberGoHome = new JMenuItem();
  JMenuItem jMenuItemCyberZBuilder = new JMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemCyberFilterBg = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemCyberFilterShowZDetail = new JCheckBoxMenuItem();
  JMenuItem jMenuItemCyberFilterSettings = new JMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemCyberFilterFog = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemCyberFilterShowLandmarks = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemCyberFilterShowMeta = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemCyberFilterShowGrid = new JCheckBoxMenuItem();
  JMenuItem jMenuItemPeerLogin = new JMenuItem();
  JMenuItem jMenuItemComMsg = new JMenuItem();
  JMenuItem jMenuItemComChat = new JMenuItem();
  JMenuItem jMenuItemFbSearch = new JMenuItem();
  JMenuItem jMenuItemFbPC = new JMenuItem();
  JMenuItem jMenuItemFbBC = new JMenuItem();
  JMenuItem jMenuItemFbMy = new JMenuItem();
  JMenuItem jMenuItemPersIM = new JMenuItem();
  JMenuItem jMenuItemPrefGeneral = new JMenuItem();
  JMenuItem jMenuItemPrefNet = new JMenuItem();
  JMenuItem jMenuItemPrefSec = new JMenuItem();
  JMenuItem jMenuItemHelpTopics = new JMenuItem();
  JMenuItem jMenuItemPeerDiscovery = new JMenuItem();
  JMenuItem jMenuItemViewOpenLog = new JMenuItem();
  JMenuItem jMenuItemPersContacts = new JMenuItem();
  JMenuItem jMenuItemProjManager = new JMenuItem();
  JMenuItem jMenuItemObjRepository = new JMenuItem();
  JMenuItem jMenuItemProjMembership = new JMenuItem();
  JMenuItem jMenuItemHelpGoTerraCentral = new JMenuItem();
  JMenuItem jMenuItemViewSPProject = new JMenuItem();
  JMenuItem jMenuItemViewSPSpaceFilter = new JMenuItem();
  JMenuItem jMenuItemViewSPSpaceNav = new JMenuItem();
  JMenuItem jMenuItemViewSPZoneBuilder = new JMenuItem();
  JMenuItem jMenuItemViewSPZoneNav = new JMenuItem();
  JMenuItem jMenuItemServFileshare = new JMenuItem();
  JMenuItem jMenuItemServSearchWeb = new JMenuItem();
  JMenuItem jMenuItemServSearchPeerName = new JMenuItem();
  JMenu jMenuViewSidePanels = new JMenu();
  JCheckBoxMenuItem jCheckBoxMenuItemShowWestPanel = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemShowEastPanel = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemShowNavPanel = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemShowToolbar = new JCheckBoxMenuItem();

  //GUI
  JToolBar jToolBar = new JToolBar();
  JButton jButtonTBMyHome = new JButton();
  JButton jButtonTBProject = new JButton();
  JButton jButtonTBHelp = new JButton();
  JButton jButtonTBPreferences = new JButton();
  JButton jButtonTBMyFeedback = new JButton();
  JButton jButtonTBMyInformation = new JButton();
  JButton jButtonTBSecurity = new JButton();
  JButton jButtonTBLogin = new JButton();
  JButton jButtonTBMembers = new JButton();
  JButton jButtonTBLog = new JButton();
  JButton jButtonTBVirtualWorld = new JButton();
  JButton jButtonTBMyContacts = new JButton();
  CardLayout cardLayoutSouth = new CardLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanelBase = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanelWest = new JPanel();
  JPanel jPanelSouth = new JPanel();
  JPanel jPanelWestPIM = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
  JPanel jPanelWestProject = new JPanel();
  JButton jButtonMyInformation = new JButton();
  JButton jButtonMyContacts = new JButton();
  JButton jButtonNavHome = new JButton();
  JPanel jPanelCardPersonal = new JPanel();
  JPanel jPanelCardProjects = new JPanel();
  VerticalFlowLayout verticalFlowLayout4 = new VerticalFlowLayout();
  JButton jButtonLoginStart = new JButton();
  JPanel jPanelWestAccess = new JPanel();
  JButton jButtonSecuritySettings = new JButton();
  JPanel jPanelOnlinePeers = new JPanel();
  JPanel jPanelOnlinePeerGroups = new JPanel();
  JList jListOnlinePeers = new JList();
  JList jListOnlinePeerGroups = new JList();
  JButton jButtonMyFeedback = new JButton();
  JPanel jPanelCardSpaceNav = new JPanel();
  JScrollPane jScrollPaneService = new JScrollPane();
  Border border1;
  JEditorPane jEditorPaneService = new JEditorPane();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel jPanelProjectsTop = new JPanel();
  JLabel jLabelEmpty1 = new JLabel();
  JLabel jLabelTitle = new JLabel();
  Border border2;
  JLabel jLabelCTopProject = new JLabel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel jPanelStatusNav = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel statusBarApp = new JLabel();
  JPanel jPanelProjectsCore = new JPanel();
  JPanel jPanelPersonalTop = new JPanel();
  JPanel jPanelMyInfoCore = new JPanel();
  JLabel jLabelCTopPIM = new JLabel();
  JPanel jPanelSpaceNavTop = new JPanel();
  JPanel jPanelSpaceNavCore = new JPanel();
  JLabel jLabelCTopSNav = new JLabel();
  BorderLayout borderLayout5 = new BorderLayout();
  JLabel jLabelWestTerraPeer = new JLabel();
  JLabel jLabelWestPersonal = new JLabel();
  JLabel jLabelWestProjects = new JLabel();
  JLabel jLabelWestServices = new JLabel();
  JLabel jLabelWestCyberspace = new JLabel();
  JButton jButtonVirtualWorld = new JButton();
  JPanel jPanelWestCyberspace = new JPanel();
  JButton jButtonFilterPanel = new JButton();
  VerticalFlowLayout verticalFlowLayout6 = new VerticalFlowLayout();
  JButton jButtonObjRepository = new JButton();
  JToggleButton jButtonNavSPProjPanel = new JToggleButton();
  JButton jButtonMembership = new JButton();
  JLabel jLabel10 = new JLabel();
  JButton jButtonLog = new JButton();
  JProgressBar jProgressBarMain = new JProgressBar();
  JLabel statusBarNet = new JLabel();
  JLabel statusBarWorld = new JLabel();
  JButton jButtonConfigPeer = new JButton();
  JButton jButtonDiscovery = new JButton();
  JPanel jPanelEast = new JPanel();
  JPanel jPanelCenter = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  VerticalFlowLayout verticalFlowLayout7 = new VerticalFlowLayout();
  JLabel jLabelEastZone = new JLabel();
  JLabel jLabelCTopZB = new JLabel();
  BorderLayout borderLayout8 = new BorderLayout();
  JPanel jPanelZoneBuilderCore = new JPanel();
  BorderLayout borderLayout9 = new BorderLayout();
  JPanel jPanelZoneBuilderTop = new JPanel();
  JPanel jPanelCardZoneBuilder = new JPanel();
  JButton jButtonZoneBuilder = new JButton();
  JLabel jLabelCTopSFilter = new JLabel();
  JPanel jPanelCardSpaceFilter = new JPanel();
  BorderLayout borderLayout10 = new BorderLayout();
  JPanel jPanelSpaceFilterCore = new JPanel();
  JPanel jPanelSpaceFilterTop = new JPanel();
  JPanel jPanelCardZoneNav = new JPanel();
  JPanel jPanelZoneNavTop = new JPanel();
  BorderLayout borderLayout12 = new BorderLayout();
  JPanel jPanelZoneNavCore = new JPanel();
  JLabel jLabelCTopZNav = new JLabel();
  JLabel jLabelEastNet = new JLabel();
  JLabel jLabelEast3 = new JLabel();
  JLabel jLabelPeerName = new JLabel();
  JLabel jLabelEast4 = new JLabel();
  JLabel jLabelPeerGroup = new JLabel();
  JLabel jLabelEast5 = new JLabel();
  JPanel jPanelDiscovery = new JPanel();
  JLabel jLabelDiscoveryResponse = new JLabel();
  JLabel jLabelDiscoveryLooking = new JLabel();
  JPanel jPanelEastZone = new JPanel();
  VerticalFlowLayout verticalFlowLayout8 = new VerticalFlowLayout();
  JPanel jPanelEastNet = new JPanel();
  VerticalFlowLayout verticalFlowLayout9 = new VerticalFlowLayout();
  JLabel jLabelEastSpace = new JLabel();
  JLabel jLabel3 = new JLabel();
  JPanel jPanelEastSpace = new JPanel();
  JLabel jLabelEast6 = new JLabel();
  JLabel jLabelEast6b = new JLabel();
  JScrollPane jScrollPaneOP = new JScrollPane();
  JScrollPane jScrollPaneOP2 = new JScrollPane();
  JPanel jPanelWestServices = new JPanel();
  JProgressBar jProgressBarDiscovery = new JProgressBar();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  VerticalFlowLayout verticalFlowLayout5 = new VerticalFlowLayout();
  JPanel jPanelBuildBlocks = new JPanel();
  JLabel jLabelBBServ = new JLabel();
  JLabel jLabelBBS_url = new JLabel();
  JLabel jLabelBBB_pyr = new JLabel();
  JLabel jLabelBBB_box = new JLabel();
  JLabel jLabelBBB_cyl = new JLabel();
  JLabel jLabelBBCon = new JLabel();
  JLabel jLabelBBBas = new JLabel();
  JPanel jPanelBBC = new JPanel();
  JScrollPane jScrollPaneBBC = new JScrollPane();
  JPanel jPanelWestCyberspaceNav = new JPanel();
  GridLayout gridLayout2 = new GridLayout();
  JButton jButtonNavZone = new JButton();
  JButton jButtonNavSpace = new JButton();
  JLabel jLabel4 = new JLabel();
  JButton jButtonProjManager = new JButton();
  JButton jButtonServSearch = new JButton();
  JButton jButtonServFb = new JButton();
  JButton jButtonServWeb = new JButton();
  FlowLayout flowLayout2 = new FlowLayout();
  JLabel jLabel5 = new JLabel();
  JToggleButton jToggleButtonSpaceNavVPP = new JToggleButton();
  JToggleButton jToggleButtonSpaceNavVPO = new JToggleButton();
  ButtonGroup buttonGroupVP = new ButtonGroup();
  JPanel jPanelSpaceNavOrbitView = new JPanel();
  JPanel jPanelBBBas = new JPanel();
  JPanel jPanelBBServ = new JPanel();
  JPanel jPanelBBCon = new JPanel();
  JLabel jLabelBBB_sphere = new JLabel();
  JLabel jLabelBBS_chat = new JLabel();
  JLabel jLabelBBC_fb = new JLabel();
  JLabel jLabelBBS_ftp = new JLabel();
  JLabel jLabelBBS_web = new JLabel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  BorderLayout borderLayout16 = new BorderLayout();
  PanelZoneInfo jPanelZoneBuildSet = new PanelZoneInfo();
  BorderLayout borderLayout6 = new BorderLayout();
  JPanel jPanelSpaceNavGridView = new JPanel();
  JPanel jPanelBottomNav = new JPanel();
  JToggleButton jButtonNavSPSpace = new JToggleButton();
  JToggleButton jButtonNavSPZone = new JToggleButton();
  JToggleButton jButtonNavSPFilterPanel = new JToggleButton();
  JToggleButton jButtonNavSPZoneBuilder = new JToggleButton();
  JSlider jSliderGridViewZoom = new JSlider();
  JPanel jPanelWestCyberspaceVP = new JPanel();
  Border border3;
  JButton jButtonSoutPanelUpDown = new JButton();
  ButtonGroup buttonGroupNav = new ButtonGroup();
  JPanel jPanelEastSpaceMap = new JPanel();
  JLabel jLabelEastSpaceCoords = new JLabel();
  VerticalFlowLayout verticalFlowLayout11 = new VerticalFlowLayout();
  JLabel jLabelEastSpaceLevel = new JLabel();
  JLabel jLabelEastSpaceDom = new JLabel();
  JLabel jLabelEastSpaceSpeed = new JLabel();
  JLabel jLabelEastSpaceHeadingYaw = new JLabel();
  JLabel jLabelEastSpaceHeadingPitch = new JLabel();
  JLabel jLabelEastSpaceHeadingRoll = new JLabel();
  JPanel jPanelSpaceNavOrbit = new JPanel();
  JLabel jLabel15 = new JLabel();
  JPanel jPanelSpaceNavGrid = new JPanel();
  JLabel jLabel111 = new JLabel();
  BorderLayout borderLayout18 = new BorderLayout();
  BorderLayout borderLayout19 = new BorderLayout();
  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  JButton jButtonGridViewOrigo = new JButton();
  JButton jButtonGridViewAvatar = new JButton();
  JPanel jPanel5 = new JPanel();
  JLabel jLabel20 = new JLabel();
  BorderLayout borderLayout20 = new BorderLayout();
  JPanel jPanel6 = new JPanel();
  BorderLayout borderLayout21 = new BorderLayout();
  JLabel jLabel21 = new JLabel();
  Compass jPanelSpaceNavCompass = new Compass();
  Altitude jPanelSpaceNavAltitude = new Altitude();
  JPanel jPanelFilterSpace = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  VerticalFlowLayout verticalFlowLayout12 = new VerticalFlowLayout();
  JLabel jLabel22 = new JLabel();
  ButtonGroup buttonGroupSpaceFilter = new ButtonGroup();
  JCheckBox jCheckBoxFilterAmbience = new JCheckBox();
  JCheckBox jCheckBoxFilterAvatars = new JCheckBox();
  JCheckBox jCheckBoxFilterLandmarks = new JCheckBox();
  JCheckBox jCheckBoxFilterAVRout = new JCheckBox();
  JCheckBox jCheckBoxFilterAVRin = new JCheckBox();
  JLabel jLabel25 = new JLabel();
  JPanel jPanelFilterZone = new JPanel();
  JCheckBox jCheckBoxFilterMyZone = new JCheckBox();
  VerticalFlowLayout verticalFlowLayout110 = new VerticalFlowLayout();
  JPanel jPanelFilterPreview = new JPanel();
  Heading jPanelSpaceNavHeading = new Heading();
  GridLayout gridLayout3 = new GridLayout();
  BorderLayout borderLayout22 = new BorderLayout();
  JLabel jLabel23 = new JLabel();
  JPanel jPanel7 = new JPanel();
  JPanel jPanelSpaceNavControl = new JPanel();
  JPanel jPanel70 = new JPanel();
  GridLayout gridLayout70 = new GridLayout();
  JButton jButtonSpaceNavCtrl_Stop = new JButton();
  VerticalFlowLayout verticalFlowLayout10 = new VerticalFlowLayout();
  NavControlDir jPanelSpaceNavCtrl_Dir = new NavControlDir();
  //NavControlSpeed jPanelSpaceNavCtrl_Speed = new NavControlSpeed();
  JPanel jPanelSpaceNavCtrl_SpeedDir = new JPanel();
  JLabel jLabelEastSpaceAltitude = new JLabel();
  Border border4;
  JLabel jLabelBBB_landmark_a = new JLabel();
  JLabel jLabelBBB_landmark_b = new JLabel();
  JLabel jLabelBBB_landmark_c = new JLabel();
  JLabel jLabelBBB_house_a = new JLabel();
  JLabel jLabelBBB_house_b = new JLabel();
  JLabel jLabelBBB_house_c = new JLabel();
  JButton jButtonSpaceNavCtrl_Home = new JButton();
  JLabel jLabelEastZoneGridMouseCoords = new JLabel();
  ZoneGrid jPanelZoneGrid = new ZoneGrid(jLabelEastZoneGridMouseCoords);
  BorderLayout borderLayout11 = new BorderLayout();
  JPanel jPanelZoneGridControl = new JPanel();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  GridBagLayout gridBagLayout42 = new GridBagLayout();
  JButton jButtonZoneGridRight = new JButton();
  JButton jButtonZoneGridUp = new JButton();
  JButton jButtonZoneGridLeft = new JButton();
  JButton jButtonZoneGridDown = new JButton();
  JTextField jTextFieldZoneGridCoord = new JTextField();
  JLabel jLabelZoneNavControl = new JLabel();
  JLabel jLabelEastZoneCoords = new JLabel();
  VerticalFlowLayout verticalFlowLayout13 = new VerticalFlowLayout();
  JLabel jLabelEastZoneSize = new JLabel();
  JLabel jLabelEastZoneName = new JLabel();
  JLabel jLabelEastZoneID = new JLabel();
  JLabel jLabel17 = new JLabel();
  Component glue1;
  JLabel jLabel18 = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JEditorPane jEditorPaneZoneDescr = new JEditorPane();
  JLabel jLabelUpdatePing = new JLabel();
  Border border5;
  Border border6;
  JButton jButtonGoCoord = new JButton();
  JCheckBoxMenuItem jCheckBoxMenuItemViewToolbarText = new JCheckBoxMenuItem();
  JSlider jSliderSpeed = new JSlider();
  JButton jButtonSpaceNavCtrl_Align = new JButton();
  JButton jButtonSpaceNavCtrl_North = new JButton();
  JButton jButtonSpaceNavCtrl_OrigoNorth = new JButton();
  JButton jButtonSpaceNavCtrl_OrigoTop = new JButton();
  JPanel jPanelFilterZoneListing = new JPanel();
  BorderLayout borderLayout13 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JScrollPane jScrollPane2 = new JScrollPane();
  JTable jTableActiveZones = new JTable();
  BorderLayout borderLayout23 = new BorderLayout();
  JLabel jLabel24 = new JLabel();
  JLabel jLabel26 = new JLabel();
  JTabbedPane jTabbedPaneActivity = new JTabbedPane();
  JPanel jPanelSpaceActivityTraffic = new JPanel();
  JPanel jPanelSpaceActivityRepository = new JPanel();
  JProgressBar jProgressBarActivityNet = new JProgressBar();
  JLabel jLabel16 = new JLabel();
  JLabel jLabel19 = new JLabel();
  JProgressBar jProgressBarActivityZoneBuffer = new JProgressBar();
  JButton jButton2 = new JButton();
  JToggleButton jToggleButton1 = new JToggleButton();
  JSlider jSliderZoneGridStep = new JSlider();
  JSlider jSliderZoneGridZoom = new JSlider();
  JPanel jPanelZoneGridControl2 = new JPanel();
  JCheckBox jCheckBoxZoneGridAutoMove = new JCheckBox();
  JLabel jLabelZoneNavControl2 = new JLabel();
  JLabel jLabel27 = new JLabel();
  JLabel jLabel28 = new JLabel();
  BorderLayout borderLayout24 = new BorderLayout();
  JPanel jPanel71 = new JPanel();
  JButton jButtonZoneGridNavCtrl_Origo = new JButton();
  JButton jButtonZoneGridNavCtrl_Home = new JButton();
  GridLayout gridLayout71 = new GridLayout();
  JButton jButtonZoneGrid = new JButton();
  BorderLayout borderLayout14 = new BorderLayout();
  BorderLayout borderLayout14b = new BorderLayout();

  /****************************************************************************
   GUI initializing methods
   ****************************************************************************/

  //Construct the frame
  public TerraPeerGUI()
  {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception
  {
    jSliderZoneGridStep.setMajorTickSpacing(10);
    jSliderZoneGridZoom.setMajorTickSpacing(1);
    border3 = BorderFactory.createLineBorder(Color.gray,2);
    border4 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,Color.white,new Color(103, 101, 98),new Color(148, 145, 140)),BorderFactory.createEmptyBorder(2,4,2,4));
    glue1 = Box.createGlue();
    border5 = BorderFactory.createMatteBorder(4,4,4,4,Color.white);
    border6 = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(SystemColor.controlHighlight,1),BorderFactory.createEmptyBorder(2,2,2,2));
    //this.setSize(new Dimension(1000, 1000));
    this.setSize(new Dimension(550, 400));
    this.setTitle("TerraPeer Manager - "+vars.JXTA_PEER_NAME+" [Port "+vars.JXTA_HTTP_PORT+"]");

    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

    loadMenu();
    if(loadImages())
      initIcons();

    vars.loadObjects();
    vars.setMessageArray();

    contentPane = (JPanel)this.getContentPane();
    border1 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,
        Color.white, Color.white, new Color(103, 101, 98),
        new Color(148, 145, 140)), BorderFactory.createEmptyBorder(4, 4, 4, 4));
    border2 = BorderFactory.createEmptyBorder(0, 20, 0, 20);
    contentPane.setLayout(borderLayout1);
    jButtonTBMyHome.setToolTipText("Goto Home Zone");
    jButtonTBMyHome.addActionListener(new TerraPeerGUI_jButtonTBMyHome_actionAdapter(this));
    jButtonTBProject.setToolTipText("Project");
    jButtonTBProject.addActionListener(new TerraPeerGUI_jButtonTBProject_actionAdapter(this));
    jButtonTBHelp.addActionListener(new TerraPeerGUI_jButtonTBHelp_actionAdapter(this));
    jButtonTBHelp.setToolTipText("Help");
    jPanelBase.setLayout(borderLayout2);
    jLabelWestPersonal.setBackground(new Color(212, 208, 255));
    jLabelWestPersonal.setOpaque(true);
    jLabelWestPersonal.setText("Personal Management");
    jLabelWestPersonal.addMouseListener(new TerraPeerGUI_jLabelWestPersonal_mouseAdapter(this));
    jPanelWest.setLayout(verticalFlowLayout1);
    jPanelWestPIM.setLayout(verticalFlowLayout2);
    jPanelWestProject.setLayout(verticalFlowLayout3);
    jLabelWestProjects.setBackground(new Color(212, 208, 255));
    jLabelWestProjects.setOpaque(true);
    jLabelWestProjects.setText("Project Management");
    jLabelWestProjects.addMouseListener(new TerraPeerGUI_jLabelWestProjects_mouseAdapter(this));
    jButtonMyInformation.setEnabled(false);
    jButtonMyInformation.setText("My Information");
    jButtonMyInformation.addActionListener(new TerraPeerManagerUI_jButtonMyInformation_actionAdapter(this));
    jButtonMyContacts.setEnabled(false);
    jButtonMyContacts.setText("My Contacts");
    jButtonMyContacts.addActionListener(new TerraPeerGUI_jButtonMyContacts_actionAdapter(this));
    jButtonNavHome.setText("Update Space");
    jButtonNavHome.addActionListener(new TerraPeerGUI_jButtonNavHome_actionAdapter(this));
    jPanelSouth.setLayout(cardLayoutSouth);
    jButtonLoginStart.setOpaque(true);
    jButtonLoginStart.setText("Login & Connect");
    jButtonLoginStart.addActionListener(new TerraPeerManagerUI_jButtonLoginStart_actionAdapter(this));
    jPanelWestAccess.setLayout(verticalFlowLayout4);
    jLabelWestTerraPeer.setBackground(new Color(212, 208, 255));
    jLabelWestTerraPeer.setOpaque(true);
    jLabelWestTerraPeer.setText("TerraPeer Access");
    jLabelWestTerraPeer.addMouseListener(new TerraPeerGUI_jLabelWestTerraPeer_mouseAdapter(this));
    jButtonSecuritySettings.setText("Security Settings");
    jButtonSecuritySettings.addActionListener(new TerraPeerGUI_jButtonSecuritySettings_actionAdapter(this));
    jPanelWest.setBorder(BorderFactory.createEtchedBorder());
    jPanelWest.setMinimumSize(new Dimension(140, 342));
    jPanelWest.setPreferredSize(new Dimension(140, 521));
    jToolBar.setBorder(null);
    jPanelBase.setBorder(null);
    jPanelSouth.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanelSouth.setMinimumSize(new Dimension(124, 140));
    jPanelSouth.setPreferredSize(new Dimension(150, 200));
    jLabelWestServices.setText("Services");
    jLabelWestServices.addMouseListener(new TerraPeerGUI_jLabelWestServices_mouseAdapter(this));
    jLabelWestServices.setOpaque(true);
    jLabelWestServices.setBackground(new Color(212, 208, 255));
    jButtonMyFeedback.setEnabled(false);
    jButtonMyFeedback.setText("My Feedback");
    jButtonMyFeedback.addActionListener(new TerraPeerGUI_jButtonMyFeedback_actionAdapter(this));
    jButtonTBPreferences.setToolTipText("Preferences");
    jButtonTBPreferences.addActionListener(new TerraPeerGUI_jButtonTBPreferences_actionAdapter(this));
    jButtonTBMyFeedback.setToolTipText("My Feedback");
    jButtonTBMyFeedback.addActionListener(new TerraPeerGUI_jButtonTBMyFeedback_actionAdapter(this));
    jButtonTBMyInformation.setToolTipText("My Information");
    jButtonTBMyInformation.addActionListener(new TerraPeerGUI_jButtonTBMyInformation_actionAdapter(this));
    jButtonTBSecurity.setToolTipText("Security");
    jButtonTBSecurity.addActionListener(new TerraPeerGUI_jButtonTBSecurity_actionAdapter(this));
    jScrollPaneService.setAutoscrolls(false);
    jScrollPaneService.setBorder(border1);
    jScrollPaneService.setMinimumSize(new Dimension(10, 10));
    jScrollPaneService.setPreferredSize(new Dimension(156, 10));
    contentPane.setMinimumSize(new Dimension(400, 400));
    jButtonTBLogin.setToolTipText("Login and Connect");
    jButtonTBLogin.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
    jButtonTBLogin.addActionListener(new TerraPeerGUI_jButtonTBLogin_actionAdapter(this));
    jEditorPaneService.setMinimumSize(new Dimension(10, 10));
    jEditorPaneService.setPreferredSize(new Dimension(128, 96));
    jEditorPaneService.setText("No Service available");
    jPanelCardProjects.setLayout(borderLayout3);
    jLabelTitle.setFont(new java.awt.Font("SansSerif", 0, 20));
    jLabelTitle.setForeground(new Color(64, 64, 128));
    jLabelTitle.setBorder(border2);
    jLabelTitle.setPreferredSize(new Dimension(200, 50));
    jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelTitle.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabelEmpty1.setPreferredSize(new Dimension(200, 0));
    jLabelEmpty1.setText("");
    jLabelCTopProject.setFont(new java.awt.Font("Dialog", 1, 9));
    jLabelCTopProject.setForeground(new Color(0, 128, 128));
    jLabelCTopProject.setText("Project Management");
    jPanelProjectsTop.setBackground(new Color(220, 220, 255));
    jPanelProjectsTop.addMouseListener(new TerraPeerGUI_jPanelProjectsTop_mouseAdapter(this));
    jPanelCardPersonal.setLayout(borderLayout4);
    jPanelStatusNav.setLayout(gridBagLayout1);
    jPanelStatusNav.setBorder(null);
    jPanelStatusNav.setMinimumSize(new Dimension(20, 20));
    jPanelStatusNav.setPreferredSize(new Dimension(0, 23));
    statusBarApp.setBorder(BorderFactory.createEtchedBorder());
    statusBarApp.setText("Application Status");
    jPanelPersonalTop.setBackground(new Color(220, 220, 255));
    jPanelPersonalTop.addMouseListener(new TerraPeerGUI_jPanelPersonalTop_mouseAdapter(this));
    jLabelCTopPIM.setFont(new java.awt.Font("Dialog", 1, 9));
    jLabelCTopPIM.setForeground(new Color(0, 128, 128));
    jLabelCTopPIM.setText("Personal Information Management");
    jPanelSpaceNavTop.setBackground(new Color(220, 220, 255));
    jPanelSpaceNavTop.addMouseListener(new TerraPeerGUI_jPanelSpaceNavTop_mouseAdapter(this));
    jLabelCTopSNav.setFont(new java.awt.Font("Dialog", 1, 9));
    jLabelCTopSNav.setForeground(new Color(0, 128, 128));
    jLabelCTopSNav.setText("Cyberspace Navigation");
    jPanelCardSpaceNav.setLayout(borderLayout5);
    jLabelWestCyberspace.setText("Cyberspace");
    jLabelWestCyberspace.addMouseListener(new TerraPeerGUI_jLabelWestCyberspace_mouseAdapter(this));
    jLabelWestCyberspace.setOpaque(true);
    jLabelWestCyberspace.setBackground(new Color(212, 208, 255));
    jButtonVirtualWorld.setText("Start");
    jButtonVirtualWorld.addActionListener(new TerraPeerGUI_jButtonVirtualWorld_actionAdapter(this));
    jPanelWestCyberspace.setLayout(verticalFlowLayout6);
    jButtonFilterPanel.setText("Filter Settings");
    jButtonFilterPanel.addActionListener(new TerraPeerGUI_jButtonFilterPanel_actionAdapter(this));
    jButtonObjRepository.setText("Repository");
    jButtonObjRepository.addActionListener(new TerraPeerGUI_jButtonObjRepository_actionAdapter(this));
    jButtonNavSPProjPanel.setBackground(new Color(128, 128, 255));
    jButtonNavSPProjPanel.setFont(new java.awt.Font("Dialog", 0, 9));
    jButtonNavSPProjPanel.setPreferredSize(new Dimension(120, 15));
    jButtonNavSPProjPanel.setText("Project Panel");
    jButtonNavSPProjPanel.addActionListener(new TerraPeerGUI_jButtonNavSPProjPanel_actionAdapter(this));
    jButtonMembership.setEnabled(false);
    jButtonMembership.setText("Membership");
    jButtonMembership.addActionListener(new TerraPeerGUI_jButtonMembership_actionAdapter(this));
    jLabel10.setText("Access Control");
    jPanelSpaceNavAvatView.setPreferredSize(new Dimension(400, 400));
    jPanelSpaceNavCore.setLayout(flowLayout2);
    jButtonTBMembers.setToolTipText("Members");
    jButtonTBMembers.addActionListener(new TerraPeerGUI_jButtonTBMembers_actionAdapter(this));
    jButtonTBLog.setToolTipText("Show Log");
    jButtonTBLog.addActionListener(new TerraPeerGUI_jButtonTBLog_actionAdapter(this));
    jButtonTBVirtualWorld.setToolTipText("Virtual World");
    jButtonTBVirtualWorld.addActionListener(new TerraPeerGUI_jButtonTBVirtualWorld_actionAdapter(this));
    jButtonTBMyContacts.setToolTipText("My Contacts");
    jButtonTBMyContacts.addActionListener(new TerraPeerGUI_jButtonTBMyContacts_actionAdapter(this));
    jButtonLog.setText("Log Viewer");
    jButtonLog.addActionListener(new TerraPeerGUI_jButtonLog_actionAdapter(this));
    statusBarNet.setText("Network Status");
    statusBarNet.setBorder(BorderFactory.createEtchedBorder());
    statusBarWorld.setText("TerraSpace Status");
    statusBarWorld.setBorder(BorderFactory.createEtchedBorder());
    jButtonConfigPeer.setText("Configure Peer");
    jButtonConfigPeer.addActionListener(new TerraPeerGUI_jButtonConfigPeer_actionAdapter(this));
    jButtonDiscovery.setText("Discover Peers");
    jButtonDiscovery.addActionListener(new TerraPeerGUI_jButtonDiscovery_actionAdapter(this));
    jPanelCenter.setLayout(borderLayout7);
    jPanelEast.setBorder(BorderFactory.createEtchedBorder());
    jPanelEast.setMinimumSize(new Dimension(140, 10));
    jPanelEast.setPreferredSize(new Dimension(140, 10));
    jPanelEast.setLayout(verticalFlowLayout7);
    jPanelCenter.setBorder(BorderFactory.createEtchedBorder());
    SceneTree.setPreferredSize(new Dimension(116, 200));
    jLabelEastZone.setText("Zone");
    jLabelEastZone.addMouseListener(new TerraPeerGUI_jLabelEastZone_mouseAdapter(this));
    jLabelEastZone.setOpaque(true);
    jLabelEastZone.setBackground(new Color(212, 208, 255));
    jLabelEastZone.setAlignmentY((float) 0.5);
    jLabelCTopZB.setText("Zone Builder");
    jLabelCTopZB.setForeground(new Color(0, 128, 128));
    jLabelCTopZB.setFont(new java.awt.Font("Dialog", 1, 9));
    jPanelZoneBuilderCore.setLayout(borderLayout9);
    jPanelZoneBuilderTop.setBackground(new Color(220, 220, 255));
    jPanelZoneBuilderTop.addMouseListener(new TerraPeerGUI_jPanelZoneBuilderTop_mouseAdapter(this));
    jPanelCardZoneBuilder.setLayout(borderLayout8);
    jButtonZoneBuilder.setText("Zone Builder");
    jButtonZoneBuilder.addActionListener(new TerraPeerGUI_jButtonZoneBuilder_actionAdapter(this));
    jLabelCTopSFilter.setText("Cyberspace Filtering");
    jLabelCTopSFilter.setForeground(new Color(0, 128, 128));
    jLabelCTopSFilter.setFont(new java.awt.Font("Dialog", 1, 9));
    jPanelCardSpaceFilter.setLayout(borderLayout10);
    jPanelSpaceFilterCore.setLayout(flowLayout1);
    jPanelSpaceFilterTop.setBackground(new Color(220, 220, 255));
    jPanelSpaceFilterTop.addMouseListener(new TerraPeerGUI_jPanelSpaceFilterTop_mouseAdapter(this));
    jPanelCardZoneNav.setLayout(borderLayout12);
    jPanelZoneNavTop.setBackground(new Color(220, 220, 255));
    jPanelZoneNavTop.addMouseListener(new TerraPeerGUI_jPanelZoneNavTop_mouseAdapter(this));
    jLabelCTopZNav.setText("Zone Navigation");
    jLabelCTopZNav.setForeground(new Color(0, 128, 128));
    jLabelCTopZNav.setFont(new java.awt.Font("Dialog", 1, 9));
    jLabelEastNet.setBackground(new Color(212, 208, 255));
    jLabelEastNet.setOpaque(true);
    jLabelEastNet.setText("JXTA Network");
    jLabelEastNet.addMouseListener(new TerraPeerGUI_jLabelEastNet_mouseAdapter(this));
    jLabelEast3.setForeground(Color.gray);
    jLabelEast3.setText("TerraPeer Name:");
    jLabelPeerName.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelPeerName.setHorizontalTextPosition(SwingConstants.LEFT);
    jLabelPeerName.setText("N/A");
    jLabelEast4.setForeground(Color.gray);
    jLabelEast4.setText("TerraPeer Group:");
    jLabelPeerGroup.setText("N/A");
    jLabelPeerGroup.setHorizontalTextPosition(SwingConstants.LEFT);
    jLabelPeerGroup.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelEast5.setForeground(Color.gray);
    jLabelEast5.setText("Discovery:");
    jLabelDiscoveryResponse.setBackground(Color.lightGray);
    jLabelDiscoveryResponse.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabelDiscoveryResponse.setBorder(BorderFactory.createLoweredBevelBorder());
    jLabelDiscoveryResponse.setOpaque(true);
    jLabelDiscoveryResponse.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelDiscoveryResponse.setText("Response");
    jLabelDiscoveryLooking.setBackground(Color.lightGray);
    jLabelDiscoveryLooking.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabelDiscoveryLooking.setBorder(BorderFactory.createLoweredBevelBorder());
    jLabelDiscoveryLooking.setOpaque(true);
    jLabelDiscoveryLooking.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelDiscoveryLooking.setText("Looking");
    jPanelEastZone.setLayout(verticalFlowLayout8);
    //jPanelEastNet.setMaximumSize(new Dimension(32767, 32767));
    jPanelEastNet.setLayout(verticalFlowLayout9);
    jLabelEastSpace.setBackground(new Color(212, 208, 255));
    jLabelEastSpace.setOpaque(true);
    jLabelEastSpace.setText("Space Navigation");
    jLabelEastSpace.addMouseListener(new TerraPeerGUI_jLabelEastSpace_mouseAdapter(this));
    jLabel3.setForeground(Color.gray);
    jLabel3.setText("Object Tree");
    jLabelEast6.setForeground(Color.gray);
    jLabelEast6.setText("Online Peers");
    jLabelEast6b.setForeground(Color.gray);
    jLabelEast6b.setText("Online PeerGroups");
    jPanelOnlinePeers.setPreferredSize(new Dimension(116, 100));
    jPanelOnlinePeers.setLayout(borderLayout14);
    jPanelOnlinePeerGroups.setPreferredSize(new Dimension(116, 100));
    jPanelOnlinePeerGroups.setLayout(borderLayout14b);
    //jListOnlinePeers.setMinimumSize(new Dimension(30, 30));
    //jListOnlinePeerGroups.setMinimumSize(new Dimension(30, 30));
    jScrollPaneOP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneOP.setAutoscrolls(true);
    jScrollPaneOP2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneOP2.setAutoscrolls(true);
    jPanelEastSpace.setBackground(new Color(200, 230, 200));
    jPanelEastSpace.setBorder(null);
    jPanelEastSpace.setMinimumSize(new Dimension(100, 100));
    jPanelEastSpace.setPreferredSize(new Dimension(126, 220));
    jPanelEastSpace.setLayout(verticalFlowLayout11);
    jPanelDiscovery.setLayout(gridBagLayout2);
    jPanelDiscovery.setMinimumSize(new Dimension(86, 30));
    jPanelDiscovery.setPreferredSize(new Dimension(128, 30));
    jPanelDiscovery.setRequestFocusEnabled(true);
    jProgressBarDiscovery.setBorder(BorderFactory.createLoweredBevelBorder());
    jProgressBarDiscovery.setValue(0);
    jPanelWestServices.setLayout(verticalFlowLayout5);
    jPanelBuildBlocks.setLayout(gridBagLayout3);
    jLabelBBServ.setBackground(new Color(200, 200, 255));
    jLabelBBServ.setForeground(Color.gray);
    jLabelBBServ.setBorder(null);
    jLabelBBServ.setOpaque(true);
    jLabelBBServ.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelBBServ.setText("Services");
    jLabelBBS_url.setText("URL");
    jLabelBBS_url.addMouseListener(new TerraPeerGUI_jLabelBBS_url_mouseAdapter(this));
    jLabelBBS_url.setBorder(border4);
    jLabelBBS_url.setOpaque(true);
    jLabelBBB_pyr.setText("Pyramid");
    jLabelBBB_pyr.addMouseListener(new TerraPeerGUI_jLabelBBB_pyr_mouseAdapter(this));
    jLabelBBB_pyr.setBorder(border4);
    jLabelBBB_pyr.setOpaque(true);
    jLabelBBB_cyl.setText("Cylinder");
    jLabelBBB_cyl.addMouseListener(new TerraPeerGUI_jLabelBBB_cyl_mouseAdapter(this));
    jLabelBBB_cyl.setBorder(border4);
    jLabelBBB_cyl.setOpaque(true);
    jLabelBBB_box.setText("Box");
    jLabelBBB_box.addMouseListener(new TerraPeerGUI_jLabelBBB_box_mouseAdapter(this));
    jLabelBBB_box.setBorder(border4);
    jLabelBBB_box.setOpaque(true);
    jLabelBBCon.setText("Connectors");
    jLabelBBCon.setBackground(new Color(200, 225, 200));
    jLabelBBCon.setForeground(Color.gray);
    jLabelBBCon.setBorder(null);
    jLabelBBCon.setOpaque(true);
    jLabelBBCon.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelBBBas.setText("Basics");
    jLabelBBBas.setBackground(new Color(255, 255, 128));
    jLabelBBBas.setForeground(Color.gray);
    jLabelBBBas.setBorder(null);
    jLabelBBBas.setOpaque(true);
    jLabelBBBas.setHorizontalAlignment(SwingConstants.CENTER);
    jPanelBBC.setLayout(borderLayout16);
    jPanelBuildBlocks.setPreferredSize(new Dimension(1500, 30));
    jScrollPaneBBC.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    jScrollPaneBBC.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    jScrollPaneBBC.setBorder(border5);
    jScrollPaneBBC.setPreferredSize(new Dimension(500, 48));
    jPanelZoneBuilderCore.setMaximumSize(new Dimension(2147483647, 2147483647));
    jPanelBBC.setBorder(null);
    jPanelBBC.setPreferredSize(new Dimension(500, 55));
    jPanelWestCyberspaceNav.setLayout(gridLayout2);
    gridLayout2.setColumns(1);
    gridLayout2.setRows(5);
    gridLayout70.setColumns(2);
    gridLayout70.setRows(3);
    jPanelWestCyberspaceNav.setBorder(border6);
    jPanelWestCyberspaceNav.setPreferredSize(new Dimension(116, 92));
    jButtonNavZone.setText("Zone Nav");
    jButtonNavZone.addActionListener(new TerraPeerGUI_jButtonNavZone_actionAdapter(this));
    jButtonNavSpace.setText("Space Nav");
    jButtonNavSpace.addActionListener(new TerraPeerGUI_jButtonNavSpace_actionAdapter(this));
    jLabel4.setText("South Panel");
    jButtonProjManager.setEnabled(false);
    jButtonProjManager.setText("Project Manager");
    jButtonProjManager.addActionListener(new TerraPeerGUI_jButtonProjManager_actionAdapter(this));
    jButtonServSearch.setEnabled(false);
    jButtonServSearch.setDoubleBuffered(false);
    jButtonServSearch.setText("Search...");
    jButtonServFb.setEnabled(false);
    jButtonServFb.setText("Feedback...");
    jButtonServWeb.setActionCommand("jButtonServCom");
    jButtonServWeb.setText("Web...");
    jButtonServWeb.addActionListener(new TerraPeerGUI_jButtonServWeb_actionAdapter(this));
    jLabel5.setText("Viewpoint");
    jToggleButtonSpaceNavVPP.setPreferredSize(new Dimension(106, 16));
    jToggleButtonSpaceNavVPP.setSelected(true);
    jToggleButtonSpaceNavVPP.setText("Personal");
    jToggleButtonSpaceNavVPP.addActionListener(new TerraPeerGUI_jToggleButtonSpaceNavVPP_actionAdapter(this));
    jToggleButtonSpaceNavVPO.setPreferredSize(new Dimension(106, 16));
    jToggleButtonSpaceNavVPO.setText("Orbital");
    jToggleButtonSpaceNavVPO.addActionListener(new TerraPeerGUI_jToggleButtonSpaceNavVPO_actionAdapter(this));
    jPanelSpaceNavOrbitView.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanelSpaceNavOrbitView.setPreferredSize(new Dimension(140, 140));
    jLabelBBB_sphere.setAlignmentY((float) 0.5);
    jLabelBBB_sphere.setBorder(border4);
    jLabelBBB_sphere.setOpaque(true);
    jLabelBBB_sphere.setText("Sphere");
    jLabelBBB_sphere.addMouseListener(new TerraPeerGUI_jLabelBBB_sphere_mouseAdapter(this));
    jLabelBBB_landmark_a.setText("Landmark A");
    jLabelBBB_landmark_a.addMouseListener(new TerraPeerGUI_jLabelBBB_landmark_a_mouseAdapter(this));
    jLabelBBB_landmark_a.setOpaque(true);
    jLabelBBB_landmark_a.setBorder(border4);
    jLabelBBB_landmark_a.setAlignmentY((float)0.5);
    jLabelBBB_landmark_b.setText("Landmark B");
    jLabelBBB_landmark_b.addMouseListener(new TerraPeerGUI_jLabelBBB_landmark_b_mouseAdapter(this));
    jLabelBBB_landmark_b.setOpaque(true);
    jLabelBBB_landmark_b.setBorder(border4);
    jLabelBBB_landmark_b.setAlignmentY((float)0.5);
    jLabelBBB_landmark_c.setText("Landmark C");
    jLabelBBB_landmark_c.addMouseListener(new TerraPeerGUI_jLabelBBB_landmark_c_mouseAdapter(this));
    jLabelBBB_landmark_c.setOpaque(true);
    jLabelBBB_landmark_c.setBorder(border4);
    jLabelBBB_landmark_c.setAlignmentY((float)0.5);
    jLabelBBB_house_a.setText("House A");
    jLabelBBB_house_a.addMouseListener(new TerraPeerGUI_jLabelBBB_house_a_mouseAdapter(this));
    jLabelBBB_house_a.setOpaque(true);
    jLabelBBB_house_a.setBorder(border4);
    jLabelBBB_house_a.setAlignmentY((float)0.5);
    jLabelBBB_house_b.setText("House B");
    jLabelBBB_house_b.addMouseListener(new TerraPeerGUI_jLabelBBB_house_b_mouseAdapter(this));
    jLabelBBB_house_b.setOpaque(true);
    jLabelBBB_house_b.setBorder(border4);
    jLabelBBB_house_b.setAlignmentY((float)0.5);
    jLabelBBB_house_c.setText("House C");
    jLabelBBB_house_c.addMouseListener(new TerraPeerGUI_jLabelBBB_house_c_mouseAdapter(this));
    jLabelBBB_house_c.setOpaque(true);
    jLabelBBB_house_c.setBorder(border4);
    jLabelBBB_house_c.setAlignmentY((float)0.5);
    jLabelBBS_chat.setBorder(border4);
    jLabelBBS_chat.setOpaque(true);
    jLabelBBS_chat.setText("Chat");
    jLabelBBS_chat.addMouseListener(new TerraPeerGUI_jLabelBBS_chat_mouseAdapter(this));
    //jLabelBBS_messenger.addMouseListener(new TerraPeerGUI_jLabelBBS_messenger_mouseAdapter(this));
    jLabelBBC_fb.setBorder(border4);
    jLabelBBC_fb.setOpaque(true);
    jLabelBBC_fb.setText("Feedback");
    jLabelBBS_ftp.setBorder(border4);
    jLabelBBS_ftp.setOpaque(true);
    jLabelBBS_ftp.setText("FTP");
    jLabelBBS_ftp.addMouseListener(new TerraPeerGUI_jLabelBBS_ftp_mouseAdapter(this));
    jLabelBBS_web.setBorder(border4);
    jLabelBBS_web.setOpaque(true);
    jLabelBBS_web.setText("Website");
    jLabelBBS_web.addMouseListener(new TerraPeerGUI_jLabelBBS_web_mouseAdapter(this));
    jPanelBBCon.setBackground(new Color(200, 225, 200));
    jPanelBBCon.setBorder(null);
    jPanelBBCon.setPreferredSize(new Dimension(100, 27));
    jPanelBBServ.setBackground(new Color(200, 200, 255));
    jPanelBBServ.setBorder(null);
    jPanelBBServ.setPreferredSize(new Dimension(300, 27));
    jPanelBBBas.setBackground(new Color(255, 255, 128));
    jPanelBBBas.setBorder(null);
    jPanelBBBas.setPreferredSize(new Dimension(300, 27));
    jPanelSpaceNavGridView.setPreferredSize(new Dimension(100, 100));
    jPanelSpaceNavGridView.setBorder(BorderFactory.createLoweredBevelBorder());
    jButtonNavSPSpace.addActionListener(new TerraPeerGUI_jButtonNavSpace1_actionAdapter(this));
    jButtonNavSPSpace.setBackground(new Color(128, 128, 255));
    jButtonNavSPSpace.setFont(new java.awt.Font("Dialog", 0, 9));
    jButtonNavSPSpace.setOpaque(true);
    jButtonNavSPSpace.setPreferredSize(new Dimension(120, 15));
    jButtonNavSPSpace.setSelected(true);
    jButtonNavSPSpace.setText("Space Nav Panel");
    jButtonNavSPSpace.addActionListener(new TerraPeerGUI_jButtonNavSpace1_actionAdapter(this));
    jButtonNavSPZone.addActionListener(new TerraPeerGUI_jButtonNavZone1_actionAdapter(this));
    jButtonNavSPZone.setBackground(new Color(128, 128, 255));
    jButtonNavSPZone.setFont(new java.awt.Font("Dialog", 0, 9));
    jButtonNavSPZone.setOpaque(true);
    jButtonNavSPZone.setPreferredSize(new Dimension(120, 15));
    jButtonNavSPZone.setText("Zone Nav Panel");
    jButtonNavSPZone.addActionListener(new TerraPeerGUI_jButtonNavZone1_actionAdapter(this));
    jButtonNavSPFilterPanel.addActionListener(new TerraPeerGUI_jButtonFilterPanel1_actionAdapter(this));
    jButtonNavSPFilterPanel.setBackground(new Color(128, 128, 255));
    jButtonNavSPFilterPanel.setFont(new java.awt.Font("Dialog", 0, 9));
    jButtonNavSPFilterPanel.setOpaque(true);
    jButtonNavSPFilterPanel.setPreferredSize(new Dimension(120, 15));
    jButtonNavSPFilterPanel.setText("Filter Panel");
    jButtonNavSPFilterPanel.addActionListener(new TerraPeerGUI_jButtonFilterPanel1_actionAdapter(this));
    jButtonNavSPZoneBuilder.setBackground(new Color(128, 128, 255));
    jButtonNavSPZoneBuilder.setFont(new java.awt.Font("Dialog", 0, 9));
    jButtonNavSPZoneBuilder.setOpaque(true);
    jButtonNavSPZoneBuilder.setPreferredSize(new Dimension(120, 15));
    jButtonNavSPZoneBuilder.setText("Builder Panel");
    jButtonNavSPZoneBuilder.addActionListener(new TerraPeerGUI_jButtonZoneBuilder_actionAdapter(this));
    jMenuViewSidePanels.setText("Side Panels");
    jCheckBoxMenuItemShowWestPanel.setSelected(true);
    jCheckBoxMenuItemShowWestPanel.setText("Show Action Panel (left)");
    jCheckBoxMenuItemShowWestPanel.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemShowWestPanel_actionAdapter(this));
    jCheckBoxMenuItemShowEastPanel.setSelected(true);
    jCheckBoxMenuItemShowEastPanel.setText("Show Information Panel (right)");
    jCheckBoxMenuItemShowEastPanel.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemShowEastPanel_actionAdapter(this));
    jCheckBoxMenuItemShowNavPanel.setSelected(true);
    jCheckBoxMenuItemShowNavPanel.setText("Show Naviation Buttons (bottom)");
    jCheckBoxMenuItemShowNavPanel.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemShowNavPanel_actionAdapter(this));
    jCheckBoxMenuItemShowToolbar.setSelected(true);
    jCheckBoxMenuItemShowToolbar.setText("Show Toolbar Menu (top)");
    jCheckBoxMenuItemShowToolbar.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemShowToolbar_actionAdapter(this));
    jSliderGridViewZoom.setOrientation(JSlider.VERTICAL);
    jSliderGridViewZoom.setMajorTickSpacing(25);
    jSliderGridViewZoom.setMinorTickSpacing(5);
    jSliderGridViewZoom.setPaintLabels(true);
    jSliderGridViewZoom.setPaintTicks(true);
    jSliderGridViewZoom.setFont(new java.awt.Font("Dialog", 0, 9));
    jSliderGridViewZoom.setPreferredSize(new Dimension(44, 160));
    jSliderGridViewZoom.addChangeListener(new TerraPeerGUI_jSliderGridViewZoom_changeAdapter(this));
    jPanelWestCyberspaceVP.setLayout(gridLayout3);
    jButtonSoutPanelUpDown.setFont(new java.awt.Font("Dialog", 0, 9));
    jButtonSoutPanelUpDown.setPreferredSize(new Dimension(20, 15));
    jButtonSoutPanelUpDown.setText("");
    jButtonSoutPanelUpDown.addActionListener(new TerraPeerGUI_jButtonSoutPanelUpDown_actionAdapter(this));
    jLabelEastSpaceCoords.setText("Coords: [X, Y]");
    jLabelEastSpaceLevel.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelEastSpaceLevel.setText("Level 0");
    jPanelEastSpaceMap.setPreferredSize(new Dimension(100, 100));
    jLabelEastSpaceDom.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelEastSpaceDom.setText("Domain: n/a");
    jLabelEastSpaceSpeed.setText("Speed: 0");
    jLabelEastSpaceHeadingYaw.setText("Heading (Yaw): 0");
    jLabelEastSpaceHeadingPitch.setText("Heading (Pitch): 0");
    jLabelEastSpaceHeadingRoll.setText("Heading (Roll): 0");
    jProgressBarMain.setPreferredSize(new Dimension(148, 10));
    jProgressBarMain.setRequestFocusEnabled(true);
    jPanelSpaceNavOrbit.setLayout(borderLayout19);
    jLabel15.setBackground(Color.lightGray);
    jLabel15.setOpaque(true);
    jLabel15.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel15.setText("Orbital View");
    jPanelSpaceNavOrbit.setPreferredSize(new Dimension(160, 160));
    jPanelSpaceNavGrid.setPreferredSize(new Dimension(160, 160));
    jPanelSpaceNavGrid.setLayout(borderLayout18);
    jLabel111.setBackground(Color.lightGray);
    jLabel111.setOpaque(true);
    jLabel111.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel111.setText("Grid View");
    jButtonGridViewOrigo.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonGridViewOrigo.setPreferredSize(new Dimension(60, 16));
    jButtonGridViewOrigo.setText("Origo");
    jButtonGridViewOrigo.addActionListener(new TerraPeerGUI_jButtonGridViewOrigo_actionAdapter(this));
    jButtonGridViewAvatar.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonGridViewAvatar.setPreferredSize(new Dimension(60, 16));
    jButtonGridViewAvatar.setText("Avatar");
    jButtonGridViewAvatar.addActionListener(new TerraPeerGUI_jButtonGridViewAvatar_actionAdapter(this));
    jPanel5.setPreferredSize(new Dimension(120, 160));
    jPanel5.setLayout(borderLayout20);
    jLabel20.setBackground(Color.lightGray);
    jLabel20.setOpaque(true);
    jLabel20.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel20.setText("Compass");
    jPanel6.setPreferredSize(new Dimension(80, 160));
    jPanel6.setLayout(borderLayout21);
    jLabel21.setBackground(Color.lightGray);
    jLabel21.setOpaque(true);
    jLabel21.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel21.setText("Altitude");
    jPanelFilterSpace.setPreferredSize(new Dimension(100, 160));
    jPanelFilterSpace.setLayout(verticalFlowLayout12);
    jLabel22.setBackground(Color.lightGray);
    jLabel22.setOpaque(true);
    jLabel22.setText("Space Filter");
    jCheckBoxFilterAmbience.setText("Ambience");
    jCheckBoxFilterAvatars.setText("Avatars");
    jCheckBoxFilterLandmarks.setText("Landmarks");
    jCheckBoxFilterAVRout.setText("Outside AVR");
    jCheckBoxFilterAVRin.setText("Inside AVR");
    jLabel25.setText("Zone Filter");
    jLabel25.setOpaque(true);
    jLabel25.setBackground(Color.lightGray);
    jPanelFilterZone.setLayout(verticalFlowLayout110);
    jPanelFilterZone.setPreferredSize(new Dimension(100, 160));
    jCheckBoxFilterMyZone.setText("My Zone");
    jPanelFilterPreview.setBorder(BorderFactory.createEtchedBorder());
    jPanelFilterPreview.setDebugGraphicsOptions(0);
    jPanelFilterPreview.setPreferredSize(new Dimension(160, 160));
    jPanelFilterPreview.setLayout(borderLayout23);
    //jPanelSpaceNavHeading.setPreferredSize(new Dimension(10, 44));
    jPanelWestCyberspaceVP.setBorder(border6);
    jPanelWestCyberspaceVP.setPreferredSize(new Dimension(116, 48));
    gridLayout3.setColumns(1);
    gridLayout3.setRows(3);
    jLabel23.setText("Control");
    jLabel23.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel23.setOpaque(true);
    jLabel23.setBackground(Color.lightGray);
    jPanel7.setLayout(borderLayout22);
    jPanel7.setForeground(Color.white);
    jPanel7.setOpaque(false);
    jPanel7.setPreferredSize(new Dimension(100, 160));
    jButtonSpaceNavCtrl_Stop.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonSpaceNavCtrl_Stop.setPreferredSize(new Dimension(56, 18));
    jButtonSpaceNavCtrl_Stop.setToolTipText("Stop");
    jButtonSpaceNavCtrl_Stop.addActionListener(new TerraPeerGUI_jButtonSpaceNavCtrl_Stop_actionAdapter(this));
    jPanelSpaceNavControl.setLayout(verticalFlowLayout10);
    jPanelSpaceNavCtrl_Dir.setOpaque(false);
    jPanelSpaceNavCtrl_Dir.setPreferredSize(new Dimension(64, 64));
    jPanelSpaceNavCtrl_SpeedDir.setPreferredSize(new Dimension(90, 70));
    jPanelSpaceNavCtrl_SpeedDir.setLayout(borderLayout24);
    jPanel70.setLayout(gridLayout70);
    jLabelEastSpaceAltitude.setText("Alt: [Z]");
    jButtonSpaceNavCtrl_Home.addActionListener(new TerraPeerGUI_jButtonSpaceNavCtrl_Home_actionAdapter(this));
    jButtonSpaceNavCtrl_Home.addActionListener(new TerraPeerGUI_jButtonSpaceNavCtrl_Home_actionAdapter(this));
    jButtonSpaceNavCtrl_Home.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonSpaceNavCtrl_Home.setPreferredSize(new Dimension(56, 18));
    jButtonSpaceNavCtrl_Home.setToolTipText("Home");
    jPanelZoneGrid.setPreferredSize(new Dimension(540, 160));
    jPanelZoneNavCore.setLayout(borderLayout11);
    jPanelZoneGridControl.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanelZoneGridControl.setPreferredSize(new Dimension(100, 160));
    jPanelZoneGridControl.setLayout(gridBagLayout4);
    //jPanelSpaceNavCompass.setPreferredSize(new Dimension(100, 100));
    jButtonZoneGridRight.setBackground(Color.lightGray);
    jButtonZoneGridRight.setPreferredSize(new Dimension(50, 25));
    jButtonZoneGridRight.addActionListener(new TerraPeerGUI_jButtonZoneGridRight_actionAdapter(this));
    jButtonZoneGridUp.setBackground(Color.lightGray);
    jButtonZoneGridUp.setPreferredSize(new Dimension(50, 25));
    jButtonZoneGridUp.addActionListener(new TerraPeerGUI_jButtonZoneGridUp_actionAdapter(this));
    jButtonZoneGridLeft.setBackground(Color.lightGray);
    jButtonZoneGridLeft.setPreferredSize(new Dimension(50, 25));
    jButtonZoneGridLeft.addActionListener(new TerraPeerGUI_jButtonZoneGridLeft_actionAdapter(this));
    jButtonZoneGridDown.setBackground(Color.lightGray);
    jButtonZoneGridDown.setPreferredSize(new Dimension(50, 25));
    jButtonZoneGridDown.addActionListener(new TerraPeerGUI_jButtonZoneGridDown_actionAdapter(this));
    jTextFieldZoneGridCoord.setBackground(Color.lightGray);
    jTextFieldZoneGridCoord.setToolTipText("");
    jTextFieldZoneGridCoord.setSelectedTextColor(Color.white);
    jTextFieldZoneGridCoord.setText(vars.ZONEGRID_START_XCOORD+","+vars.ZONEGRID_START_YCOORD);
    jTextFieldZoneGridCoord.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelZoneNavControl.setBackground(new Color(212, 208, 255));
    jLabelZoneNavControl.setOpaque(true);
    jLabelZoneNavControl.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelZoneNavControl.setText("Control");
    jLabelEastZoneCoords.setText("Coords: [X, Y]");
    jLabelEastZoneSize.setText("Size: [X, Y]");
    jLabelEastZoneName.setText("Name: N/A");
    jLabelEastZoneID.setText("ID: N/A");
    jLabelEastZoneGridMouseCoords.setForeground(Color.darkGray);
    jLabelEastZoneGridMouseCoords.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelEastZoneGridMouseCoords.setText("X,Y");
    jLabel17.setText("Grid Coordinate:");
    jLabel17.setForeground(Color.gray);
    jLabel18.setText("Description");
    jLabel18.setForeground(Color.gray);
    jEditorPaneZoneDescr.setFont(new java.awt.Font("Dialog", 0, 9));
    jEditorPaneZoneDescr.setPreferredSize(new Dimension(116, 40));
    jEditorPaneZoneDescr.setText("");
    jLabelUpdatePing.setFont(new java.awt.Font("Dialog", 0, 9));
    jLabelUpdatePing.setText("UPDATE");
    jLabelUpdatePing.setOpaque(true);
    jButtonGoCoord.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonGoCoord.setPreferredSize(new Dimension(49, 10));
    jButtonGoCoord.setText("Jump");
    jButtonGoCoord.addActionListener(new TerraPeerGUI_jButtonGoCoord_actionAdapter(this));
    jSliderSpeed.setOrientation(JSlider.VERTICAL);
    //jSliderSpeed.setInverted(true);
    jSliderSpeed.setMajorTickSpacing(1);
    jSliderSpeed.setMaximum(10);
    jSliderSpeed.setPaintTicks(true);
    jSliderSpeed.setValue(5);
    jSliderSpeed.setPreferredSize(new Dimension(16, 16));
    jSliderSpeed.addChangeListener(new TerraPeerGUI_jSliderSpeed_changeAdapter(this));
    jPanelSpaceNavControl.setBackground(Color.white);
    jPanelSpaceNavControl.setOpaque(false);
    jButtonSpaceNavCtrl_Align.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonSpaceNavCtrl_Align.setPreferredSize(new Dimension(56, 18));
    jButtonSpaceNavCtrl_Align.setToolTipText("Align");
    jButtonSpaceNavCtrl_Align.setText("");
    jButtonSpaceNavCtrl_North.addActionListener(new TerraPeerGUI_jButtonSpaceNavCtrl_North_actionAdapter(this));
    jButtonSpaceNavCtrl_North.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonSpaceNavCtrl_North.setPreferredSize(new Dimension(56, 18));
    jButtonSpaceNavCtrl_North.setToolTipText("North");
    jButtonSpaceNavCtrl_North.setText("");
    jButtonSpaceNavCtrl_OrigoNorth.addActionListener(new TerraPeerGUI_jButtonSpaceNavCtrl_OrigoNorth_actionAdapter(this));
    jButtonSpaceNavCtrl_OrigoNorth.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonSpaceNavCtrl_OrigoNorth.setPreferredSize(new Dimension(56, 18));
    jButtonSpaceNavCtrl_OrigoNorth.setToolTipText("Origo North");
    jButtonSpaceNavCtrl_OrigoNorth.setText("");
    jButtonSpaceNavCtrl_OrigoTop.addActionListener(new TerraPeerGUI_jButtonSpaceNavCtrl_OrigoTop_actionAdapter(this));
    jButtonSpaceNavCtrl_OrigoTop.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButtonSpaceNavCtrl_OrigoTop.setPreferredSize(new Dimension(56, 18));
    jButtonSpaceNavCtrl_OrigoTop.setToolTipText("Origo Top");
    jButtonSpaceNavCtrl_OrigoTop.setText("");
    jPanelFilterZoneListing.setBorder(BorderFactory.createEtchedBorder());
    jPanelFilterZoneListing.setPreferredSize(new Dimension(160, 160));
    jPanelFilterZoneListing.setLayout(borderLayout13);
    jLabel24.setText("Space Activity");
    jLabel24.setOpaque(true);
    jLabel24.setBackground(Color.lightGray);
    jLabel26.setText("Active Zones");
    jLabel26.setOpaque(true);
    jLabel26.setBackground(Color.lightGray);
    jLabel16.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel16.setText("Network Traffic");
    jProgressBarActivityNet.setValue(11);
    jLabel19.setText("Zone Buffer");
    jLabel19.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButton2.setFont(new java.awt.Font("Tahoma", 0, 9));
    jButton2.setMinimumSize(new Dimension(75, 24));
    jButton2.setPreferredSize(new Dimension(60, 16));
    jButton2.setText("Refresh");
    jToggleButton1.setFont(new java.awt.Font("Tahoma", 0, 9));
    jToggleButton1.setPreferredSize(new Dimension(30, 16));
    jToggleButton1.setText("Show");
    jSliderZoneGridStep.addChangeListener(new TerraPeerGUI_jSliderZoneGridStep_changeAdapter(this));
    jSliderZoneGridStep.setMaximum(100);
    jSliderZoneGridStep.setMinimum(1);
    jSliderZoneGridStep.setPreferredSize(new Dimension(90, 16));
    jSliderZoneGridStep.setSnapToTicks(true);
    jSliderZoneGridStep.setValue(vars.ZONEGRID_COORD_STEP);
    jSliderZoneGridStep.setPaintTicks(true);
    jSliderZoneGridZoom.addChangeListener(new TerraPeerGUI_jSliderZoneGridZoom_changeAdapter(this));
    jSliderZoneGridZoom.setMaximum(20);
    jSliderZoneGridZoom.setMinimum(2);
    jSliderZoneGridZoom.setSnapToTicks(true);
    jSliderZoneGridZoom.setValue(vars.ZONEGRID_GRIDSPACE_PIXEL);
    jSliderZoneGridZoom.setPreferredSize(new Dimension(90, 16));
    jSliderZoneGridZoom.setPaintTicks(true);
    jPanelZoneGridControl2.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanelZoneGridControl2.setPreferredSize(new Dimension(100, 160));
    jPanelZoneGridControl2.setLayout(gridBagLayout42);
    jCheckBoxZoneGridAutoMove.setText("Auto Move");
    jCheckBoxZoneGridAutoMove.addActionListener(new TerraPeerGUI_jCheckBoxZoneGridAutoMove_actionAdapter(this));
    jLabelZoneNavControl2.setText("Grid Setting");
    jLabelZoneNavControl2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelZoneNavControl2.setOpaque(true);
    jLabelZoneNavControl2.setBackground(new Color(212, 208, 255));
    jLabel27.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel27.setForeground(Color.gray);
    jLabel27.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel27.setHorizontalTextPosition(SwingConstants.TRAILING);
    jLabel27.setText("Step Rate");
    jLabel27.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel28.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel28.setForeground(Color.gray);
    jLabel28.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel28.setHorizontalTextPosition(SwingConstants.TRAILING);
    jLabel28.setText("Zoom");
    jLabel28.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
    gridLayout71.setColumns(2);
    gridLayout71.setRows(1);
    jPanel71.setLayout(gridLayout71);
    jButtonZoneGridNavCtrl_Origo.setFont(new java.awt.Font("Tahoma", 0, 9));
    //jButtonZoneGridNavCtrl_Origo.setPreferredSize(new Dimension(25, 25));
    jButtonZoneGridNavCtrl_Origo.setToolTipText("Origo");
    jButtonZoneGridNavCtrl_Origo.addActionListener(new TerraPeerGUI_jButtonSpaceGridNavCtrl_Origo_actionAdapter(this));
    jButtonZoneGridNavCtrl_Home.addActionListener(new TerraPeerGUI_jButtonSpaceGridNavCtrl_Home_actionAdapter(this));
    jButtonZoneGridNavCtrl_Home.setFont(new java.awt.Font("Tahoma", 0, 9));
    //jButtonZoneGridNavCtrl_Home.setPreferredSize(new Dimension(25, 25));
    jButtonZoneGridNavCtrl_Home.setToolTipText("Home");
    //jPanel71.setPreferredSize(new Dimension(50, 25));
    jButtonZoneGrid.setText("Zone Grid");
    jButtonZoneGrid.addActionListener(new TerraPeerGUI_jButtonZoneGrid_actionAdapter(this));
    jPanelEastZone.add(jLabelEastZoneID, null);
    jPanelEastZone.add(jLabelEastZoneName, null);
    jPanelWestCyberspaceVP.add(jLabel5, null);
    jPanelWestCyberspaceVP.add(jToggleButtonSpaceNavVPP, null);
    jPanelWestCyberspaceVP.add(jToggleButtonSpaceNavVPO, null);
    jPanel7.add(jLabel23, BorderLayout.NORTH);
    jPanel7.add(jPanelSpaceNavControl, BorderLayout.CENTER);
    jPanelSpaceNavCore.add(jPanel7, null);
    jPanelSpaceNavCore.add(jPanelSpaceNavGrid, null);
    jPanelSpaceNavGrid.add(jLabel111, BorderLayout.NORTH);
    jPanelSpaceNavGrid.add(jSliderGridViewZoom, BorderLayout.WEST);
    jPanelSpaceNavGrid.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jPanelSpaceNavGridView, null);
    jPanelSpaceNavGrid.add(jPanel4,  BorderLayout.SOUTH);
    jPanel4.add(jButtonGridViewOrigo, null);
    jPanel4.add(jButtonGridViewAvatar, null);
    jPanelSpaceNavCore.add(jPanel5, null);
    jPanel5.add(jLabel20, BorderLayout.NORTH);
    jPanel5.add(jPanelSpaceNavCompass, BorderLayout.CENTER);
    jPanel5.add(jPanelSpaceNavHeading,  BorderLayout.SOUTH);
    jPanelSpaceNavCore.add(jPanelSpaceNavOrbit, null);
    jPanelSpaceNavOrbit.add(jLabel15,  BorderLayout.NORTH);
    jPanelSpaceNavOrbit.add(jPanel3,  BorderLayout.CENTER);
    jPanel3.add(jPanelSpaceNavOrbitView, null);
    jPanelSpaceNavCore.add(jPanel6, null);
    jPanel6.add(jLabel21,  BorderLayout.NORTH);
    jPanelBBServ.add(jLabelBBS_web, null);
    jPanelBBServ.add(jLabelBBS_ftp, null);
    jPanelBBServ.add(jLabelBBS_chat, null);
    jPanelBBServ.add(jLabelBBS_url, null);
    jPanelBuildBlocks.add(jLabelBBCon, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 27, 33));
    jPanelBuildBlocks.add(jPanelBBCon,   new GridBagConstraints(5, 0, 1, 1, 0.2, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), -17, 23));
    jPanelBBCon.add(jLabelBBC_fb, null);
    jPanelWestServices.add(jScrollPaneService, null);
    jScrollPaneService.getViewport().add(jEditorPaneService, null);
    jPanelWestServices.add(jButtonServSearch, null);
    jPanelWestServices.add(jButtonServFb, null);
    jPanelWestCyberspaceNav.add(jLabel4, null);
    jPanelWestCyberspaceNav.add(jButtonNavSpace, null);
    jPanelWestCyberspaceNav.add(jButtonNavZone, null);
    jPanelWestCyberspaceNav.add(jButtonZoneBuilder, null);
    jPanelWestCyberspaceNav.add(jButtonFilterPanel, null);
    jPanelBBC.add(jScrollPaneBBC, BorderLayout.CENTER);
    jScrollPaneBBC.getViewport().add(jPanelBuildBlocks, null);
    jPanelBuildBlocks.add(jLabelBBBas, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 53, 33));
    jPanelBuildBlocks.add(jPanelBBBas,  new GridBagConstraints(1, 0, 1, 1, 0.5, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), -217, 23));
    jPanelBBBas.add(jLabelBBB_sphere, null);
    jPanelBBBas.add(jLabelBBB_box, null);
    jPanelBBBas.add(jLabelBBB_pyr, null);
    jPanelBBBas.add(jLabelBBB_cyl, null);
    jPanelBBBas.add(jLabelBBB_landmark_a, null);
    jPanelBBBas.add(jLabelBBB_landmark_b, null);
    jPanelBBBas.add(jLabelBBB_landmark_c, null);
    jPanelBBBas.add(jLabelBBB_house_a, null);
    jPanelBBBas.add(jLabelBBB_house_b, null);
    jPanelBBBas.add(jLabelBBB_house_c, null);
    jPanelBuildBlocks.add(jLabelBBServ, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 42, 33));
    jPanelBuildBlocks.add(jPanelBBServ,    new GridBagConstraints(3, 0, 1, 1, 0.3, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), -217, 23));
    jPanelEastZone.add(jLabelEastZoneCoords, null);
    jPanelEastZone.add(jLabelEastZoneSize, null);
    jPanelEastZone.add(jLabel18, null);
    jPanelEastZone.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(jEditorPaneZoneDescr, null);
    jPanelEastZone.add(jLabel3, null);
    jPanelEast.add(jLabelEastNet, null);
    jPanelEast.add(jPanelEastNet, null);
    jPanelEastNet.add(jLabelEast3, null);
    jPanelEastNet.add(jLabelPeerName, null);
    jPanelEastNet.add(jLabelEast4, null);
    jPanelEastNet.add(jLabelPeerGroup, null);
    jPanelEastNet.add(jLabelEast5, null);
    jPanelEastNet.add(jPanelDiscovery, null);
    jPanelEastNet.add(jLabelEast6, null);
    jPanelEastNet.add(jPanelOnlinePeers, null);
    jPanelEastNet.add(jLabelEast6b, null);
    jPanelEastNet.add(jPanelOnlinePeerGroups, null);
    jPanelCardSpaceNav.add(jPanelSpaceNavTop, BorderLayout.NORTH);
    jPanelSpaceNavTop.add(jLabelCTopSNav, null);
    jPanelCardSpaceNav.add(jPanelSpaceNavCore, BorderLayout.CENTER);
    jPanelCardPersonal.add(jPanelPersonalTop, BorderLayout.NORTH);
    jPanelPersonalTop.add(jLabelCTopPIM, null);
    jPanelCardPersonal.add(jPanelMyInfoCore, BorderLayout.CENTER);
    jPanelMyInfoCore.add(jLabel10, null);
    jPanelCardProjects.add(jPanelProjectsTop, BorderLayout.NORTH);
    jPanelProjectsTop.add(jLabelCTopProject, null);
    jPanelCardProjects.add(jPanelProjectsCore, BorderLayout.CENTER);
    jPanelWestCyberspace.add(jButtonVirtualWorld, null);
    jPanelWestCyberspace.add(jButtonZoneGrid, null);
    jPanelWestCyberspace.add(jButtonNavHome, null);
    jPanelWestCyberspace.add(jPanelWestCyberspaceNav, null);
    jPanelWestCyberspace.add(jPanelWestCyberspaceVP, null);
    jToolBar.add(jButtonTBLogin, null);
    jToolBar.add(jButtonTBSecurity, null);
    jToolBar.add(jButtonTBMyHome);
    jToolBar.add(jButtonTBMyInformation, null);
    jToolBar.add(jButtonTBMyFeedback, null);
    jToolBar.add(jButtonTBMyContacts, null);
    jToolBar.add(jButtonTBProject);
    jToolBar.add(jButtonTBMembers, null);
    jToolBar.add(jButtonTBVirtualWorld, null);
    jToolBar.add(jButtonTBLog, null);
    jToolBar.add(jButtonTBPreferences, null);
    jToolBar.add(jButtonTBHelp);
    jToolBar.add(jLabelEmpty1, null);
    jToolBar.add(glue1, null);
    jToolBar.add(jLabelTitle, null);
    jPanelWestAccess.add(jButtonLoginStart, null);
    jPanelWestAccess.add(jButtonConfigPeer, null);
    jPanelWestAccess.add(jButtonDiscovery, null);
    jPanelWestAccess.add(jButtonObjRepository, null);
    contentPane.add(jToolBar, BorderLayout.NORTH);
    contentPane.add(jPanelBase, BorderLayout.CENTER);
    jPanelWestAccess.add(jButtonSecuritySettings, null);
    jPanelWestAccess.add(jButtonLog, null);
    contentPane.add(jPanelStatusNav, BorderLayout.SOUTH);
    jPanelWestPIM.add(jButtonMyInformation, null);
    jPanelWestPIM.add(jButtonMyContacts, null);
    jPanelWestPIM.add(jButtonMyFeedback, null);
    jPanelStatusNav.add(statusBarApp,            new GridBagConstraints(2, 2, 1, 1, 0.2, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 2, 2));
    jPanelStatusNav.add(statusBarNet,          new GridBagConstraints(3, 2, 1, 1, 0.2, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 2, 2));
    jPanelStatusNav.add(statusBarWorld,        new GridBagConstraints(4, 2, 1, 1, 0.2, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 2, 2));
    jPanelStatusNav.add(jProgressBarMain,        new GridBagConstraints(5, 2, 1, 1, 0.4, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 4, 4));
    jPanelStatusNav.add(jLabelUpdatePing,       new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 4, 2, 2), 0, 0));
    jPanelBase.add(jPanelEast,  BorderLayout.EAST);
    jPanelWestProject.add(jButtonProjManager, null);
    jPanelWestProject.add(jButtonMembership, null);
    jPanelWest.add(jLabelWestTerraPeer, null);
    jPanelWest.add(jPanelWestAccess, null);
    jPanelWest.add(jLabelWestPersonal, null);
    jPanelWest.add(jPanelWestPIM, null);
    jPanelWest.add(jLabelWestProjects, null);
    jPanelWest.add(jPanelWestProject, null);
    jPanelWest.add(jLabelWestCyberspace, null);
    jPanelWest.add(jPanelWestCyberspace, null);
    jPanelWest.add(jLabelWestServices, null);
    jPanelWest.add(jPanelWestServices, null);
    jPanelBase.add(jPanelWest, BorderLayout.WEST);
    jPanelBase.add(jPanelCenter, BorderLayout.CENTER);
    jPanelBase.add(jPanelBottomNav, BorderLayout.SOUTH);
    jPanelCenter.add(jPanelSpaceNavAvatView,  BorderLayout.CENTER);
    jPanelCenter.add(jPanelSouth, BorderLayout.SOUTH);
    jPanelCardZoneBuilder.add(jPanelZoneBuilderTop, BorderLayout.NORTH);
    jPanelZoneBuilderTop.add(jLabelCTopZB, null);
    jPanelCardZoneBuilder.add(jPanelZoneBuilderCore,  BorderLayout.CENTER);
    jPanelSouth.add(jPanelCardSpaceNav,  "jPanelCardSpaceNav");
    jPanelSouth.add(jPanelCardSpaceFilter,   "jPanelCardSpaceFilter");
    jPanelSouth.add(jPanelCardZoneNav,   "jPanelCardZoneNav");
    jPanelSouth.add(jPanelCardZoneBuilder,  "jPanelCardZoneBuilder");
    jPanelSouth.add(jPanelCardProjects,  "jPanelCardProjects");
    jPanelSouth.add(jPanelCardPersonal,  "jPanelCardPersonal");
    jPanelCardSpaceFilter.add(jPanelSpaceFilterTop, BorderLayout.NORTH);
    jPanelSpaceFilterTop.add(jLabelCTopSFilter, null);
    jPanelCardSpaceFilter.add(jPanelSpaceFilterCore, BorderLayout.CENTER);
    jPanelCardZoneNav.add(jPanelZoneNavTop, BorderLayout.NORTH);
    jPanelZoneNavTop.add(jLabelCTopZNav, null);
    jPanelCardZoneNav.add(jPanelZoneNavCore,  BorderLayout.CENTER);
    jPanelZoneNavCore.add(jPanelZoneGrid,  BorderLayout.CENTER);
    jPanelZoneNavCore.add(jPanelZoneGridControl,  BorderLayout.WEST);
    jPanelZoneNavCore.add(jPanelZoneGridControl2,  BorderLayout.EAST);
    jPanelZoneGridControl.add(jButtonZoneGridRight,                            new GridBagConstraints(1, 5, 1, 1, 0.5, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelZoneGridControl.add(jButtonZoneGridUp,                          new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelZoneGridControl.add(jButtonZoneGridLeft,                            new GridBagConstraints(0, 5, 1, 1, 0.5, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelZoneGridControl.add(jButtonZoneGridDown,                          new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelDiscovery.add(jLabelDiscoveryLooking,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 1));
    jPanelDiscovery.add(jLabelDiscoveryResponse,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 1));
    jPanelDiscovery.add(jProgressBarDiscovery,     new GridBagConstraints(0, 1, 2, 1, 1.0, 0.5
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanelEast.add(jLabelEastSpace, null);
    jPanelEast.add(jPanelEastSpace, null);
    jPanelEast.add(jLabelEastZone, null);
    jPanelEast.add(jPanelEastZone, null);
    jPanelEastZone.add(SceneTree, null);
    jPanelOnlinePeers.add(jScrollPaneOP,  BorderLayout.CENTER);
    jScrollPaneOP.getViewport().add(jListOnlinePeers, null);
    jPanelOnlinePeerGroups.add(jScrollPaneOP2,  BorderLayout.CENTER);
    jScrollPaneOP2.getViewport().add(jListOnlinePeerGroups, null);
    jPanelZoneBuilderCore.add(jPanelBBC,  BorderLayout.NORTH);
    jPanelWestServices.add(jButtonServWeb, null);
    buttonGroupVP.add(jToggleButtonSpaceNavVPP);
    buttonGroupVP.add(jToggleButtonSpaceNavVPO);
    jPanelZoneBuilderCore.add(jPanelZoneBuildSet,  BorderLayout.CENTER);
    jPanelBottomNav.add(jButtonSoutPanelUpDown, null);
    jPanelBottomNav.add(jButtonNavSPSpace, null);
    jPanelBottomNav.add(jButtonNavSPFilterPanel, null);
    jPanelBottomNav.add(jButtonNavSPZone, null);
    jPanelBottomNav.add(jButtonNavSPZoneBuilder, null);
    jPanelBottomNav.add(jButtonNavSPProjPanel, null);
    buttonGroupNav.add(jButtonNavSPSpace);
    buttonGroupNav.add(jButtonNavSPFilterPanel);
    buttonGroupNav.add(jButtonNavSPZone);
    buttonGroupNav.add(jButtonNavSPZoneBuilder);
    buttonGroupNav.add(jButtonNavSPProjPanel);
    jPanelEastSpace.add(jLabelEastSpaceCoords, null);
    jPanelEastSpace.add(jLabelEastSpaceHeadingYaw, null);
    jPanelEastSpace.add(jLabelEastSpaceHeadingPitch, null);
    jPanelEastSpace.add(jLabelEastSpaceHeadingRoll, null);
    jPanelEastSpace.add(jLabelEastSpaceAltitude, null);
    jPanelEastSpace.add(jLabelEastSpaceSpeed, null);
    jPanelEastSpace.add(jPanelEastSpaceMap, null);
    jPanelEastSpace.add(jLabelEastSpaceLevel, null);
    jPanelEastSpace.add(jLabelEastSpaceDom, null);
    jPanel6.add(jPanelSpaceNavAltitude, BorderLayout.CENTER);
    jPanelFilterZone.add(jLabel25, null);
    jPanelFilterZone.add(jCheckBoxFilterAVRin, null);
    jPanelFilterZone.add(jCheckBoxFilterAVRout, null);
    jPanelFilterZone.add(jCheckBoxFilterMyZone, null);
    jPanelSpaceFilterCore.add(jPanelFilterZoneListing, null);
    jPanelFilterZoneListing.add(jPanel1,  BorderLayout.SOUTH);
    jPanelFilterZoneListing.add(jScrollPane2, BorderLayout.CENTER);
    jPanelFilterZoneListing.add(jLabel26, BorderLayout.NORTH);
    jScrollPane2.getViewport().add(jTableActiveZones, null);
    jPanelSpaceFilterCore.add(jPanelFilterSpace, null);
    jPanelFilterSpace.add(jLabel22, null);
    jPanelFilterSpace.add(jCheckBoxFilterAmbience, null);
    buttonGroupSpaceFilter.add(jCheckBoxFilterAmbience);
    jPanelFilterSpace.add(jCheckBoxFilterAvatars, null);
    jPanelFilterSpace.add(jCheckBoxFilterLandmarks, null);
    jPanelSpaceFilterCore.add(jPanelFilterZone, null);
    jPanelSpaceFilterCore.add(jPanelFilterPreview, null);
    jPanelFilterPreview.add(jLabel24, BorderLayout.NORTH);
    jPanel70.add(jButtonSpaceNavCtrl_Home, null);
    jPanel70.add(jButtonSpaceNavCtrl_Stop, null);
    jPanel70.add(jButtonSpaceNavCtrl_Align, null);
    jPanel70.add(jButtonSpaceNavCtrl_North, null);
    jPanel70.add(jButtonSpaceNavCtrl_OrigoNorth, null);
    jPanel70.add(jButtonSpaceNavCtrl_OrigoTop, null);
    jPanelSpaceNavControl.add(jPanel70, null);
    jPanelSpaceNavControl.add(jPanelSpaceNavCtrl_SpeedDir, null);
    jPanelSpaceNavCtrl_SpeedDir.add(jPanelSpaceNavCtrl_Dir, BorderLayout.CENTER);
    jPanelSpaceNavCtrl_SpeedDir.add(jSliderSpeed,  BorderLayout.WEST);
    jPanelZoneGridControl.add(jTextFieldZoneGridCoord,  new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanelZoneGridControl.add(jLabelZoneNavControl,   new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0), 0, 0));
    jPanelZoneGridControl.add(jLabelEastZoneGridMouseCoords,  new GridBagConstraints(0, 8, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(8, 0, 0, 0), 0, 0));
    jPanelZoneGridControl.add(jLabel17,  new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanelZoneGridControl.add(jButtonGoCoord,      new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 8), 0, 0));
    jPanelFilterPreview.add(jTabbedPaneActivity, BorderLayout.CENTER);
    jTabbedPaneActivity.add(jPanelSpaceActivityTraffic,  "Traffic");
    jTabbedPaneActivity.add(jPanelSpaceActivityRepository,  "Repository");
    jPanelSpaceActivityTraffic.add(jLabel16, null);
    jPanelSpaceActivityTraffic.add(jProgressBarActivityNet, null);
    jPanelSpaceActivityTraffic.add(jLabel19, null);
    jPanelSpaceActivityTraffic.add(jProgressBarActivityZoneBuffer, null);
    jPanel1.add(jButton2, null);
    jPanel1.add(jToggleButton1, null);
    jPanel71.add(jButtonZoneGridNavCtrl_Home, null);
    jPanel71.add(jButtonZoneGridNavCtrl_Origo, null);
    jPanelZoneGridControl2.add(jLabelZoneNavControl2,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0), 0, 0));
    jPanelZoneGridControl2.add(jPanel71,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanelZoneGridControl2.add(jCheckBoxZoneGridAutoMove,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
    jPanelZoneGridControl2.add(jLabel27,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    jPanelZoneGridControl2.add(jSliderZoneGridStep,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
    jPanelZoneGridControl2.add(jLabel28,  new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    jPanelZoneGridControl2.add(jSliderZoneGridZoom, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 3, 3, 3), 0, 0));





    resetGUI();
    initSecondary();
  }

  /**
   * Secondary initialization procedures of GUI
   *
   * - start Log window
   *
   */
  private void initSecondary()
  {
    jPanelSpaceNavCompass.setPreferredSize(new Dimension(120, 140));
    //showLog();
    jPanelSpaceNavAltitude.initAltitude();
    jPanelSpaceNavHeading.initHeading();
    jPanelSpaceNavCompass.initCompass();
    //jPanelSpaceNavCtrl_Speed.initControl();
    jPanelSpaceNavCtrl_Dir.initControl();

    southPanelUp = true;
    showHideSouth();

    //startCyberspace();
    //jPanelZoneGrid.initZoneGrid(mySpace.myZoneWorld, mySpace.myZone);

    initSideThread();
  }

  /**
   * Load all app images
   * @return
   */
  private boolean loadImages()
  {
    myLog.addMessage(4,"Loading images...");
    try
    {
      vars.loadImages();
      for(int i=0; i<vars.IMG_ARRAY; i++)
        images[i] = new ImageIcon(terrapeer.gui.TerraPeerGUI.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[i]));
    }
    catch (Exception e)
    {
      myLog.addMessage(2,"Argh! Where are my images?");
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * TerraPeer GUI Menu
   */
  private void loadMenu()
  {
    jMenuPeer.setText("TeeraPeer");
    jMenuPersonal.setText("Personal");
    jMenuFeedback.setText("Feedback");
    jMenuCyberspace.setText("Cyberspace");
    jMenuServices.setText("Services");
    jMenuProject.setText("Project");
    jMenuView.setText("View");
    jMenuHelp.setText("Help");

    jMenuViewSouthPanel.setText("South Panel");
    jMenuServSearch.setText("Search");
    jMenuCommunication.setText("Communication");

    jMenuItemHelpAbout.setText("About TerraPeer");
    jMenuItemHelpAbout.addActionListener(new TerraPeerGUI_jMenuItemHelpAbout_ActionAdapter(this));
    jMenuItemPeerLogin.setText("Login");
    jMenuItemPeerLogin.addActionListener(new TerraPeerGUI_jMenuItemPeerLogin_actionAdapter(this));
    jMenuItemExit.setText("Exit");
    jMenuItemExit.addActionListener(new TerraPeerManagerUI_jMenuItemExit_ActionAdapter(this));
    jMenuItemComMsg.setText("Messenger");
    jMenuItemComMsg.addActionListener(new TerraPeerGUI_jMenuItemComMsg_actionAdapter(this));
    jMenuItemComChat.setText("Chat");
    jMenuItemComChat.addActionListener(new TerraPeerGUI_jMenuItemComChat_actionAdapter(this));
    jMenuItemFbSearch.setText("Search...");
    jMenuItemFbPC.setText("Personal Contacts");
    jMenuItemFbBC.setText("Business Contacts");
    jMenuItemFbMy.setText("My Feedback");
    jMenuItemFbMy.addActionListener(new TerraPeerGUI_jMenuItemFbMy_actionAdapter(this));
    jMenuItemPersIM.setText("My Information");
    jMenuItemPersIM.addActionListener(new TerraPeerGUI_jMenuItemPersIM_actionAdapter(this));
    jMenuItemPeerPrefs.setText("Preferences");
    jMenuItemPrefGeneral.setText("General");
    jMenuItemPrefGeneral.addActionListener(new TerraPeerGUI_jMenuItemPrefGeneral_actionAdapter(this));
    jMenuItemPrefNet.setText("Network");
    jMenuItemPrefNet.addActionListener(new TerraPeerGUI_jMenuItemPrefNet_actionAdapter(this));
    jMenuItemPrefSec.setText("Security");
    jMenuItemPrefSec.addActionListener(new TerraPeerGUI_jMenuItemPrefSec_actionAdapter(this));
    jMenuItemHelpTopics.setText("Help Topics");
    jMenuItemHelpTopics.addActionListener(new TerraPeerGUI_jMenuItemHelpTopics_actionAdapter(this));
    jMenuItemViewWorldSepWin.setText("World in separate window");
    jMenuItemViewWorldSepWin.addActionListener(new TerraPeerGUI_jMenuItemViewWorldSepWin_actionAdapter(this));
    jMenuItemViewZoneGridSepWin.setText("ZoneGrid in separate window");
    jCheckBoxMenuItemViewToolbarText.setText("Show Toolbar Text");
    jCheckBoxMenuItemViewToolbarText.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemViewToolbarText_actionAdapter(this));
    jCheckBoxMenuItemViewExpandAll.setText("Expand All Side Panel Groups");
    jCheckBoxMenuItemViewExpandAll.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemViewExpandAll_actionAdapter(this));
    jCheckBoxMenuItemShowSouthPanel.setSelected(true);
    jCheckBoxMenuItemShowSouthPanel.setText("Show South Panel");
    jCheckBoxMenuItemShowSouthPanel.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemShowSouthPanel_actionAdapter(this));
    jMenuItemCyberStart.setText("Start Cyberspace Console");
    jMenuItemCyberStart.addActionListener(new TerraPeerGUI_jMenuItemCyberStart_actionAdapter(this));
    jMenuItemCyberGoHome.setText("Go to My Home");
    jMenuItemCyberGoHome.addActionListener(new TerraPeerGUI_jMenuItemCyberGoHome_actionAdapter(this));
    jMenuItemCyberZBuilder.setText("Zone Builder Settings");
    jMenuItemCyberZBuilder.addActionListener(new TerraPeerGUI_jMenuItemCyberZBuilder_actionAdapter(this));
    jMenuCyberFilters.setText("Filter Settings");
    jCheckBoxMenuItemCyberFilterBg.setSelected(true);
    jCheckBoxMenuItemCyberFilterBg.setText("Enable Background");
    jCheckBoxMenuItemCyberFilterBg.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemCyberFilterBg_actionAdapter(this));
    jCheckBoxMenuItemCyberFilterShowZDetail.setSelected(true);
    jCheckBoxMenuItemCyberFilterShowZDetail.setText("Show Zone Details");
    jCheckBoxMenuItemCyberFilterShowZDetail.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowZDetail_actionAdapter(this));
    jMenuItemCyberFilterSettings.setText("Edit Settings...");
    jMenuItemCyberFilterSettings.addActionListener(new TerraPeerGUI_jMenuItemCyberFilterSettings_actionAdapter(this));
    jCheckBoxMenuItemCyberFilterFog.setText("Enable Fogging");
    jCheckBoxMenuItemCyberFilterFog.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemCyberFilterFog_actionAdapter(this));
    jCheckBoxMenuItemCyberFilterShowLandmarks.setSelected(true);
    jCheckBoxMenuItemCyberFilterShowLandmarks.setText("Show Landmarks");
    jCheckBoxMenuItemCyberFilterShowLandmarks.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowLandmarks_actionAdapter(this));
    jCheckBoxMenuItemCyberFilterShowMeta.setText("Show Metadata");
    jCheckBoxMenuItemCyberFilterShowMeta.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowMeta_actionAdapter(this));
    jCheckBoxMenuItemCyberFilterShowGrid.setSelected(true);
    jCheckBoxMenuItemCyberFilterShowGrid.setText("Show Grid");
    jCheckBoxMenuItemCyberFilterShowGrid.addActionListener(new TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowGrid_actionAdapter(this));
    jMenuItemPeerDiscovery.setText("Send Discovery");
    jMenuItemPeerDiscovery.addActionListener(new TerraPeerGUI_jMenuItemPeerDiscovery_actionAdapter(this));
    jMenuItemViewOpenLog.setText("Open Log window");
    jMenuItemViewOpenLog.addActionListener(new TerraPeerGUI_jMenuItemViewOpenLog_actionAdapter(this));
    jMenuItemPersContacts.setText("My Contacts");
    jMenuItemPersContacts.addActionListener(new TerraPeerGUI_jMenuItemPersContacts_actionAdapter(this));
    jMenuItemProjManager.setText("Project Manager");
    jMenuItemProjManager.addActionListener(new TerraPeerGUI_jMenuItemProjManager_actionAdapter(this));
    jMenuItemObjRepository.setText("Object Repository");
    jMenuItemObjRepository.addActionListener(new TerraPeerGUI_jMenuItemObjRepository_actionAdapter(this));
    jMenuItemProjMembership.setText("Membership");
    jMenuItemProjMembership.addActionListener(new TerraPeerGUI_jMenuItemProjMembership_actionAdapter(this));
    jMenuItemHelpGoTerraCentral.setText("Go to TerraCentral Zone");
    jMenuItemHelpGoTerraCentral.addActionListener(new TerraPeerGUI_jMenuItemHelpGoTerraCentral_actionAdapter(this));
    jMenuItemViewSPProject.setText("Project");
    jMenuItemViewSPProject.addActionListener(new TerraPeerGUI_jMenuItemViewSPProject_actionAdapter(this));
    jMenuItemViewSPSpaceFilter.setText("Space Filter");
    jMenuItemViewSPSpaceFilter.addActionListener(new TerraPeerGUI_jMenuItemViewSPSpaceFilter_actionAdapter(this));
    jMenuItemViewSPSpaceNav.setText("Space Navigation");
    jMenuItemViewSPSpaceNav.addActionListener(new TerraPeerGUI_jMenuItemViewSPSpaceNav_actionAdapter(this));
    jMenuItemViewSPZoneBuilder.setText("Zone Builder");
    jMenuItemViewSPZoneBuilder.addActionListener(new TerraPeerGUI_jMenuItemViewSPZoneBuilder_actionAdapter(this));
    jMenuItemViewSPZoneNav.setText("Zone Navigation");
    jMenuItemViewSPZoneNav.addActionListener(new TerraPeerGUI_jMenuItemViewSPZoneNav_actionAdapter(this));
    jMenuItemServFileshare.setText("Filesharing");
    jMenuItemServSearchWeb.setText("Websites");
    jMenuItemServSearchPeerName.setText("Peer Name");
    jMenuItemServSearchPeerName.addActionListener(new TerraPeerGUI_jMenuItemServSearchPeerName_actionAdapter(this));

    //Peer
    jMenuItemPeerPrefs.add(jMenuItemPrefGeneral);
    jMenuItemPeerPrefs.add(jMenuItemPrefNet);
    jMenuItemPeerPrefs.add(jMenuItemPrefSec);
    jMenuPeer.add(jMenuItemPeerLogin);
    jMenuPeer.add(jMenuItemPeerDiscovery);
    jMenuPeer.add(jMenuItemObjRepository);
    jMenuPeer.add(jMenuItemPeerPrefs);
    jMenuPeer.add(jMenuItemExit);

    //Pers
    jMenuPersonal.add(jMenuItemPersIM);
    jMenuPersonal.add(jMenuItemPersContacts);
    jMenuPersonal.add(jMenuItemFbMy);

    //Space
    jMenuCyberFilters.add(jMenuItemCyberFilterSettings);
    jMenuCyberFilters.add(jCheckBoxMenuItemCyberFilterShowZDetail);
    jMenuCyberFilters.add(jCheckBoxMenuItemCyberFilterShowMeta);
    jMenuCyberFilters.add(jCheckBoxMenuItemCyberFilterShowLandmarks);
    jMenuCyberFilters.add(jCheckBoxMenuItemCyberFilterShowGrid);
    jMenuCyberFilters.add(jCheckBoxMenuItemCyberFilterBg);
    jMenuCyberFilters.add(jCheckBoxMenuItemCyberFilterFog);
    jMenuCyberspace.add(jMenuItemCyberStart);
    jMenuCyberspace.add(jMenuItemCyberGoHome);
    jMenuCyberspace.add(jMenuCyberFilters);
    jMenuCyberspace.add(jMenuItemCyberZBuilder);

    //Project
    jMenuProject.add(jMenuItemProjManager);
    jMenuProject.add(jMenuItemProjMembership);

    //Service
    jMenuFeedback.add(jMenuItemFbSearch);
    jMenuFeedback.add(jMenuItemFbPC);
    jMenuFeedback.add(jMenuItemFbBC);
    jMenuCommunication.add(jMenuItemComMsg);
    jMenuCommunication.add(jMenuItemComChat);
    jMenuServSearch.add(jMenuItemServSearchWeb);
    jMenuServSearch.add(jMenuItemServSearchPeerName);
    jMenuServices.add(jMenuServSearch);
    jMenuServices.add(jMenuCommunication);
    jMenuServices.add(jMenuItemServFileshare);
    jMenuServices.add(jMenuFeedback);

    //View
    jMenuViewSidePanels.add(jCheckBoxMenuItemShowToolbar);
    jMenuViewSidePanels.add(jCheckBoxMenuItemShowWestPanel);
    jMenuViewSidePanels.add(jCheckBoxMenuItemShowEastPanel);
    jMenuViewSidePanels.add(jCheckBoxMenuItemShowNavPanel);
    jMenuViewSouthPanel.add(jCheckBoxMenuItemShowSouthPanel);
    jMenuViewSouthPanel.add(jMenuItemViewSPProject);
    jMenuViewSouthPanel.add(jMenuItemViewSPSpaceFilter);
    jMenuViewSouthPanel.add(jMenuItemViewSPSpaceNav);
    jMenuViewSouthPanel.add(jMenuItemViewSPZoneBuilder);
    jMenuViewSouthPanel.add(jMenuItemViewSPZoneNav);
    jMenuView.add(jCheckBoxMenuItemViewToolbarText);
    jMenuView.add(jCheckBoxMenuItemViewExpandAll);
    jMenuView.add(jMenuViewSidePanels);
    jMenuView.add(jMenuViewSouthPanel);
    jMenuView.add(jMenuItemViewWorldSepWin);
    jMenuView.add(jMenuItemViewZoneGridSepWin);
    jMenuView.add(jMenuItemViewOpenLog);

    //Help
    jMenuHelp.add(jMenuItemHelpTopics);
    jMenuHelp.add(jMenuItemHelpGoTerraCentral);
    jMenuHelp.add(jMenuItemHelpAbout);

    //Bar
    jMenuBar1.add(jMenuPeer);
    jMenuBar1.add(jMenuPersonal);
    jMenuBar1.add(jMenuCyberspace);
    jMenuBar1.add(jMenuProject);
    jMenuBar1.add(jMenuServices);
    jMenuBar1.add(jMenuView);
    jMenuBar1.add(jMenuHelp);

    this.setJMenuBar(jMenuBar1);
  }

  private void initIcons()
  {
    jButtonTBHelp.setIcon(images[3]);
    jButtonTBLog.setIcon(images[4]);
    jButtonTBLogin.setIcon(images[5]);
    jButtonTBMyHome.setIcon(images[20]);
    jButtonTBProject.setIcon(images[21]);
    jButtonTBPreferences.setIcon(images[24]);
    jButtonTBMyFeedback.setIcon(images[25]);
    jButtonTBMyInformation.setIcon(images[26]);
    jButtonTBSecurity.setIcon(images[27]);
    jButtonTBMembers.setIcon(images[29]);
    jButtonTBVirtualWorld.setIcon(images[30]);
    jButtonTBMyContacts.setIcon(images[31]);

    jButtonZoneGridUp.setIcon(images[45]);
    jButtonZoneGridDown.setIcon(images[46]);
    jButtonZoneGridLeft.setIcon(images[47]);
    jButtonZoneGridRight.setIcon(images[48]);

    jButtonSpaceNavCtrl_Home.setIcon(images[61]);
    jButtonSpaceNavCtrl_Stop.setIcon(images[62]);
    jButtonSpaceNavCtrl_Align.setIcon(images[63]);
    jButtonSpaceNavCtrl_North.setIcon(images[64]);
    jButtonSpaceNavCtrl_OrigoNorth.setIcon(images[67]);
    jButtonSpaceNavCtrl_OrigoTop.setIcon(images[68]);
    jButtonZoneGridNavCtrl_Home.setIcon(images[61]);
    jButtonZoneGridNavCtrl_Origo.setIcon(images[68]);

    jLabelWestCyberspace.setIcon(images[8]);
    jLabelWestPersonal.setIcon(images[8]);
    jLabelWestProjects.setIcon(images[8]);
    jLabelWestServices.setIcon(images[8]);
    jLabelWestTerraPeer.setIcon(images[8]);
    jLabelEastNet.setIcon(images[8]);
    jLabelEastSpace.setIcon(images[8]);
    jLabelEastZone.setIcon(images[8]);

    jButtonSoutPanelUpDown.setIcon(images[10]);//9 if up

    /*
        jLabelCTopPIM.setIcon(images[9]);
        jLabelCTopProject.setIcon(images[9]);
        jLabelCTopSFilter.setIcon(images[9]);
        jLabelCTopSNav.setIcon(images[9]);
        jLabelCTopZB.setIcon(images[9]);
        jLabelCTopZNav.setIcon(images[9]);
    */

   jLabelBBB_sphere.setIcon(images[71]);
   jLabelBBB_box.setIcon(images[74]);
   jLabelBBB_pyr.setIcon(images[70]);
   jLabelBBB_cyl.setIcon(images[72]);
   jLabelBBB_landmark_a.setIcon(images[16]);
   jLabelBBB_landmark_b.setIcon(images[16]);
   jLabelBBB_landmark_c.setIcon(images[16]);
   jLabelBBB_house_a.setIcon(images[16]);
   jLabelBBB_house_b.setIcon(images[16]);
   jLabelBBB_house_c.setIcon(images[16]);
   jLabelBBS_chat.setIcon(images[87]);
   jLabelBBS_ftp.setIcon(images[111]);
   jLabelBBS_url.setIcon(images[112]);
   jLabelBBS_web.setIcon(images[114]);
   //jLabelBBS_messenger.setIcon(images[15]);
   jLabelBBC_fb.setIcon(images[17]);

   jLabelTitle.setIcon(images[11]);
  }

  private void resetGUI()
  {
    myLog.addMessage(4,"Resetting GUI");
    for (int i = 0; i < vars.MENU_COUNT; i++)
    {
      isMenuVisible[i] = true; //next call makes it invisible
      showHideButtons(i);
    }
  }

  /****************************************************************************
   start methods
   ****************************************************************************/

  public void exitTerraPeer()
  {
    myLog.addMessage(4,">> EXIT");
    System.exit(0);
  }

  /**
   * Calls startPeer()
   */
  public void startLogin()
  {
    updateStatus(vars.STATUS_line_app, ">> Login", 0, false);
    myLog.addMessage(4,">> Login");
    //showLogin();

    jButtonLoginStart.setEnabled(false);
    jButtonTBLogin.setEnabled(false);
    jMenuItemPeerLogin.setEnabled(false);

    startPeer();

  }

  public void startMyInformationManager()
  {
    updateStatus(vars.STATUS_line_app, ">> My Information", 0, false);
    myLog.addMessage(4,">> My Information");
    flipCard(vars.FLIP_PIM);
  }

  public void startGoToHomeZone()
  {
    myLog.addMessage(4,">> My Home Zone");
    manualSpaceControl(vars.SPACENAV_CTRL_HOME);
  }

  public void startGoToCentralZone()
  {
    myLog.addMessage(4,">> Central Zone (Help)");

  }

  public void startMyFeedback()
  {
    myLog.addMessage(4,"SENDING MESSAGE");//">> My Feedback");
    TERRA_NetPeer.messageMngr.sendMyZone();

    //misuse of button :)
  }

  /**
   * Starts the peer discovery
   * Calls TERRA_NetPeer.runDiscovery()
   */
  public void startDiscovery()
  {
    updateStatus(vars.STATUS_line_net, "Starting Peer Discovery", 0, false);
    myLog.addMessage(4,">> Discovery");
    if(connectionOK)
      try
      {
        TERRA_NetPeer.runDiscovery();
      }
      catch (InterruptedException ex)
      {
      }
  }

  public void startRepository()
  {
    myLog.addMessage(4,">> Repository");
    RepositoryFrame dlg = new RepositoryFrame();//mySpace.zoneBuilder.getBuilderRepository());
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    //dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  /**
   * Initializes and starts virtual space
   * Calls mySpace.initSpace()
   *  and  mySpace.startSpace()
   */
  public void startCyberspace()
  {
    updateStatus(vars.STATUS_line_app, ">> Cyberspace", 0, false);
    myLog.addMessage(4,">> Cyberspace");

    //VR
    flipSpaceNav();
    updateStatus(vars.STATUS_line_space, "Initializing Space", 0, false);
    mySpace.initSpace(SceneTree, jPanelSpaceNavGridView, jPanelSpaceNavAvatView, jPanelSpaceNavOrbitView, jPanelEastSpaceMap);
    updateStatus(vars.STATUS_line_space, "Starting Space", 0, false);
    mySpace.startSpace();

    //GUI zonegrid panel
    jPanelZoneGrid.initZoneGrid(mySpace.myZoneWorld, mySpace.myZone, true);
    jPanelSpaceNavCtrl_Dir.initControl2(mySpace.spaceCore.spaceNav);
    jButtonVirtualWorld.setEnabled(false);
    jButtonTBVirtualWorld.setEnabled(false);
    jMenuItemCyberStart.setEnabled(false);
    isCyberspaceRunning = true;
  }

  public void startUpdateCyberspace()
  {
    updateStatus(vars.STATUS_updateRep);
  }

  /**
   * Calls startConfigPeer()
   *  and  connectJXTA()
   */
  public void startPeer()
  {
    updateStatus(vars.STATUS_line_app, ">> Peer", 0, false);
    myLog.addMessage(4,">> Peer");
    if(!peerIsConfigured)
    {
      updateStatus(vars.STATUS_line_net, "Configuring Peer", 0, false);
      myLog.addMessage(2, "StartPeer(): "+vars.MSG_CONFIG_ERR);
      startConfigPeer();
    }
    if(peerIsConfigured)
    {
      updateStatus(vars.STATUS_line_net, "Connecting to JXTA Network", 0, false);
      connectJXTA();
      if(connectionOK)
        myLog.addMessage(4, "StartPeer(): "+vars.MSG_CONNECT_OK);
      else
        myLog.addMessage(2, "StartPeer(): "+vars.MSG_CONNECT_ERR);
    }
    else
      myLog.addMessage(2,"Cannot start. JXTA peer needs to be configured first!");
  }

  /**
   * Calls TERRA_NetPeer.configureJxta()
   */
  public void startConfigPeer()
  {
    myLog.addMessage(4,">> Configuring JXTA peer...");
    //create new Peer Core if not exist
    if(TERRA_NetPeer==null)
      TERRA_NetPeer = new PeerCore();
    //configure Peer
    peerIsConfigured = TERRA_NetPeer.configureJxta();
    //add event listener to update ZoneGrid when Zone repository is updated
/*
    TERRA_NetPeer.peerService.addMessageEventListener(new MessageEventListener()
                                        {
                                          public void myEventOccurred(MessageEvent evt)
                                          {
                                            // MyEvent was fired
                                            TERRA_NetPeer.messageMngr.readMessage(evt);
                                            updateStatus(vars.STATUS_updateRep);
                                          }
                                        }
                                        );*/
  }

  public void startFilterSettings()
  {

  }

  public void startZoneBuilderSettings()
  {

  }

  public void startProjManager()
  {

  }

  public void startProjMembers()
  {

  }

  public void startService(int serviceType)
  {

  }


  /****************************************************************************
   networking methods
   ****************************************************************************/

  /**
   * Connects to JXTA network
   * Calls TERRA_NetPeer.startJxta()
   * and   TERRA_NetPeer.run()
   */
  private void connectJXTA()
  {
    myLog.addMessage(4,"Connecting to JXTA network...");

    try
    {
      TERRA_NetPeer.startJxta();
      connectionOK = true;
    }
    catch (Exception ex)
    {
      connectionOK = false;
      myLog.addMessage(0, "connectJXTA(): "+vars.MSG_CONNECT_EX);
      ex.printStackTrace();
    }

    final SwingWorker worker = new SwingWorker()
    {
      public Object construct()
      {
        TERRA_NetPeer.run();
        return null;
      }
    };

    if(connectionOK)
      worker.start();
  }

  private void disconnectJXTA()
  {
    updateStatus(vars.STATUS_line_net, "Disconnecting JXTA", 0, false);
    myLog.addMessage(4,"Disconnecting JXTA...");

  }


  /****************************************************************************
   vui methods
   ****************************************************************************/

  private void initSideThread()
  {
    myLog.addMessage(4,"Running side-thread...");
    sideThread = new TerraSideThread();
    sideThread.start();
  }

  private void runSideThread()
  {
    sideThread.going = true;
  }

  private void haltSideThread()
  {
    //myLog.addMessage(4,"Halting side-thread...");
    sideThread.going = false;
  }

  /**
   * Side-thread running parallel to main application to update various
   * information real-time.
   * <p>Title: TerraPeer</p>
   * <p>Description: P2P 3D System</p>
   * <p>Copyright: Copyright (c) 2003</p>
   * <p>Company: </p>
   * @author Henrik Gehrmann
   * @version 1.0
   */
  private class TerraSideThread extends Thread
  {
    public boolean going = true;
    private boolean updatePing = false;

    public void TerraSideThread()
    {
    }
    public void run()
    {
      while(going)
      {
        updateStatus(vars.STATUS_line_space, "Updating Space Coords", 0, false);

        updateStatus(vars.STATUS_east_net);
        updateStatus(vars.STATUS_east_space);
        updateStatus(vars.STATUS_east_zone);
        updateStatus(vars.STATUS_nav_alt);
        updateStatus(vars.STATUS_nav_heading);
        updateStatus(vars.STATUS_progress, "", 0, false);
        updateStatus(vars.STATUS_updatePing, "", 0, updatePing);

        if(zonegridAutomoveOn)
          updateStatus(vars.STATUS_zonegrid);

        if(updatePing)
          updatePing = false;
        else
          updatePing = true;

        try
        {
          Thread.sleep(vars.SIDETHREAD_TIMER);
        }
        catch (InterruptedException ex)
        {
        }
      }
    }
  }

  /**
   * filterMeta
   *
   * @todo enable filtering on space view
   */
  private void filterMeta()
  {
    jCheckBoxMenuItemCyberFilterShowMeta.setState(true);
  }

  private void filterLandmark()
  {
    jCheckBoxMenuItemCyberFilterShowLandmarks.setState(true);

  }

  private void filterZoneDetail()
  {
    jCheckBoxMenuItemCyberFilterShowZDetail.setState(true);

  }

  private void filterGrid()
  {
    jCheckBoxMenuItemCyberFilterShowGrid.setState(true);

  }

  private void filterBg()
  {
    jCheckBoxMenuItemCyberFilterBg.setState(true);

  }

  private void filterFog()
  {
    jCheckBoxMenuItemCyberFilterFog.setState(true);

  }

  private void changeVP()
  {
    if(mySpace.isSpaceInitialized())
      if (!currentViewPointPersonal)
      {
        mySpace.changeVP();
        jToggleButtonSpaceNavVPP.setSelected(true);
        jToggleButtonSpaceNavVPO.setSelected(false);
        currentViewPointPersonal = true;
      }
      else
      {
        mySpace.changeVP();
        jToggleButtonSpaceNavVPP.setSelected(false);
        jToggleButtonSpaceNavVPO.setSelected(true);
        currentViewPointPersonal = false;
      }
  }

  private void changeVPGrid()
  {
    if(!currentViewPointGrid)
    {
      mySpace.changeVPGrid();
      currentViewPointGrid = true;
    }
    else
    {
      mySpace.changeVPGrid();
      currentViewPointGrid = false;
    }
  }

  /**
   * User asks to add a building block to the scene (Zone)
   * @param bbType int block-type (such as object or service)
   */
  private void zoneAddBuildingBlock(int bbType)
  {
    if(mySpace.isSpaceInitialized())
    {
      haltSideThread();
      mySpace.zoneBuilder.addBuildingBlock(bbType);
      runSideThread();
    }
  }

  /**
   * User asks to change the zoom
   * @param viewWin int window to be zoomed
   * @param rate int zoom-rate
   */
  private void viewChangeZoom(int viewWin, int rate)
  {
    if(mySpace.isSpaceInitialized())
      mySpace.spaceCore.spaceNav.changeZoom(viewWin, rate);
  }

  /**
   * manualSpaceControl
   *
   * @param controlType int
   * @todo connect 2d control to space view control; finish all manuals
   */
  private void manualSpaceControl(int controlType)
  {
    switch(controlType)
    {
      case vars.SPACENAV_CTRL_STOP:
        mySpace.spaceCore.spaceNav.stopAvatar();
        break;
      case vars.SPACENAV_CTRL_ALIGN:
        mySpace.spaceCore.spaceNav.alignAvatar();
        break;
      case vars.SPACENAV_CTRL_NORTH:
        mySpace.spaceCore.spaceNav.northAvatar();
        break;
      case vars.SPACENAV_CTRL_SPEED_FORWARD:
        mySpace.spaceCore.spaceNav.moveAvatarForward(0);
        break;
      case vars.SPACENAV_CTRL_SPEED_BACK:
        mySpace.spaceCore.spaceNav.moveAvatarBackward(0);
        break;
      case vars.SPACENAV_CTRL_SPEED_FORWARD_1:
        mySpace.spaceCore.spaceNav.moveAvatarForward(1);
        break;
      case vars.SPACENAV_CTRL_SPEED_BACK_1:
        mySpace.spaceCore.spaceNav.moveAvatarBackward(1);
        break;
      case vars.SPACENAV_CTRL_SPEED_FORWARD_2:
        mySpace.spaceCore.spaceNav.moveAvatarForward(2);
        break;
      case vars.SPACENAV_CTRL_SPEED_BACK_2:
        mySpace.spaceCore.spaceNav.moveAvatarBackward(2);
        break;
      case vars.SPACENAV_CTRL_SPEED_LEFT:

        break;
      case vars.SPACENAV_CTRL_SPEED_RIGHT:

        break;
      case vars.SPACENAV_CTRL_SPEED_UP:

        break;
      case vars.SPACENAV_CTRL_SPEED_DOWN:

        break;
      case vars.SPACENAV_CTRL_TURN_LEFT:
        mySpace.spaceCore.spaceNav.dirAvatarLeft();
        break;
      case vars.SPACENAV_CTRL_TURN_RIGHT:
        mySpace.spaceCore.spaceNav.dirAvatarRight();
        break;
      case vars.SPACENAV_CTRL_PITCH_LEFT:

        break;
      case vars.SPACENAV_CTRL_PITCH_RIGHT:

        break;
      case vars.SPACENAV_CTRL_PITCH_UP:

        break;
      case vars.SPACENAV_CTRL_PITCH_DOWN:

        break;
      case vars.SPACENAV_CTRL_DEFAULT:
        mySpace.spaceCore.spaceNav.moveAvatarToDefault();
        break;
      case vars.SPACENAV_CTRL_HOME:
        mySpace.spaceCore.spaceNav.moveAvatarToHome(mySpace.myZone.myGeometry);
        break;
      case vars.SPACENAV_CTRL_ORIGO_NORTH:
        mySpace.spaceCore.spaceNav.moveAvatarToOrigoNorth();
        break;
      case vars.SPACENAV_CTRL_ORIGO_TOP:
        mySpace.spaceCore.spaceNav.moveAvatarToOrigoTop();
        break;
      default:
        break;
    }

  }

  private void moveZoneGrid()
  {
    int x = 0;
    int y = 0;
    String t = jTextFieldZoneGridCoord.getText();
    StringTokenizer st = new StringTokenizer(t, ",");
    try
    {
      String a = st.nextToken();
      x = Integer.parseInt(a);
      a = st.nextToken();
      y = Integer.parseInt(a);
    }
    catch(Exception e)
    {
      myLog.addMessage(2, "The Grid Coordinates entered cannot be parsed! Check input (should be x,y).");
    }

    jPanelZoneGrid.moveGrid(x, y);
    jPanelZoneGrid.repaint();
    jTextFieldZoneGridCoord.setText( jPanelZoneGrid.getCurrZoneCoordsX() +","+ jPanelZoneGrid.getCurrZoneCoordsY() );
  }

  private void moveZoneGrid(int controlType)
  {
    jPanelZoneGrid.moveGrid(controlType);
    jTextFieldZoneGridCoord.setText( jPanelZoneGrid.getCurrZoneCoordsX() +","+ jPanelZoneGrid.getCurrZoneCoordsY() );
  }

  /****************************************************************************
   GUI show and flip methods
   ****************************************************************************/

  public void showLog()
  {
    myLog.addMessage(4,"-> Show Log");
    Dimension dlgSize = myLog.logWindow.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    myLog.logWindow.setLocation(0,0);//(frmSize.width - dlgSize.width) / 2, frmSize.height);
    myLog.logWindow.setModal(false);
    myLog.logWindow.pack();
    myLog.logWindow.show();
  }

  public void showLogin()
  {
    myLog.addMessage(4,"-> Show Login");
    DialogLogin dlgLogin = new DialogLogin();
    Dimension dlgSize = dlgLogin.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlgLogin.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                         (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlgLogin.setModal(true);
    dlgLogin.pack();
    dlgLogin.show();
  }

  public void showAbout()
  {
    myLog.addMessage(4,"-> Show AboutBox");
    AboutBox dlg = new AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  public void showHelp()
  {
    myLog.addMessage(4,"-> Show HelpBox");
    HelpBox dlg = new HelpBox();
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  public void showPrefs(int showPrefPage)
  {
    myLog.addMessage(4,"-> Show PrefsBox");
    DialogPrefs dlg = new DialogPrefs(showPrefPage);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  public void showMyContacts()
  {
    myLog.addMessage(4,"-> Show MyContacts");
    DialogContacs dlg = new DialogContacs();
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  public void showBrowser()
  {
    myLog.addMessage(4,">> Browser");
    BrowserFrame dlg = new BrowserFrame();
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    //dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  public void flipSpaceFilter()
  {
    myLog.addMessage(4,"-> Flip to Space View/Filter Settings");
    flipCard(vars.FLIP_SPACE_FILTER);

  }

  public void flipZoneNav()
  {
    myLog.addMessage(4,"-> Flip to Zone Nav");
    flipCard(vars.FLIP_ZONE_NAV);

  }

  public void flipZoneBuilder()
  {
    myLog.addMessage(4,"-> Flip to Zone Builder");
    flipCard(vars.FLIP_ZONE_BUILDER);

  }

  public void flipProject()
  {
    myLog.addMessage(4,"-> Flip to Project Manager");
    flipCard(vars.FLIP_PROJECT);

  }

  public void flipSpaceNav()
  {
    myLog.addMessage(4,"-> Flip to Space Nav");
    flipCard(vars.FLIP_SPACE_NAV);

  }

  public void updateStatus(int statusType)
  {
    updateStatus(statusType, "", 0, false);
  }

  /**
   * GUI help methods Sends request to update status labels and views. The
   * update on the particular widget happens either here (show a particular
   * message directly) or somewhere else (call sub method). Update information
   * is found from the place it is generated, except simple messages.
   *
   * @param statusType int The status widget/view to be updated:
   * STATUS_ALL all
   * _line_app 1st line in south-status-bar
   * _line_net 2nd line in south-status-bar
   * _line_space 3rd line in south-status-bar
   * _east_net network east-status-panel
   * _east_space space east-status-panel
   * _east_zone zone east-status-panel
   * _progress big progress bar on south-status-bar
   * _nav_alt
   * _nav_heading
   * @param msg String
   * @todo update all east panels
   */
  public void updateStatus(int statusType, String msg, int progress, boolean flag)
  {
    switch(statusType)
    {
      case vars.STATUS_ALL:
        for(int i=1; i<vars.STATUS_COUNT; i++)
          updateStatus(i);
        break;
      case vars.STATUS_line_app:
        if(msg!=null)
          statusBarApp.setText(msg);
        break;
      case vars.STATUS_line_net:
        if(msg!=null)
          statusBarNet.setText(msg);
        break;
      case vars.STATUS_line_space:
        if(msg!=null)
          statusBarWorld.setText(msg);
        break;
      case vars.STATUS_east_net:
        jLabelPeerName.setText(vars.JXTA_PEER_NAME); // + " [port " + vars.JXTA_HTTP_PORT + "]");
        jLabelPeerGroup.setText(vars.JXTA_PEER_GROUP);
        jLabelDiscoveryResponse.setBackground(vars.COLOR_WHITE);
        jLabelDiscoveryLooking.setBackground(vars.COLOR_WHITE);
        jListOnlinePeers.setListData(TERRA_NetPeer.currKnownPeers);
        jListOnlinePeerGroups.setListData(TERRA_NetPeer.currKnownPeerGroups);
        jProgressBarDiscovery.setValue(progress);
        break;
      case vars.STATUS_east_space:
        if(isCyberspaceRunning)
        {
          updateSpaceMap();
          String a, b, c;
          //a = ""+mySpace.spaceCore.spaceNav.getAvatarPostition().x;
          //b = ""+mySpace.spaceCore.spaceNav.getAvatarPostition().y;
          //c = ""+mySpace.spaceCore.spaceNav.getAvatarPostition().z;

          //cut digits
          //com.Ostermiller.util.SignificantFigures rup = new com.Ostermiller.util.SignificantFigures(a);
          a = "" + Math.round(mySpace.spaceCore.spaceNav.getAvatarPostition().x);
          b = "" + Math.round(mySpace.spaceCore.spaceNav.getAvatarPostition().y);
          c = "" + Math.round(mySpace.spaceCore.spaceNav.getAvatarPostition().z);

          jLabelEastSpaceCoords.setText("Coords: [" + a + "," + c + "]");
          jLabelEastSpaceAltitude.setText("Alt: [" + b + "]");

          a = "" + mySpace.spaceCore.spaceNav.getAvatarHeadingYaw();
          b = "" + mySpace.spaceCore.spaceNav.getAvatarHeadingPitch();
          c = "" + mySpace.spaceCore.spaceNav.getAvatarHeadingRoll();
          if (a.length() > 4)
            a = a.substring(0, 4);
          if (b.length() > 4)
            b = b.substring(0, 4);
          if (c.length() > 4)
            c = c.substring(0, 4);
          jLabelEastSpaceHeadingYaw.setText("Heading (Yaw): " + a);
          jLabelEastSpaceHeadingPitch.setText("Heading (Pitch): " + b);
          jLabelEastSpaceHeadingRoll.setText("Heading (Roll): " + c);

          a = "" + mySpace.spaceCore.spaceNav.getAvatarSpeed();
          if (a.length() > 4)
            a = a.substring(0, 4);
          jLabelEastSpaceSpeed.setText("Speed: [" + a + "]");

          jLabelEastSpaceLevel.setText("Level " + mySpace.spaceCore.spaceNav.getAvatarLevel());
          jLabelEastSpaceDom.setText("Domain: n/a");
        }
        break;
      case vars.STATUS_east_zone:
        if(isCyberspaceRunning)
        {
          if (mySpace.myZoneWorld.isCurrSelectedZone)
          {
            jLabelEastZoneID.setText("ID: " + mySpace.myZoneWorld.currSelectedZone.getZone_ID() + "");
            jLabelEastZoneName.setText("Name: " + mySpace.myZoneWorld.currSelectedZone.getZone_Name() + "");
            jLabelEastZoneCoords.setText("Coords: " + mySpace.myZoneWorld.currSelectedZone.myGeometry.getTwoPoint_X1()
                                         + ","
                                         + mySpace.myZoneWorld.currSelectedZone.myGeometry.getTwoPoint_Y1() + "");
            jLabelEastZoneSize.setText("Size: " + mySpace.myZoneWorld.currSelectedZone.myGeometry.getTwoPoint_W() + ","
                                       + mySpace.myZoneWorld.currSelectedZone.myGeometry.getTwoPoint_H() + "");
            jEditorPaneZoneDescr.setText(mySpace.myZoneWorld.currSelectedZone.getZone_Description());
            //SceneTree
          }
          else
          {
            jLabelEastZoneID.setText("ID: N/A");
            jLabelEastZoneName.setText("Name: N/A");
            jLabelEastZoneCoords.setText("Coords: N/A");
            jLabelEastZoneSize.setText("Size: N/A");
            jEditorPaneZoneDescr.setText("");
          }
        }
        break;
      case vars.STATUS_progress:
        //jProgressBarMain.setString("Updating...");
        jProgressBarMain.setValue(progress);
        break;
      case vars.STATUS_nav_alt:
        if(isCyberspaceRunning)
        {
          int alt = (int)mySpace.spaceCore.spaceNav.getAvatarPostition().y;
          jPanelSpaceNavAltitude.moveAltitude(alt);
          jPanelSpaceNavAltitude.repaint();
        }
        break;
      case vars.STATUS_nav_heading:
        if(isCyberspaceRunning)
        {
          double degree = mySpace.spaceCore.spaceNav.getAvatarHeadingYaw();
          jPanelSpaceNavHeading.moveHeading(degree);
          jPanelSpaceNavHeading.repaint();
          jPanelSpaceNavCompass.moveHeading(degree);
          jPanelSpaceNavCompass.repaint();
        }
        break;
      case vars.STATUS_zonegrid:
        if(isCyberspaceRunning)
        {
          jTextFieldZoneGridCoord.setText(jPanelZoneGrid.getCurrZoneCoordsX() + "," + jPanelZoneGrid.getCurrZoneCoordsY());
          jPanelZoneGrid.moveGrid((int)mySpace.spaceCore.spaceNav.getAvatarPostition().x,
                                  (int)mySpace.spaceCore.spaceNav.getAvatarPostition().z);
          jPanelZoneGrid.repaint();
        }
        break;
      case vars.STATUS_updateRep:
        haltSideThread();
        mySpace.updateSpace();
        jPanelZoneGrid.updateZoneGrid(mySpace.myZoneWorld, mySpace.myZone, isCyberspaceRunning);
        runSideThread();
        break;
      case vars.STATUS_updatePing:
        if(flag)
          jLabelUpdatePing.setBackground(Color.lightGray);
        else
          jLabelUpdatePing.setBackground(Color.yellow);
        break;
      default:
          break;
    }
  }

  private void updateSpaceMap()
  {
    //jPanelEastSpaceMap

  }

  /**
   * Flips the south-panel
   * @param cardno int
   * @todo all flaps
   */
  private void flipCard(int cardno)
  {
    switch(cardno)
    {
      case vars.FLIP_ZONE_NAV:
        cardLayoutSouth.show(jPanelSouth, "jPanelCardZoneNav");
        break;
      case vars.FLIP_ZONE_BUILDER:
        cardLayoutSouth.show(jPanelSouth, "jPanelCardZoneBuilder");
        break;
      case vars.FLIP_SPACE_NAV:
        cardLayoutSouth.show(jPanelSouth, "jPanelCardSpaceNav");
        break;
      case vars.FLIP_SPACE_FILTER:
        cardLayoutSouth.show(jPanelSouth, "jPanelCardSpaceFilter");
        break;
      case vars.FLIP_COM:
        break;
      case vars.FLIP_PIM:
        cardLayoutSouth.show(jPanelSouth, "jPanelCardPersonal");
        break;
      case vars.FLIP_FB:
        break;
      case vars.FLIP_PROJECT:
        cardLayoutSouth.show(jPanelSouth, "jPanelCardProjects");
        break;
      default:
        cardLayoutSouth.show(jPanelSouth, "jPanelCardSpaceNav");
        break;
    }
  }

  private void showHideSouth()
  {
    if(southPanelUp)
    {
      myLog.addMessage(4,"South Panel Up");
      southPanelUp = false;
      jPanelSouth.setVisible(false);//.setSize(jPanelSouth.getWidth(), 30);
      jButtonSoutPanelUpDown.setIcon(images[10]);
    }
    else
    {
      myLog.addMessage(4,"South Panel Down");
      southPanelUp = true;
      jPanelSouth.setVisible(true);//.setSize(jPanelSouth.getWidth(), 160);
      jButtonSoutPanelUpDown.setIcon(images[9]);
    }
    this.repaint();
  }

  private void showHideWest()
  {
    if(westPanelUp)
    {
      myLog.addMessage(4,"West Panel Up");
      westPanelUp = false;
      jPanelWest.setVisible(false);
    }
    else
    {
      myLog.addMessage(4,"West Panel Down");
      westPanelUp = true;
      jPanelWest.setVisible(true);
    }
    this.repaint();
  }

  private void showHideEast()
  {
    if(eastPanelUp)
    {
      myLog.addMessage(4,"East Panel Up");
      eastPanelUp = false;
      jPanelEast.setVisible(false);
    }
    else
    {
      myLog.addMessage(4,"East Panel Down");
      eastPanelUp = true;
      jPanelEast.setVisible(true);
    }
    this.repaint();
  }

  private void showHideToolbar()
  {
    if(jToolBar.isVisible())
    {
      jToolBar.setVisible(false);
    }
    else
    {
      jToolBar.setVisible(true);
    }
    this.repaint();
  }

  private void showHideNav()
  {
    if(navPanelUp)
    {
      myLog.addMessage(4,"Nav Panel Up");
      navPanelUp = false;
      jPanelBottomNav.setVisible(false);
    }
    else
    {
      myLog.addMessage(4,"Nav Panel Down");
      navPanelUp = true;
      jPanelBottomNav.setVisible(true);
    }
    this.repaint();
  }

  private void viewExpandContractAll()
  {
    for(int i=0; i<vars.MENU_COUNT; i++)
      showHideButtons(i);
    jCheckBoxMenuItemViewExpandAll.setState(isMenuVisible[0]);
  }

  private void showHideToolbarText()
  {
    if(isToolbarTextVisible)
    {
      isToolbarTextVisible = false;
      jButtonTBMyHome.setText("");
      jButtonTBProject.setText("");
      jButtonTBHelp.setText("");
      jButtonTBPreferences.setText("");
      jButtonTBMyFeedback.setText("");
      jButtonTBMyInformation.setText("");
      jButtonTBSecurity.setText("");
      jButtonTBLogin.setText("");
      jButtonTBMembers.setText("");
      jButtonTBLog.setText("");
      jButtonTBVirtualWorld.setText("");
      jButtonTBMyContacts.setText("");
    }
    else
    {
        isToolbarTextVisible = true;
      jButtonTBMyHome.setText("HomeZone");
      jButtonTBProject.setText("Project");
      jButtonTBHelp.setText("Help");
      jButtonTBPreferences.setText("Prefs");
      jButtonTBMyFeedback.setText("Feedback");
      jButtonTBMyInformation.setText("My Info");
      jButtonTBSecurity.setText("Security");
      jButtonTBLogin.setText("Login");
      jButtonTBMembers.setText("Members");
      jButtonTBLog.setText("Log");
      jButtonTBVirtualWorld.setText("Cyberspace");
      jButtonTBMyContacts.setText("Contacts");
    }
  }

  private void showHideButtons(int menu)
  {
    myLog.addMessage(4,"ShowHide Menu Click ["+menu+"] = "+isMenuVisible[menu]);

    int imageNr = 9;
    if(isMenuVisible[menu])
    {
      isMenuVisible[menu] = false;
      imageNr = 8;
    }
    else
    {
      isMenuVisible[menu] = true;
      imageNr = 9;
    }

    switch (menu)
    {
      case vars.MENU_west_access:
        jLabelWestTerraPeer.setIcon(images[imageNr]);
        jPanelWestAccess.setVisible(isMenuVisible[menu]);
        /*
        jButtonLogin.setVisible(isMenuVisible[menu]);
        jButtonStartPeer.setVisible(isMenuVisible[menu]);
        jButtonDiscovery.setVisible(isMenuVisible[menu]);
        jButtonConfigPeer.setVisible(isMenuVisible[menu]);
        jButtonSecuritySettings.setVisible(isMenuVisible[menu]);
        jButtonLog.setVisible(isMenuVisible[menu]);
            */
        break;
      case vars.MENU_west_personal:
        jLabelWestPersonal.setIcon(images[imageNr]);
        jPanelWestPIM.setVisible(isMenuVisible[menu]);
        /*
        jButtonMyContacts.setVisible(isMenuVisible[menu]);
        jButtonMyFeedback.setVisible(isMenuVisible[menu]);
        jButtonMyInformation.setVisible(isMenuVisible[menu]);
            */
        if(isMenuVisible[menu])
          cardLayoutSouth.show(jPanelSouth, "jPanelCardPersonal");
        break;
      case vars.MENU_west_projects:
        jLabelWestProjects.setIcon(images[imageNr]);
        jPanelWestProject.setVisible(isMenuVisible[menu]);
        /*
        jButtonProjControl.setVisible(isMenuVisible[menu]);
        jButtonMembership.setVisible(isMenuVisible[menu]);
        jButtonObjRepository.setVisible(isMenuVisible[menu]);
            */
        if(isMenuVisible[menu])
          cardLayoutSouth.show(jPanelSouth, "jPanelCardProjects");
        break;
      case vars.MENU_west_cyberspace:
        jLabelWestCyberspace.setIcon(images[imageNr]);
        jPanelWestCyberspace.setVisible(isMenuVisible[menu]);
        /*
        jButtonVirtualWorld.setVisible(isMenuVisible[menu]);
        jButtonMyHome.setVisible(isMenuVisible[menu]);
        jButtonZoneBuilder.setVisible(isMenuVisible[menu]);
        jButtonFilterSettings.setVisible(isMenuVisible[menu]);
            */
        if(isMenuVisible[menu])
          cardLayoutSouth.show(jPanelSouth, "jPanelCardSpaceNav");
        break;
      case vars.MENU_west_services:
        jLabelWestServices.setIcon(images[imageNr]);
        jPanelWestServices.setVisible(isMenuVisible[menu]);
        break;
      case vars.MENU_east_net:
        jLabelEastNet.setIcon(images[imageNr]);
        jPanelEastNet.setVisible(isMenuVisible[menu]);
        //jListOnlinePeers.setVisible(isMenuVisible[menu]);
        break;
      case vars.MENU_east_space:
        jLabelEastSpace.setIcon(images[imageNr]);
        jPanelEastSpace.setVisible(isMenuVisible[menu]);
        break;
      case vars.MENU_east_zone:
        jLabelEastZone.setIcon(images[imageNr]);
        jPanelEastZone.setVisible(isMenuVisible[menu]);
        break;
      default:
        break;
    }
  }

  private void enlightenButtons(int menu)
  {
    Color tempColor = vars.COLOR_GRAY2;
    if (isMenuMouseOver[menu])
    {
      isMenuMouseOver[menu] = false;
      tempColor = vars.COLOR_GRAY2;
    }
    else
    {
      isMenuMouseOver[menu] = true;
      tempColor = vars.COLOR_GRAY3;
    }

    switch (menu)
    {
      case vars.MENU_west_access:
        jLabelWestTerraPeer.setBackground(tempColor);
        break;
      case vars.MENU_west_personal:
        jLabelWestPersonal.setBackground(tempColor);
        break;
      case vars.MENU_west_projects:
        jLabelWestProjects.setBackground(tempColor);
        break;
      case vars.MENU_west_cyberspace:
        jLabelWestCyberspace.setBackground(tempColor);
        break;
      case vars.MENU_west_services:
        jLabelWestServices.setBackground(tempColor);
        break;
      case vars.MENU_east_net:
        jLabelEastNet.setBackground(tempColor);
        break;
      case vars.MENU_east_space:
        jLabelEastSpace.setBackground(tempColor);
        break;
      case vars.MENU_east_zone:
        jLabelEastZone.setBackground(tempColor);
        break;
      default:
        break;
    }
  }

  private void enlightenBlocks(int block)
  {
    Color tempColor = vars.COLOR_LBLUE;
    if (isBlockMouseOver[block])
    {
      isBlockMouseOver[block] = false;
      tempColor = vars.COLOR_GRAY3;
    }
    else
    {
      isBlockMouseOver[block] = true;
      tempColor = vars.COLOR_LBLUE;
    }

    switch(block)
    {
      case vars.BBTYPE_BOX:
        jLabelBBB_box.setBackground(tempColor);
        break;
      case vars.BBTYPE_LANDMARK_A:
        jLabelBBB_landmark_a.setBackground(tempColor);
        break;
      case vars.BBTYPE_LANDMARK_B:
        jLabelBBB_landmark_b.setBackground(tempColor);
        break;
      case vars.BBTYPE_LANDMARK_C:
        jLabelBBB_landmark_c.setBackground(tempColor);
        break;
      case vars.BBTYPE_PYRAMID:
        jLabelBBB_pyr.setBackground(tempColor);
        break;
      case vars.BBTYPE_CYLINDER:
        jLabelBBB_cyl.setBackground(tempColor);
        break;
      case vars.BBTYPE_SPHERE:
        jLabelBBB_sphere.setBackground(tempColor);
        break;
      case vars.BBTYPE_HOUSE_A:
        jLabelBBB_house_a.setBackground(tempColor);
        break;
      case vars.BBTYPE_HOUSE_B:
        jLabelBBB_house_b.setBackground(tempColor);
        break;
      case vars.BBTYPE_HOUSE_C:
        jLabelBBB_house_c.setBackground(tempColor);
        break;
      case vars.SERVICE_HTTP:
        jLabelBBS_web.setBackground(tempColor);
        break;
      case vars.SERVICE_FTP:
        jLabelBBS_ftp.setBackground(tempColor);
        break;
      case vars.SERVICE_URL:
        jLabelBBS_url.setBackground(tempColor);
        break;
      case vars.SERVICE_COM_CHAT:
        jLabelBBS_chat.setBackground(tempColor);
        break;
      case vars.SERVICE_COM_MESSENGER:
        //jLabelBBS_messenger.setBackground(tempColor);
        break;
      case vars.CONNECTOR_FEEDBACK:
        jLabelBBC_fb.setBackground(tempColor);
        break;
    }
  }

  private void viewWorldSepWin()
  {
    myLog.addMessage(4,"-> Show World");
    FrameWorld dlg = new FrameWorld();
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    //dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  private void viewZoneGridSepWin()
  {
    myLog.addMessage(4,"-> Show ZoneGrid");
    FrameZoneGrid dlg = new FrameZoneGrid();
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    //dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  /****************************************************************************
   GUI action methods
   ****************************************************************************/

  /**
   * Overridden so we can exit when window is closed
   * @param e
   */
  protected void processWindowEvent(WindowEvent e)
  {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING)
    {
      exitTerraPeer();
    }
  }

  /**
   * File | Exit action performed
   * @param e
   */
  public void jMenuItemExit_actionPerformed(ActionEvent e)
  {
    exitTerraPeer();
  }

  /**
   * Help | About action performed
   * @param e
   */
  public void jMenuItemHelpAbout_actionPerformed(ActionEvent e)
  {
    showAbout();
  }

  void jLabelWestTerraPeer_mouseClicked(MouseEvent e)
  {
    showHideButtons(vars.MENU_west_access);
  }

  void jLabelWestPersonal_mouseClicked(MouseEvent e)
  {
    showHideButtons(vars.MENU_west_personal);
  }

  void jLabelWestProjects_mouseClicked(MouseEvent e)
  {
    showHideButtons(vars.MENU_west_projects);
  }

  void jLabelWestCyberspace_mouseClicked(MouseEvent e)
  {
    showHideButtons(vars.MENU_west_cyberspace);
  }

  void jLabelWestServices_mouseClicked(MouseEvent e)
  {
    showHideButtons(vars.MENU_west_services);
  }

  void jLabelWestTerraPeer_mouseEntered(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_access);
  }

  void jLabelWestTerraPeer_mouseExited(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_access);
  }

  void jLabelWestPersonal_mouseEntered(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_personal);
  }

  void jLabelWestPersonal_mouseExited(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_personal);
  }

  void jLabelWestProjects_mouseEntered(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_projects);
  }

  void jLabelWestProjects_mouseExited(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_projects);
  }

  void jLabelWestCyberspace_mouseEntered(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_cyberspace);
  }

  void jLabelWestCyberspace_mouseExited(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_cyberspace);
  }

  void jLabelWestServices_mouseEntered(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_services);
  }

  void jLabelWestServices_mouseExited(MouseEvent e)
  {
    enlightenButtons(vars.MENU_west_services);
  }


  void jButtonMyInformation_actionPerformed(ActionEvent e)
  {
    startMyInformationManager();
  }

  void jButtonLoginStart_actionPerformed(ActionEvent e)
  {
    startLogin();
  }


  void jButtonTBLogin_actionPerformed(ActionEvent e)
  {
    startLogin();
  }

  void jButtonTBSecurity_actionPerformed(ActionEvent e)
  {
    showPrefs(2);
  }

  void jButtonTBHelp_actionPerformed(ActionEvent e)
  {
    showHelp();
  }


  void jButtonConfigPeer_actionPerformed(ActionEvent e)
  {
    startConfigPeer();
  }

  void jButtonDiscovery_actionPerformed(ActionEvent e)
  {
    startDiscovery();
  }

  void jButtonFilterPanel_actionPerformed(ActionEvent e)
  {
    flipSpaceFilter();
  }

  void jButtonZoneBuilder_actionPerformed(ActionEvent e)
  {
    flipZoneBuilder();
  }

  void jButtonNavSPProjPanel_actionPerformed(ActionEvent e)
  {
    flipProject();
  }

  void jButtonVirtualWorld_actionPerformed(ActionEvent e)
  {
    startCyberspace();
  }

  void jButtonSecuritySettings_actionPerformed(ActionEvent e)
  {
    showPrefs(2);
  }

  void jButtonNavHome_actionPerformed(ActionEvent e)
  {
    startUpdateCyberspace();
  }

  void jButtonLog_actionPerformed(ActionEvent e)
  {
    showLog();
  }

  void jLabelEastNet_mouseClicked(MouseEvent e)
  {
    showHideButtons(vars.MENU_east_net);
  }

  void jLabelEastNet_mouseEntered(MouseEvent e)
  {
    enlightenButtons(vars.MENU_east_net);
  }

  void jLabelEastNet_mouseExited(MouseEvent e)
  {
    enlightenButtons(vars.MENU_east_net);
  }

  void jLabelEastZone_mouseClicked(MouseEvent e)
  {
    showHideButtons(vars.MENU_east_zone);
  }

  void jLabelEastZone_mouseEntered(MouseEvent e)
  {
    enlightenButtons(vars.MENU_east_zone);
  }

  void jLabelEastZone_mouseExited(MouseEvent e)
  {
    enlightenButtons(vars.MENU_east_zone);
  }

  void jLabelEastSpace_mouseClicked(MouseEvent e)
  {
    showHideButtons(vars.MENU_east_space);
  }

  void jLabelEastSpace_mouseEntered(MouseEvent e)
  {
    enlightenButtons(vars.MENU_east_space);
  }

  void jLabelEastSpace_mouseExited(MouseEvent e)
  {
    enlightenButtons(vars.MENU_east_space);
  }

  void jPanelPersonalTop_mouseClicked(MouseEvent e)
  {
    showHideSouth();
  }

  void jPanelProjectsTop_mouseClicked(MouseEvent e)
  {
    showHideSouth();
  }

  void jPanelZoneBuilderTop_mouseClicked(MouseEvent e)
  {
    showHideSouth();
  }

  void jPanelZoneNavTop_mouseClicked(MouseEvent e)
  {
    showHideSouth();
  }

  void jPanelSpaceFilterTop_mouseClicked(MouseEvent e)
  {
    showHideSouth();
  }

  void jPanelSpaceNavTop_mouseClicked(MouseEvent e)
  {
    showHideSouth();
  }

  void jMenuItemViewOpenLog_actionPerformed(ActionEvent e)
  {
    showLog();
  }

  void jMenuItemPeerLogin_actionPerformed(ActionEvent e)
  {
    startLogin();
  }

  void jMenuItemPeerDiscovery_actionPerformed(ActionEvent e)
  {
    startDiscovery();
  }

  void jMenuItemObjRepository_actionPerformed(ActionEvent e)
  {
    startRepository();
  }

  void jMenuItemPrefGeneral_actionPerformed(ActionEvent e)
  {
    showPrefs(0);
  }

  void jMenuItemPersIM_actionPerformed(ActionEvent e)
  {
    startMyInformationManager();
  }

  void jMenuItemCyberStart_actionPerformed(ActionEvent e)
  {
    startCyberspace();
  }

  void jMenuItemCyberGoHome_actionPerformed(ActionEvent e)
  {
    startGoToHomeZone();
  }

  void jMenuItemCyberFilterSettings_actionPerformed(ActionEvent e)
  {
    startFilterSettings();
  }

  void jCheckBoxMenuItemCyberFilterShowZDetail_actionPerformed(ActionEvent e)
  {
    filterZoneDetail();
  }

  void jCheckBoxMenuItemCyberFilterShowMeta_actionPerformed(ActionEvent e)
  {
    filterMeta();

  }

  void jCheckBoxMenuItemCyberFilterShowLandmarks_actionPerformed(ActionEvent e)
  {
    filterLandmark();

  }

  void jCheckBoxMenuItemCyberFilterShowGrid_actionPerformed(ActionEvent e)
  {
    filterGrid();
  }

  void jCheckBoxMenuItemCyberFilterBg_actionPerformed(ActionEvent e)
  {
    filterBg();
  }

  void jCheckBoxMenuItemCyberFilterFog_actionPerformed(ActionEvent e)
  {
    filterFog();
  }

  void jMenuItemCyberZBuilder_actionPerformed(ActionEvent e)
  {
    startZoneBuilderSettings();
  }

  void jMenuItemProjManager_actionPerformed(ActionEvent e)
  {
    startProjManager();
  }

  void jMenuItemProjMembership_actionPerformed(ActionEvent e)
  {
    startProjMembers();
  }

  void jMenuItemComMsg_actionPerformed(ActionEvent e)
  {
    startService(vars.SERVICE_COM_MESSENGER);
  }

  void jMenuItemComChat_actionPerformed(ActionEvent e)
  {
    startService(vars.SERVICE_COM_CHAT);
  }

  void jCheckBoxMenuItemViewExpandAll_actionPerformed(ActionEvent e)
  {
    viewExpandContractAll();
  }

  void jCheckBoxMenuItemShowSouthPanel_actionPerformed(ActionEvent e)
  {
    showHideSouth();
  }

  void jMenuItemViewWorldSepWin_actionPerformed(ActionEvent e)
  {
    viewWorldSepWin();
  }

  void jMenuItemHelpTopics_actionPerformed(ActionEvent e)
  {
    showHelp();
  }

  void jMenuItemHelpGoTerraCentral_actionPerformed(ActionEvent e)
  {
    startGoToCentralZone();
  }

  void jMenuItemPrefNet_actionPerformed(ActionEvent e)
  {
    showPrefs(1);
  }

  void jMenuItemPrefSec_actionPerformed(ActionEvent e)
  {
    showPrefs(2);
  }

  void jMenuItemPersContacts_actionPerformed(ActionEvent e)
  {
    showMyContacts();
  }

  void jButtonMyFeedback_actionPerformed(ActionEvent e)
  {
    startMyFeedback();
  }

  void jButtonMyContacts_actionPerformed(ActionEvent e)
  {
    showMyContacts();
  }

  void jButtonMembership_actionPerformed(ActionEvent e)
  {
    startProjMembers();
  }

  void jButtonObjRepository_actionPerformed(ActionEvent e)
  {
    startRepository();
  }

  void jMenuItemViewSPProject_actionPerformed(ActionEvent e)
  {
    flipProject();
  }

  void jMenuItemViewSPSpaceFilter_actionPerformed(ActionEvent e)
  {
    flipSpaceFilter();
  }

  void jMenuItemViewSPSpaceNav_actionPerformed(ActionEvent e)
  {
    flipSpaceNav();
  }

  void jMenuItemViewSPZoneBuilder_actionPerformed(ActionEvent e)
  {
    flipZoneBuilder();
  }

  void jMenuItemViewSPZoneNav_actionPerformed(ActionEvent e)
  {
    flipZoneNav();
  }

  void jButtonNavSpace_actionPerformed(ActionEvent e)
  {
    flipSpaceNav();
  }

  void jButtonNavZone_actionPerformed(ActionEvent e)
  {
    flipZoneNav();
  }

  void jButtonProjManager_actionPerformed(ActionEvent e)
  {
    startProjManager();
  }

  void jMenuItemServSearchPeerName_actionPerformed(ActionEvent e)
  {
    startService(vars.SERVICE_SEARCH_PEER_NAME);
  }

  void jMenuItemFbMy_actionPerformed(ActionEvent e)
  {
    startMyFeedback();
  }

  void jToggleButtonSpaceNavVPP_actionPerformed(ActionEvent e)
  {
    changeVP();
  }

  void jToggleButtonSpaceNavVPO_actionPerformed(ActionEvent e)
  {
    changeVP();
  }

  void jButtonZoneBuilder1_actionPerformed(ActionEvent e)
  {
    flipZoneBuilder();
  }

  void jButtonNavSpace1_actionPerformed(ActionEvent e)
  {
    flipSpaceNav();
  }

  void jButtonFilterPanel1_actionPerformed(ActionEvent e)
  {
    flipSpaceFilter();
  }

  void jButtonNavZone1_actionPerformed(ActionEvent e)
  {
    flipZoneNav();
  }

  void jCheckBoxMenuItemShowWestPanel_actionPerformed(ActionEvent e)
  {
    showHideWest();
  }

  void jCheckBoxMenuItemShowEastPanel_actionPerformed(ActionEvent e)
  {
    showHideEast();
  }

  void jCheckBoxMenuItemShowToolbar_actionPerformed(ActionEvent e)
  {
    showHideToolbar();
  }

  void jCheckBoxMenuItemShowNavPanel_actionPerformed(ActionEvent e)
  {
    showHideNav();
  }

  void jSliderGridViewZoom_stateChanged(ChangeEvent e)
  {
    viewChangeZoom(vars.SPACEVIEW_ORBIT_1, ((JSlider)e.getSource()).getValue());
  }

  void jLabelBBB_sphere_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_SPHERE);
  }

  void jLabelBBB_sphere_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_SPHERE);
  }

  void jLabelBBB_sphere_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_SPHERE);
  }

  void jLabelBBB_cyl_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_CYLINDER);
  }

  void jLabelBBB_cyl_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_CYLINDER);
  }

  void jLabelBBB_cyl_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_CYLINDER);
  }

  void jButtonSoutPanelUpDown_actionPerformed(ActionEvent e)
  {
    showHideSouth();
  }

  void jButtonGridViewOrigo_actionPerformed(ActionEvent e)
  {
    changeVPGrid();
  }

  void jButtonGridViewAvatar_actionPerformed(ActionEvent e)
  {
    changeVPGrid();
  }

  void jLabelBBB_box_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_BOX);
  }

  void jLabelBBB_box_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_BOX);
  }

  void jLabelBBB_box_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_BOX);
  }

  void jLabelBBB_pyr_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_PYRAMID);
  }

  void jLabelBBB_pyr_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_PYRAMID);
  }

  void jLabelBBB_pyr_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_PYRAMID);
  }

  void jLabelBBS_web_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.SERVICE_HTTP);
  }

  void jLabelBBS_web_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.SERVICE_HTTP);
  }

  void jLabelBBS_web_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.SERVICE_HTTP);
  }

  void jLabelBBS_url_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.SERVICE_URL);
  }

  void jLabelBBS_url_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.SERVICE_URL);
  }

  void jLabelBBS_url_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.SERVICE_URL);
  }

  void jLabelBBS_ftp_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.SERVICE_FTP);
  }

  void jLabelBBS_ftp_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.SERVICE_FTP);
  }

  void jLabelBBS_ftp_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.SERVICE_FTP);
  }

  void jButtonSpaceNavCtrl_Stop_actionPerformed(ActionEvent e)
  {
    manualSpaceControl(vars.SPACENAV_CTRL_STOP);
  }

  void jLabelBBS_chat_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.SERVICE_COM_CHAT);
  }

  void jLabelBBS_chat_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.SERVICE_COM_CHAT);
  }

  void jLabelBBS_chat_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.SERVICE_COM_CHAT);
  }

  void jLabelBBB_landmark_a_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_LANDMARK_A);
  }

  void jLabelBBB_landmark_a_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_LANDMARK_A);
  }

  void jLabelBBB_landmark_a_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_LANDMARK_A);
  }

  void jLabelBBB_landmark_b_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_LANDMARK_B);
  }

  void jLabelBBB_landmark_b_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_LANDMARK_B);
  }

  void jLabelBBB_landmark_b_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_LANDMARK_B);
  }

  void jLabelBBB_landmark_c_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_LANDMARK_C);
  }

  void jLabelBBB_landmark_c_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_LANDMARK_C);
  }

  void jLabelBBB_landmark_c_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_LANDMARK_C);
  }

  void jLabelBBB_house_a_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_HOUSE_A);
  }

  void jLabelBBB_house_a_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_HOUSE_A);
  }

  void jLabelBBB_house_a_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_HOUSE_A);
  }

  void jLabelBBB_house_b_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_HOUSE_B);
  }

  void jLabelBBB_house_b_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_HOUSE_B);
  }

  void jLabelBBB_house_b_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_HOUSE_B);
  }

  void jLabelBBB_house_c_mouseClicked(MouseEvent e)
  {
    zoneAddBuildingBlock(vars.BBTYPE_HOUSE_C);
  }

  void jLabelBBB_house_c_mouseEntered(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_HOUSE_C);
  }

  void jLabelBBB_house_c_mouseExited(MouseEvent e)
  {
    enlightenBlocks(vars.BBTYPE_HOUSE_C);
  }

  void jButtonSpaceNavCtrl_Home_actionPerformed(ActionEvent e)
  {
    manualSpaceControl(vars.SPACENAV_CTRL_DEFAULT);
  }

  void jButtonZoneGridUp_actionPerformed(ActionEvent e)
  {
    //
    moveZoneGrid(vars.NAV_UP);
  }

  void jButtonZoneGridDown_actionPerformed(ActionEvent e)
  {
    moveZoneGrid(vars.NAV_DOWN);
  }

  void jButtonZoneGridLeft_actionPerformed(ActionEvent e)
  {
    moveZoneGrid(vars.NAV_LEFT);
  }

  void jButtonZoneGridRight_actionPerformed(ActionEvent e)
  {
    moveZoneGrid(vars.NAV_RIGHT);
  }

  void jButtonTBMyHome_actionPerformed(ActionEvent e)
  {
    startGoToHomeZone();
  }

  void jButtonTBMyInformation_actionPerformed(ActionEvent e)
  {
    startMyInformationManager();
  }

  void jButtonTBMyFeedback_actionPerformed(ActionEvent e)
  {
    startMyFeedback();
  }

  void jButtonTBMyContacts_actionPerformed(ActionEvent e)
  {
    showMyContacts();
  }

  void jButtonTBProject_actionPerformed(ActionEvent e)
  {
    startProjManager();
  }

  void jButtonTBMembers_actionPerformed(ActionEvent e)
  {
    startProjMembers();
  }

  void jButtonTBVirtualWorld_actionPerformed(ActionEvent e)
  {
    startCyberspace();
  }

  void jButtonTBLog_actionPerformed(ActionEvent e)
  {
    showLog();
  }

  void jButtonTBPreferences_actionPerformed(ActionEvent e)
  {
    showPrefs(0);
  }

  void jButtonGoCoord_actionPerformed(ActionEvent e)
  {
    moveZoneGrid();
  }

  void jCheckBoxMenuItemViewToolbarText_actionPerformed(ActionEvent e)
  {
    showHideToolbarText();
  }

  void jSliderSpeed_stateChanged(ChangeEvent e)
  {
    if (jSliderSpeed.getValue() >= 9)
      manualSpaceControl(vars.SPACENAV_CTRL_SPEED_FORWARD_2);
    if (jSliderSpeed.getValue() <= 1)
      manualSpaceControl(vars.SPACENAV_CTRL_SPEED_BACK_2);

    if (jSliderSpeed.getValue() >= 8)
      manualSpaceControl(vars.SPACENAV_CTRL_SPEED_FORWARD_1);
    if (jSliderSpeed.getValue() <= 2)
      manualSpaceControl(vars.SPACENAV_CTRL_SPEED_BACK_1);

    if (jSliderSpeed.getValue() > 5)
      manualSpaceControl(vars.SPACENAV_CTRL_SPEED_FORWARD);
    else if (jSliderSpeed.getValue() == 5)
      manualSpaceControl(vars.SPACENAV_CTRL_STOP);
    else if (jSliderSpeed.getValue() < 5)
      manualSpaceControl(vars.SPACENAV_CTRL_SPEED_BACK);
  }

  void jButtonSpaceNavCtrl_Align_actionPerformed(ActionEvent e)
  {
    manualSpaceControl(vars.SPACENAV_CTRL_ALIGN);
  }

  void jButtonSpaceNavCtrl_North_actionPerformed(ActionEvent e)
  {
    manualSpaceControl(vars.SPACENAV_CTRL_NORTH);
  }

  void jButtonSpaceNavCtrl_OrigoNorth_actionPerformed(ActionEvent e)
  {
    manualSpaceControl(vars.SPACENAV_CTRL_ORIGO_NORTH);
  }

  void jButtonSpaceNavCtrl_OrigoTop_actionPerformed(ActionEvent e)
  {
    manualSpaceControl(vars.SPACENAV_CTRL_ORIGO_TOP);
  }

  void jButtonSpaceGridNavCtrl_Origo_actionPerformed(ActionEvent e)
  {
    jPanelZoneGrid.moveGridOrigo();
    jPanelZoneGrid.repaint();
  }

  void jButtonSpaceGridNavCtrl_Home_actionPerformed(ActionEvent e)
  {
    jPanelZoneGrid.moveGridHome();
    jPanelZoneGrid.repaint();
  }

  void jSliderZoneGridStep_stateChanged(ChangeEvent e)
  {
    jPanelZoneGrid.setCoord_step(jSliderZoneGridStep.getValue());
    jPanelZoneGrid.repaint();
  }

  void jSliderZoneGridZoom_stateChanged(ChangeEvent e)
  {
    jPanelZoneGrid.setGridspace_pixel(jSliderZoneGridZoom.getValue());
    jPanelZoneGrid.repaint();
  }

  void jCheckBoxZoneGridAutoMove_actionPerformed(ActionEvent e)
  {
    if (jCheckBoxZoneGridAutoMove.isSelected())
      zonegridAutomoveOn = true;
    else
      zonegridAutomoveOn = false;
  }

  void jButtonServWeb_actionPerformed(ActionEvent e)
  {
    showBrowser();
  }

  void jButtonZoneGrid_actionPerformed(ActionEvent e)
  {
    viewZoneGridSepWin();
  }



















}

class TerraPeerManagerUI_jMenuItemExit_ActionAdapter implements ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerManagerUI_jMenuItemExit_ActionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemExit_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemHelpAbout_ActionAdapter implements
    ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemHelpAbout_ActionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemHelpAbout_actionPerformed(e);
  }
}

class TerraPeerManagerUI_jButtonMyInformation_actionAdapter implements java.awt.
    event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerManagerUI_jButtonMyInformation_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonMyInformation_actionPerformed(e);
  }
}

class TerraPeerManagerUI_jButtonLoginStart_actionAdapter implements java.awt.event.
    ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerManagerUI_jButtonLoginStart_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonLoginStart_actionPerformed(e);
  }
}

class TerraPeerGUI_jLabelWestPersonal_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelWestPersonal_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelWestPersonal_mouseClicked(e);
  }

  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelWestPersonal_mouseEntered(e);
  }

  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelWestPersonal_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelWestTerraPeer_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelWestTerraPeer_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelWestTerraPeer_mouseClicked(e);
  }

  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelWestTerraPeer_mouseEntered(e);
  }

  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelWestTerraPeer_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelWestProjects_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelWestProjects_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelWestProjects_mouseClicked(e);
  }

  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelWestProjects_mouseEntered(e);
  }

  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelWestProjects_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelWestCyberspace_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelWestCyberspace_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelWestCyberspace_mouseClicked(e);
  }

  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelWestCyberspace_mouseEntered(e);
  }

  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelWestCyberspace_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelWestServices_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelWestServices_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelWestServices_mouseClicked(e);
  }

  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelWestServices_mouseEntered(e);
  }

  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelWestServices_mouseExited(e);
  }
}

class TerraPeerGUI_jButtonTBLogin_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBLogin_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBLogin_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBSecurity_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBSecurity_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBSecurity_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBHelp_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBHelp_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBHelp_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonConfigPeer_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonConfigPeer_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonConfigPeer_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonDiscovery_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonDiscovery_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonDiscovery_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonFilterPanel_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonFilterPanel_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonFilterPanel_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonZoneBuilder_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonZoneBuilder_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonZoneBuilder_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonNavSPProjPanel_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonNavSPProjPanel_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonNavSPProjPanel_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonVirtualWorld_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonVirtualWorld_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonVirtualWorld_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonSecuritySettings_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSecuritySettings_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSecuritySettings_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonNavHome_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonNavHome_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonNavHome_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonLog_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonLog_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonLog_actionPerformed(e);
  }
}

class TerraPeerGUI_jLabelEastNet_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelEastNet_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelEastNet_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelEastNet_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelEastNet_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelEastZone_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelEastZone_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelEastZone_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelEastZone_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelEastZone_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelEastSpace_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelEastSpace_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelEastSpace_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelEastSpace_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelEastSpace_mouseExited(e);
  }
}

class TerraPeerGUI_jPanelPersonalTop_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jPanelPersonalTop_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jPanelPersonalTop_mouseClicked(e);
  }
}

class TerraPeerGUI_jPanelProjectsTop_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jPanelProjectsTop_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jPanelProjectsTop_mouseClicked(e);
  }
}

class TerraPeerGUI_jPanelZoneBuilderTop_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jPanelZoneBuilderTop_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jPanelZoneBuilderTop_mouseClicked(e);
  }
}

class TerraPeerGUI_jPanelZoneNavTop_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jPanelZoneNavTop_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jPanelZoneNavTop_mouseClicked(e);
  }
}

class TerraPeerGUI_jPanelSpaceFilterTop_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jPanelSpaceFilterTop_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jPanelSpaceFilterTop_mouseClicked(e);
  }
}

class TerraPeerGUI_jPanelSpaceNavTop_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jPanelSpaceNavTop_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jPanelSpaceNavTop_mouseClicked(e);
  }
}

class TerraPeerGUI_jMenuItemViewOpenLog_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemViewOpenLog_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemViewOpenLog_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemPeerLogin_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemPeerLogin_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemPeerLogin_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemPeerDiscovery_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemPeerDiscovery_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemPeerDiscovery_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemObjRepository_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemObjRepository_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemObjRepository_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemPrefGeneral_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemPrefGeneral_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemPrefGeneral_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemPersIM_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemPersIM_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemPersIM_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemCyberStart_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemCyberStart_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemCyberStart_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemCyberGoHome_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemCyberGoHome_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemCyberGoHome_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemCyberFilterSettings_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemCyberFilterSettings_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemCyberFilterSettings_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowZDetail_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowZDetail_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemCyberFilterShowZDetail_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowMeta_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowMeta_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemCyberFilterShowMeta_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowLandmarks_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowLandmarks_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemCyberFilterShowLandmarks_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowGrid_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemCyberFilterShowGrid_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemCyberFilterShowGrid_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemCyberFilterBg_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemCyberFilterBg_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemCyberFilterBg_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemCyberFilterFog_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemCyberFilterFog_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemCyberFilterFog_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemCyberZBuilder_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemCyberZBuilder_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemCyberZBuilder_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemProjManager_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemProjManager_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemProjManager_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemProjMembership_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemProjMembership_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemProjMembership_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemComMsg_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemComMsg_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemComMsg_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemComChat_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemComChat_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemComChat_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemViewExpandAll_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemViewExpandAll_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemViewExpandAll_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemShowSouthPanel_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemShowSouthPanel_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemShowSouthPanel_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemViewWorldSepWin_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemViewWorldSepWin_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemViewWorldSepWin_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemHelpTopics_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemHelpTopics_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemHelpTopics_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemHelpGoTerraCentral_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemHelpGoTerraCentral_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemHelpGoTerraCentral_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemPrefNet_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemPrefNet_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemPrefNet_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemPrefSec_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemPrefSec_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemPrefSec_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemPersContacts_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemPersContacts_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemPersContacts_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonMyFeedback_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonMyFeedback_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonMyFeedback_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonMyContacts_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonMyContacts_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonMyContacts_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonMembership_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonMembership_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonMembership_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonObjRepository_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonObjRepository_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonObjRepository_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemViewSPProject_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemViewSPProject_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemViewSPProject_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemViewSPSpaceFilter_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemViewSPSpaceFilter_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemViewSPSpaceFilter_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemViewSPSpaceNav_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemViewSPSpaceNav_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemViewSPSpaceNav_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemViewSPZoneBuilder_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemViewSPZoneBuilder_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemViewSPZoneBuilder_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemViewSPZoneNav_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemViewSPZoneNav_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemViewSPZoneNav_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonNavSpace_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonNavSpace_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonNavSpace_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonNavZone_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonNavZone_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonNavZone_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonProjManager_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonProjManager_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonProjManager_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemServSearchPeerName_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemServSearchPeerName_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemServSearchPeerName_actionPerformed(e);
  }
}

class TerraPeerGUI_jMenuItemFbMy_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jMenuItemFbMy_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jMenuItemFbMy_actionPerformed(e);
  }
}

class TerraPeerGUI_jToggleButtonSpaceNavVPP_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jToggleButtonSpaceNavVPP_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jToggleButtonSpaceNavVPP_actionPerformed(e);
  }
}

class TerraPeerGUI_jToggleButtonSpaceNavVPO_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jToggleButtonSpaceNavVPO_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jToggleButtonSpaceNavVPO_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonNavSpace1_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonNavSpace1_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonNavSpace1_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonFilterPanel1_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonFilterPanel1_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonFilterPanel1_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonNavZone1_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonNavZone1_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonNavZone1_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemShowWestPanel_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemShowWestPanel_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemShowWestPanel_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemShowEastPanel_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemShowEastPanel_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemShowEastPanel_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemShowToolbar_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemShowToolbar_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemShowToolbar_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemShowNavPanel_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemShowNavPanel_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemShowNavPanel_actionPerformed(e);
  }
}

class TerraPeerGUI_jLabelBBB_sphere_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_sphere_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_sphere_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_sphere_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_sphere_mouseExited(e);
  }
}

class TerraPeerGUI_jSliderGridViewZoom_changeAdapter implements javax.swing.event.ChangeListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jSliderGridViewZoom_changeAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e)
  {
    adaptee.jSliderGridViewZoom_stateChanged(e);
  }
}

class TerraPeerGUI_jButtonSoutPanelUpDown_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSoutPanelUpDown_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSoutPanelUpDown_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonGridViewOrigo_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonGridViewOrigo_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonGridViewOrigo_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonGridViewAvatar_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonGridViewAvatar_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonGridViewAvatar_actionPerformed(e);
  }
}

class TerraPeerGUI_jLabelBBB_box_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_box_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_box_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_box_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_box_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBB_pyr_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_pyr_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_pyr_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_pyr_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_pyr_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBB_cyl_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_cyl_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_cyl_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_cyl_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_cyl_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBS_web_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBS_web_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBS_web_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBS_web_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBS_web_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBS_url_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBS_url_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBS_url_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBS_url_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBS_url_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBS_ftp_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBS_ftp_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBS_ftp_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBS_ftp_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBS_ftp_mouseExited(e);
  }
}

class TerraPeerGUI_jButtonSpaceNavCtrl_Stop_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSpaceNavCtrl_Stop_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSpaceNavCtrl_Stop_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonSpaceGridNavCtrl_Origo_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSpaceGridNavCtrl_Origo_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSpaceGridNavCtrl_Origo_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonSpaceGridNavCtrl_Home_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSpaceGridNavCtrl_Home_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSpaceGridNavCtrl_Home_actionPerformed(e);
  }
}

class TerraPeerGUI_jLabelBBS_chat_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBS_chat_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBS_chat_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBS_chat_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBS_chat_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBB_landmark_a_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_landmark_a_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_a_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_a_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_a_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBB_landmark_b_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_landmark_b_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_b_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_b_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_b_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBB_landmark_c_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_landmark_c_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_c_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_c_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_landmark_c_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBB_house_a_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_house_a_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_house_a_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_house_a_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_house_a_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBB_house_b_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_house_b_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_house_b_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_house_b_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_house_b_mouseExited(e);
  }
}

class TerraPeerGUI_jLabelBBB_house_c_mouseAdapter extends java.awt.event.MouseAdapter
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jLabelBBB_house_c_mouseAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jLabelBBB_house_c_mouseClicked(e);
  }
  public void mouseEntered(MouseEvent e)
  {
    adaptee.jLabelBBB_house_c_mouseEntered(e);
  }
  public void mouseExited(MouseEvent e)
  {
    adaptee.jLabelBBB_house_c_mouseExited(e);
  }
}

class TerraPeerGUI_jButtonSpaceNavCtrl_Home_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSpaceNavCtrl_Home_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSpaceNavCtrl_Home_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonZoneGridUp_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonZoneGridUp_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonZoneGridUp_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonZoneGridDown_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonZoneGridDown_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonZoneGridDown_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonZoneGridLeft_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonZoneGridLeft_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonZoneGridLeft_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonZoneGridRight_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonZoneGridRight_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonZoneGridRight_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBMyHome_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBMyHome_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBMyHome_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBMyInformation_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBMyInformation_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBMyInformation_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBMyFeedback_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBMyFeedback_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBMyFeedback_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBMyContacts_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBMyContacts_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBMyContacts_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBProject_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBProject_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBProject_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBMembers_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBMembers_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBMembers_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBVirtualWorld_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBVirtualWorld_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBVirtualWorld_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBLog_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBLog_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBLog_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonTBPreferences_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonTBPreferences_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonTBPreferences_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonGoCoord_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonGoCoord_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonGoCoord_actionPerformed(e);
  }
}

class TerraPeerGUI_jCheckBoxMenuItemViewToolbarText_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxMenuItemViewToolbarText_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxMenuItemViewToolbarText_actionPerformed(e);
  }
}

class TerraPeerGUI_jSliderSpeed_changeAdapter implements javax.swing.event.ChangeListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jSliderSpeed_changeAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e)
  {
    adaptee.jSliderSpeed_stateChanged(e);
  }
}

class TerraPeerGUI_jButtonSpaceNavCtrl_Align_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSpaceNavCtrl_Align_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSpaceNavCtrl_Align_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonSpaceNavCtrl_North_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSpaceNavCtrl_North_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSpaceNavCtrl_North_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonSpaceNavCtrl_OrigoNorth_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSpaceNavCtrl_OrigoNorth_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSpaceNavCtrl_OrigoNorth_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonSpaceNavCtrl_OrigoTop_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonSpaceNavCtrl_OrigoTop_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonSpaceNavCtrl_OrigoTop_actionPerformed(e);
  }
}

class TerraPeerGUI_jSliderZoneGridStep_changeAdapter implements javax.swing.event.ChangeListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jSliderZoneGridStep_changeAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e)
  {
    adaptee.jSliderZoneGridStep_stateChanged(e);
  }
}

class TerraPeerGUI_jSliderZoneGridZoom_changeAdapter implements javax.swing.event.ChangeListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jSliderZoneGridZoom_changeAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e)
  {
    adaptee.jSliderZoneGridZoom_stateChanged(e);
  }
}

class TerraPeerGUI_jCheckBoxZoneGridAutoMove_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jCheckBoxZoneGridAutoMove_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jCheckBoxZoneGridAutoMove_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonServWeb_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonServWeb_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonServWeb_actionPerformed(e);
  }
}

class TerraPeerGUI_jButtonZoneGrid_actionAdapter implements java.awt.event.ActionListener
{
  TerraPeerGUI adaptee;

  TerraPeerGUI_jButtonZoneGrid_actionAdapter(TerraPeerGUI adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonZoneGrid_actionPerformed(e);
  }
}

