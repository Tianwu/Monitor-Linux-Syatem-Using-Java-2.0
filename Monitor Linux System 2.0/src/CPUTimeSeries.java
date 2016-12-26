import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;


public class CPUTimeSeries implements Runnable{
	ChartPanel CPUframe;
	
	private TimeSeries timeseries1;
	private TimeSeries timeseries2;
	private TimeSeries timeseries3;
	static Thread thread;
	private double value1 ,value2, value3;
	private static String command = "iostat -c";   
	private LinuxCommand linuxcommand  = null;	   
	private BufferedReader in = null;
	
	public CPUTimeSeries(){
		 linuxcommand  = new LinuxCommand(command);
		 thread = new Thread(this);
		 XYDataset xydataset = createDataset();  
	     JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("CPU Total Usage","Time(s)","Value(%)",
	    		 xydataset,true,true,false);
	     jfreechart.setBackgroundPaint(Color.white);	     
	     
	     XYPlot xyplot = jfreechart.getXYPlot();       
	     ValueAxis valueaxis = xyplot.getDomainAxis();
	     valueaxis.setAutoRange(true);
	     valueaxis.setFixedAutoRange(60000D);
	     CPUframe = new ChartPanel(jfreechart,true); 
	}

	   
	//produce DataSet  
	private XYDataset createDataset() {
		timeseries1 = new TimeSeries("user%");
		timeseries2 = new TimeSeries("sys%");
		timeseries3 = new TimeSeries("iowait%");
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeseries1); 
		timeseriescollection.addSeries(timeseries2);
		timeseriescollection.addSeries(timeseries3);
		return timeseriescollection;
	}

	//return a new Panel
	public ChartPanel getChartPanel(){  
		   return CPUframe;    
	}

	public void startThread(){
		thread.start();
	}

	public void clearDataSet() {
		this.timeseries1.clear();
		this.timeseries2.clear();
		this.timeseries3.clear();
	}
	
	@Override
	public void run() {
		 while(true){  
	        try {  
	        	in = linuxcommand.getInfoReader();
	            String line = null; 
	            int cnt = 0;
	            while((line=in.readLine()) != null && ++cnt != 4){ 
	            } 
	            line = line.trim();  
	            String[] temp = line.split("\\s+");
	            //string to double
	            value1 = Double.parseDouble(temp[0]);
	            value2 = Double.parseDouble(temp[2]);
	            value3 = Double.parseDouble(temp[3]);
	            //fufill the timeseries
	            try {
				  Millisecond millisecond1 = new Millisecond();
			     timeseries1.add(millisecond1, value1);
			     timeseries2.add(millisecond1, value2);
			     timeseries3.add(millisecond1, value3);
			     Thread.sleep(1000);
			 }catch(InterruptedException e) {
				 e.printStackTrace();
			 }
	            in.close();  
	            linuxcommand.processDestroy();	             
	        } catch (IOException e) {  
	            e.printStackTrace();  
	       	}      	
		 }
	}    
}
