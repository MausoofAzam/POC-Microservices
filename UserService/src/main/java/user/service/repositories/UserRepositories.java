package user.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import user.service.entities.User;

public interface UserRepositories extends JpaRepository<User,String> {
}
