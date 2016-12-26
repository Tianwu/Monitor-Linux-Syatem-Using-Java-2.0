import java.awt.Font;  
import java.text.DecimalFormat;  
import java.text.NumberFormat;  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;  
import org.jfree.chart.plot.PiePlot;  
import org.jfree.data.general.DefaultPieDataset;  
  
public class PieChart {  
	private ChartPanel frame1;  
    private JFreeChart chart = null ; 
    private DefaultPieDataset data = null;
    
    public PieChart(){  
          data = getDataSet();  
          chart = ChartFactory.createPieChart3D("Disk Info",data,true,false,false); 

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
           
          chart.getTitle().setFont(new Font("Serif",Font.BOLD,15));
          PiePlot piePlot= (PiePlot) chart.getPlot();
          piePlot.setLabelFont(new Font("Serif",Font.BOLD,10));
          chart.getLegend().setItemFont(new Font("Serif",Font.BOLD,10));  
    }  
    
    private static DefaultPieDataset getDataSet() {  
        DefaultPieDataset dataset = new DefaultPieDataset();  
        dataset.setValue("wanghao",100);  
        dataset.setValue("renlei",200);  
//        dataset.setValue("liuqian",300);  
//        dataset.setValue("jiangzhiqang",400);  
//        dataset.setValue("huangjinlong",500);  
        return dataset;  
    } 
    
    public void refresh() {
    	DefaultPieDataset dataset = new DefaultPieDataset();  
        dataset.setValue("wanghao",500);  
        dataset.setValue("renlei",200);  
        dataset.setValue("liuqian",300);  
        dataset.setValue("jiangzhiqang",400);  
        dataset.setValue("huangjinlong",400);
    	data = dataset;
    }
    
    public ChartPanel getChartPanel(){  
    	frame1=new ChartPanel(chart,true);
    	return frame1;    
    }  
}  