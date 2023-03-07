package k23SB3.CarDatabaseWeek6.domain;

import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {

	ApplicationUser findByUsername(String username);

}
