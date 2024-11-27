package nl.ragbecca.abn;

import nl.ragbecca.abn.model.Address;
import nl.ragbecca.abn.model.util.AddressType;
import nl.ragbecca.abn.model.Customer;
import nl.ragbecca.abn.model.Employer;
import nl.ragbecca.abn.repository.CustomerRepository;
import nl.ragbecca.abn.utils.CustomerCreationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    String employerId;
    String addressId;

    @BeforeEach
    void createAddressAndEmployer() {
        Employer employer = new Employer("David Beckham");
        employerId = employer.getEmployerId();
        entityManager.persistAndFlush(employer);
        Address address = new Address("Rotterdam", "2222DE", "Netherlands", AddressType.PERMANENT);
        addressId = address.getAddressId();
        entityManager.persistAndFlush(address);
    }


    @Test
    void whenFindById_thenReturnEmployee() {
        Customer customer = CustomerCreationUtil.responseBuilder("Alex", employerId, addressId);
        entityManager.merge(customer);

        assertTrue(customerRepository.findById(customer.getCustomerId()).isPresent());
        Customer found = customerRepository.findById(customer.getCustomerId()).get();
        assertThat(found.getName()).isEqualTo(customer.getName());
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        assertTrue(customerRepository.findById("doesNotExist").isEmpty());
    }

    @Test
    void givenSetOfEmployees_whenFindAll_thenReturnAllEmployees() {
        Customer customer1 = CustomerCreationUtil.responseBuilder("Alex", employerId, addressId);
        customer1.setCustomerId(customer1.getCustomerId() + "-1");
        entityManager.persistAndFlush(customer1);
        Customer customer2 = CustomerCreationUtil.responseBuilder("John", employerId, addressId);
        customer2.setCustomerId(customer2.getCustomerId() + "-2");
        entityManager.persistAndFlush(customer2);
        Customer customer3 = CustomerCreationUtil.responseBuilder("David", employerId, addressId);
        customer3.setCustomerId(customer3.getCustomerId() + "-3");
        entityManager.persistAndFlush(customer3);

        List<Customer> allCustomers = customerRepository.findAll();

        assertThat(allCustomers).hasSize(3).extracting(Customer::getName).containsOnly(customer1.getName(), customer2.getName(), customer3.getName());
        entityManager.clear();
    }
}
