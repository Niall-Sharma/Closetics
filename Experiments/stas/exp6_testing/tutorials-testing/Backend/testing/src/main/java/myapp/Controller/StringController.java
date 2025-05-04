package myapp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import myapp.Model.StringEntity;
import myapp.Model.StringRepo;
import myapp.Service.CapitalizeService;
import myapp.Service.ReverseStringService;

@RestController
public class StringController {

	@Autowired
	StringRepo sRepo;

	@Autowired
	ReverseStringService rss;

	@Autowired
	CapitalizeService capitalizeService;

	// 1. sending a string
	// 2. An object is created
	// 3. string is reversed by using the service
	// 4. the data in object is set to this reversed string
	// 5. the object is saved
	// 6. all objects are returned (incl. those sent earlier)

	@PostMapping("/reverse")
	public List<StringEntity> reverse(@RequestBody String s){
		StringEntity rs = new StringEntity();
		rs.setData(rss.reverse(s));
		sRepo.save(rs);
		List <StringEntity> l = sRepo.findAll();
		return l;
	}

	@PostMapping("/capitalize")
	public List<StringEntity> capitalize(@RequestBody String s){
		return capitalizeService.capitalize(s);
	}
}
