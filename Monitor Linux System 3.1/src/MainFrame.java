import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

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
					frame.cputimeseries.startThread();
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
		comboBoxDisk.setEnabled(false);
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
						if(firstClick[2] == true) {
							diskioreadwrite.markTheState((String)(comboBoxDisk.getSelectedItem()));
						}
						break;
					case 3:
						//comboBoxDisk.removeAllItems();
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
						comboBoxDisk.setEnabled(false);
//						if(firstClick[0] == false) {
//							firstClick[0] = true;
//							cputimeseries.startThread();
//						}
						break;
					case 1:
						comboBoxDisk.setEnabled(false);
						if(firstClick[1] == false) {
							firstClick[1] = true;
							networkxyareachart.startThread();
						}
						break;
					case 2:
						comboBoxDisk.setEnabled(true);
						if(firstClick[2] == false) {
							firstClick[2] = true;
							diskioreadwrite.startThread();
							for(String t : diskioreadwrite.loadKind())
								comboBoxDisk.addItem(t);
						}
					
						break;
					case 3:
						comboBoxDisk.setEnabled(false);
						if(firstClick[3] == false) {
							firstClick[3] = true;
							memeoryareachart.startThread();
						}
					
						break;
					default :
						break;	
				}
			}  
		});
		
		tabbedPane.setBounds(0, 0, 683, 461);
		getContentPane().add(tabbedPane);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnQuit.setBounds(690, 265, 88, 23);
		getContentPane().add(btnQuit);

		//clear the dataset 
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
					default :
						break;	
				}
			}
		});
		button.setBounds(690, 166, 88, 23);
		getContentPane().add(button);
	}
}
