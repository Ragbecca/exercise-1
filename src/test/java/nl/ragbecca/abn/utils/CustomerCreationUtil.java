package nl.ragbecca.abn.utils;

import nl.ragbecca.abn.model.Address;
import nl.ragbecca.abn.model.request.AddressRequest;
import nl.ragbecca.abn.model.request.CustomerRequest;
import nl.ragbecca.abn.model.request.EmployerRequest;
import nl.ragbecca.abn.model.util.AddressType;
import nl.ragbecca.abn.model.Customer;
import nl.ragbecca.abn.model.Employer;

import java.sql.Date;
import java.util.List;

public class CustomerCreationUtil {
    public static Customer responseBuilder(String name, String customerId) {
        Address address = new Address("1", "Rotterdam", "2222DE", "Netherlands", AddressType.PERMANENT);
        Employer employer = new Employer("2222", "David Beckham");
        return new Customer(customerId, name, Date.valueOf("2022-01-03"), List.of(address), employer, true);
    }

    public static Customer responseBuilder(String name, String employerId, String addressId) {
        Address address = new Address(addressId, "Rotterdam", "2222DE", "Netherlands", AddressType.PERMANENT);
        Employer employer = new Employer(employerId, "David Beckham");
        return new Customer(name, Date.valueOf("2022-01-03"), List.of(address), employer, true);
    }

    public static Customer responseBuilderTwoAddresses(String name, String customerId) {
        Address address1 = new Address("1", "Rotterdam", "2222DE", "Netherlands", AddressType.PERMANENT);
        Address address2 = new Address("2", "Amsterdam", "2221DE", "Germany", AddressType.PERMANENT);
        Employer employer = new Employer("2222", "David Beckham");
        return new Customer(customerId, name, Date.valueOf("2022-01-03"), List.of(address1, address2), employer, true);
    }

    public static CustomerRequest createCustomerRequest(String name, String dateOfBirth, String addressType) {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName(name);
        customerRequest.setDateOfBirth(dateOfBirth);
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity("Rotterdam");
        addressRequest.setPostalCode("2222DE");
        addressRequest.setCountry("Netherlands");
        addressRequest.setAddressType(addressType);
        customerRequest.setAddresses(List.of(addressRequest));
        EmployerRequest employerRequest = new EmployerRequest();
        employerRequest.setEmployerName("David Beckham");
        customerRequest.setEmployer(employerRequest);
        customerRequest.setIsEmployed(true);
        return customerRequest;
    }
}
