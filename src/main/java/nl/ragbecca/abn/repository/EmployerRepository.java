package nl.ragbecca.abn.repository;

import nl.ragbecca.abn.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, String> {
    @Nullable
    Employer findByEmployerName(String employerName);
}
