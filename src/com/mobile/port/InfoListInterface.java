package com.mobile.port;


import com.mobile.model.InfoList;
import com.mobile.service.InfoListService;
import com.mobile.util.Configuration;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * 新闻接口类
 * @author zq
 * @version 1.0
 * @since 2019-1-3 10:00:18
 *
 */
@Controller
public class InfoListInterface extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1144366992613969902L;
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
		this.response.setContentType("text/html;charset=utf-8");
	}
	
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}
	
	@Resource(name = "infoListService")
	private InfoListService infoListService;
	
	
	/**
	 * 获取焦点图
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping("imgpics")
	public String imgpics(HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException{
		PrintWriter out;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		out = response.getWriter();
		StringBuffer s = new StringBuffer();
		try {
			List<InfoList> lists = this.infoListService.getByHql("from InfoList where type = 1 order by orderid, posttime desc");
			
			for(InfoList info : lists){
				s.append("{");
				s.append("\"img\":\"" + info.getPic() + "\",");
				s.append("\"title\":\"" + info.getTitle() + "\",");
				s.append("\"url\":\"" + (info.getUrl() == null ? "" : info.getUrl()) + "\"");
				s.append("},");
			}
			if(s.length()>0){
				out.print("{\"state\":\"true\",\"data\":[" + s.deleteCharAt(s.length()-1) + "]}");
			}
			else{
				out.print("{\"state\":\"nodata\",\"data\":[]}");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.print("{\"state\":\"error\",\"data\":[]}");
		}
			
			out.close();
			
         return null;
	}
	
	/**
	 * 获取迎新引导，包含新生手册
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public String entrance() throws IOException, SQLException, ClassNotFoundException{
		PrintWriter out;
		this.request.setCharacterEncoding("utf-8");
		this.response.setContentType("text/html;charset=utf-8");
		out = response.getWriter();
		StringBuffer s = new StringBuffer();
		StringBuffer hdStr = new StringBuffer();
		try {
			//新生手册
			List<InfoList> handbooks = this.infoListService.getByHql("from InfoList where type = 7 order by orderid, posttime desc");
			//迎新引导
			List<InfoList> guid = this.infoListService.getByHql("from InfoList where type = 6 order by orderid, posttime desc");
			s.append("{");
			s.append("\"title\":\"新生手册\",");
			s.append("\"navigation\":\"0\",");
			s.append("\"lonlat\":\"\",");
			s.append("\"index\":\"1\",");
			s.append("\"childData\":[");
			for(InfoList hd : handbooks){
				hdStr.append("{");
				hdStr.append("\"content\":\"" + hd.getTitle() + "\",");
				hdStr.append("\"url\":\"" + Configuration.getInstance().getValue("basepath") + "mobile/handBookManage_view?model.infoid=" + hd.getInfoid() + "\"");
				hdStr.append("},");
			}
			if(hdStr.length() > 0){
				s.append(hdStr.deleteCharAt(hdStr.length() - 1));
			}
			s.append("]");
			s.append("},");
			for(InfoList gd : guid){
				s.append("{");
				s.append("\"title\":\"" + gd.getTitle() + "\",");
				s.append("\"navigation\":\"" + gd.getIsnavigation() + "\",");
				s.append("\"lonlat\":\"" + gd.getLonlat() + "\",");
				s.append("\"index\":\"0\",");
				s.append("\"childData\":[");
				s.append("{");
				s.append("\"content\":\"" + gd.getContent().replaceAll(":", "：").replaceAll("\"", "&quot;").replaceAll("[\\t\\n\\r]", "") + "\",");
				s.append("\"url\":\"\"");
				s.append("}");
				s.append("]");
				s.append("},");
			}
			s.append("{");
			s.append("\"title\":\"学籍资料\",");
			s.append("\"navigation\":\"0\",");
			s.append("\"lonlat\":\"\",");
			s.append("\"index\":\"-1\",");
			s.append("\"childData\":[]");
			s.append("}");
			out.print("{\"state\":\"true\",\"data\":[" + s + "]}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.print("{\"state\":\"error\",\"data\":[]}");
		}
			
			out.close();
			
         return null;
	}

}
