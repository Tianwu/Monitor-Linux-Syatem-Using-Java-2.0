import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.BufferedReader;
import java.io.IOException;

public class MemoryAreaChart implements Runnable{
	ChartPanel CPUframe;
	static Thread thread;

	private LinuxCommand linuxcommand  = null;  
	private BufferedReader in = null;
	private final String command = "/proc/meminfo";   
	private long[] mem = new long[4];
	private double[] value = new double[20];
	DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset(); 
	
	public MemoryAreaChart(){
		linuxcommand  = new LinuxCommand(command);
		 thread = new Thread(this);
		 
		 final CategoryDataset dataset = getDataSet();
	     JFreeChart jfreechart = ChartFactory.createAreaChart(
	    		 "Memory Total Usage",
	    		 "Time(s)",
	    		 "Value(%)",
	    		 dataset,
	    		 PlotOrientation.VERTICAL,
	    		 true,
	    		 true,
	    		 false);
	    jfreechart.setBackgroundPaint(Color.white);	     
	    final CategoryPlot plot = jfreechart.getCategoryPlot();
        plot.setForegroundAlpha(0.5f);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    rangeAxis.setLabelAngle(0 * Math.PI / 2.0);
	     CPUframe = new ChartPanel(jfreechart,true); 
	}

	private CategoryDataset getDataSet() {  
		for(int i=0 ; i<20 ; ++i) {
			value[i] = 0.0D;
			categoryDataset.addValue(value[i], "", ""+i);
		}
      return categoryDataset;  
    }  

	//return a new Panel
	public ChartPanel getChartPanel(){  
		   return CPUframe;    
	}

	public void startThread(){
		thread.start();
	}
	
	public void clearDateSet() {
		categoryDataset.clear();
	}
	
	@Override
	public void run() {
		while(true) {
					in = linuxcommand.getInfoReader2();
//only read four lines
					String line = null; 
					try {
						for(int i=0 ; i<4 ; ++i) {
							line = in.readLine();
							mem[i] = Long.parseLong(line.split("\\s+")[1]);
						} 
						in.close();  
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					//fufill the dataset			
					for(int i=0 ; i<19 ; ++i) {
						value[i] = value[i+1];
						categoryDataset.addValue(value[i], "", "" + i);
					}
					value[19] = 100.0D * (mem[0] - mem[1] - mem[2] - mem[3]) / mem[0];
					categoryDataset.addValue(value[19], "", "19");
			     try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
							e.printStackTrace();
					}
					
				}	
		}    
	   
}

