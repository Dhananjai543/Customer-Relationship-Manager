package com.dhananjai.crm.repository;

import com.dhananjai.crm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByCustId(int custId);

}
