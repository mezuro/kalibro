package org.kalibro.desktop.old.project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeModel;

import org.kalibro.Kalibro;
import org.kalibro.Configuration;
import org.kalibro.ModuleResult;
import org.kalibro.ProjectResult;
import org.kalibro.Range;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.desktop.old.listeners.ResultPanelListener;
import org.kalibro.desktop.old.model.ConfiguredResultTableCellRenderer;
import org.kalibro.desktop.old.model.ConfiguredResultTableModel;
import org.kalibro.desktop.old.model.ResultTableModel;
import org.kalibro.desktop.old.model.SourceTreeNode;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.field.UneditableField;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;

public class ResultPanel extends JPanel implements ListSelectionListener, TreeSelectionListener {

	private UneditableField<String> labelConfiguration, labelModuleName, labelWeightedMean, labelDate;
	private UneditableField<Long> labelLoadTime, labelAnalysisTime;
	private TextField textPaneComments;

	private JButton buttonChooseConfiguration, buttonExport, buttonRequestAnalysis;

	private JTable tableResults;
	private ResultTableModel tableModel;
	private Configuration configuration;

	private ResultTreePanel treePanel;
	private DefaultTreeModel treeModel;
	private SourceTreeNode selectedNode;

	private ResultPanelListener listener;
	private ProjectResult projectResult;

	public ResultPanel(ResultPanelListener listener) {
		super();
		this.listener = listener;
		setName("result");

		createComponents();
		setLayout(new BorderLayout());
		add(northPanel(), BorderLayout.NORTH);
		add(treePanel, BorderLayout.WEST);
		add(centerPanel(), BorderLayout.CENTER);
		add(southPanel(), BorderLayout.SOUTH);
		setMinimumSize(new Dimension(900, 500));
	}

	public void result(ProjectResult result) {
		this.projectResult = result;
		configuration = null;
		tableModel = new ResultTableModel();
		tableResults.setModel(tableModel);
		setTableWidth(tableResults, 140, 75, 75, 75, 75, 75, 75, 75);
		setTableCellRenderer(tableResults, new DefaultRenderer());
		labelConfiguration.setText("none");
		tree(result);
	}

	public void resultTree(ProjectResult result, Configuration theConfiguration) {
		this.projectResult = result;
		configuration = theConfiguration;
		tableModel = new ConfiguredResultTableModel();
		tableResults.setModel(tableModel);
		setTableWidth(tableResults, 140, 90, 75, 70, 120, 95, 85, 70);
		setTableCellRenderer(tableResults, new ConfiguredResultTableCellRenderer());
		labelConfiguration.setText(configuration.getName());
		tree(result);
	}

	private static void setTableWidth(JTable table, int... columnWidths) {
		TableColumnModel columnModel = table.getColumnModel();
		int sumWidths = 0;
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
			if (i < columnWidths.length)
				column.setPreferredWidth(columnWidths[i]);
			sumWidths += column.getPreferredWidth();
		}
		table.setMinimumSize(new Dimension(sumWidths, 100));
	}

	private static void setTableCellRenderer(JTable table, TableCellRenderer renderer) {
		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			columnModel.getColumn(i).setCellRenderer(renderer);
		}
	}

	private ModuleResult selectedResult() {
		String projectName = projectResult.getProject().getName();
		String moduleName = selectedNode.getModule().getName();
		Date date = projectResult.getDate();
		ModuleResultDao moduleResultDao = Kalibro.getModuleResultDao();
		return moduleResultDao.getModuleResult(projectName, moduleName, date);
	}

	private void tree(ProjectResult result) {
		treeModel.setRoot(new SourceTreeNode(result.getSourceTree(), null));
		labelDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(result.getDate()));
		labelLoadTime.setText(new SimpleDateFormat("HH:mm:ss").format(result.getLoadTime()));
		labelAnalysisTime.setText(new SimpleDateFormat("HH:mm:ss").format(result.getAnalysisTime()));
	}

	private void createComponents() {
		createPassiveComponents();
		createActiveComponents();
	}

	private void createPassiveComponents() {
		labelConfiguration = new UneditableField<String>("configuration");
		labelConfiguration.setValue("none");

		labelWeightedMean = new UneditableField<String>("weightedMean");
		labelModuleName = new UneditableField<String>("moduleName");

		labelDate = new UneditableField<String>("loadDate");
		labelLoadTime = new UneditableField<Long>("loadTime");
		labelAnalysisTime = new UneditableField<Long>("analysisTime");
		textPaneComments = new TextField("comments", 4, 30, "Comments", true);
	}

	private void createActiveComponents() {
		createButtonChooseConfiguration();
		createButtonExport();
		createButtonRequestAnalysis();

		treeModel = new DefaultTreeModel(null);
		treePanel = new ResultTreePanel(treeModel);
		treePanel.addTreeSelectionListener(this);

		tableResults = new JTable();
		tableResults.setName("tableResults");
		tableResults.setShowGrid(true);
		tableResults.setAutoCreateRowSorter(true);
		tableResults.setColumnSelectionAllowed(false);
		tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableResults.getSelectionModel().addListSelectionListener(this);
	}

	private void createButtonChooseConfiguration() {
		buttonChooseConfiguration = new Button("chooseConfiguration", "Choose configuration", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				listener.chooseConfiguration();
			}
		});
	}

	private void createButtonExport() {
		buttonExport = new Button("export", "Export results", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				listener.exportResults(selectedResult());
			}
		});
		buttonExport.setEnabled(false);
	}

	private void createButtonRequestAnalysis() {
		buttonRequestAnalysis = new Button("requestAnalysis", "Request analysis", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				listener.requestAnalysis();
			}
		});
	}

	private JPanel northPanel() {
		JPanel northLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		northLeftPanel.add(new Label("Configuration:"));
		northLeftPanel.add(labelConfiguration);

		JPanel northRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		northRightPanel.add(buttonChooseConfiguration);
		northRightPanel.add(buttonExport);

		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.add(northLeftPanel, 1.0);
		builder.add(northRightPanel, 1.0);
		return builder.getPanel();
	}

	private JPanel centerPanel() {
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new TitledBorder(""));
		centerPanel.add(centerNorthPanel(), BorderLayout.NORTH);
		centerPanel.add(new JScrollPane(tableResults), BorderLayout.CENTER);
		centerPanel.add(textPaneComments, BorderLayout.SOUTH);
		return centerPanel;
	}

	private JPanel centerNorthPanel() {
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(labelModuleName);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.add(labelWeightedMean);

		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.add(leftPanel, 1.0);
		builder.add(rightPanel, 1.0);
		return builder.getPanel();
	}

	private JPanel southPanel() {
		JPanel southLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		southLeftPanel.add(new Label("Load date:"));
		southLeftPanel.add(labelDate);
		southLeftPanel.add(new Label("   Load time:"));
		southLeftPanel.add(labelLoadTime);
		southLeftPanel.add(new Label("   Analysis time:"));
		southLeftPanel.add(labelAnalysisTime);

		JPanel southRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southRightPanel.add(buttonRequestAnalysis);

		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.add(southLeftPanel, 1.0);
		builder.add(southRightPanel, 1.0);
		return builder.getPanel();
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		JTree tree = (JTree) event.getSource();
		selectedNode = (SourceTreeNode) tree.getLastSelectedPathComponent();
		if (selectedNode == null) {
			buttonExport.setEnabled(false);
			labelModuleName.setText("");
			labelWeightedMean.setText("");
			tableModel.moduleResult(null);
		} else {
			buttonExport.setEnabled(true);
			labelModuleName.setText(selectedNode.getModule().getName());
			ModuleResult result = selectedResult();
			tableModel.moduleResult(result);
			if (configuration != null) {
				result.setConfiguration(configuration);
				Double weightedMean = result.getGrade();
				labelWeightedMean.setText("Weighted mean: " + new DecimalFormat().format(weightedMean));
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting())
			return;
		ListSelectionModel model = (ListSelectionModel) event.getSource();
		int row = model.getMinSelectionIndex();
		if (row < 0)
			textPaneComments.setValue("");
		else
			setComments(row);
	}

	private void setComments(int row) {
		Range range = tableModel.range(row);
		if (range == null || range.getComments() == null || range.getComments().isEmpty())
			textPaneComments.setValue(tableModel.metricResult(row).getMetric().getDescription());
		else
			textPaneComments.setValue(range.getComments());
	}
}