package nl.ragbecca.abn;

import nl.ragbecca.abn.controller.CustomerController;
import nl.ragbecca.abn.model.Customer;
import nl.ragbecca.abn.model.request.CustomerRequest;
import nl.ragbecca.abn.service.CustomerService;
import nl.ragbecca.abn.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static nl.ragbecca.abn.utils.CustomerCreationUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CustomerController.class)
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CustomerService service;

    @Test
    void givenCustomers_whenGetCustomers_thenStatus200() throws Exception {
        Customer customer = new Customer("1111", "John Doe", Date.valueOf("2010-11-10"), null, null, false);
        given(service.createCustomer(any())).willReturn(customer);

        CustomerRequest request = createCustomerRequest("John Doe", "2010-11-10", "PERMANENT");

        mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(request)));
        verify(service, VerificationModeFactory.times(1)).createCustomer(any());
        reset(service);
    }

    @Test
    void givenCustomers_whenGetCustomers_thenReturnJsonArray() throws Exception {
        List<Customer> allCustomers = Arrays.asList(responseBuilder("David", "4444"), responseBuilder("John", "4445"));

        given(service.getAllDetailsOfAllCustomers()).willReturn(allCustomers);

        mvc.perform(MockMvcRequestBuilders.get("/customers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("David")))
                .andExpect(jsonPath("$[1].name", is("John")));
        verify(service, VerificationModeFactory.times(1)).getAllDetailsOfAllCustomers();
        reset(service);
    }

    @Test
    void givenCustomer_whenGetSingleCustomerById_thenReturnJsonArray() throws Exception {
        Customer customer = responseBuilder("David", "4444");

        given(service.getAllDetailsOfCustomer(any())).willReturn(customer);

        mvc.perform(MockMvcRequestBuilders.get("/customers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("customerId", is(customer.getCustomerId())))
                .andExpect(jsonPath("name", is(customer.getName())))
                .andExpect(jsonPath("dateOfBirth", is(customer.getDateOfBirth().toString())))
                .andExpect(jsonPath("addresses", hasSize(1)))
                .andExpect(jsonPath("employerDetails.employerId", is(customer.getEmployerDetails().getEmployerId())))
                .andExpect(jsonPath("employerDetails.employerName", is(customer.getEmployerDetails().getEmployerName())))
                .andExpect(jsonPath("isEmployed", is(customer.getIsEmployed())));
        verify(service, VerificationModeFactory.times(1)).getAllDetailsOfCustomer(any());
        reset(service);
    }

    @Test
    void givenCustomer_whenGetSingleCustomerByIdWithAddresses_thenReturnJsonArrayOfAddresses() throws Exception {
        Customer customer = responseBuilder("David", "4444");

        given(service.getAllAddressesForCertainCustomer(any())).willReturn(customer.getAddresses());

        mvc.perform(MockMvcRequestBuilders.get("/customers/1/addresses").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].city", is(customer.getAddresses().get(0).getCity())))
                .andExpect(jsonPath("$[0].postalCode", is(customer.getAddresses().get(0).getPostalCode())))
                .andExpect(jsonPath("$[0].country", is(customer.getAddresses().get(0).getCountry())))
                .andExpect(jsonPath("$[0].addressType", is(customer.getAddresses().get(0).getAddressType().toString())));
        verify(service, VerificationModeFactory.times(1)).getAllAddressesForCertainCustomer(any());
        reset(service);
    }

    @Test
    void givenCustomer_whenGetSingleCustomerByIdWithAddresses_twoHits_thenReturnJsonArrayOfAddresses() throws Exception {
        Customer customer = responseBuilderTwoAddresses("David", "4444");

        given(service.getAllAddressesForCertainCustomer(any())).willReturn(customer.getAddresses());

        mvc.perform(MockMvcRequestBuilders.get("/customers/1/addresses").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].city", is(customer.getAddresses().get(0).getCity())))
                .andExpect(jsonPath("$[0].postalCode", is(customer.getAddresses().get(0).getPostalCode())))
                .andExpect(jsonPath("$[0].country", is(customer.getAddresses().get(0).getCountry())))
                .andExpect(jsonPath("$[0].addressType", is(customer.getAddresses().get(0).getAddressType().toString())))
                .andExpect(jsonPath("$[1].city", is(customer.getAddresses().get(1).getCity())))
                .andExpect(jsonPath("$[1].postalCode", is(customer.getAddresses().get(1).getPostalCode())))
                .andExpect(jsonPath("$[1].country", is(customer.getAddresses().get(1).getCountry())))
                .andExpect(jsonPath("$[1].addressType", is(customer.getAddresses().get(1).getAddressType().toString())));
        verify(service, VerificationModeFactory.times(1)).getAllAddressesForCertainCustomer(any());
        reset(service);
    }

    @Test
    void givenCustomer_whenGetSingleCustomerByIdAndCountryWithAddresses_thenReturnJsonArrayOfAddresses() throws Exception {
        Customer customer = responseBuilderTwoAddresses("David", "4444");

        given(service.getAllAddressesForCertainCustomerAndCountry(any(), any())).willReturn(List.of(customer.getAddresses().get(1)));

        mvc.perform(MockMvcRequestBuilders.get("/customers/1/addresses?country=Germany").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].city", is(customer.getAddresses().get(1).getCity())))
                .andExpect(jsonPath("$[0].postalCode", is(customer.getAddresses().get(1).getPostalCode())))
                .andExpect(jsonPath("$[0].country", is(customer.getAddresses().get(1).getCountry())))
                .andExpect(jsonPath("$[0].addressType", is(customer.getAddresses().get(1).getAddressType().toString())));
        verify(service, VerificationModeFactory.times(1)).getAllAddressesForCertainCustomerAndCountry(any(), any());
        reset(service);
    }
}
