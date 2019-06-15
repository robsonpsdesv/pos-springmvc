package com.posdelta.fullstack.posrobson.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.posdelta.fullstack.posrobson.model.Motorista;
import com.posdelta.fullstack.posrobson.repository.MotoristaRepository;
import com.posdelta.fullstack.posrobson.type.Sexo;

@Controller
@RequestMapping("/motoristas")
public class MotoristaController {

    private static final String MOTORISTA_CADASTRO = "motoristaCadastro";
    private static final String MOTORISTA_LISTA = "motoristaLista";

    @Autowired
    private MotoristaRepository repository;

    @RequestMapping("/novo")
    public ModelAndView novo() {
        ModelAndView modelAndView = new ModelAndView(MOTORISTA_CADASTRO);
        modelAndView.addObject(new Motorista());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView salvar(@Valid Motorista motorista, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return new ModelAndView(MOTORISTA_CADASTRO);
        }

        if (motorista.getId() == null) {
            repository.save(motorista);
            redirectAttributes.addFlashAttribute("mensagem", "Inclusão realizada com sucesso!");
        } else {
            repository.save(motorista);
            redirectAttributes.addFlashAttribute("mensagem", "Alteração realizada com sucesso!");
        }

        return new ModelAndView("redirect:/motoristas/novo");
    }

    @GetMapping
    public ModelAndView listar() {
        ModelAndView modelAndView = new ModelAndView(MOTORISTA_LISTA);
        modelAndView.addObject("motoristas", repository.findAll());
        return modelAndView;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView(MOTORISTA_CADASTRO);

        modelAndView.addObject(repository
                .findById(id).orElseThrow(()
                        -> new EmptyResultDataAccessException(0)));

        return modelAndView;
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/motoristas");
        repository.deleteById(id);
        return modelAndView;
    }

    @ModelAttribute(name = "todosSexos")
    public Sexo[] todosSexos(){
        return Sexo.values();
    }
}