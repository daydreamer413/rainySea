package com.example.controller;

import com.example.common.Result;
import com.example.dao.CartDao;
import com.example.dao.GoodsDao;
import com.example.dao.MemberDao;
import com.example.entity.Cart;
import com.example.entity.Goods;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 饮食清单(Cart)表控制层
 *
 */
@RestController
public class CartController {
    @Resource
    CartDao cartDao;
    @Resource
    MemberDao memberDao;
    @Resource
    GoodsDao goodsDao;

    //信息列表
    @ResponseBody
    @RequestMapping("cartList")
    public Result cartList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize, HttpServletRequest request){
        String key = request.getParameter("key");
        HashMap map = new HashMap();
        map.put("key", key);
        PageHelper.startPage(pageNum, pageSize);
        List<Cart> list = cartDao.selectAll(map);
        for (Cart cart : list) {
            Goods goods = goodsDao.findById(cart.getGoodsid());
            cart.setGoodsname(goods.getName());
            cart.setGoods(goods);
            Double heji = goods.getRlnum()*cart.getSlnum();
            Double yyheji = goods.getYynum()*cart.getSlnum();
            cart.setHeji(heji);
            cart.setYyheji(yyheji);
        }
        PageInfo<Cart> pageInfo = new PageInfo<Cart>(list);
        return Result.success(pageInfo);
    }
    
    //添加饮食清单信息
    @ResponseBody
    @RequestMapping("cartAdd")
    public Result cartAdd(Cart cart ,HttpServletRequest request) {
        HashMap map = new HashMap();
        map.put("memberid", cart.getMemberid());
        map.put("goodsid", cart.getGoodsid());
        List<Cart> list = cartDao.selectAll(map);
        if (list != null && list.size() > 0) {
            Cart cart1 = list.get(0);
            cart1.setSlnum(cart1.getSlnum() + cart.getSlnum());
            cartDao.update(cart1);
        }else{
            cartDao.add(cart);
        }
        return Result.success();
    }

    //编辑饮食清单信息
    @ResponseBody
    @RequestMapping("cartEdit")
    public Result cartEdit(Cart cart ,HttpServletRequest request) {
        cartDao.update(cart);
        return Result.success();
    }

    //删除信息
    @ResponseBody
    @RequestMapping("cartDel")
    public Result cartDel(int id,HttpServletRequest request) {
        cartDao.delete(id);
        return Result.success();
    }
    

}

