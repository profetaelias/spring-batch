package br.com.tenaciousthinking.spring.batch.demo.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import br.com.tenaciousthinking.spring.batch.demo.model.User;

@Component
public class Processor implements ItemProcessor<User, User> {

	private static final Map<String, String> DEPT_NAMES = new HashMap<>();
	
	public Processor() {
		DEPT_NAMES.put("001", "Tecnologia");
		DEPT_NAMES.put("002", "RH");
		DEPT_NAMES.put("003", "Contabilidade");
		DEPT_NAMES.put("004", "Jurídico");
		DEPT_NAMES.put("005", "Presidência");
	}
	
	@Override
	public User process(User user) throws Exception {
		String departamento = DEPT_NAMES.get(user.getDept());
		user.setDept(departamento);
		
		return user;
	}

	

}
