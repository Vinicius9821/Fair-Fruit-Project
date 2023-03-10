package Global.Points.FeiraOnline.service.impl;

import Global.Points.FeiraOnline.dto.OrderDTO;
import Global.Points.FeiraOnline.dto.OrderItemDTO;
import Global.Points.FeiraOnline.entities.Client;
import Global.Points.FeiraOnline.entities.Order;
import Global.Points.FeiraOnline.entities.OrderItem;
import Global.Points.FeiraOnline.entities.Product;
import Global.Points.FeiraOnline.entities.enums.OrderStatus;
import Global.Points.FeiraOnline.exception.BusinessRuleException;
import Global.Points.FeiraOnline.exception.OrderNotFoundException;
import Global.Points.FeiraOnline.repository.Clients;
import Global.Points.FeiraOnline.repository.OrderItems;
import Global.Points.FeiraOnline.repository.Orders;
import Global.Points.FeiraOnline.repository.Products;
import Global.Points.FeiraOnline.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final Orders repository;
    private final Clients clientsRepository;
    private final Products productsRepository;
    private final OrderItems orderItemsRepository;

    @Override
    @Transactional
    public Order save(OrderDTO dto) {
        Integer idClient = dto.getClient();
        Client client = clientsRepository
                .findById(idClient)
                .orElseThrow(() -> new BusinessRuleException("Client code not found."));

        Order order = new Order();
        order.setTotal(dto.getTotal());
        order.setOrderData(LocalDate.now());
        order.setClient(client);
        order.setStatus(OrderStatus.Realized);

        List<OrderItem> orderItems = convertItems(order, dto.getItems());
        repository.save(order);
        orderItemsRepository.saveAll(orderItems);
        order.setItems(orderItems);
        return order;
    }

    @Override
    public Optional<Order> getCompleteOrder(Integer id) {
        return repository.findByIdFetchItems(id);
    }

    @Override
    @Transactional
    public void StatusUpdate(Integer id, OrderStatus orderStatus) {
        repository
                .findById(id)
                .map( order -> {
                    order.setStatus(orderStatus);
                    return repository.save(order);
                }).orElseThrow(()-> new OrderNotFoundException());
    }

    private List<OrderItem> convertItems(Order order, List<OrderItemDTO> items){
        if(items.isEmpty()){
            throw new BusinessRuleException("it's not possible to order without products.");
        }

        return items
                .stream()
                .map(dto ->{
                    Integer idProduct = dto.getProduct();
                    Product product = productsRepository
                            .findById(idProduct)
                            .orElseThrow(() -> new BusinessRuleException("Invalid product code ."+ idProduct
                            ));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(dto.getQuantity());
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    return orderItem;
                }).collect(Collectors.toList());

    }

}
