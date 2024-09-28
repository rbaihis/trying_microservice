package tn.seif.ecommerce.orderline.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.seif.ecommerce.orderline.dto.OrderLineResponse;
import tn.seif.ecommerce.orderline.service.OrderLineService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-line")
@RequiredArgsConstructor
public class OrderLineController {
    private final OrderLineService service;

    @GetMapping()
    // fixme: replace with Pagination
    public ResponseEntity<List<OrderLineResponse>> getAllResponse(){
        return ResponseEntity.accepted().body(
                service.findAll()
        );
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<List<OrderLineResponse>> getAllResponse(@PathVariable Long order_id){
        return ResponseEntity.accepted().body( service.findOrderIsOrderLines( order_id) );
    }
}
