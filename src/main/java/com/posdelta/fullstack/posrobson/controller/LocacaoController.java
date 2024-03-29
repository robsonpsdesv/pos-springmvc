package com.posdelta.fullstack.posrobson.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.posdelta.fullstack.posrobson.model.Carro;
import com.posdelta.fullstack.posrobson.model.Locacao;
import com.posdelta.fullstack.posrobson.model.Motorista;
import com.posdelta.fullstack.posrobson.service.CarroService;
import com.posdelta.fullstack.posrobson.service.LocacaoService;
import com.posdelta.fullstack.posrobson.service.MotoristaService;

@Controller
@RequestMapping("/locacoes")
public class LocacaoController {

    private static final String LOCACAO_CADASTRO = "locacaoCadastro";
    private static final String LOCACAO_LISTA = "locacaoLista";

    @Autowired
    private LocacaoService locacaoService;

    @Autowired
    private CarroService carroService;

    @Autowired
    private MotoristaService motoristaService;

    @RequestMapping("/novo")
    public ModelAndView novo() {
        ModelAndView modelAndView = new ModelAndView(LOCACAO_CADASTRO);
        modelAndView.addObject(new Locacao());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView salvar(@Valid Locacao locacao, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return new ModelAndView(LOCACAO_CADASTRO);
        }

        int dias = (int) ((locacao.getDataDevolucao().getTime() - locacao.getDataLocacao().getTime()) / 86400000);
        locacao.setValorTotal(locacao.getCarro().getValorDiaria().multiply(new BigDecimal(dias)));

        if (locacao.getId() == null) {
        	locacaoService.incluir(locacao);
            redirectAttributes.addFlashAttribute("mensagem", "Inclusão realizada com sucesso!");
        } else {
        	locacaoService.alterar(locacao);
            redirectAttributes.addFlashAttribute("mensagem", "Alteração realizada com sucesso!");
        }

        return new ModelAndView("redirect:/locacoes/novo");
    }

    @GetMapping
    public ModelAndView listar() {
        ModelAndView modelAndView = new ModelAndView(LOCACAO_LISTA);
        modelAndView.addObject("locacoes", locacaoService.listar());
        return modelAndView;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView(LOCACAO_CADASTRO);

        modelAndView.addObject(locacaoService.pesquisaPorId(id));

        return modelAndView;
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/locacoes");
        locacaoService.excluir(id);
        return modelAndView;
    }

    @ModelAttribute(name = "todosMotoristas")
    public List<Motorista> todosMotoristas(){
    	return motoristaService.listar();
    }

    @ModelAttribute(name = "todosCarros")
    public List<Carro> todosCarros(){
        return carroService.listar();
    }
}
