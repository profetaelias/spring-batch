package br.com.tenaciousthinking.spring.batch.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.tenaciousthinking.spring.batch.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
