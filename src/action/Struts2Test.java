package action;


import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import service.CreateChart;

@SuppressWarnings("serial")
public class Struts2Test extends ActionSupport {
	private String url;
	private CreateChart createChart = new CreateChart();

	@Override
	public String execute() throws Exception {
		//获取应用的上文路径
		String chartURL = ServletActionContext.getServletContext().getContextPath();
		url = chartURL + "/" + createChart.getChartPath();
		return "success";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	
}
