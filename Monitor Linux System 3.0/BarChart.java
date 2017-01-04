import java.awt.Font;   
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.CategoryAxis;  
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.plot.CategoryPlot;  
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.data.category.CategoryDataset;  
import org.jfree.data.category.DefaultCategoryDataset;  
  
public class BarChart {  
    ChartPanel frame1;  
    public  BarChart(){  
        CategoryDataset dataset = getDataSet();  
        JFreeChart chart = ChartFactory.createBarChart3D(  
                             "people", 
                            "kind",
                            "num",
                            dataset,
                            PlotOrientation.VERTICAL, 
                            true,            
                            false,         
                            false          
                            );  
          

        CategoryPlot plot=chart.getCategoryPlot();
        CategoryAxis domainAxis=plot.getDomainAxis();      
         domainAxis.setLabelFont(new Font("Serif",Font.BOLD,11));      
         domainAxis.setTickLabelFont(new Font("Serif",Font.BOLD,8));
         ValueAxis rangeAxis=plot.getRangeAxis();
         rangeAxis.setLabelFont(new Font("Serif",Font.BOLD,11));  
          chart.getLegend().setItemFont(new Font("Serif", Font.BOLD, 11));  
          chart.getTitle().setFont(new Font("Serif",Font.BOLD,16));
               
         frame1=new ChartPanel(chart,true);        
           
    }  
    private static CategoryDataset getDataSet() {  
           DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
           dataset.addValue(100, "1", "wang");  
           dataset.addValue(100, "2", "wang");  
           dataset.addValue(100, "3", "wang");  
           dataset.addValue(200, "1", "ren");  
           dataset.addValue(200, "2", "ren");  
           dataset.addValue(200, "3", "ren");  
           dataset.addValue(300, "1", "liu");  
           dataset.addValue(300, "3", "liu");  
           dataset.addValue(300, "2", "liu");  
           dataset.addValue(400, "1", "huang");  
           dataset.addValue(400, "3", "huang");  
           dataset.addValue(400, "2", "huang");  
           dataset.addValue(400, "1", "jiang");  
           dataset.addValue(400, "3", "jiang");  
           dataset.addValue(400, "2", "jiang");  
           return dataset;  
}  
	public ChartPanel getChartPanel(){  
	    return frame1;    
	}  
}  