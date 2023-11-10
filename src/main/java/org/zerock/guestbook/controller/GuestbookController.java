package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;

    // 게시글 리스트
    @GetMapping("/list")
    public void list(@ModelAttribute PageRequestDTO pageRequestDTO, Model model) {
        log.info("list..........." + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
    }
    // 게시글 등록 페이지
    @GetMapping("register")
    public void register() {
        log.info("register get....");
    }
    // 게시글 등록
    @PostMapping("register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
        log.info("dto...." + dto);

        //새로 추가된 엔티티의 번호
        Long gno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg",gno);
        return "redirect:/guestbook/list";
    }
    // 게시글 상세 , 게시글 등록
    @GetMapping({"read","modify"})
    public void read(Long gno , @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {
        log.info("gno" + gno);
        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }
    // 게시글 삭제
    @PostMapping("remove")
    public String remove(Long gno, RedirectAttributes redirectAttributes) {
        log.info("gno:" + gno);
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    }
}
