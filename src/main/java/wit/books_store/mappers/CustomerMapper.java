package wit.books_store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import wit.books_store.dto.CustomerDto;
import wit.books_store.models.Customer;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    Customer toCustomer(CustomerDto customerDto);
    CustomerDto toCustomerDto(Customer customer);
}
