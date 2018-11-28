package com.colossus.admin.service.impl;


import com.colossus.admin.service.ContentService;
import com.colossus.common.dao.CategoryMapper;
import com.colossus.common.dao.CategorySecondaryMapper;
import com.colossus.common.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容维护 ServiceImpl
 */

@Api(value = "API - ContentServiceImpl", description = "内容操作")
@RestController
public class ContentServiceImpl implements ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategorySecondaryMapper categorySecondaryMapper;


    @Override
    @ApiOperation("获取一级分类列表")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "sEcho", value = "", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "iDisplayStart", value = "", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "iDisplayLength", value = "", required = true, dataType = "Integer")
        }
    )
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 401, message = "未授权客户机访问数据"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")
        }
    )
    public Map<String, Object> getCategoryList(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength) {
        HashMap<String, Object> map = new HashMap<>();
        int pageNum = iDisplayStart / iDisplayLength + 1;
        //System.out.println(pageNum);
        PageHelper.startPage(pageNum, iDisplayLength);

        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();
        criteria.andSortOrderEqualTo(1);

        List<Category> list = categoryMapper.selectByExample(example);
        //System.out.println(list.size());
        PageInfo<Category> pageInfo = new PageInfo<>(list);

        map.put("sEcho", sEcho + 1);
        map.put("iTotalRecords", pageInfo.getTotal());//数据总条数
        map.put("iTotalDisplayRecords", pageInfo.getTotal());//显示的条数
        map.put("aData", list);//数据集合

        return map;
    }

    @Override
    @ApiOperation("保存一级分类列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "name", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "sort_order", value = "", required = true, dataType = "Integer")
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    //@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public BaseResult saveCategory(String id, String name, Integer sort_order) {

        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setSortOrder(sort_order);
        category.setUpdateTime(new Date());

        int i = categoryMapper.updateByPrimaryKey(category);

        return i > 0 ? BaseResult.ok() : BaseResult.build(400, "更新失败！");
    }

    @Override
    @ApiOperation(value = "获取二级分类列表",notes = "获取不是父分类")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "sEcho", value = "", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "iDisplayStart", value = "", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "iDisplayLength", value = "", required = true, dataType = "Integer")
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public Map<String, Object> getCategorySecondaryList(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength) {

        HashMap<String, Object> map = new HashMap<>();
        int pageNum = iDisplayStart / iDisplayLength + 1;
        //System.out.println(pageNum);
        PageHelper.startPage(pageNum, iDisplayLength);

        CategorySecondaryExample example = new CategorySecondaryExample();
        CategorySecondaryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(0L);

        List<CategorySecondary> list = categorySecondaryMapper.selectByExample(example);
        //System.out.println(list.size());
        PageInfo<CategorySecondary> pageInfo = new PageInfo<>(list);

        map.put("sEcho", sEcho + 1);
        map.put("iTotalRecords", pageInfo.getTotal());//数据总条数
        map.put("iTotalDisplayRecords", pageInfo.getTotal());//显示的条数
        map.put("aData", list);//数据集合

        return map;
    }

    @Override
    @ApiOperation("根据条件,获取二级分类列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "sSearch", value = "", required = true, dataType = "String"),
                    @ApiImplicitParam(name = "sEcho", value = "", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "iDisplayStart", value = "", required = true, dataType = "Integer"),
                    @ApiImplicitParam(name = "iDisplayLength", value = "", required = true, dataType = "Integer")
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public Map<String, Object> getSearchCategorySecondaryList(String sSearch, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength) {

        HashMap<String, Object> map = new HashMap<>();
        int pageNum = iDisplayStart / iDisplayLength + 1;
        //System.out.println(pageNum);
        PageHelper.startPage(pageNum, iDisplayLength);

        CategorySecondaryExample example = new CategorySecondaryExample();
        CategorySecondaryExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%" + sSearch + "%");

        List<CategorySecondary> list = categorySecondaryMapper.selectByExample(example);
        //System.out.println(list.size());
        PageInfo<CategorySecondary> pageInfo = new PageInfo<>(list);

        map.put("sEcho", sEcho + 1);
        map.put("iTotalRecords", pageInfo.getTotal());//数据总条数
        map.put("iTotalDisplayRecords", pageInfo.getTotal());//显示的条数
        map.put("aData", list);//数据集合

        return map;
    }

    @Override
    @ApiOperation("保存二级分类")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "CategorySecondary", value = "", required = true, dataType = "CategorySecondary"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    //@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public BaseResult saveCategorySecondary(@RequestBody CategorySecondary categorySecondary) {

        categorySecondary.setUpdateTime(new Date());

        int i = categorySecondaryMapper.updateByPrimaryKeySelective(categorySecondary);

        return i > 0 ? BaseResult.ok() : BaseResult.build(400, "服务器出错!");
    }
}
