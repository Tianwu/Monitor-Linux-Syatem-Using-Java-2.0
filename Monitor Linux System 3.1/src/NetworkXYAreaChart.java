import java.text.SimpleDateFormat;
import java.util.Locale;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import java.io.BufferedReader;
import java.io.IOException;
import org.jfree.ui.HorizontalAlignment;  
import org.jfree.ui.RectangleEdge; 
import org.jfree.chart.title.TextTitle; 
import java.awt.Font; 
import org.jfree.ui.VerticalAlignment;

public class NetworkXYAreaChart implements Runnable{
	ChartPanel networkframe;
	
	private TimeSeries timeseries1;
	private TimeSeries timeseries2;
	//private TimeSeries timeseries3;
	static Thread thread;
	private double value1 ,value2;
	private long rece1, rece2, tran1, tran2;

	private LinuxCommand linuxcommand  = null;  
	private BufferedReader in = null;
	private final String command = "/proc/net/dev";   
 
	public NetworkXYAreaChart(){
		 thread = new Thread(this);
		 linuxcommand  = new LinuxCommand(command);
		 XYDataset xydataset = createDataset(); 
		 JFreeChart jfreechart = ChartFactory.createXYAreaChart(
	      "Network Receive&Transmit", 
	      "Time(s)", 
	      "Data(KB)", 
	      xydataset, 
	      PlotOrientation.VERTICAL, 
	      true, 
	      false, 
	      false);

		//jfreechart.getTitle().setFont(new Font("Serif", 0, 16));
	    XYPlot xyplot1 = jfreechart.getXYPlot();
	    xyplot1.setForegroundAlpha(0.6F);
	    DateAxis domainAxis1 = new DateAxis("");
	    domainAxis1.setDateFormatOverride(new SimpleDateFormat("MM/dd HH:ss", Locale.PRC));
	    domainAxis1.setLowerMargin(0.001D);
	    domainAxis1.setUpperMargin(0.001D);
	    xyplot1.setDomainAxis(domainAxis1);
//
        TextTitle copyright = new TextTitle("Copyright@Hao Wang");  
        copyright.setPosition(RectangleEdge.BOTTOM);  
        copyright.setHorizontalAlignment(HorizontalAlignment.RIGHT);  
        copyright.setFont(new Font("Serif", 12, 12));  
        jfreechart.addSubtitle(copyright);
//	
	TextTitle copyright1 = new TextTitle("to show the infomation about the Linux Network of the Linux Fastnet receive per Second/KB and transmit per Second/KB by using command </proc/net/dev>");  
        copyright1.setPosition(RectangleEdge.TOP);  
        copyright1.setVerticalAlignment(VerticalAlignment.BOTTOM);  
        copyright1.setFont(new Font("Serif", 12, 12));  
        jfreechart.addSubtitle(copyright1); 	
	    //this.initReceTran();
	    networkframe = new ChartPanel(jfreechart,true); 
	}

	   
	//produce DataSet  
	private XYDataset createDataset() {
		//timeseries1 = new TimeSeries("receive",Millisecond.class);
		//timeseries2 = new TimeSeries("transmit",Millisecond.class);
		timeseries1 = new TimeSeries("receive");
		timeseries2 = new TimeSeries("transmit");
		//timeseries3 = new TimeSeries("wait%",Millisecond.class);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeseries1); 
		timeseriescollection.addSeries(timeseries2);
		//timeseriescollection.addSeries(timeseries3);
		return timeseriescollection;
	}

	public void clearDataSet() {
		timeseries1.clear();
		timeseries2.clear();
		//timeseries3.clear();
	}

	//return a new Panel
	public ChartPanel getChartPanel(){  
		   //startThread();
		   return networkframe;    
	}

	public void startThread(){
		this.initReceTran();
		thread.start();
	}

	private void initReceTran() {
		in = linuxcommand.getInfoReader2();
		String line = null; 
		try {
			line = in.readLine();
			line = in.readLine();
			line = in.readLine();//the data we looking for
			in.close(); 
			String[] temp = line.split("[\\s]+");
			rece1 = Long.parseLong(temp[1]);
			tran1 = Long.parseLong(temp[9]);
		} catch (IOException e2) {
			e2.printStackTrace();
		}	
	}

	@Override
	public void run() {
		while(true) {
			in = linuxcommand.getInfoReader2();
			String line = null; 
			try {
				line = in.readLine();
				line = in.readLine();
				line = in.readLine();//the data we looking for
				in.close(); 
				//linuxcommand.processDestroy();
				String[] temp = line.split("[\\s]+");
				rece2 = Long.parseLong(temp[1]);
				tran2 = Long.parseLong(temp[9]);
				//System.out.println(rece1 + " " + tran1);
			} catch (IOException e2) {
				e2.printStackTrace();
			}		
			//
			value1 = (rece2 - rece1) * 8.0D / 1024;
			value2 = (tran2 - tran1) * 8.0D / 1024;
			//System.out.println(value1 + " " + value2);
			rece1 = rece2;
			tran1 = tran2;
            //value3 = 100.0D*Math.random();
			 Millisecond millisecond = new Millisecond();
		     timeseries1.add(millisecond, value1);
		     timeseries2.add(millisecond, value2);
		     //timeseries3.add(millisecond1, value3);
		     try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}    
	   
}

