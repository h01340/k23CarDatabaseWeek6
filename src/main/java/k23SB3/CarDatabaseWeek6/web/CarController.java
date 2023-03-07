package k23SB3.CarDatabaseWeek6.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import k23SB3.CarDatabaseWeek6.domain.Car;
import k23SB3.CarDatabaseWeek6.domain.CarRepository;
import k23SB3.CarDatabaseWeek6.domain.OwnerRepository;

@Controller
public class CarController {
	private static final Logger log = LoggerFactory.getLogger(CarController.class);
	
	@Autowired
	CarRepository carRepository; 
	
	@Autowired
	OwnerRepository ownerRepository;
	
	@GetMapping(value= {"/", "index"})
	public String showMainPage() {
		log.info("open main page");
		return "index";

	}
	
	@GetMapping("/carlist")
	public String showCars(Model model) {
		log.info("Read cars from database..");
		model.addAttribute("cars", carRepository.findAll());
		return "carList";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/addCar")
	public String addCar(Model model) {
		log.info("Lets go to create a new car....");
		model.addAttribute("car", new Car());
		model.addAttribute("omistajat", ownerRepository.findAll());
		return "newCar";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("editCar/{id}")
	public String editCar(@PathVariable("id") Long id, Model model) {
		model.addAttribute("editCar", carRepository.findById(id));
		model.addAttribute("omistajat", ownerRepository.findAll());
		return "editCar";
	}

	@PostMapping("/saveCar")
	public String saveCar(@Valid @ModelAttribute ("car") Car car, BindingResult bindingResult, Model model) {
		log.info("CONTROLLER: Save some the car - check validation of car: " + car);
		//Check if there are errors based on validation
		if (bindingResult.hasErrors()) {
			log.info("some validation error happened, omistajat: " + ownerRepository.findAll());
			model.addAttribute("omistajat", ownerRepository.findAll());
			return "newCar";
		}
		carRepository.save(car);
		return "redirect:carlist";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("delete/{id}")
	public String deleteCar(@PathVariable("id") Long id, Model model) {
		log.info("delete car");
		carRepository.deleteById(id);

		return "redirect:/carlist";
	}
	


}
