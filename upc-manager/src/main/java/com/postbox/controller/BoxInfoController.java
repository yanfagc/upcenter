package com.postbox.controller;

import com.alibaba.fastjson.JSON;
import com.postbox.controller.params.BoxInfoParams;
import com.postbox.enums.BoxExpressStatus;
import com.postbox.enums.BoxInfoStatus;
import com.postbox.model.BoxInfo;
import com.postbox.service.BoxInfoService;
import com.postbox.vo.BoxInfoVo;
import org.hanzhdy.manager.settings.model.Area;
import org.hanzhdy.manager.settings.service.AreaService;
import org.hanzhdy.manager.support.constants.resp.RespResult;
import org.hanzhdy.manager.support.controller.ApplicationController;
import org.hanzhdy.web.bean.DatatableResult;
import org.hanzhdy.web.throwable.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by H.CAAHN on 2017/8/1.
 */
@Controller
@RequestMapping("/postbox/boxinfo")
public class BoxInfoController extends ApplicationController {
    @Resource
    private BoxInfoService       boxInfoService;
    
    @Resource
    private AreaService          areaService;
    
    /** 日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(BoxInfoController.class);
    
    /**
     * 打开箱子组管理列表页面
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String toList(Model model, HttpServletRequest request) {
        List<Area> provinceList = this.areaService.queryByParent(0l);
        model.addAttribute("provinceList", provinceList);
        model.addAttribute("statusList", BoxInfoStatus.values());
        return "/postbox/boxinfo/boxinfo-list";
    }
    
    /**
     * 请求获取箱子列表数据
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "dataList", method = RequestMethod.POST)
    @ResponseBody
    public Object dataList(BoxInfoParams params, HttpServletRequest request) {
        try {
            DatatableResult dataResult = this.boxInfoService.queryAsDatatableResult(params);
            return JSON.toJSONString(dataResult);
        }
        catch (BizException ex) {
            logger.error("查询箱子列表数据失败，查询参数：{}, 错误信息：[{}, {}]", JSON.toJSONString(params), ex.getCode(), ex.getMsg());
        }
        catch (Exception ex) {
            logger.error("查询箱子列表数据失败，查询参数：" + JSON.toJSONString(params), ex);
        }
        return null;
    }
    
    /**
     * 跳转到编辑企业信息的页面
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "toEdit", method = RequestMethod.GET)
    public String toEdit(Long id, Model model, HttpServletRequest request) {
        List<Area> provinceList = this.areaService.queryByParent(0l);
        model.addAttribute("statusList", BoxInfoStatus.values());
        model.addAttribute("expStatusList", BoxExpressStatus.values());
        model.addAttribute("provinceList", provinceList);
        if (id != null) {
            BoxInfoVo record = this.boxInfoService.queryById(id);
            if (record != null) {
                if (record.getProvince() != null) {
                    model.addAttribute("cityList", this.areaService.queryByParentName(record.getProvince()));
                }
                
                model.addAttribute("record", record);
                return "/postbox/boxinfo/boxinfo-edit";
            }
            return redirect(REDIRECT_404);
        }
        else {
            return "/postbox/boxinfo/boxinfo-edit";
        }
    }
    
    /**
     * 保存箱子数据
     * @param record
     * @param request
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Object saveBoxGroup(BoxInfo record, HttpServletRequest request) {
        try {
            // 执行新增或更新操作
            boolean result = false;
            if (record.getBoxInfoId() == null) {
                result = this.boxInfoService.insert(record);
            }
            else {
                result = this.boxInfoService.update(record);
            }
            
            // 处理返回结果
            return RespResult.create(result ? respCode.SUCCESS : respCode.SAVE_NORECORD);
        }
        catch (BizException ex) {
            logger.error("保存箱子信息失败，数据参数：{}, 错误信息：[{}, {}]", JSON.toJSONString(record), ex.getCode(), ex.getMsg());
            return RespResult.create(ex.getCode(), ex.getMsg());
        }
        catch (Exception ex) {
            logger.error("保存箱子信息失败，数据参数：" + JSON.toJSONString(record), ex);
            return RespResult.create(respCode.ERROR_EXCEPTION);
        }
    }
    
    /**
     * 处理更新箱子信息状态请求
     * @param record
     * @param request
     * @return
     */
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ResponseBody
    public Object updateStatus(BoxInfo record, HttpServletRequest request) {
        try {
            this.boxInfoService.updateStatus(record);
            return RespResult.create(respCode.SUCCESS);
        }
        catch (BizException ex) {
            logger.error("更新箱子信息状态失败，数据参数：{}, 错误信息：[{}, {}]", JSON.toJSONString(record), ex.getCode(), ex.getMsg());
            return RespResult.create(ex.getCode(), ex.getMsg());
        }
        catch (Exception ex) {
            logger.error("更新箱子信息状态失败，数据参数：" + JSON.toJSONString(record), ex);
            return RespResult.create(respCode.ERROR_EXCEPTION);
        }
    }
}