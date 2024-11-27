package nl.ragbecca.abn;

import nl.ragbecca.abn.model.Address;
import nl.ragbecca.abn.model.util.AddressType;
import nl.ragbecca.abn.model.Customer;
import nl.ragbecca.abn.model.Employer;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void customerCreation_checkIfInstantNameWorks() {
        Instant now = Instant.now();
        await().atLeast(Duration.ofMillis(10));
        Customer customer = new Customer("John Doe", Date.valueOf("2022-10-11"), null, null, true);
        assertTrue(Instant.parse(customer.getCustomerId().split("_")[0]).isAfter(now));
    }

    @Test
    void customerCreation_checkIfValuesAreWorking() {
        Address address = new Address("1", "Rotterdam", "2222DE", "Netherlands", AddressType.PERMANENT);
        Employer employer = new Employer("2", "Dave");
        Customer customer = new Customer("John Doe", Date.valueOf("2022-10-11"), List.of(address), employer, true);
        assertFalse(customer.getCustomerId().isBlank());
        assertEquals("John Doe", customer.getName());
        assertEquals(Date.valueOf("2022-10-11"), customer.getDateOfBirth());

        assertNotNull(customer.getAddresses().get(0).getAddressId());
        assertEquals(address.getCity(), customer.getAddresses().get(0).getCity());
        assertEquals(address.getPostalCode(), customer.getAddresses().get(0).getPostalCode());
        assertEquals(address.getCountry(), customer.getAddresses().get(0).getCountry());
        assertEquals(address.getAddressType(), customer.getAddresses().get(0).getAddressType());

        assertNotNull(customer.getEmployerDetails().getEmployerId());
        assertEquals(employer.getEmployerName(), customer.getEmployerDetails().getEmployerName());

        assertTrue(customer.getIsEmployed());
    }

    @Test
    void customerCreation_checkIfValuesAreWorking_withoutFilledAddressAndEmployer() {
        Customer customer = new Customer("John Doe", Date.valueOf("2022-10-11"), null, null, true);
        assertFalse(customer.getCustomerId().isBlank());
        assertEquals("John Doe", customer.getName());
        assertEquals(Date.valueOf("2022-10-11"), customer.getDateOfBirth());
        assertNull(customer.getAddresses());
        assertNull(customer.getEmployerDetails());
        assertTrue(customer.getIsEmployed());
    }
}
