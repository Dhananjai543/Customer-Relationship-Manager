package com.dhananjai.crm.controller;

import com.dhananjai.crm.entity.Answer;
import com.dhananjai.crm.entity.Order;
import com.dhananjai.crm.entity.Question;
import com.dhananjai.crm.repository.OrderRepository;
import com.dhananjai.crm.service.OpenAIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class QuestionController {

    private final OpenAIService openAIService;

    private final OrderRepository orderRepository;

    public QuestionController(OpenAIService openAIService, OrderRepository orderRepository) {
        this.openAIService = openAIService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question){
        return openAIService.getAnswer(question);
    }

    @GetMapping("/analyzeOrders")
    public String analyzeOrders(Model model){
        List<Order> orders = orderRepository.findAll();
        StringBuilder questionString = new StringBuilder();
        for(Order order : orders){
            questionString.append(order.toString());
        }

        String prompt = "Act as a market analyst and analyze the given data to produce useful insights." +
                "Provide generalized insights and recommendations without referencing specific customers. Focus on trends and overall patterns in the data. Don't write unnecessary text. Answer in paragraph.";

        Question question = new Question(prompt + " : " + questionString.toString());
        Answer answer = openAIService.getAnswer(question);
        model.addAttribute("answer", answer.answer());
        return "ai-results";
    }

}
