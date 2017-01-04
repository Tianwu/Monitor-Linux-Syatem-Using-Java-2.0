import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridLayout;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean[] firstClick = new Boolean[5];
	private CPUTimeSeries cputimeseries = new CPUTimeSeries();
	private NetworkXYAreaChart networkxyareachart = new NetworkXYAreaChart();
	private DiskIOReadWrite diskioreadwrite = new DiskIOReadWrite();
	private MemoryAreaChart memeoryareachart = new MemoryAreaChart();
	
	private PieChart piechart = new PieChart();
	//private DiskCapacityInfo diskcapacity = new DiskCapacityInfo();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
firstClick[0] = firstClick[1] = firstClick[2] = firstClick[3] = firstClick[4] = false;
		setResizable(false);
		setTitle("Monitor Linux System 3.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 799, 490);
		getContentPane().setLayout(null);
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		final JComboBox<String> comboBoxDisk = new JComboBox<String>();
		//final JComboBox comboBoxDisk = new JComboBox();

		comboBoxDisk.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch(tabbedPane.getSelectedIndex()) {
					case 0:
						//comboBoxDisk.removeAllItems();
						break;
					case 1:
						//comboBoxDisk.removeAllItems();
						break;
					case 2:
						diskioreadwrite.markTheState((String)(comboBoxDisk.getSelectedItem()));
						break;
					case 3:
						//comboBoxDisk.removeAllItems();
						break;
					case 4:
						break;
					case 5:
						break;
					default :
						break;	
				}

			}
		});
		comboBoxDisk.setBounds(690, 103, 91, 23);
		getContentPane().add(comboBoxDisk);
		
		tabbedPane.addTab("CPU", null, cputimeseries.getChartPanel(), "CPU\u7684\u4F7F\u7528\u7387\uFF08%\uFF09");
		
		tabbedPane.addTab("Network", null, networkxyareachart.getChartPanel(), "\u7F51\u7EDC\u6BCF\u79D2R&T\u7684\u6570\u636E\u91CF\uFF08KB\uFF09");
		
		tabbedPane.addTab("Disk",null, diskioreadwrite.getChartPanel(),"\u78C1\u76D8IO\u7684\u6BCF\u79D2\u8BFB\u5199\u6570\u636E\u91CF\uFF08KB\uFF09");
		
		tabbedPane.addTab("Memory", null, memeoryareachart.getChartPanel(), "\u5185\u5B58\u4F7F\u7528\u7387\uFF08%\uFF09");
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				//System.out.println(tabbedPane.getSelectedIndex());			
//first click , then start the Thread
				switch(tabbedPane.getSelectedIndex()) {
					case 0:
		
						
						if(firstClick[0] == false) {
							firstClick[0] = true;
							cputimeseries.startThread();
						}
						break;
					case 1:
						
						if(firstClick[1] == false) {
							firstClick[1] = true;
							networkxyareachart.startThread();
						}
						break;
					case 2:
						
						if(firstClick[2] == false) {
							firstClick[2] = true;

							diskioreadwrite.startThread();
							for(String t : diskioreadwrite.loadKind())
								comboBoxDisk.addItem(t);
						}
						
						//comboBoxDisk.removeAllItems();
						//for(String t : diskioreadwrite.loadKind())
						//	comboBoxDisk.addItem(t);
						break;
					case 3:
					
						if(firstClick[3] == false) {
							firstClick[3] = true;
							memeoryareachart.startThread();
						}
					
						break;
					case 4:
						//updateComboBoxDisk();
						//comboBoxDisk.setEnabled(true);
						//comboBoxDisk.removeAll();;
						//comboBoxDisk.addItem("nihasadfasdfasdfasdfsao");
						//comboBoxDisk.addItem("why");
						//panelDisk2.add(piechart.getChartPanel());
						//refresh();
						break;
					case 5:
						//panelDiskSummary.add(piechart.getChartPanel());
						//panelDiskIO.add(piechart.getChartPanel());
//						if(myPanelDiskCapacity == null)
//						{
//							myPanelDiskCapacity = diskcapacity.getChartPanel();
//							panelDiskCapacityInfo.add(myPanelDiskCapacity);
//						}
					default :
						break;	
				}
			}  
		});
		
		tabbedPane.setBounds(0, 0, 683, 461);
		getContentPane().add(tabbedPane);
		

		JButton btnButton = new JButton("Refresh");
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				piechart.refresh();
			}
		});
		
		btnButton.setBounds(690, 217, 88, 23);
		getContentPane().add(btnButton);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnQuit.setBounds(690, 265, 88, 23);
		getContentPane().add(btnQuit);
		

		

		JButton button = new JButton("Clear");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(tabbedPane.getSelectedIndex()) {
					case 0:
						cputimeseries.clearDataSet();
						break;
					case 1:
						networkxyareachart.clearDataSet();
						break;
					case 2:
						diskioreadwrite.clearDataSet();
						break;
					case 3:
						memeoryareachart.clearDateSet();
						break;
					case 4:
						break;
					case 5:
						break;
					default :
						break;	
				}
			}
		});
		button.setBounds(690, 166, 88, 23);
		getContentPane().add(button);
	}
}
