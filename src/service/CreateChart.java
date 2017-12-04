package service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import dao.ChartDao;
import entity.Chart;

public class CreateChart {
	private ChartDao chartDao = new ChartDao();
	/**
	 * @author w
	 * @return
	 */
	public String getChartPath() {
		String chartUrl = "chart.png";
		
		//生成报表
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		List<Map<String, Object>> list = this.getList();
		for(Map<String, Object> map : list) {
			dataSet.setValue((Integer) map.get("num"), (String) map.get("mn"), map.get("ym").toString());
		}
//		dataSet.addValue(1, "First", "2013");
//		dataSet.addValue(3, "First", "2014");
//		dataSet.addValue(2, "First", "2015");
//		dataSet.addValue(6, "First", "2016");
//		dataSet.addValue(5, "First", "2017");
//		dataSet.addValue(12, "First", "2018");
//		dataSet.addValue(14, "Second", "2013");
//		dataSet.addValue(13, "Second", "2014");
//		dataSet.addValue(12, "Second", "2015");
//		dataSet.addValue(9, "Second", "2016");
//		dataSet.addValue(5, "Second", "2017");
//		dataSet.addValue(7, "Second", "2018");
		// 创建主题样式,防止中文乱码
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
		// 如果把createLineChart改为createLineChart3D就变为了3D效果的折线图
		JFreeChart chart = ChartFactory.createLineChart("图表标题", "X轴标题", "Y轴标题", dataSet, PlotOrientation.VERTICAL, // 绘制方向
				true, // 显示图例
				true, // 采用标准生成器
				false // 是否生成超链接
		);
		Font titleFont = new Font("隶书", Font.BOLD, 20);
		Font font = new Font("宋书", Font.PLAIN, 15);
		
		chart.getTitle().setFont(titleFont); // 设置标题字体
		chart.getLegend().setItemFont(font);// 设置图例类别字体
		chart.setBackgroundPaint(Color.blue);// 设置背景色
		// 获取绘图区对象
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.LIGHT_GRAY); // 设置绘图区背景色
		plot.setRangeGridlinePaint(Color.WHITE); // 设置水平方向背景线颜色
		plot.setRangeGridlinesVisible(true);// 设置是否显示水平方向背景线,默认值为true
		plot.setDomainGridlinePaint(Color.WHITE); // 设置垂直方向背景线颜色
		plot.setDomainGridlinesVisible(true); // 设置是否显示垂直方向背景线,默认值为false

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(font); // 设置横轴字体
		domainAxis.setTickLabelFont(font);// 设置坐标轴标尺值字体
		domainAxis.setLowerMargin(0.01);// 左边距 边框距离
		domainAxis.setUpperMargin(0.06);// 右边距 边框距离,防止最后边的一个数据靠近了坐标轴。
		domainAxis.setMaximumCategoryLabelLines(2);

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(font);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// Y轴显示整数
		rangeAxis.setAutoRangeMinimumSize(1); // 最小跨度
		rangeAxis.setUpperMargin(0.18);// 上边距,防止最大的一个数据靠近了坐标轴。
		rangeAxis.setLowerBound(0); // 最小值显示0
		rangeAxis.setAutoRange(false); // 不自动分配Y轴数据
		rangeAxis.setTickMarkStroke(new BasicStroke(1.6f)); // 设置坐标标记大小
		rangeAxis.setTickMarkPaint(Color.BLACK); // 设置坐标标记颜色

		// 获取折线对象
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		BasicStroke realLine = new BasicStroke(1.8f); // 设置实线
		// 设置虚线
		float dashes[] = { 5.0f };
		BasicStroke brokenLine = new BasicStroke(2.2f, // 线条粗细
				BasicStroke.CAP_ROUND, // 端点风格
				BasicStroke.JOIN_ROUND, // 折点风格
				8f, dashes, 0.6f);
		for (int i = 0; i < dataSet.getRowCount(); i++) {
			if (i % 2 == 0)
				renderer.setSeriesStroke(i, realLine); // 利用实线绘制
			else
				renderer.setSeriesStroke(i, brokenLine); // 利用虚线绘制
		}

		plot.setNoDataMessage("无对应的数据，请重新查询。");//没有数据时显示的文字说明
		plot.setNoDataMessageFont(titleFont);// 字体的大小
		plot.setNoDataMessagePaint(Color.RED);// 字体颜色
		// 保存为图片
		String chartURL = ServletActionContext.getServletContext().getRealPath("/"); // 获取应用在硬盘上的根目录
		// System.out.println(chartURL);

		this.saveAsFile(chart, chartURL + "\\" + chartUrl, 600, 400);
		
		return chartUrl;
	}
	
	/**
	 * 将生成的报表做成图片
	 * 
	 * @author w
	 * @param chart
	 * @param outputPath
	 * @param weight
	 * @param height
	 */
	private void saveAsFile(JFreeChart chart, String outputPath, int weight, int height) {

		FileOutputStream out = null;

		try {
			File outFile = new File(outputPath);
			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdirs();
			}
			out = new FileOutputStream(outputPath);
			// 保存为PNG
			ChartUtilities.writeChartAsPNG(out, chart, weight, height);
			// 保存为JPEG
			// ChartUtilities.writeChartAsJPEG(out, chart, weight, height);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @author w
	 * @return
	 */
	private List<Map<String, Object>> getList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Chart> charts = chartDao.findAllChart();
		for(Chart temp : charts) {
			Map<String, Object> chartlist = new HashMap<String, Object>();
			chartlist.put("num", temp.getNum());
			chartlist.put("mn", temp.getMn());
			chartlist.put("ym", temp.getYm());
			list.add(chartlist);
		}
		return list;
	}
}
