package nl.ragbecca.abn;

import nl.ragbecca.abn.exception.BadRequestException;
import nl.ragbecca.abn.model.Address;
import nl.ragbecca.abn.model.Customer;
import nl.ragbecca.abn.model.Employer;
import nl.ragbecca.abn.model.request.CustomerRequest;
import nl.ragbecca.abn.repository.AddressRepository;
import nl.ragbecca.abn.repository.CustomerRepository;
import nl.ragbecca.abn.repository.EmployerRepository;
import nl.ragbecca.abn.service.CustomerService;
import nl.ragbecca.abn.utils.CustomerCreationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static nl.ragbecca.abn.utils.CustomerCreationUtil.createCustomerRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private EmployerRepository employerRepository;
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void whenCreateCustomer_returnCorrectCustomer_withoutErrors() {
        when(customerRepository.save(any())).thenReturn(CustomerCreationUtil.responseBuilder("John Doe", "1"));
        when(employerRepository.findByEmployerName(any())).thenReturn(new Employer("David Beckham"));
        Customer customer = customerService.createCustomer(createCustomerRequest("John Doe", "2010-01-01", "PERMANENT"));

        assertEquals("John Doe", customer.getName());
        assertEquals("David Beckham", customer.getEmployerDetails().getEmployerName());
        assertEquals(1, customer.getAddresses().size());
    }

    @Test
    void whenCreateCustomer_returnCorrectCustomer_withTemporaryResidence_withoutErrors() {
        when(customerRepository.save(any())).thenReturn(CustomerCreationUtil.responseBuilder("John Doe", "1"));
        when(employerRepository.findByEmployerName(any())).thenReturn(new Employer("David Beckham"));
        Customer customer = customerService.createCustomer(createCustomerRequest("John Doe", "2010-01-01", "TEMPORARY"));

        assertEquals("John Doe", customer.getName());
        assertEquals("David Beckham", customer.getEmployerDetails().getEmployerName());
        assertEquals(1, customer.getAddresses().size());
    }

    @Test
    void whenCreateCustomerWithWrongAddressType_throwBadRequestException() {
        when(employerRepository.findByEmployerName(any())).thenReturn(new Employer("David Beckham"));
        CustomerRequest customerRequest = createCustomerRequest("John Doe", "2010-01-01", "");
        assertThrows(BadRequestException.class, () -> customerService.createCustomer(customerRequest));
    }

    @Test
    void whenCreateCustomerWithDateOfBirthFormat_throwBadRequestException() {
        CustomerRequest customerRequest = createCustomerRequest("John Doe", "", "PERMANENT");
        assertThrows(BadRequestException.class, () -> customerService.createCustomer(customerRequest));
    }

    @Test
    void whenCreateCustomer_noExistingEmployer_returnCorrectCustomer_withoutErrors() {
        when(customerRepository.save(any())).thenReturn(CustomerCreationUtil.responseBuilder("John Doe", "1"));
        when(employerRepository.findByEmployerName(any())).thenReturn(null);
        when(employerRepository.save(any())).thenReturn(new Employer("David Beckham"));
        Customer customer = customerService.createCustomer(createCustomerRequest("John Doe", "2010-01-01", "PERMANENT"));

        assertEquals("John Doe", customer.getName());
        assertEquals(1, customer.getAddresses().size());
    }

    @Test
    void whenGetAllDetailsOfAllCustomers_withSavedInfo_returnArray() {
        when(customerRepository.findAll()).thenReturn(List.of(
                CustomerCreationUtil.responseBuilder("John Doe", "1"),
                CustomerCreationUtil.responseBuilder("John Jones", "2")));
        List<Customer> customers = customerService.getAllDetailsOfAllCustomers();

        assertEquals(2, customers.size());
        assertEquals("John Doe", customers.get(0).getName());
        assertEquals("John Jones", customers.get(1).getName());
    }

    @Test
    void whenGetAllDetailsOfCustomer_withSavedInfo_returnArray() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(CustomerCreationUtil.responseBuilder("John Doe", "1")));
        Customer customer = customerService.getAllDetailsOfCustomer("1");

        assertEquals("John Doe", customer.getName());
        assertEquals(1, customer.getAddresses().size());
    }

    @Test
    void whenGetAllDetailsOfCustomer_withoutCustomerWithIdPresent_returnBadRequestException() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> customerService.getAllDetailsOfCustomer("1"));
    }

    @Test
    void whenGetAllAddressesForCertainCustomer_withSavedInfo_returnArray() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(CustomerCreationUtil.responseBuilder("John Doe", "1")));
        List<Address> addresses = customerService.getAllAddressesForCertainCustomer("1");

        assertEquals(1, addresses.size());
        assertEquals("Rotterdam", addresses.get(0).getCity());
    }

    @Test
    void whenGetAllAddressesForCertainCustomer_withoutCustomerWithIdPresent_returnBadRequestException() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> customerService.getAllAddressesForCertainCustomer("1"));
    }

    @Test
    void whenGetAllAddressesForCertainCustomerAndCountry_withSavedInfo_returnArray() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(CustomerCreationUtil.responseBuilderTwoAddresses("John Doe", "1")));
        List<Address> addresses = customerService.getAllAddressesForCertainCustomerAndCountry("1", "Germany");

        assertEquals(1, addresses.size());
        assertEquals("Amsterdam", addresses.get(0).getCity());
    }

    @Test
    void whenGetAllAddressesForCertainCustomerAndCountry_withoutCustomerWithIdPresent_returnBadRequestException() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> customerService.getAllAddressesForCertainCustomerAndCountry("1", "Germany"));
    }

    @Test
    void whenGetAllAddressesForCertainCustomerAndCountry_withoutAddressesOfCountry_returnBadRequestException() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(CustomerCreationUtil.responseBuilder("John Doe", "1")));
        assertThrows(BadRequestException.class, () -> customerService.getAllAddressesForCertainCustomerAndCountry("1", "Germany"));
    }
}
