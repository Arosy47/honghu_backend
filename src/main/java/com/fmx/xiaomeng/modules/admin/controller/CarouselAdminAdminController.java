package com.fmx.xiaomeng.modules.admin.controller;

import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.admin.entity.LinkEntity;
import com.fmx.xiaomeng.modules.admin.model.CarouselModel;
import com.fmx.xiaomeng.modules.admin.service.CarouselAdminService;
import com.fmx.xiaomeng.modules.application.service.param.CarouselPageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.controller.AbstractAdminController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * @Description 后台轮播图管理模块
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("admin/link")
public class CarouselAdminAdminController extends AbstractAdminController {
    @Autowired
    private CarouselAdminService carouselAdminService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("admin:link:list")
    public Result list(){

        Long userId = getUser().getUserId();
        CarouselPageQueryParam param = new CarouselPageQueryParam();
        if(!userId.equals(AdminGlobalConstants.SUPER_ADMIN)){
            param.setSchoolId(getUser().getSchoolId());
        }

        PageList page = carouselAdminService.queryPage(param);
        return Result.ok(page);
    }


    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/info")
    @RequiresPermissions("admin:link:info")
    public Result info(@RequestParam("id") Integer id){

        return Result.ok(null);
    }

    /**
     * 保存
     * @param carouselModel
     * @return
     */
    @RequestMapping("/save")
    @RequiresPermissions("admin:link:save")
    public Result add(@RequestBody CarouselModel carouselModel){
        carouselModel.setCreateTime(new Date());
        carouselAdminService.addCarousel(carouselModel);
        return Result.ok();
    }

    /**
     * 修改
     * @param link
     * @return
     */
    @RequestMapping("/update")
    @RequiresPermissions("admin:link:update")
    public Result update(@RequestBody LinkEntity link){

        return Result.ok();
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("admin:link:delete")
    public Result delete(@RequestBody Integer[] ids){

        return Result.ok();
    }

}
