package terrapeer;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.vecmath.*;

/**
 * Global Variables and Properties
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class vars
{
  //GUI properties
  public static String APP_HOME_PATH = "";
  public static String APP_HELP_PATH = "terrapeer"+File.separatorChar+"help";
  public static final String THEME_PATH = "terrapeer"+File.separatorChar+"objects"+File.separatorChar+"skins"+File.separatorChar+"aquathemepack.zip";
  public static final boolean LOG_TO_CONSOLE = true;
  public static final int LOGLIST_SIZE = 4000;

  //JXTA properties
  public static String JXTA_PEER_NAME = "TERRAPEER_DEFAULT_NAME";
  public static String JXTA_PEER_PW = "TERRAPEER_DEFAULT_PW";
  public static String JXTA_PEER_ID = "TERRAPEER_DEFAULT_ID";
  public static String JXTA_PEER_GROUP = "TERRAPEER";
  public static int JXTA_HTTP_PORT = 9700;
  public static int JXTA_TCP_PORT = 9701;
  public static final String JXTA_HOME_PATH = ".jxta";//"C:/liu/_g/projects/TerraPeer/TerraPeer/.jxta";//"C:/_dev/jxta/jxta2.1.1";
  public static final String PLATFORM_XML_FILE = "helpers"+File.separatorChar+"extPlatform.xml";
  public static final String PLATFORM_CFG_ADV_FILE = "PlatformConfig";
  public static final String TEST_ADV_FILE = "helpers"+File.separatorChar+"jxta_terra_B.xml";

  //Timing properties
  public static final int DISCOVERY_TIME_INTERVAL = 30000;//90000; //01:30 minutes
  public static final int SIDETHREAD_TIMER = 500; //milisecs

  //JXTA environment
  public static final String SERVICE_ADV_FILENAME = "TerraPeerService.adv";
  public static final String BIPIPE_FILENAME = "TerraPeerBiPipe.xml";
  public static final String BIPIPE_NAME = "TerraPeerBiPipe";
  public static final String BIPIPE_TAG = "TerraPeerTag_1";
  public static final String SERVICE_MODSPEC_ADV = "JXTA_SPEC:Terra-S";
  public static final String SERVICE_MODCLASS_ADV = "JXTA_CLASS:Terra-X";

  //pipe communication message types (name tags)
  //generic
  public static final int MSG_TYPE_X = 0;         // reserved
  public static final int MSG_TYPE_TXT = 1;       // text
  public static final int MSG_TYPE_XML = 2;       // xml
  public static final int MSG_TYPE_X3D = 3;       // vrml, x3d
  public static final int MSG_TYPE_BIN = 4;       // binary
  public static final int MSG_TYPE_GEO = 5;       // geometric data
  public static final int MSG_TYPE_From = 6;      // peer ID string
  public static final int MSG_TYPE_Zones = 7;     // zones XML
  public static final int MSG_TYPE_ZoneData = 8;  // data within a zone
  public static final int MSG_TYPE_Blocks = 9;    // blocks within a zone
  public static final int MSG_TYPE_Services = 10; // services within a zone
  //request
  public static final int MSG_TYPE_REQ_All = 11;              // yes|no
  public static final int MSG_TYPE_REQ_HaveZones = 12;        // ID[]
  public static final int MSG_TYPE_REQ_RequestZones = 13;     // ID[]
  public static final int MSG_TYPE_REQ_MaxZones = 14;         // int
  public static final int MSG_TYPE_REQ_LevelOfDetail = 15;    // int
  public static final int MSG_TYPE_REQ_Services = 16;         // ID[]
  public static final int MSG_TYPE_REQ_SubscribeService = 17; // ID[]
  public static final int MSG_TYPE_REQ_Listener = 18;         // int
  //reply
  public static final int MSG_TYPE_RPLY_SubscriptionOK = 19;  // yes|no
  public static final int MSG_TYPE_RPLY_NotificationOK = 20;  // yes|no
  //notification
  public static final int MSG_TYPE_HELO_Notification = 21; // int
  public static final int MSG_TYPE_HELO_TextMessage = 22;  // text
  public static final int MSG_TYPE_HELO_Status = 23;       // text

  public static final int MSG_TYPE_COUNT = 24;
  public static final String[] MESSAGE_TYPE = new String[vars.MSG_TYPE_COUNT];

  //Description Tags
  public static final String MESSAGE_NAMESPACE = "TerraPeer";

  public static final String MSG_NAME_X = "MSG-X";
  public static final String MSG_NAME_TXT = "MSG-TXT";
  public static final String MSG_NAME_XML = "MSG-XML";
  public static final String MSG_NAME_X3D = "MSG-X3D";
  public static final String MSG_NAME_BIN = "MSG-DATA";
  public static final String MSG_NAME_GEO = "MSG-XML:GEO";
  public static final String MSG_NAME_From = "MSG-TXT:FROM";
  public static final String MSG_NAME_Zones = "MSG-XML:ZONE";
  public static final String MSG_NAME_ZoneData = "MSG-XML:ZONEDATA";
  public static final String MSG_NAME_Blocks = "MSG-XML:BLOCKS";
  public static final String MSG_NAME_Services = "MSG-XML:SERVICES";
  public static final String MSG_NAME_REQ_All = "MSG-SPEC:All";
  public static final String MSG_NAME_REQ_HaveZones = "MSG-SPEC:HAVE";
  public static final String MSG_NAME_REQ_RequestZones = "MSG-SPEC:REQ";
  public static final String MSG_NAME_REQ_MaxZones = "MSG-SPEC:MAX";
  public static final String MSG_NAME_REQ_LevelOfDetail = "MSG-SPEC:LOD";
  public static final String MSG_NAME_REQ_Services = "MSG-SPEC:SVC";
  public static final String MSG_NAME_REQ_SubscribeService = "MSG-SPEC:SUB";
  public static final String MSG_NAME_REQ_Listener = "MSG-SPEC:LISTEN";
  public static final String MSG_NAME_RPLY_SubscriptionOK = "MSG-SPEC:SUBOK";
  public static final String MSG_NAME_RPLY_NotificationOK = "MSG-SPEC:NFYOK";
  public static final String MSG_NAME_HELO_Notification = "MSG-SPEC:NFY";
  public static final String MSG_NAME_HELO_TextMessage = "MSG-SPEC:MSG";
  public static final String MSG_NAME_HELO_Status = "MSG-SPEC:STATUS";

  public static final String PIPE_TAG_MSG = "TerraTag_MSG";
  public static final String PIPE_TAG_TYPE = "TerraTag_TYPE";
  public static final String PIPE_TAG_ZONE = "TerraTag_ZONE";
  public static final String PIPE_TAG_DATA = "TerraTag_DATA";
  public static final String PIPE_TAG_TIME = "TerraTag_TIME";

  //GUI Main App States (switches South Panel, other display)
  public static final int FLIP_ZONE_NAV = 0;
  public static final int FLIP_ZONE_BUILDER = 1;
  public static final int FLIP_SPACE_NAV = 2;
  public static final int FLIP_SPACE_FILTER = 3;
  public static final int FLIP_COM = 4;
  public static final int FLIP_PIM = 5;
  public static final int FLIP_FB = 6;
  public static final int FLIP_PROJECT = 7;

  //Generic Colors
  public static final Color COLOR_LBLUE = new Color(0.5f, 0.5f, 1f);
  public static final Color COLOR_WHITE = new Color(1f, 1f, 1f);
  public static final Color COLOR_GRAY1 = new Color(0.5f, 0.5f, 0.5f);
  public static final Color COLOR_GRAY2 = new Color(212, 208, 255); //new Color(0.7f, 0.7f, 0.7f);
  public static final Color COLOR_GRAY3 = new Color(0.9f, 0.9f, 0.9f);
  public static final Color3f COLOR3F_RED = new Color3f(1.0f, 0.0f, 0.0f);
  public static final Color3f COLOR3F_GREEN = new Color3f(0.0f, 1.0f, 0.0f);
  public static final Color3f COLOR3F_BLUE = new Color3f(0.0f, 0.0f, 1.0f);
  public static final Color3f COLOR3F_GRAY = new Color3f(0.9f, 0.9f, 0.9f);

  //GUI West & East Menues (for input and display)
  public static final int MENU_COUNT = 9; //total #
  public static final int MENU_west_access = 0;
  public static final int MENU_west_personal = 1;
  public static final int MENU_west_projects = 2;
  public static final int MENU_west_cyberspace = 3;
  public static final int MENU_west_services = 4;
  public static final int MENU_x = 5;
  public static final int MENU_east_net = 6;
  public static final int MENU_east_space = 7;
  public static final int MENU_east_zone = 8;

  //GUI Status Areas (for information updates)
  public static final int STATUS_COUNT = 13; //total #
  public static final int STATUS_ALL = 0;
  public static final int STATUS_line_app = 1;
  public static final int STATUS_line_net = 2;
  public static final int STATUS_line_space = 3;
  public static final int STATUS_east_net = 4;
  public static final int STATUS_east_space = 5;
  public static final int STATUS_east_zone = 6;
  public static final int STATUS_progress = 7;
  public static final int STATUS_nav_alt = 8;
  public static final int STATUS_nav_heading = 9;
  public static final int STATUS_updatePing = 10;
  public static final int STATUS_zonegrid = 11;
  public static final int STATUS_updateRep = 12;

  //Zone: Global States
  public static final int STATE_OK = 0;
  public static final int STATE_ERR = 1;

  //Zone: Build States
  public static final int BUILD_STATE_ZONE_NEW = 0;
  public static final int BUILD_STATE_ZONE_UNDER_CONSTRUCTION = 1;
  public static final int BUILD_STATE_ZONE_BASE = 2;
  public static final int BUILD_STATE_ZONE_SERVICE = 3;
  public static final int BUILD_STATE_ZONE_XT = 4;

  //Zone: Settle States
  public static final int SETTLE_NONE = 0;
  public static final int SETTLE_RESERVED = 1;
  public static final int SETTLE_MOVED = 2;
  public static final int SETTLE_SHARED = 3;
  public static final int SETTLE_SETTLED = 4;

  //Message Strings
  public static String MSG_CONNECT_EX = "JXTA Connection Exception!";
  public static String MSG_CONNECT_OK = "Peer set up and connected to JXTA network successfully!";
  public static String MSG_CONNECT_ERR = "JXTA Connection Error!";
  public static String MSG_CONFIG_ERR = "JXTA is not Configured!";

  //Space properties
  public static final String VRML_REPOSITORY = "vrml/";

  //Grid properties
  public static final Color3f GRID_X_COLOR = COLOR3F_GREEN;
  public static final Color3f GRID_Y_COLOR = COLOR3F_BLUE;
  public static final Color3f GRID_Z_COLOR = COLOR3F_GRAY;
  public static final double GRID_DEFAULT_GRIDSTEP = 10.0;
  public static final float GRID_DEFAULT_AXISLEN = 2000;
  public static final float GRID_DEFAULT_GRIDLEN = 1000;
  public static final int GRID_DEFAULT_GRIDCOUNT = 2000;

  //Zone properties
  public static final Color3f ZONE_DEFAULT_MYCOLOR = COLOR3F_GREEN;
  public static final Color3f ZONE_DEFAULT_COLOR = COLOR3F_GRAY;
  public static final double ZONE_DEFAULT_HEIGHT = 1000;
  public static final double ZONE_DEFAULT_DEPTH = 10;
  public static final int ZONE_DEFAULT_PLANE_WIDTH = 10;
  public static final int ZONE_DEFAULT_PLANE_HEIGHT = 10;
  public static final int GEOTYPE_2P = 1;
  public static final int GEOTYPE_4P = 2;
  public static final int GEOTYPE_MP = 3;
  public static final int GEOTYPE_3D = 4;

  //private final Appearance _app = new Appearance();
  //private final Material _mat = new Material();
  public static final double PLANE_THICKNESS = 0.025;  // Plane thickness
  public static final Color3f PLANE_DEFAULT = new Color3f(.0f, .0f, .0f); // Standard plane example usage colors

  //Services
  //Service ID format is "TERRA-Z000zoneID-S-typeStr-count"
  public static final int SERVICE_X = 100;
  public static final int SERVICE_SEARCH_PEER_ID = 101;
  public static final int SERVICE_SEARCH_PEER_NAME = 102;
  public static final int SERVICE_SEARCH_PEER_DESCR = 103;
  public static final int SERVICE_SEARCH_PEER_SERVICE = 104;
  public static final int SERVICE_URL = 110;
  public static final int SERVICE_HTTP = 111;
  public static final int SERVICE_FTP = 112;
  public static final int SERVICE_WEBSERVICES = 115;
  public static final int SERVICE_COM_MESSENGER = 120;
  public static final int SERVICE_COM_CHAT = 121;

  //Connectors
  public static final int CONNECTOR_X = 200;
  public static final int CONNECTOR_FEEDBACK = 201;

  //GUI Strings
  public static final String DESCR_LOG_DIALOG = "This is the application log-file viewer. All information displayed reflects the current debugging level.\n\n"+
                                                "Log Levels:\n"+
                                                "<0> = EXCEPTION \n    (total crash)\n"+
                                                "<1> = SERIOUS ERROR \n    (need to shutdown)\n"+
                                                "<2> = ERROR \n (not good, but can continue)\n"+
                                                "<4> = INFORMATIVE \n    (just FYI)\n"+
                                                "<8> = DEBUG";

  public static final String DESCR_HELP_DIALOG = "Welcome to the TerraPeer Help Dialog. \n"+
                                                 "Please select one of the topics from the drop-down list to the right.\n"+
                                                 "\nFor further information, go to the TerraPeer website.";

  //VUI properties
  public static final Vector4d SPACENAV_POS_ZERO = new Vector4d(0, 0, 0, 0);
  public static final Vector4d SPACENAV_POS_START = new Vector4d(-10, 10, 70, 0);
  public static final Vector4d SPACENAV_YAW_START = new Vector4d(1, 1, Math.toRadians(15), Math.toRadians(-15));
  public static final Vector4d SPACENAV_YAW_HOME = new Vector4d(0, 1, 0, 0);
  public static final Vector4d SPACENAV_POS_ORIGO_NORTH = new Vector4d(0, 10, 40, 0);
  public static final Vector4d SPACENAV_POS_ORIGO_TOP = new Vector4d(0, 200, 0, 0);
  public static final Vector4d SPACENAV_ROLL_ORIGO_TOP = new Vector4d(1, 0, 0, Math.toRadians(-90));
  public static final int SPACENAV_LIMIT_UP = 2000; //2000
  public static final int SPACENAV_LIMIT_DN = 1;
  public static final int SPACENAV_LIMIT_XY = 5000; //5000
  public static final double SPACENAV_LIMIT_SPEED_A = 0.45;
  public static final double SPACENAV_LIMIT_SPEED_B = 0.75;
  public static final double SPACENAV_LIMIT_PITCH_UP = 0.4;//-(Math.PI - (Math.PI / 6));
  public static final double SPACENAV_LIMIT_PITCH_DN = 0.6;// (Math.PI + (Math.PI / 6));
  public static final double SPACENAV_SPEED_STEP = 10;//0.15;
  public static final double SPACENAV_DIR_STEP = 15; //degrees

  public static final int SPACENAV_CTRL_STOP = 0;
  public static final int SPACENAV_CTRL_SPEED_FORWARD = 1;
  public static final int SPACENAV_CTRL_SPEED_BACK = 2;
  public static final int SPACENAV_CTRL_SPEED_LEFT = 3;
  public static final int SPACENAV_CTRL_SPEED_RIGHT = 4;
  public static final int SPACENAV_CTRL_SPEED_UP = 7;
  public static final int SPACENAV_CTRL_SPEED_DOWN = 8;
  public static final int SPACENAV_CTRL_TURN_LEFT = 9;
  public static final int SPACENAV_CTRL_TURN_RIGHT = 10;
  public static final int SPACENAV_CTRL_PITCH_LEFT = 11;
  public static final int SPACENAV_CTRL_PITCH_RIGHT = 12;
  public static final int SPACENAV_CTRL_PITCH_UP = 13;
  public static final int SPACENAV_CTRL_PITCH_DOWN = 14;
  public static final int SPACENAV_CTRL_ALIGN = 15;
  public static final int SPACENAV_CTRL_NORTH = 16;
  public static final int SPACENAV_CTRL_HOME = 20;
  public static final int SPACENAV_CTRL_ORIGO_NORTH = 21;
  public static final int SPACENAV_CTRL_ORIGO_TOP = 22;
  public static final int SPACENAV_CTRL_DEFAULT = 23;
  public static final int SPACENAV_CTRL_SPEED_FORWARD_1 = 31;
  public static final int SPACENAV_CTRL_SPEED_FORWARD_2 = 32;
  public static final int SPACENAV_CTRL_SPEED_BACK_1 = 35;
  public static final int SPACENAV_CTRL_SPEED_BACK_2 = 36;

  public static final int NAV_STOP = 0;
  public static final int NAV_UP = 1;
  public static final int NAV_DOWN = 2;
  public static final int NAV_RIGHT = 3;
  public static final int NAV_LEFT = 4;
  public static final int NAV_FORDARDW = 5;
  public static final int NAV_BACK = 6;

  public static final int SPACEVIEW_GRID = 0;
  public static final int SPACEVIEW_AVATAR = 1;
  public static final int SPACEVIEW_ORBIT_1 = 2;
  public static final int SPACEVIEW_ORBIT_2 = 3;

  public static final int BLOCK_COUNT = 300; //includes services+connectors
  public static final int BBTYPE_ORIGO = 0;
  public static final int BBTYPE_SPHERE = 1;
  public static final int BBTYPE_BOX = 2;
  public static final int BBTYPE_PYRAMID = 3;
  public static final int BBTYPE_CYLINDER = 4;
  public static final int BBTYPE_LANDMARK_A = 11;
  public static final int BBTYPE_LANDMARK_B = 12;
  public static final int BBTYPE_LANDMARK_C = 13;
  public static final int BBTYPE_AVATAR_A = 21;
  public static final int BBTYPE_AVATAR_B = 22;
  public static final int BBTYPE_HOUSE_A = 31;
  public static final int BBTYPE_HOUSE_B = 32;
  public static final int BBTYPE_HOUSE_C = 33;

  public static final String[] OBJECT_FILES = new String[BLOCK_COUNT];

  //Zone properties
  //Zone ID format is "TerraPeer.ZID:T-YEAR.MONTH.DAY_OF_MONTH.HOUR_OF_DAY.MINUTE.SECOND:PID-JXTA_PEER_ID.JXTA_PEER_NAME"
  public static final String ZONE_ID_START = "TerraPeer.ZID";
  public static final Font ZONE_TEXT_FONT = new Font("Arial", Font.PLAIN, 1);
  public static final String ZONE_REPOSITORY_FILE = "terra.xml";
  public static final String ZONE_REPOSITORY_VER = "1.0";

  //ZoneGrid Panel properties
  public static final int ZONEGRID_GRIDSPACE_PIXEL = 5;
  public static final int ZONEGRID_START_XCOORD = 0;
  public static final int ZONEGRID_START_YCOORD = 0;
  public static final int ZONEGRID_COORD_STEP = 10;
  public static final int ZONEGRID_LABEL_STEP = 5;


  //Help HTML Page index
  public static final String[] HELP_PAGES = new String[100];
  public static final int HELP_START = 0;
  public static final int HELP_INTRO = 1;
  public static final int HELP_ABOUT = 9;
  public static final int HELP_GUI_START = 10;
  public static final int HELP_SPACE_START = 30;
  public static final int HELP_SPACE_A = 31;
  public static final int HELP_NET_START = 50;
  public static final int HELP_NET_A = 51;
  public static final int HELP_PERSONAL_START = 70;
  public static final int HELP_TROUBLE_START = 90;


  /**
   * Constructor
   */
  public vars()
  {
    loadHelp();

  }

  public static void setMessageArray()
  {
    MESSAGE_TYPE[MSG_TYPE_X] = MSG_NAME_X;
    MESSAGE_TYPE[MSG_TYPE_TXT] = MSG_NAME_TXT;
    MESSAGE_TYPE[MSG_TYPE_XML] = MSG_NAME_XML;
    MESSAGE_TYPE[MSG_TYPE_X3D] = MSG_NAME_X3D;
    MESSAGE_TYPE[MSG_TYPE_BIN] = MSG_NAME_BIN;
    MESSAGE_TYPE[MSG_TYPE_GEO] = MSG_NAME_GEO;
    MESSAGE_TYPE[MSG_TYPE_From] = MSG_NAME_From;
    MESSAGE_TYPE[MSG_TYPE_Zones] = MSG_NAME_Zones;
    MESSAGE_TYPE[MSG_TYPE_ZoneData] = MSG_NAME_ZoneData;
    MESSAGE_TYPE[MSG_TYPE_Blocks] = MSG_NAME_Blocks;
    MESSAGE_TYPE[MSG_TYPE_Services] = MSG_NAME_Services;
    MESSAGE_TYPE[MSG_TYPE_REQ_All] = MSG_NAME_REQ_All;
    MESSAGE_TYPE[MSG_TYPE_REQ_HaveZones] = MSG_NAME_REQ_HaveZones;
    MESSAGE_TYPE[MSG_TYPE_REQ_RequestZones] = MSG_NAME_REQ_RequestZones;
    MESSAGE_TYPE[MSG_TYPE_REQ_MaxZones] = MSG_NAME_REQ_MaxZones;
    MESSAGE_TYPE[MSG_TYPE_REQ_LevelOfDetail] = MSG_NAME_REQ_LevelOfDetail;
    MESSAGE_TYPE[MSG_TYPE_REQ_Services] = MSG_NAME_REQ_Services;
    MESSAGE_TYPE[MSG_TYPE_REQ_SubscribeService] = MSG_NAME_REQ_SubscribeService;
    MESSAGE_TYPE[MSG_TYPE_REQ_Listener] = MSG_NAME_REQ_Listener;
    MESSAGE_TYPE[MSG_TYPE_RPLY_SubscriptionOK] = MSG_NAME_RPLY_SubscriptionOK;
    MESSAGE_TYPE[MSG_TYPE_RPLY_NotificationOK] = MSG_NAME_RPLY_NotificationOK;
    MESSAGE_TYPE[MSG_TYPE_HELO_Notification] = MSG_NAME_HELO_Notification;
    MESSAGE_TYPE[MSG_TYPE_HELO_TextMessage] = MSG_NAME_HELO_TextMessage;
    MESSAGE_TYPE[MSG_TYPE_HELO_Status] = MSG_NAME_HELO_Status;

  }

  //GUI image properties
  public static final int IMG_ARRAY = 120;
  public static final String[] IMG_FILE = new String[IMG_ARRAY];
  public static final String IMG_PATH = ".."+File.separatorChar+"objects"+File.separatorChar+"images";
  public static String IMG_FULL_PATH = "terrapeer"+File.separatorChar+"objects"+File.separatorChar+"images";

  /**
   * PreLoads all objects for GUI
   */
  public static void loadObjects()
  {
    OBJECT_FILES[BBTYPE_ORIGO] = "long_tower.wrl";
    OBJECT_FILES[BBTYPE_SPHERE] = "sphere.wrl";
    OBJECT_FILES[BBTYPE_BOX] = "box.wrl";
    OBJECT_FILES[BBTYPE_PYRAMID] = "pyramid.wrl";
    OBJECT_FILES[BBTYPE_CYLINDER] = "cylinder.wrl";
    OBJECT_FILES[BBTYPE_LANDMARK_A] = "column.wrl";
    OBJECT_FILES[BBTYPE_LANDMARK_B] = "pillar.wrl";
    OBJECT_FILES[BBTYPE_LANDMARK_C] = "landmark3.wrl";
    OBJECT_FILES[BBTYPE_AVATAR_A] = "avatar_a.wrl";
    OBJECT_FILES[BBTYPE_AVATAR_B] = "avatar_b.wrl";
    OBJECT_FILES[SERVICE_X] = "";
    OBJECT_FILES[SERVICE_URL] = "url.wrl";
    OBJECT_FILES[SERVICE_HTTP] = "http.wrl";
    OBJECT_FILES[SERVICE_FTP] = "ftp.wrl";
    OBJECT_FILES[SERVICE_WEBSERVICES] = "service.wrl";
    OBJECT_FILES[SERVICE_COM_MESSENGER] = "messenger.wrl";
    OBJECT_FILES[SERVICE_COM_CHAT] = "chat.wrl";
    OBJECT_FILES[BBTYPE_HOUSE_A] = "home.wrl";
    OBJECT_FILES[BBTYPE_HOUSE_B] = "pagoda.wrl";
    OBJECT_FILES[BBTYPE_HOUSE_C] = "chat.wrl";

  }

  /**
   * PreLoads all images for GUI
   */
  public static void loadImages()
  {
    IMG_FILE[0] = "empty.gif"; //empty 1
    IMG_FILE[1] = "openFile.png"; //open 16
    IMG_FILE[2] = "closeFile.png"; //close 16
    IMG_FILE[3] = "Qmark.gif"; //help 32 TB
    IMG_FILE[4] = "log.gif"; //log 32  TB
    IMG_FILE[5] = "network.png"; //login 32 TB
    IMG_FILE[6] = "trash.png"; //trash 32
    IMG_FILE[7] = "arrowlf.gif"; //arrow left 7
    IMG_FILE[8] = "arrowri.gif"; //arrow right 7
    IMG_FILE[9] = "arrowdn.gif"; //arrow down 9
    IMG_FILE[10] = "arrowup.gif"; //arrow up 9
    IMG_FILE[11] = "terra_10b.gif";//"tps1.png" //"tp_6.gif"; //logo 1 200
    IMG_FILE[12] = "house.gif"; //logo 2 200
    IMG_FILE[13] = "empty.gif"; //logo 3
    IMG_FILE[14] = "empty.gif"; //logo 4
    IMG_FILE[15] = "o_red.gif"; //object 9
    IMG_FILE[16] = "o_yellow.gif"; //object 9
    IMG_FILE[17] = "o_blue.gif"; //object 9
    IMG_FILE[18] = "empty.gif"; //object
    IMG_FILE[19] = "empty.gif"; //object
    IMG_FILE[20] = "myhome.gif"; //myhome 32 TB
    IMG_FILE[21] = "Folder0.gif"; //project 32 TB
    IMG_FILE[22] = "Folder1.gif"; //
    IMG_FILE[23] = "empty.gif"; //
    IMG_FILE[24] = "prefs.gif"; //prefs 32 TB
    IMG_FILE[25] = "fb.gif"; //fb 32 TB
    IMG_FILE[26] = "myinfo.gif"; //myinfo 32 TB
    IMG_FILE[27] = "lock.gif"; //sec 32 TB
    IMG_FILE[28] = "empty.gif"; //
    IMG_FILE[29] = "agroup.gif"; //members 32 TB
    IMG_FILE[30] = "3d.gif"; //vr world 32 TB
    IMG_FILE[31] = "contact.gif"; //contact 32 TB
    IMG_FILE[32] = "empty.gif"; //
    IMG_FILE[33] = "empty.gif"; //
    IMG_FILE[34] = "empty.gif"; //
    IMG_FILE[35] = "empty.gif"; //
    IMG_FILE[36] = "go.gif"; //30x20
    IMG_FILE[37] = "stop.gif"; //stop 32
    IMG_FILE[38] = "join.gif"; //join 24
    IMG_FILE[39] = "house.gif"; //house 24
    IMG_FILE[40] = "discover.gif"; //discover 24
    IMG_FILE[41] = "objects.gif"; //objects 32
    IMG_FILE[42] = "on0.gif"; //light 12
    IMG_FILE[43] = "on1.gif"; //light 12
    IMG_FILE[44] = "on2.gif"; //light 12
    IMG_FILE[45] = "uparrow.gif"; //
    IMG_FILE[46] = "downarrow.gif"; //
    IMG_FILE[47] = "leftarrow.gif"; //
    IMG_FILE[48] = "rightarrow.gif"; //
    IMG_FILE[49] = "era.gif"; //spinning world 92
    IMG_FILE[50] = "pointer.gif"; // 16x32
    IMG_FILE[51] = "opoint_dn.gif"; // 14x11
    IMG_FILE[52] = "opoint_r.gif"; // 11x14
    IMG_FILE[53] = "heading.gif"; // 128x32
    IMG_FILE[54] = "terra_alt.gif"; // 32x128
    IMG_FILE[55] = "terra_compass_128.gif"; // 128
    IMG_FILE[56] = "opoint_up.gif"; // 14x11
    IMG_FILE[57] = "direction.png"; //
    IMG_FILE[58] = "direction2.png"; //
    IMG_FILE[59] = "empty.gif"; //
    IMG_FILE[60] = "sky.jpg"; // bg
    IMG_FILE[61] = "nav_home.jpg"; //nav home
    IMG_FILE[62] = "nav_stop.png"; //nav stop
    IMG_FILE[63] = "nav_align.gif"; //nav align
    IMG_FILE[64] = "nav_north.gif"; //nav north
    IMG_FILE[65] = "help.png"; //help 16
    IMG_FILE[66] = "world.gif"; //vr world 16
    IMG_FILE[67] = "nav_onorth.png"; //nav origo north
    IMG_FILE[68] = "nav_otop.png"; //nav origo top
    IMG_FILE[69] = "mail.gif"; //mail 14x10
    IMG_FILE[70] = "cone.gif"; //3D 20x20
    IMG_FILE[71] = "sphere.gif"; //3D 20x20
    IMG_FILE[72] = "cylinder.gif"; //3D 20x20
    IMG_FILE[73] = "cylinderM.gif"; //3D 20x20
    IMG_FILE[74] = "cube.gif"; //3D 20x20
    IMG_FILE[75] = "cubeM.gif"; //3D 20x20
    IMG_FILE[76] = "shell.gif"; //3D 20x20
    IMG_FILE[77] = "empty.gif"; //
    IMG_FILE[78] = "doc0.gif"; //16x16
    IMG_FILE[79] = "doc1.gif"; //16x16
    IMG_FILE[80] = "home.gif"; //20x20
    IMG_FILE[81] = "viewpoint.gif"; //20x20
    IMG_FILE[82] = "scene.gif"; //20x20
    IMG_FILE[83] = "empty.gif"; //
    IMG_FILE[84] = "empty.gif"; //
    IMG_FILE[85] = "empty.gif"; //
    IMG_FILE[86] = "empty.gif"; //
    IMG_FILE[87] = "icon_smile.gif"; //smiley 15x15
    IMG_FILE[88] = "icon_smile_angry.gif"; //smiley 15x15
    IMG_FILE[89] = "icon_smile_approve.gif"; //smiley 15x15
    IMG_FILE[90] = "icon_smile_big.gif"; //smiley 15x15
    IMG_FILE[91] = "icon_smile_blush.gif"; //smiley 15x15
    IMG_FILE[92] = "icon_smile_cool.gif"; //smiley 15x15
    IMG_FILE[93] = "icon_smile_dead.gif"; //smiley 15x15
    IMG_FILE[94] = "icon_smile_sad.gif"; //smiley 15x15
    IMG_FILE[95] = "icon_smile_shy.gif"; //smiley 15x15
    IMG_FILE[96] = "icon_smile_shock.gif"; //smiley 15x15
    IMG_FILE[97] = "empty.gif"; //
    IMG_FILE[98] = "empty.gif"; //
    IMG_FILE[99] = "empty.gif"; //
    IMG_FILE[100] = "java.gif"; //26x16
    IMG_FILE[101] = "linux.gif"; //14x16
    IMG_FILE[102] = "empty.gif"; //
    IMG_FILE[103] = "empty.gif"; //
    IMG_FILE[104] = "empty.gif"; //
    IMG_FILE[105] = "empty.gif"; //
    IMG_FILE[106] = "empty.gif"; //
    IMG_FILE[107] = "empty.gif"; //
    IMG_FILE[108] = "empty.gif"; //
    IMG_FILE[109] = "empty.gif"; //
    IMG_FILE[110] = "service1.gif"; //service 16x16
    IMG_FILE[111] = "service2.gif"; //service 16x16
    IMG_FILE[112] = "service3.gif"; //service 16x16
    IMG_FILE[113] = "service4.gif"; //service 16x16
    IMG_FILE[114] = "service5.gif"; //service 16x16
    IMG_FILE[115] = "empty.gif"; //
    IMG_FILE[116] = "empty.gif"; //
    IMG_FILE[117] = "empty.gif"; //
    IMG_FILE[118] = "empty.gif"; //
    IMG_FILE[119] = "empty.gif"; //
  }

  public static void loadHelp()
  {
    HELP_PAGES[HELP_ABOUT] = "about.html";
    HELP_PAGES[HELP_START] = "index.html";
    HELP_PAGES[HELP_INTRO] = "intro.html";
    HELP_PAGES[HELP_GUI_START] = "gui_start.html";
    HELP_PAGES[HELP_SPACE_START] = "space_start.html";
    HELP_PAGES[HELP_SPACE_A] = "space_a.html";
    HELP_PAGES[HELP_NET_START] = "net_start.html";
    HELP_PAGES[HELP_NET_A] = "net_a.html";
    HELP_PAGES[HELP_PERSONAL_START] = "personal_start.html";
    HELP_PAGES[HELP_TROUBLE_START] = "trouble_start.html";
  }

}
