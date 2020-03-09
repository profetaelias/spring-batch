package br.com.tenaciousthinking.spring.batch.demo.batch;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.tenaciousthinking.spring.batch.demo.model.User;
import br.com.tenaciousthinking.spring.batch.demo.repository.UserRepository;

@Component
public class Writer implements ItemWriter<User>{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void write(List<? extends User> users) throws Exception {
		userRepository.saveAll(users);	
	}
}
