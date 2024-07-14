package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String readAllUsers(Model model) {
        model.addAttribute("users", userService.readAllUsers());
        return "admin_page";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "create";
    }

    @PostMapping("/createauser")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam("role") String selectedRole) {
        if (bindingResult.hasErrors()) {
            return "create";
        }
        if (selectedRole.equals("ROLE_USER")) {
            user.setRoles(roleService.findByName("ROLE_USER"));
        } else if (selectedRole.equals("ROLE_ADMIN")) {
            user.setRoles(roleService.findAll());
        }
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String updateForm(Model model,
                             @RequestParam("id") Long id) {
        model.addAttribute(userService.readUserById(id));
        return "update";
    }

    @PostMapping("/updateauser")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam("role") String selectedRole,
                         @RequestParam("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "update";
        }
        if (selectedRole.equals("ROLE_USER")) {
            user.setRoles(roleService.findByName("ROLE_USER"));
        } else if (selectedRole.equals("ROLE_ADMIN")) {
            user.setRoles(roleService.findAll());
        }
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteForm(Model model,
                             @RequestParam("id") Long id) {
        model.addAttribute(userService.readUserById(id));
        return "delete";
    }

    @PostMapping("/deleteauser")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}