package ru;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.EnumSet;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

@Controller
@RequestMapping(value = "/users")
public class JspAdminController {


    @Autowired
    private UserService userService;

    @GetMapping()
    public String users(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @GetMapping(value = "/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute(userService.get(id));
        return "userForm";
    }

    @GetMapping("/create")
    public String create(Model model) {
        User user = new User(
                null,
                "Name",
                "email@gmail.com",
                "password",
                DEFAULT_CALORIES_PER_DAY,
                true,
                new Date(),
                EnumSet.of(Role.ROLE_USER));
        model.addAttribute(user);
        return "userForm";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        int userId = SecurityUtil.authUserId();
        userService.delete(userId);
        return "redirect:/users";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String update(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        User user = userService.get(id);
        user.setName(request.getParameter("name"));
        user.setName(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setCaloriesPerDay(Integer.parseInt(request.getParameter("caloriesPerDay")));
        user.setEnabled(Boolean.valueOf(request.getParameter("enabled")));
        user.setRoles(EnumSet.of(Role.ROLE_USER));
        return "redirect:/users";
    }

}
