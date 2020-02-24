package com.codessquad.qna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/form")
    public String viewUserForm() {
        return "/users/form";
    }

    @PostMapping("/create")
    public String createUser(User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/loginForm")
    public String viewLoginForm() {
        return "/users/login";
    }

    @PostMapping("/login")
    public String login(String userId, String password, HttpSession session) {
        try {
            User user = userRepository.findByUserId(userId);
            if(!(isPasswordEquals(user.getId(), password))){
                return "redirect:/users/loginForm";
            }
            session.setAttribute("user", user);
            return "redirect:/";
        } catch (NullPointerException e) {
            // userId not found
            return "redirect:/users/loginForm";
        }
    }

    @GetMapping("")
    public String viewList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/users/list";
    }

    @GetMapping("/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        if(!(isIdPresent(id))) {
            return "redirect:/users";
        }
        model.addAttribute("user", userRepository.findById(id).get());
        return "/users/profile";
    }

    @GetMapping("/{id}/form")
    public String viewUpdateForm(@PathVariable Long id, Model model) {
        if(!(isIdPresent(id))) {
            return "redirect:/users";
        }
        model.addAttribute("user", userRepository.findById(id).get());
        return "/users/updateForm";
    }

    @PutMapping("/{id}/update")
    public String viewUpdatedList(@PathVariable Long id, String password, String name, String email) {
        if(!(isPasswordEquals(id, password))) {
            return "redirect:/users";
        }
        userRepository.findById(id).get().setName(name);
        userRepository.findById(id).get().setEmail(email);
        userRepository.save(userRepository.findById(id).get());
        return "redirect:/users";
    }

    private Boolean isIdPresent(Long id) {
        return userRepository.findById(id).isPresent();
    }

    private Boolean isPasswordEquals(Long id, String password) {
        return userRepository.findById(id).get().getPassword().equals(password);
    }
}
