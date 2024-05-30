package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        // 모델은 Controller에서 View로 넘어갈 때 데이터를 실어서 넘길 수 있음 (Session도 마찬가지)
        // memberForm이라는 빈 껍데기 객체를 보내줌 -> MemberForm에 구현된 validation을 해줌
        return "members/createMemberForm";

    }

    @PostMapping("/members/new")
    // @Valid를 써주면, 그 객체에서 어노테이션으로 적용했던 Validation을 사용 가능
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        // 입력받은 form 데이터를 address에 저장
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; // 회원가입 완료되면 첫화면으로 보냄
        // 재로딩이 필요할 때 보통 redirect를 사용

    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
        // 서비스를 통해 멤버를 조회한 후, 모델에 담아서 화면에 넘겨줌
        // html에서 루프를 돌면서 쭉 뿌려주면된다!
    }
}
