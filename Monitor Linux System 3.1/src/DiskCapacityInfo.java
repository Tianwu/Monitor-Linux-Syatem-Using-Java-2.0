import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class DiskCapacityInfo {
	
	 private ChartPanel frame;
	 
	 private String command  = "df -hl";
	 private LinuxCommand linuxcommand  = new LinuxCommand(command);
	 private DefaultPieDataset dataset = new DefaultPieDataset();	
	 private JFreeChart chart = null;
	 List<String[]> myList = new ArrayList<String[]>();
		
	 public DiskCapacityInfo(){  
		 setDataSet();
         chart = ChartFactory.createPieChart3D("Disk Info",dataset,true,false,false);  
         this.configure(chart);
         frame = new ChartPanel (chart, true); 
	 }  
	 
	    private void configure(JFreeChart chart) {
	          PiePlot pieplot = (PiePlot) chart.getPlot();  
	          DecimalFormat df = new DecimalFormat("0.00%");
	          NumberFormat nf = NumberFormat.getNumberInstance();
	          StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {2}", nf, df);  
	          pieplot.setLabelGenerator(sp1);

	          pieplot.setNoDataMessage("none data");  
	          pieplot.setCircular(false);  
	          pieplot.setLabelGap(0.02D);  
	        
	          pieplot.setIgnoreNullValues(true);
	          pieplot.setIgnoreZeroValues(true);
	           
	          chart.getTitle().setFont(new Font("Serif",Font.BOLD,20));
	          PiePlot piePlot= (PiePlot) chart.getPlot();
	          piePlot.setLabelFont(new Font("Serif",Font.BOLD,10));
	          chart.getLegend().setItemFont(new Font("Serif",Font.BOLD,10)); 
	    }
	 
	    private void setDataSet() {  
	    	BufferedReader in = linuxcommand.getInfoReader();
	    	String line = null;
	    	myList.clear();
	    	//read the first line to throw(skip one line)
	    	try {
				line = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	try {
				while((line = in.readLine()) != null) {
					String[] temp = line.split("[\\s]+");
					if(temp.length == 6) {
						temp[1].replaceAll("[GMK]", "");
						temp[2].replaceAll("[GM]", "");
						temp[3].replaceAll("[GM]", "");
						myList.add(temp);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	        dataset.setValue("used",Double.parseDouble(myList.get(0)[2]));  
	        dataset.setValue("usable",Double.parseDouble(myList.get(0)[3])); 
	    } 
	    
	    public ChartPanel getChartPanel(String kind) {  

	       for(String[] t : myList) {
	    	   	if(t[0].equals(kind)) { 
	    	   		dataset.clear();
    	   			dataset.setValue("used",Double.parseDouble(t[2]));  
    	   			dataset.setValue("usable",Double.parseDouble(t[2])); 
    		        chart = ChartFactory.createPieChart3D("Disk Info",dataset,true,false,false);  
    		        this.configure(chart);
    		        frame = new ChartPanel (chart, true); 
    	   			return frame;  
	    	   	}
	       }
	       return null; 
	
	    } 

	    /*
	     * return the Panel after switch the kind
	     */
		public void refresh() {
			
	        chart = ChartFactory.createPieChart3D("Disk Info",dataset,true,false,false); 
	        this.configure(chart);
	        frame = new ChartPanel (chart, true); 
	    }
	    
	    /*
	     * return the Panel
	     */
	    public ChartPanel getChartPanel(){  
	        return frame;    
	    }  
}
