package gui.chart;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;

import common.util.DateUtils;

import model.Job;
import model.Location;

import gui.job.AddJobView;
import gui.job.EditJobView;
import gui.job.RandomJobView;
import gui.location.AddLocationView;
import gui.location.EditLocationView;
import gui.main.GUI;
import gui.common.*;

@SuppressWarnings("serial")
public class ChartView extends View implements IChartView {

	//-------------------------------
	// Product Container Tree members
	//-------------------------------
	private DefaultTreeModel _locationTreeModel;
	private JTree _locationTree;
	private JScrollPane _locationTreeScrollPane;
	private JPopupMenu _allLocationsMenu;
	private JMenuItem _addLocationMenuItem;
	private JMenuItem _addGlobalJobMenuItem;
	private JMenuItem _randomGlobalJobMenuItem;
	private JPopupMenu _locationMenu;
	private JMenuItem _addJobMenuItem;
	private JMenuItem _randomJobMenuItem;
	private JMenuItem _editLocationMenuItem;
	private JMenuItem _deleteLocationMenuItem;
	
	//--------------------------
	// Product Table members
	//--------------------------
	private JTable _jobTable;
	private DefaultTableModel _jobTableModel;
	private DefaultTableColumnModel _jobTableColumnModel;
	private JTableHeader _jobTableHeader;
	private JScrollPane _jobTableScrollPane;
	private JPopupMenu _jobMenu;
	private JMenuItem _editJobMenuItem;
	private JMenuItem _deleteJobMenuItem;
	
	//--------------------------
	// Other members
	//--------------------------
	private JPanel _jobPanel;
	private JSplitPane _midSplitPane;
	
	private HashMap<Location, LocationTreeNode> _locations;

	public ChartView(GUI parent) 
	{
		super(parent);
	
		construct();
		
		_controller = new ChartController(this);
	}
	
	@Override
	public IChartController getController() {
		return (IChartController)super.getController();
	}

	@Override
	protected void createComponents() {
		createLocationTree();
		createJobTable();
	}
	
	@Override
	protected void layoutComponents() {
		
		_jobPanel = new JPanel();
		_jobPanel.setLayout(new BoxLayout(_jobPanel, BoxLayout.Y_AXIS));
		_jobPanel.setPreferredSize(new Dimension(600, 300));
		
		_jobPanel.add(_jobTableScrollPane);

		_midSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _locationTreeScrollPane,
				_jobPanel);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(_midSplitPane);
	}

	private void createLocationTree() {
		
		TreeSelectionListener treeSelectionListener = 
			createLocationTreeSelectionListener();

		MouseListener mouseListener = createLocationTreeMouseListener();
		
		_locationTree = new JTree();
		_locationTree.setCellRenderer(new LocationTreeCellRenderer());
		_locationTree.setFont(createFont(_locationTree.getFont(), ContentFontSize));
		_locationTree.setRootVisible(true);
		_locationTree.setExpandsSelectedPaths(true);
		_locationTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		_locationTree.setDropMode(DropMode.ON);
		_locationTree.setTransferHandler(new LocationTransferHandler());

		_locationTreeScrollPane = new JScrollPane(_locationTree, 
											JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
											JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_locationTreeScrollPane.setPreferredSize(new Dimension(250, 175));
		_locationTreeScrollPane.setBorder(createTitledBorder("", BorderFontSize));
		_locationTree.addTreeSelectionListener(treeSelectionListener);
		_locationTree.addMouseListener(mouseListener);
		
		ActionListener actionListener = createLocationTreeActionListener();
		
		_allLocationsMenu = new JPopupMenu();
		_addLocationMenuItem = new JMenuItem("Add Location");
		_addLocationMenuItem.addActionListener(actionListener);
		_allLocationsMenu.add(_addLocationMenuItem);
		_addGlobalJobMenuItem = new JMenuItem("Add Job");
		_addGlobalJobMenuItem.addActionListener(actionListener);
		_allLocationsMenu.add(_addGlobalJobMenuItem);
		_allLocationsMenu.addSeparator();
		_randomGlobalJobMenuItem = new JMenuItem("Select Random Job");
		_randomGlobalJobMenuItem.addActionListener(actionListener);
		_allLocationsMenu.add(_randomGlobalJobMenuItem);
		
		_locationMenu = new JPopupMenu();
		_addJobMenuItem = new JMenuItem("Add Job");
		_addJobMenuItem.addActionListener(actionListener);
		_locationMenu.add(_addJobMenuItem);
		_randomJobMenuItem = new JMenuItem("Select Random Job");
		_randomJobMenuItem.addActionListener(actionListener);
		_locationMenu.add(_randomJobMenuItem);
		_locationMenu.addSeparator();
		_editLocationMenuItem = new JMenuItem("Edit Location");
		_editLocationMenuItem.addActionListener(actionListener);
		_locationMenu.add(_editLocationMenuItem);
		_deleteLocationMenuItem = new JMenuItem("Delete Location");
		_deleteLocationMenuItem.addActionListener(actionListener);
		_locationMenu.add(_deleteLocationMenuItem);
	}
	
	private TreeSelectionListener createLocationTreeSelectionListener() {
		return new TreeSelectionListener() {
		    public void valueChanged(TreeSelectionEvent e) {
				if (eventsAreDisabled()) {
					return;
				}
				getController().locationSelectionChanged();
		    }			
		};
	}
	
	private MouseListener createLocationTreeMouseListener() {
		return new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
    		}

			@Override
    		public void mouseReleased(MouseEvent e) {
    			handleMouseEvent(e);
    		}
    	    private void handleMouseEvent(MouseEvent e) {
    			if (eventsAreDisabled()) {
    				return;
    			}
				LocationTreeNode node = 
					(LocationTreeNode)TreeOperations.getSelectedTreeNode(_locationTree);
				if (node != null) {
					if (node.isAllLocations()) {
						if (e.isPopupTrigger()) {
			    			_locationTree.requestFocus();
			    			enableAllLocationsMenuItems();
			    			_allLocationsMenu.show(e.getComponent(), e.getX(), e.getY());
						}
					}
					else {
						if(e.isPopupTrigger())
						{
							_locationTree.requestFocus();
							enableLocationMenuItems();
							_locationMenu.show(e.getComponent(), e.getX(), e.getY());
						}
					}
				}
    	    }
		};
	}
	
	private ActionListener createLocationTreeActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(evt.getSource() == _addLocationMenuItem)
				{
					addLocation();
				}
				else if(evt.getSource() == _editLocationMenuItem)
				{
					renameLocation();
				}
				else if(evt.getSource() == _deleteLocationMenuItem)
				{
					deleteLocation();
				}
				else if(evt.getSource() == _randomGlobalJobMenuItem)
				{
					getRandomJob();
				}
				else if(evt.getSource() == _randomJobMenuItem)
				{
					getRandomJob();
				}
				else if(evt.getSource() == _addGlobalJobMenuItem)
				{
					addJob();
				}
				else if(evt.getSource() == _addJobMenuItem)
				{
					addJob();
				}
				else if(evt.getSource() == _editJobMenuItem)
				{
					editJob();
				}
				else if(evt.getSource() == _deleteJobMenuItem)
				{
					deleteJob();
				}
			}
		};
	}
	
	private void createJobTable() {
		
		MouseAdapter mouseListener = new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
    		}
			
			@Override
    		public void mouseReleased(MouseEvent e) {
				handleMouseEvent(e);
    		}
    	    
			private void handleMouseEvent(MouseEvent e) {
    	 		if (eventsAreDisabled()) {
    				return;
    			}
    			if (e.getSource() == _jobTable) {
    	        	if (e.isPopupTrigger()) {
    	    			enableJobMenuItems();
    	    			_jobMenu.show(e.getComponent(), e.getX(), e.getY());
    				}
    			}
    			else if (e.getSource() == _jobTableHeader) {
    	        	if (e.isPopupTrigger()) {
    	        		enableJobMenuItems();
    	    			_jobMenu.show(e.getComponent(), e.getX(), e.getY());
    				}
//    	    		else if (e.getButton() == MouseEvent.BUTTON1 &&
//    						e.getID() == MouseEvent.MOUSE_PRESSED) {
//    					int clickedColumnIndex = 
//    						commentsColumnModel.getColumnIndexAtX(e.getX());
//    					if (clickedColumnIndex >= 0) {
//    						updateCommentSortOrder(clickedColumnIndex);
//    					}
//    	    		}
    			}
    	    }
		};

		_jobTableColumnModel = new DefaultTableColumnModel();
		TableColumn column = createTableColumn(0, "Description", ContentFontSize);
		_jobTableColumnModel.addColumn(column);
		column = createTableColumn(1, "Time Required", ContentFontSize);
		_jobTableColumnModel.addColumn(column);
		column = createTableColumn(2, "Location", ContentFontSize);
		_jobTableColumnModel.addColumn(column);
		column = createTableColumn(3, "Last Completed", ContentFontSize);
		_jobTableColumnModel.addColumn(column);
		
		_jobTableModel = new DefaultTableModel(0, 4) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		_jobTable = new JTable(_jobTableModel, _jobTableColumnModel);
		_jobTable.setDragEnabled(true);
		_jobTable.setTransferHandler(new JobTransferHandler());
		_jobTable.setFont(createFont(_jobTable.getFont(), ContentFontSize));
		_jobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_jobTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						if (eventsAreDisabled()) {
							return;
						}
						if (evt.getValueIsAdjusting()) {
							return;
						}
						getController().jobSelectionChanged();
					}
				});
		_jobTable.addMouseListener(mouseListener);
		
		_jobTableHeader = _jobTable.getTableHeader();
		_jobTableHeader.setReorderingAllowed(false);
		_jobTableHeader.addMouseListener(mouseListener);
		_jobTableHeader.setBackground(Color.blue);
		
		_jobTableScrollPane = new JScrollPane(_jobTable, 
									JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
									JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_jobTableScrollPane.setBorder(createTitledBorder("Jobs", BorderFontSize));

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == _editJobMenuItem) {
					editJob();
				}
				else if (evt.getSource() == _deleteJobMenuItem) {
					deleteJob();
				}
			}
		};

		_jobMenu = new JPopupMenu();
		_editJobMenuItem = new JMenuItem("Edit Job");
		_editJobMenuItem.addActionListener(actionListener);
		_jobMenu.add(_editJobMenuItem);
		_deleteJobMenuItem = new JMenuItem("Delete Job");
		_deleteJobMenuItem.addActionListener(actionListener);
		_jobMenu.add(_deleteJobMenuItem);
	}
	
	private void enableAllLocationsMenuItems() {
		_addLocationMenuItem.setEnabled(getController().canAddLocation());
		_addJobMenuItem.setEnabled(getController().canAddJob());
	}
	
	private void enableLocationMenuItems() {
		_addJobMenuItem.setEnabled(getController().canAddJob());
		_editLocationMenuItem.setEnabled(getController().canEditLocation());
		_deleteLocationMenuItem.setEnabled(getController().canRemoveLocation());
	}
	
	private void enableJobMenuItems() {
		_editJobMenuItem.setEnabled(getController().canEditJob());
		_deleteJobMenuItem.setEnabled(getController().canRemoveJob());
	}
	
	//////////////////
	// Action Methods
	//////////////////

	private void addLocation() {
		getController().addLocation();
	}
	
	private void renameLocation() {
		getController().editLocation();
	}

	private void deleteLocation() {
		getController().removeLocation();
	}
	
	private void addJob() {
		getController().addJob();
	}
	
	private void editJob() {
		getController().editJob();
	}
	
	private void deleteJob() {
		getController().removeJob();
	}
	
	private void getRandomJob() {
		getController().getRandomJob();
	}

	//////////////////
	// IInventoryView
	//////////////////

	@Override
	public void setLocations(Location rootData) {
		boolean disabledEvents = disableEvents();
		try {
			_locations = new HashMap<Location, LocationTreeNode>();
			DefaultMutableTreeNode rootNode = null;
			if (rootData == null) {
				rootNode = new LocationTreeNode(null);
			}
			else {
				rootNode = buildLocationTree(rootData);
			}
			_locationTreeModel = new DefaultTreeModel(rootNode);
			_locationTree.setModel(_locationTreeModel);	
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	/**
	 * Builds the item container tree that will be displayed in the JTree.
	 */
	private DefaultMutableTreeNode buildLocationTree(Location data) {
		LocationTreeNode node = new LocationTreeNode(data);
		_locations.put(data, node);
		for (int i = 0; i < data.getChildCount(); ++i) {
			node.add(buildLocationTree(data.getChild(i)));
		}
		return node;
	}

	@Override
	public Location getSelectedLocation() {
		LocationTreeNode selectedNode = getSelectedLocationNode();
		return((selectedNode != null) ? selectedNode.getLocation() : null);		
	}

	@Override
	public void selectLocation(Location container) {
		boolean disabledEvents = disableEvents();
		try {
			if (container != null) {
				if (_locations.containsKey(container)) {
					selectLocationNode(_locations.get(container));
					// Update all views tied to this controller
					getController().locationSelectionChanged();
				}
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	/**
	 * Selects the specified node in the product container tree.
	 * 
	 * @param node The node to be selected
	 */
	private void selectLocationNode(LocationTreeNode node) {
		TreeOperations.selectTreeNode(_locationTree, node);
	}
	
	/**
	 * Returns the currently selected node in the product container tree.
	 */
	private LocationTreeNode getSelectedLocationNode() {
		return (LocationTreeNode)
					TreeOperations.getSelectedTreeNode(_locationTree);
	}

	@Override
	public Job getSelectedJob() {
		int selectedIndex = _jobTable.getSelectedRow();
		if (selectedIndex >= 0) {
			JobFormatter formatter = 
				(JobFormatter)_jobTableModel.getValueAt(selectedIndex, 0);
			return (Job)formatter.getTag();
		}
		return null;
	}

	@Override
	public void selectJob(Job job) {
		boolean disabledEvents = disableEvents();
		try {
			for (int i = 0; i < _jobTableModel.getRowCount(); ++i) {
				JobFormatter formatter = (JobFormatter)_jobTableModel.getValueAt(i, 0);
				Job id = (Job)formatter.getTag();
				if (id == job) {
					TableOperations.selectTableRow(_jobTable, i);
					return;
				}
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setJobs(Job[] jobs) {
		boolean disabledEvents = disableEvents();
		try {
			_jobTableModel.setRowCount(0);
			for (Job job : jobs) {			
				JobFormatter[] row = new JobFormatter[6];
				row[0] = new JobFormatter(0);
				row[0].setTag(job);
				row[1] = new JobFormatter(1);
				row[1].setTag(job);
				row[2] = new JobFormatter(2);
				row[2].setTag(job);
				row[3] = new JobFormatter(3);
				row[3].setTag(job);
				row[4] = new JobFormatter(4);
				row[4].setTag(job);
				row[5] = new JobFormatter(5);
				row[5].setTag(job);
				_jobTableModel.addRow(row);
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	@Override
	public void displayAddLocationView()
	{
		DialogBox dialogBox = new DialogBox(_parent, "Add Location");
		AddLocationView dialogView = new AddLocationView(_parent, dialogBox);
		dialogBox.display(dialogView, false);
	}
	
	@Override
	public void displayEditLocationView()
	{
		Location target = getSelectedLocation();
		DialogBox dialogBox = new DialogBox(_parent, "Edit Location");
		EditLocationView dialogView = new EditLocationView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}
	
	@Override
	public void displayAddJobView()
	{
		Location target = getSelectedLocation();
		DialogBox dialogBox = new DialogBox(_parent, "Add Job");
		AddJobView dialogView = new AddJobView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}
	
	@Override
	public void displayEditJobView()
	{
		Job target = getSelectedJob();
		DialogBox dialogBox = new DialogBox(_parent, "Edit Job");
		EditJobView dialogView = new EditJobView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}
	
	@Override
	public void displayRandomJobView()
	{
		Location target = getSelectedLocation();
		DialogBox dialogBox = new DialogBox(_parent, "Get Random Job");
		RandomJobView dialogView = new RandomJobView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	//
	//
	//

	private class JobFormatter extends Tagable
	{
		private int column;
		
		public JobFormatter(int column) {
			this.column = column;
		}
		
		@Override
		public String toString() {
			Job data = (Job)getTag();
			if (data != null) {
				switch (column) {
				case 0: return data.getDescription();
				case 1: return data.getTime().toString();
				case 2: return (data.getLocation() != null ? data.getLocation().toString() : "");
				case 3: return (data.getLastCompleted() != null ?
						DateUtils.formatDate(data.getLastCompleted()) :
						"");
				default: assert false;
				}
			}
			return "";
		}
	}
	
	private static DataFlavor InventoryFlavor = 
		new DataFlavor(InventoryTransferable.class, "Inventory Flavor");
	
	
	private class JobTransferHandler extends TransferHandler {

		@Override
		protected Transferable createTransferable(JComponent component) {
			Job data = getSelectedJob();
			if (data != null) {
				return new InventoryTransferable(data);
			}
			else {
				return null;
			}
		}

		@Override
		protected void exportDone(JComponent component, Transferable transferable, int action) {
			return;
		}

		@Override
		public int getSourceActions(JComponent component) {
			return COPY_OR_MOVE;
		}
		
	}
	
	private class LocationTransferHandler extends TransferHandler {

		@Override
		public boolean canImport(TransferSupport support) {

			if (!support.isDrop()) {
				return false;
			}
			
			if (!support.isDataFlavorSupported(InventoryFlavor)) {
				return false;
			}
			
			javax.swing.JTree.DropLocation dropLoc = 
				(javax.swing.JTree.DropLocation)support.getDropLocation();
			
			if (dropLoc == null) {
				return false;
			}
			
			TreePath path = dropLoc.getPath();
			
			if (path == null) {
				return false;
			}
			
			LocationTreeNode dropNode = 
				(LocationTreeNode)path.getLastPathComponent();
			
			if (dropNode == null) {
				return false;
			}
			
			if (dropNode.isAllLocations()) {
				return false;
			}
			
			return true;
		}

		@Override
		public boolean importData(TransferSupport support) {

			if (!support.isDrop()) {
				return false;
			}
			
			if (!support.isDataFlavorSupported(InventoryFlavor)) {
				return false;
			}
			
			javax.swing.JTree.DropLocation dropLoc = 
				(javax.swing.JTree.DropLocation)support.getDropLocation();
			
			if (dropLoc == null) {
				return false;
			}
			
			TreePath path = dropLoc.getPath();
			
			if (path == null) {
				return false;
			}
			
			LocationTreeNode dropNode = 
				(LocationTreeNode)path.getLastPathComponent();
			
			if (dropNode == null) {
				return false;
			}
			
			if (dropNode.isAllLocations()) {
				return false;
			}
			
			Transferable transferable = support.getTransferable();
			
			if (transferable == null) {
				return false;
			}
			
			Object data = null;
			try {
				data = transferable.getTransferData(InventoryFlavor);
			}
			catch (Exception e) {
			}
			
			if (data == null) {
				return false;
			}

			Location container = dropNode.getLocation();
			assert (container != null);
			
			if (data instanceof Job) {
				getController().moveJobToLocation((Job)data, container);
			}
			else {
				return false;
			}
			
			return true;
		}
		
	}
	

	private class InventoryTransferable implements Transferable {

		private Object _data;
		
		public InventoryTransferable(Object data) {
			_data = data;
		}
		
		@Override
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (InventoryFlavor.equals(flavor)) {
				return _data;
			}
			else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[]{ InventoryFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return InventoryFlavor.equals(flavor);
		}
		
	}
	
}

@SuppressWarnings("serial")
class LocationTreeCellRenderer extends DefaultTreeCellRenderer {

	private Icon _storageUnitIcon;
	
    public LocationTreeCellRenderer() {
        _storageUnitIcon = new ImageIcon("images" + java.io.File.separator + "door-icon.png");
    }

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        
        if (value instanceof LocationTreeNode) {
	        LocationTreeNode node = (LocationTreeNode)value;
	        if (node.isAllLocations() || node.isLocation()) {
	        	setIcon(_storageUnitIcon);
	        }
	        else {
	        	setIcon(closedIcon);
	        }
        }

        return this;
    }
	
}