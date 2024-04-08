package top.zeds1aw.store.cartorder.model.dao;

import org.apache.ibatis.annotations.Param;
import top.zeds1aw.store.cartorder.model.pojo.Order;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByOrderNo(String orderNo);

    List<Order> selectForCustomer(Integer userId);

    List<Order> selectAllForAdmin();

    List<Order> selectUnpaidOrders(@Param("begTime")Date beginTime, @Param("endTime")Date currentTime);
}