package terrapeer.vui.helpers;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.media.j3d.*;

public class UISceneTree extends JScrollPane
{
  public UISceneTree()
  {
  }

  /**
             Constructs a FancyTree with the specified scene graph.
             @param root Root node of the scene graph.
   */
  public UISceneTree(Node root)
  {
    this();
    updateTree(root);
  }

  /**
             Updates this object's contents with the specified scene
             graph.
             @param root Root node of the scene graph.
   */
  public void updateTree(Node root)
  {
    JTree tree = new JTree(buildTree(root));
    setViewportView(tree);
    tree.revalidate();
  }

  // personal body ============================================

  /**
             Builds a tree by recursively walking the scene graph.
             @param node Root node of the current scene subgraph.
             @return Tree node containing a scene subgraph.  Null if no
             tree.
   */
  protected DefaultMutableTreeNode buildTree(Node node)
  {
    if (node == null)
      return null;
    String name = node.getClass().getName();

    DefaultMutableTreeNode treeNode =
        new DefaultMutableTreeNode(
        name.substring(name.lastIndexOf('.') + 1)
        );

    if (node instanceof Group)
    {
      Group group = (Group)node;
      final int n = group.numChildren();
      for (int i = 0; i < n; i++)
        treeNode.add(buildTree(group.getChild(i)));
    }

    return treeNode;
  }

}
