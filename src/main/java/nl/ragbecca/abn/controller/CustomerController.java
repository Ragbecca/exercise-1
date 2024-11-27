package nl.ragbecca.abn.controller;

import jakarta.annotation.Nullable;
import nl.ragbecca.abn.model.Address;
import nl.ragbecca.abn.model.Customer;
import nl.ragbecca.abn.model.request.CustomerRequest;
import nl.ragbecca.abn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Customer createCustomer(@RequestBody CustomerRequest customerRequest) {
        return customerService.createCustomer(customerRequest);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllDetailsOfAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomers(@PathVariable String customerId) {
        return customerService.getAllDetailsOfCustomer(customerId);
    }

    @GetMapping("/{customer_id}/addresses")
    public List<Address> getAddressesFromCustomer(@PathVariable("customer_id") String customerId, @Nullable @RequestParam String country) {
        if (country == null) {
            return customerService.getAllAddressesForCertainCustomer(customerId);
        }
        return customerService.getAllAddressesForCertainCustomerAndCountry(customerId, country);
    }
}
