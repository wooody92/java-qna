package com.codessquad.qna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

import static com.codessquad.qna.HttpSessionUtils.isLogin;
import static com.codessquad.qna.HttpSessionUtils.getUserFromSession;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("")
    public String createAnswer(@PathVariable Long questionId, String contents, HttpSession session) {
        if (!isLogin(session)) {
            return "/users/loginForm";
        }
        User sessionUser = getUserFromSession(session);
        Question question = findQuestion(questionId);
        Answer answer = new Answer(sessionUser, question, contents);
        answerRepository.save(answer);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/{id}/form")
    public String viewUpdateForm(@PathVariable Long id, Model model, HttpSession session) {
        try {
            model.addAttribute("answer", getVerifiedAnswer(id, session));
            return "/qna/updatedAnswerForm";
        } catch (IllegalAccessException | EntityNotFoundException e) {
            log.info("Error Code > " + e.toString());
            return e.getMessage();
        }
    }

    @PutMapping("/{id}/form")
    public String updateAnswer(@PathVariable Long questionId, @PathVariable Long id, String contents, HttpSession session) {
        try {
            Answer answer = getVerifiedAnswer(id, session);
            answer.update(contents);
            answerRepository.save(answer);
            return "redirect:/questions/" + questionId;
        } catch (IllegalAccessException | EntityNotFoundException e) {
            log.info("Error Code > " + e.toString());
            return e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    public String deleteAnswer(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
        try {
            Answer answer = getVerifiedAnswer(id, session);
            answer.delete();
            answerRepository.save(answer);
            return "redirect:/questions/" + questionId;
        } catch (IllegalAccessException | EntityNotFoundException e) {
            log.info("Error Code > " + e.toString());
            return e.getMessage();
        }
    }

    private Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException("/error/notFound"));
    }

    private Answer findAnswer(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("/error/notFound"));
    }

    private Answer getVerifiedAnswer(Long id, HttpSession session) throws IllegalAccessException {
        if (!isLogin(session)) {
            throw new IllegalAccessException("/error/unauthorized");
        }
        User sessionUser = getUserFromSession(session);
        Answer answer = findAnswer(id);
        if (!answer.isWriterEquals(sessionUser)) {
            throw new IllegalAccessException("/error/forbidden");
        }
        return answer;
    }
}