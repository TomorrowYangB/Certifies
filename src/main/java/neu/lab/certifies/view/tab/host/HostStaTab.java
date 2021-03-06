package neu.lab.certifies.view.tab.host;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import neu.lab.certifies.sta.HostMSta;
import neu.lab.certifies.sta.HostSta;
import neu.lab.certifies.sta.LibSta;
import neu.lab.certifies.sta.SysJarSta;
import neu.lab.certifies.view.Screen;
import neu.lab.certifies.view.ViewCons;
import neu.lab.certifies.view.listener.MouseInOutListener;
import neu.lab.certifies.view.tab.host.StaTableModel;

public class HostStaTab extends JPanel {
	private static final long serialVersionUID = 1L;

	JPanel ckBoxsP;

	JScrollPane tableP;
	JTable t;

	List<TableColumn> clsColumns;
	List<TableColumn> mthdColumns;

	public HostStaTab() {
		super();
		initTableP();
		initCkboxP();
		this.setLayout(new BorderLayout());
		this.add(ckBoxsP, BorderLayout.NORTH);
		this.add(tableP, BorderLayout.CENTER);
	}

	private void initCkboxP() {
		ckBoxsP = new JPanel();
		ckBoxsP.setLayout(new FlowLayout(FlowLayout.LEFT));
		Box b = new Box(BoxLayout.X_AXIS);
		b.add(Box.createHorizontalStrut(10));
		b.add(Box.createHorizontalGlue());

		ckBoxsP.add(b);
	}

	private void initTableP() {
		HostMSta hostmSta = new HostMSta(Screen.i().sysInfo);
		tableP = new JScrollPane();
		
		Object[][] rows = new Object[hostmSta.hostMs.size()][1];
		int i = 0;
		for (String hostM : hostmSta.hostMs) {
			rows[i][0] = hostM;
			i++;
		}
		
		StaTableModel tModel = new StaTableModel(rows);
		t = new JTable(tModel);
		addHeadListner();

		setTableFont();
		tableP.setViewportView(t);
	
		

		
	}

	private void addHeadListner() {
		t.getTableHeader().addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int col = t.columnAtPoint(e.getPoint());
				if (col > -1) {
					String head = t.getColumnModel().getColumn(col).getHeaderValue().toString();
					Screen.i().updateLabel(StaTableModel.head2tip.get(head));
				}
			}
		});
		t.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				Screen.i().updateLabel("");
			}
		});

	}

	private List<TableColumn> getColumnList(String suffix) {
		List<TableColumn> result = new ArrayList<TableColumn>();
		for (int i = 0; i < t.getColumnCount(); i++) {
			String columnName = t.getColumnName(i);
			if (columnName.endsWith(suffix))
				result.add(t.getColumnModel().getColumn(i));
		}
		return result;
	}

	private void setTableFont() {
		t.setRowHeight(ViewCons.TAB_ROW_H);
		t.setFont(t.getFont().deriveFont((float) 18.0));
		t.setPreferredScrollableViewportSize(new Dimension(ViewCons.FRAME_W, 0));
		t.getColumnModel().getColumn(0).setPreferredWidth(300);// 设置第一列的宽度
		
		JTableHeader h = t.getTableHeader();
		h.setFont(h.getFont().deriveFont((float) 16.0));
		RowSorter sorter = new TableRowSorter(t.getModel());
		t.setRowSorter(sorter);
	}

	private JCheckBox getMthdCkBox(String text) {
		JCheckBox checkBox = new JCheckBox(text);
		checkBox.setSelected(true);
		checkBox.addMouseListener(new MouseInOutListener("Method Level"));
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox box = (JCheckBox) e.getItem();
				if ("class".equals(box.getText())) {
					changeColumns(box.isSelected(), clsColumns);
				}
				if ("method".equals(box.getText())) {
					changeColumns(box.isSelected(), mthdColumns);
				}

			}
		});
		return checkBox;
	}

	private JCheckBox getClsCkBox(String text) {
		JCheckBox checkBox = new JCheckBox(text);
		checkBox.addMouseListener(new MouseInOutListener("Class Level"));
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox box = (JCheckBox) e.getItem();
				changeColumns(box.isSelected(), clsColumns);
			}
		});
		return checkBox;
	}

	/**
	 * 改变table中的包含在columns中的列
	 * 
	 * @param add
	 *            ：true-向table中添加列；false：从table中删除列
	 * @param columns
	 */
	private void changeColumns(boolean add, List<TableColumn> columns) {
		TableColumnModel cModel = t.getColumnModel();
		if (add) {
			for (TableColumn c : columns)
				cModel.addColumn(c);
		} else {
			for (TableColumn c : columns)
				cModel.removeColumn(c);
		}
	}

}
// DefaultTableCellRenderer render = DefaultTableCellRenderer(){
// public Component getTableCellRenderComponent(JTable table,Object
// value,boolean isSelected,)
// }
