import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;  
import org.jfree.ui.RectangleEdge; 
import org.jfree.chart.title.TextTitle; 
import java.awt.Font; 
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.ui.VerticalAlignment;

public class DiskIOReadWrite implements Runnable{
	ChartPanel DiskIOFrame;
	
	private TimeSeries timeseries1;
	private TimeSeries timeseries2;
	private TimeSeries timeseries3;

	static Thread thread;
	private double value1 ,value2;
	private static String command = "iostat -k -d -x";   
	private LinuxCommand linuxcommand  = null;  
	private BufferedReader in = null;
	private String state = "all";
	private Boolean changed = false;
	private ArrayList<String>  kinds = new ArrayList<String>(); 

	
	public DiskIOReadWrite(){
		linuxcommand  = new LinuxCommand(command);
		 thread = new Thread(this);
		 XYDataset xydataset = createDataset();  
	     JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("Disk IO Read&Write","Time(s)","Value(kb/s)",
	    		 xydataset,true,true,false);
	     jfreechart.setBackgroundPaint(Color.white);	     
	     
	     XYPlot xyplot = jfreechart.getXYPlot();  
	     ValueAxis valueaxis = xyplot.getDomainAxis();
	     valueaxis.setAutoRange(true);
	     valueaxis.setFixedAutoRange(60000D);


NumberAxis axis2 = new NumberAxis("Sum of R&W Value(kb/s)");  

axis2.setAxisLinePaint(Color.PINK);  
axis2.setLabelPaint(Color.PINK);  
axis2.setTickLabelPaint(Color.PINK);  

xyplot.setRangeAxis(1, axis2);  
xyplot.setDataset(1, this.createDataset2());   
xyplot.mapDatasetToRangeAxis(1, 1);  /**/
//XYSplineRenderer  XYLineAndShapeRenderer
XYSplineRenderer render2 =  new XYSplineRenderer();   
render2.setSeriesPaint(0, Color.PINK);  
xyplot.setRenderer(1, render2); 

//set title
        TextTitle copyright = new TextTitle("Copyright@Hao Wang");  
        copyright.setPosition(RectangleEdge.BOTTOM);  
        copyright.setHorizontalAlignment(HorizontalAlignment.RIGHT);  
        copyright.setFont(new Font("Serif", 12, 12));  
        jfreechart.addSubtitle(copyright);
//
	  TextTitle copyright1 = new TextTitle("to show the infomation about the Linux Disk Input and output of the Disk Input per Second/KB and Disk Output per Second/KB by using command <iostat -k -d -x>");  
        copyright1.setPosition(RectangleEdge.TOP);  
        copyright1.setVerticalAlignment(VerticalAlignment.BOTTOM);  
        copyright1.setFont(new Font("Serif", 12, 12));  
        jfreechart.addSubtitle(copyright1);  
	     DiskIOFrame = new ChartPanel(jfreechart,true); 
	}

	   
	public void clearDataSet() {
		this.timeseries1.clear();
		this.timeseries2.clear();
	}

	//produce DataSet  
	private XYDataset createDataset() {
		timeseries1 = new TimeSeries("Disk Read");
		timeseries2 = new TimeSeries("Disk Write");
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeseries1); 
		timeseriescollection.addSeries(timeseries2);
		return timeseriescollection;
	}

	private XYDataset createDataset2() {
		timeseries3 = new TimeSeries("R&W per sec");
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeseries3);
		return timeseriescollection;
	}

	private void getKinds() {
		kinds.add("all");
		in = linuxcommand.getInfoReader(); 
		String line = null;
		try {//skip three lines
			line = in.readLine();
			line = in.readLine();
			line = in.readLine();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			while((line = in.readLine()) != null) {
				if(line.equals("") == true)
					break;
				String[] temp = line.split("[\\s]+");
				kinds.add(temp[0]);
			}
			in.close();
			linuxcommand.processDestroy();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}

	public ArrayList<String> loadKind() {
		return kinds;
	}

	//return a new Panel
	public ChartPanel getChartPanel(){  
		   //startThread();
		   return DiskIOFrame;    
	}

	public void startThread(){
		this.getKinds();
		thread.start();
	}
	
	public void markTheState(String state) {
		if(this.state.equals(state) == false) {
			this.changed = true;
			this.state = state;
		}
	}
	
	private void fillTimeSeries(double value1, double value2) {
		 Millisecond millisecond1 = new Millisecond();
	     this.timeseries1.add(millisecond1, value1);
	     this.timeseries2.add(millisecond1, value2);
this.timeseries3.add(millisecond1, value1 + value2);
	}
	
	@Override
	public void run() {
		while(true) {
			//prepare the collect
			in = linuxcommand.getInfoReader(); 
			String line = null;
			try {//skip three lines
				line = in.readLine();
				line = in.readLine();
				line = in.readLine();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			//collect the data
			if(this.changed == true) {
				this.timeseries1.clear();
				this.timeseries2.clear();
this.timeseries3.clear();
				value1 = value2 = 0.0D;
				//change the show state
				this.changed = false;
			}
			if(state.equals("all") == true) {
				try {
value1 = value2 = 0.0D;
					while((line = in.readLine()) != null) {
						if(line.equals("") == true)
							break;						
						String[] temp = line.split("[\\s]+");
						value1 += Double.parseDouble(temp[5]);
			         value2 += Double.parseDouble(temp[6]);
					}
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}   
			}else {
				try {
					while((line = in.readLine()) != null) {
						if(line.equals("") == true)
							break;
						String[] temp = line.split("[\\s]+");
						if(temp[0].equals(this.state)) {
							value1 = Double.parseDouble(temp[5]);
							value2 = Double.parseDouble(temp[6]);
							break;
						}
					}	
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}   
			}
			//
			this.fillTimeSeries(value1, value2);
			 try {
				Thread.sleep(1000);
			 } catch (InterruptedException e) {
				e.printStackTrace();
			 }
			linuxcommand.processDestroy();
		}
	} 

}
