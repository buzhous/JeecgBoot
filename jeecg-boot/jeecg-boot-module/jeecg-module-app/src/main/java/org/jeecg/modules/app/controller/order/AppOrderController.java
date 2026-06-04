package org.jeecg.modules.app.controller.order;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.bean.vo.order.OrderVO;
import org.jeecg.modules.app.service.IAppUserService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/app/order")
@Slf4j
public class AppOrderController extends JeecgController<AppUser, IAppUserService> {

    @Autowired
    private IAppUserService iAppUserService;


    @GetMapping
    public Result<Object> order(HttpServletRequest request) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error("用户信息错误！");
        }
        AppUser appUser = iAppUserService.getUserInfoById(loginUser.getId());
        if (ObjectUtil.isEmpty(appUser)) {
            return Result.error("用户信息不存在！");
        }
        // 10使用记录；20 订单记录；30 赠送记录
        String type = request.getParameter("type");
        if (StrUtil.isEmpty(type)) {
            return Result.OK(new ArrayList<>());
        }

        Page<OrderVO> page = new Page<>(1, 20);
        List<OrderVO> orders = new ArrayList<>();

        switch (type) {
            case "10":
                OrderVO orderUseVO = new OrderVO();
                orderUseVO.setOrderId("1");
                orderUseVO.setId("1");
                orderUseVO.setStatus(1);
                orderUseVO.setItemCount(1);
                orderUseVO.setItemIcon("https://sf-pic.oss-cn-guangzhou.aliyuncs.com/temp/%E7%BC%96%E7%BB%84352x_1748271707704.png");
                orderUseVO.setUseTime(DateUtil.date());
                orderUseVO.setItemName("蘑菇头");
                orders.add(orderUseVO);
                page.setRecords(orders);
                page.setTotal(orders.size());
                return Result.OK(page);
            case "20":
                OrderVO orderPayVO = new OrderVO();
                orderPayVO.setOrderId("2");
                orderPayVO.setId("2");
                orderPayVO.setStatus(1);
                orderPayVO.setItemCount(1);
                orderPayVO.setItemIcon("https://sf-pic.oss-cn-guangzhou.aliyuncs.com/temp/%E7%BC%96%E7%BB%84352x_1748271707704.png");
                orderPayVO.setUseTime(DateUtil.date());
                orderPayVO.setItemName("蘑菇头");
                String json = JSON.toJSONString(orderPayVO);
                orders.add(orderPayVO);
                for (int i = 0; i <= 99; i++) {
                    orders.add(JSON.parseObject(json, OrderVO.class));
                }
                page.setRecords(orders);
                page.setTotal(orders.size());
                return Result.OK(page);
            case "30":
                // 赠送记录
                OrderVO orderGiveVO = new OrderVO();
                orderGiveVO.setOrderId("3");
                orderGiveVO.setId("3");
                orderGiveVO.setStatus(1);
                orderGiveVO.setItemCount(1);
                orderGiveVO.setItemIcon("https://sf-pic.oss-cn-guangzhou.aliyuncs.com/temp/%E7%BC%96%E7%BB%84352x_1748271707704.png");
                orderGiveVO.setUseTime(DateUtil.date());
                orderGiveVO.setItemName("蘑菇头");
                orders.add(orderGiveVO);
                page.setRecords(orders);
                page.setTotal(orders.size());
                return Result.OK(page);
            default:
                break;
        }

        return Result.OK(new ArrayList<>());
    }

}
