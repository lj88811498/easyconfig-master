package org.september.easyconf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.september.core.exception.BusinessException;
import org.september.core.exception.NotAuthorizedException;
import org.september.easyconf.entity.*;
import org.september.easyconf.model.ConfigHistoryVo;
import org.september.simpleweb.model.ResponseVo;
import org.september.simpleweb.utils.SessionHelper;
import org.september.smartdao.CommonDao;
import org.september.smartdao.CommonValidator;
import org.september.smartdao.model.Order;
import org.september.smartdao.model.Page;
import org.september.smartdao.model.ParamMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/configHistory")
public class ConfigHistoryController {

	protected final static Logger logger = LoggerFactory.getLogger(ConfigHistoryController.class);

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private CommonValidator commonValidator;

	@RequestMapping("/configHistoryList")
	public ModelAndView configHistoryList(Long configId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("configId", configId);
		Config cfg = commonDao.get(Config.class, configId);
		EnvType envType = commonDao.get(EnvType.class, cfg.getEnvTypeId());
		if (envType != null) {
			if (StringUtils.isNotBlank(envType.getSecret())) {
				mv.addObject("secret", envType.getSecret());
			}
		}
		Project project = commonDao.get(Project.class, cfg.getProjectId());
		mv.addObject("projectName", project.getName());
		mv.addObject("envName", cfg.getEnvName());
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "listConfigHistoryData")
	public ResponseVo<Page<ConfigHistoryVo>> listConfigHistoryData(Page<ConfigHistoryVo> page, Long configId) {
		ParamMap pm = new ParamMap();
		pm.put("configId", configId);
		page = commonDao.findPageByParams(ConfigHistoryVo.class, page, "ConfigHistory.listConfigHistoryData", pm);
		return ResponseVo.<Page<ConfigHistoryVo>>BUILDER().setData(page).setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
	}

	@RequestMapping(value = "/addConfigHistory")
	public ModelAndView addConfig(Long configId) throws Exception {
		ModelAndView mv = new ModelAndView();
		Config po = commonDao.get(Config.class, configId);
		mv.addObject("configId", configId);
		mv.addObject("version", po.getVersion());
		ConfigHistory ch = new ConfigHistory();
		ch.setConfigId(configId);
		Order order = new Order("version", Order.Direction.ASC);
		List<Order> orders = new ArrayList<>();
		orders.add(order);
		List<ConfigHistory> chs = commonDao.listByExample(ch, orders);
		String oldversion = "";
		for (ConfigHistory c : chs) {
			if (StringUtils.isNotBlank(oldversion)) {
				oldversion += ",";
			}
			oldversion += " " + c.getVersion();
		}
		mv.addObject("oldversion", oldversion);
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/doAddConfigHistory")
	public ResponseVo<String> doAddConfig(@Valid ConfigHistory entity) {
		if (commonValidator.exsits(ConfigHistory.class, new String[] { "configId" , "version"},new Object[] { entity.getConfigId() , entity.getVersion()})) {
//			throw new BusinessException("版本重复");
			return ResponseVo.<String>BUILDER().setData("-1").setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
		}
		entity.setCreateTime(new Date());
		Config config = commonDao.get(Config.class, entity.getConfigId());
		entity.setContent(config.getContent());
		entity.setFormat(config.getFormat());
		commonDao.save(entity);
		return ResponseVo.<String>BUILDER().setData("").setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
	}

	@ResponseBody
	@RequestMapping(value = "/deleteConfigHistory")
	public ResponseVo<String> deleteConfigHistory(Long id) throws Exception {
		ConfigHistory po = commonDao.get(ConfigHistory.class, id);
		if (po == null) {
			throw new BusinessException("数据不存在或已删除");
		}
		commonDao.delete(po);
		return ResponseVo.<String>BUILDER().setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
	}

	@RequestMapping(value = "/editContentHistory")
	public ModelAndView editContentHistory(Long id) {
		Config po = checkDataPriviledge(id);
		ModelAndView mv = new ModelAndView();
		ConfigHistory ch = commonDao.get(ConfigHistory.class, id);
		mv.addObject("config", ch);
		Project project = commonDao.get(Project.class, po.getProjectId());
		mv.addObject("projectName", project.getName());
		mv.addObject("envName", po.getEnvName());
		return mv;
	}

	private Config checkDataPriviledge(Object configHistoryId) {
		ConfigHistory ch = commonDao.get(ConfigHistory.class, configHistoryId);
		Config po = commonDao.get(Config.class, ch.getConfigId());
		ProjectMember pmVo = new ProjectMember();
		pmVo.setProjectId(po.getProjectId());
		ConfigUser me = SessionHelper.getSessionUser(ConfigUser.class);
		pmVo.setUid(me.getId());

		ProjectMember pm = commonDao.getByExample(pmVo);
		if(1!=me.getIsAdmin() && pm==null) {
			throw new NotAuthorizedException("没有权限操作");
		}
		return po;
	}

	@ResponseBody
	@RequestMapping(value = "/doUpdateContent")
	public ResponseVo<String> doUpdateContent(Long id ,String format, String content) {
		checkDataPriviledge(id);
		ConfigHistory cfg = new ConfigHistory();
		cfg.setId(id);
		cfg.setContent(content);
		cfg.setUpdateTime(new Date());
		cfg.setFormat(format);
		commonDao.update(cfg);
		return ResponseVo.<String>BUILDER();
	}

}
