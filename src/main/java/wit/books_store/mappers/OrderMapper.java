package wit.books_store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import wit.books_store.dto.OrderDto;
import wit.books_store.models.Order;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    Order toOrder(OrderDto OrderDto);
    OrderDto toOrderDto(Order Order);
}
