package cn.fun.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ModelDriven;

import cn.fun.common.BaseAction;
import cn.fun.entity.Driver;
import cn.fun.service.BizService;
import util.Constant;
import util.FieldUtil;
import util.MessageUtil;
import util.Page;
import util.SearchParamBean;
import util.StringUtil;

@ParentPackage("struts-default")
@Namespace("/sys")
@Component
public class DriverAction extends BaseAction implements ModelDriven<Driver> {
	private String		actionname1		= "司机";
	private String		actionclass1	= "Driver";
	@Autowired
	private BizService	service;

	private int			uid;
	private Driver		bean			= new Driver();

	@Action(value = "add2Driver", results = { @Result(name = "add2", location = "/ahtml/addDriver.jsp") })
	public String add2() {
		return "add2";
	}

	@Action(value = "getDriver", results = { @Result(name = "getOne", location = "/ahtml/modifyDriver.jsp") })
	public String get() {
		try {
			Driver temp = (Driver) service.get(Driver.class, uid);
			putRequestValue("modifybean", temp);
			return "getOne";
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	@Action(value = "deleteDriver")
	public String delete() {
		try {
			service.delete(Driver.class, ids);
			MessageUtil.addRelMessage(getHttpServletRequest(), "删除信息成功.", "mainqueryDriver");
			return SUCCESS;
		} catch (Exception e) {
			MessageUtil.addMessage(getRequest(), "删除失败");
			return ERROR;
		}
	}

	@Action(value = "updateDriver")
	public String update() {
		try {
			service.update(bean);
			MessageUtil.addCloseMessage(getHttpServletRequest(), "更新成功.", "mainqueryDriver");
			return SUCCESS;
		} catch (Exception e) {
			MessageUtil.addMessage(getRequest(), "更新失败");
			return ERROR;
		}
	}

	@Action(value = "addDriver")
	public String add() {
		try {
			service.add(bean);
			MessageUtil.addRelMessage(getHttpServletRequest(), "添加成功.", "mainqueryDriver");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(getRequest(), "添加失败");
			return ERROR;
		}
	}

	@Action(value = "queryDriver", results = { @Result(name = "queryList", location = "/ahtml/listDriver.jsp") })
	public String query() {
		try {
			int pageNum = 0;
			String t = getHttpServletRequest().getParameter("pageCurrent");
			if (StringUtil.notEmpty(t)) {
				pageNum = Integer.valueOf(t);
			}
			Page p = new Page();
			//if (pageNum == 1 || p == null) {
			p = new Page();
			p.setCurrentPageNumber(pageNum);
			p.setTotalNumber(0l);
			p.setItemsPerPage(Constant.SESSION_PAGE_NUMBER);

			Map<String, String> textmap = new HashMap<String, String>();

			// 字段名称集合
			LinkedList<String> parmnames = new LinkedList<String>();
			// 字段值集合
			LinkedList<Object> parmvalues = new LinkedList<Object>();
			// 页面参数集合
			Set valueset = getHttpServletRequest().getParameterMap().entrySet();
			for (Object o : valueset) {
				Entry<String, Object> e = (Entry<String, Object>) o;
				String name = e.getKey();// 页面字段名称
				if (name.startsWith("s_")) {
					String fieldValue = getHttpServletRequest().getParameter(name);// 页面字段值
					if (StringUtil.notEmpty(fieldValue)) {
						name = name.substring(2, name.length());// 实体字段名称
						parmnames.add(name);
						parmvalues.add(FieldUtil.format(Driver.class, name, fieldValue));
						textmap.put(name.replaceAll("\\.", ""), fieldValue);
					}
				}
			}

			SearchParamBean sbean = new SearchParamBean();
			sbean.setParmnames(parmnames);
			sbean.setParmvalues(parmvalues);

			p.setConditonObject(sbean);

			Page page = null;
			page = service.find(p, Driver.class);

			putRequestValue("textmap", textmap);

			getHttpSession().setAttribute(Constant.SESSION_PAGE, page);
			return "queryList";
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public Driver getModel() {
		return bean;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	private String ids;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public BizService getService() {
		return service;
	}

	public Driver getBean() {
		return bean;
	}

	public void setService(BizService service) {
		this.service = service;
	}

	public void setBean(Driver bean) {
		this.bean = bean;
	}

	public String getActionname1() {
		return actionname1;
	}

	public void setActionname1(String actionname1) {
		this.actionname1 = actionname1;
	}

	public String getActionclass1() {
		return actionclass1;
	}

	public void setActionclass1(String actionclass1) {
		this.actionclass1 = actionclass1;
	}

}
