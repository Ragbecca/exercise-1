package nl.ragbecca.abn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Employer")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String employerId;

    @Setter
    private String employerName;

    public Employer(String employerName) {
        this.employerName = employerName;
    }
}
