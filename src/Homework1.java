import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;

public class Homework1 {

    public static String input = "251-*32*+";
    public static int inputlength= input.length() - 1;

    public static void main(String[] args) {
        // Begin of arguments input sample
        if (args.length > 0) {
            input = args[0];
            inputlength = input.length() - 1;
        }
         
        inorder(new Node(input.charAt(inputlength)));
        System.out.print(infix(root));
        System.out.print("=" + calculate(root));
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.createAndShowGUI();
            }
        });
    }
    // End of arguments input sample

    // TODO: Implement your project here
    static Node root;
    static int count;
    public static String infix(Node n) {
    	String result = "";
        if (count == 0) {
        	count++;
        	if(!IsOp(n)) {
        		count=0;
        		return Character.toString(n.key);
        				
        	}
        	result += infix(n.left);
            result += Character.toString(n.key);
            result +=infix(n.right);
            count  =0;
        } else if (IsOp(n)) {
        	result += "(";
        	result +=infix(n.left);
            result += Character.toString(n.key);
            result +=infix(n.right);
            result += ")";
        } else {
        	
        	result += Character.toString(n.key);
        }
        return result;
        
    }

    public static void inorder(Node n) {
    	   if (root == null) {
               root = n;
               inputlength--;
           }
           if (IsOp(n)) {
               n.right = new Node(input.charAt(inputlength));
               inputlength--;
               inorder(n.right);
               n.left = new Node(input.charAt(inputlength));
               inputlength--;
               inorder(n.left);
    }
  }
    public static int calculate(Node n) {
        if (IsOp(n)) {
            switch (n.key) {
                case '+':
                    return calculate(n.left) + calculate(n.right);
                case '-':
                    return calculate(n.left) - calculate(n.right);
                case '/':
                    return calculate(n.left) / calculate(n.right);
                case '*':
                    return calculate(n.left) * calculate(n.right);
            }
        } else {
            return Character.getNumericValue(n.key);
        }
        return 0;
    }

    public static boolean IsOp(Node n) {
        return n.key == '+' || n.key == '-' || n.key == '*' || n.key == '/';
    }
    public static  class Node {

        Node left;
        Node right;

        char key;

        public Node(char data) {
            this.key = data;
        }
        public String toString() {
            return Character.toString(key);
        }
       
    }

}




 class GUI extends JPanel
                      implements TreeSelectionListener {
    private JEditorPane htmlPane;
    private JTree tree;
    private URL helpURL;
    private static boolean DEBUG = false;

    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
    
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;

    public GUI() {
        super(new GridLayout(1,0));

        //Create the nodes.
        DefaultMutableTreeNode top =  new DefaultMutableTreeNode(Homework1.root);
        createNodes(top);

        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
        

        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        
        JScrollPane htmlView = new JScrollPane(htmlPane);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); 
        splitPane.setPreferredSize(new Dimension(500, 300));
        
        
        ImageIcon leafIcon = createImageIcon("middle.gif"); 
        if (leafIcon != null) { 
            DefaultTreeCellRenderer renderer =  
                new DefaultTreeCellRenderer(); 
         
            renderer.setOpenIcon(leafIcon);
            renderer.setClosedIcon(leafIcon);
            tree.setCellRenderer(renderer); 
        }

        //Add the split pane to this panel.
        add(splitPane);
    }
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
    	  DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                  tree.getLastSelectedPathComponent();
    	
    	  Homework1.Node n =(Homework1.Node)node.getUserObject();
        if (node == null) return;
        
        htmlPane.setText(Homework1.infix(n)+"="+Homework1.calculate(n));
        
    }

   

    private void createNodes(DefaultMutableTreeNode top) {
    	
    	Homework1.Node n =(Homework1.Node)top.getUserObject();
    	
    	if(n.left!= null) {
    		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(n.left);    		
    		top.add(node1);
    		createNodes(node1);
    		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(n.right);
    		top.add(node2);
    		createNodes(node2);
    	}
    }
    
        
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread. 
     */
    public static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new GUI());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

