package nl.ragbecca.abn.service;

import nl.ragbecca.abn.exception.BadRequestException;
import nl.ragbecca.abn.model.Address;
import nl.ragbecca.abn.model.util.AddressType;
import nl.ragbecca.abn.model.Customer;
import nl.ragbecca.abn.model.Employer;
import nl.ragbecca.abn.model.request.AddressRequest;
import nl.ragbecca.abn.model.request.CustomerRequest;
import nl.ragbecca.abn.repository.AddressRepository;
import nl.ragbecca.abn.repository.CustomerRepository;
import nl.ragbecca.abn.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static nl.ragbecca.abn.exception.ExceptionCodes.*;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final EmployerRepository employerRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public CustomerService(CustomerRepository repository, EmployerRepository employerRepository, AddressRepository addressRepository) {
        this.repository = repository;
        this.employerRepository = employerRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Create a customer in the database, if employer already exists use that, check if address type is correct for all addresses (and if there are any). Also check date-format
     * @param customerRequest - The request containing dateOfBirth, at least one address
     * @return - return the newly created Customer containing the address and employer (if applicable)
     */
    public Customer createCustomer(CustomerRequest customerRequest) {
        Date date;
        try {
            date = Date.valueOf(customerRequest.getDateOfBirth());
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(INVALID_DATE_OF_BIRTH_EXCEPTION, INVALID_DATE_OF_BIRTH_CODE);
        }

        Employer employer = employerRepository.findByEmployerName(customerRequest.getEmployer().getEmployerName());
        if (employer == null) {
            employer = employerRepository.save(new Employer(customerRequest.getEmployer().getEmployerName()));
        }
        List<Address> addresses = new ArrayList<>();
        for (AddressRequest addressRequest : customerRequest.getAddresses()) {
            if (!addressRequest.getAddressType().equals(AddressType.PERMANENT.toString()) && !addressRequest.getAddressType().equals(AddressType.TEMPORARY.toString())) {
                throw new BadRequestException(ADDRESS_TYPE_INVALID_EXCEPTION, ADDRESS_TYPE_INVALID_CODE);
            }
            Address address = new Address(addressRequest.getCity(), addressRequest.getPostalCode(), addressRequest.getCountry(), AddressType.valueOf(addressRequest.getAddressType()));
            addresses.add(addressRepository.save(address));
        }

        Customer customer = new Customer(customerRequest.getName(), date, addresses, employer, customerRequest.getIsEmployed());
        return repository.save(customer);
    }

    /**
     * Get all the customers and their info
     * @return - all the customers in a single list
     */
    public List<Customer> getAllDetailsOfAllCustomers() {
        return repository.findAll();
    }

    /**
     * Get all information of a single customer
     * @param customerId - The ID where the customer is being searched on
     * @return - the customer or throw a BadRequestException
     */
    public Customer getAllDetailsOfCustomer(String customerId) {
        return repository.findById(customerId).orElseThrow(() -> new BadRequestException(CUSTOMER_NOT_FOUND_EXCEPTION.formatted(customerId), CUSTOMER_NOT_FOUND_CODE));
    }

    /**
     * Get all addresses of a single customer
     * @param customerId - The ID where the customer is being searched on
     * @return - the addresses which the customer has
     */
    public List<Address> getAllAddressesForCertainCustomer(String customerId) {
        Customer customer = repository.findById(customerId).orElseThrow(() -> new BadRequestException(CUSTOMER_NOT_FOUND_EXCEPTION.formatted(customerId), CUSTOMER_NOT_FOUND_CODE));
        return customer.getAddresses();
    }

    /**
     * Get all the addresses of a single customer if they are from a certain country
     * @param customerId - The ID where the customer is being searched on
     * @param country - The full name of the country
     * @return - the addresses which the customer has in this country OR a BadRequestException if none
     */
    public List<Address> getAllAddressesForCertainCustomerAndCountry(String customerId, String country) {
        Customer customer = repository.findById(customerId).orElseThrow(() -> new BadRequestException(CUSTOMER_NOT_FOUND_EXCEPTION.formatted(customerId), CUSTOMER_NOT_FOUND_CODE));
        List<Address> addresses = customer.getAddresses();
        List<Address> countryAddresses = addresses.stream().filter(a -> a.getCountry().equals(country)).toList();
        if (countryAddresses.isEmpty()) {
            throw new BadRequestException(COUNTRY_NO_ADDRESSES_FOUND_EXCEPTION.formatted(country), COUNTRY_NO_ADDRESSES_FOUND_CODE);
        }
        return countryAddresses;
    }
}
