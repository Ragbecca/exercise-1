package nl.ragbecca.abn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.ragbecca.abn.utils.SingletonIdCreation;

import java.sql.Date;
import java.util.List;

@Entity(name = "Customer")
@Table(name = "Customer")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private String customerId;

    private String name;

    private Date dateOfBirth;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Address> addresses;

    @ManyToOne(cascade = CascadeType.ALL)
    private Employer employerDetails;

    private Boolean isEmployed;

    public Customer(String name, Date dateOfBirth, List<Address> addresses, Employer employerDetails, boolean isEmployed) {
        this.customerId = SingletonIdCreation.getInstance().createId();
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.addresses = addresses;
        this.employerDetails = employerDetails;
        this.isEmployed = isEmployed;
    }
}
